package com.tvdashboard.androidwebservice;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tvdashboard.channelsetup.Channel;
import com.tvdashboard.channelsetup.FragmentTvGuideMain;
import com.tvdashboard.channelsetup.UpcomingAdapter;
import com.tvdashboard.database.R;
import com.tvdashboard.main.FragmentTvGuide;
import com.tvdashboard.utility.CombinedImageLoadingListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

public class ScheduleManager implements IScheduleManager {

	public static Context context;
	private ChannelManager channelManager;
	private int numberOfPendingRequests;
	private static ImageLoader imageLoader;
	private static HashMap<Show, Bitmap> bmpImages;
	public static volatile int imageDownloaderTasks;
	public static volatile ProgressDialog prgDiag;
	public static Handler mHandler = new Handler();
	public static HashMap<Show, Long> showDuration;
	public static HashMap<ChannelSchedule, List<Show>> schedules = null;
	public static volatile boolean isScheduleRunning = true;

	public static volatile HashMap<Show, ChannelSchedule> nowPlayingMedia = null;
	public static volatile LinkedHashMap<Show, ChannelSchedule> upComingMedia = null;
	public static volatile HashMap<ChannelSchedule, Channel> channelData = null;
	public static volatile ArrayList<Channel> channels = null;

	public static HashMap<ChannelSchedule, Channel> chData = null;
	private static int numTasks;
	public static boolean asyncWorker;

	public ScheduleManager(Context context, ImageLoader imageLoader) {
		ScheduleManager.context = context;
		channelManager = new ChannelManager(this.context);
		this.imageLoader = imageLoader;
		bmpImages = new HashMap<Show, Bitmap>();
		this.showDuration = new HashMap<Show, Long>();

		ScheduleManager.nowPlayingMedia = new HashMap<Show, ChannelSchedule>();
		ScheduleManager.upComingMedia = new LinkedHashMap<Show, ChannelSchedule>();
		ScheduleManager.channelData = new HashMap<ChannelSchedule, Channel>();
	}

	@Override
	public Show getNowPlayingScheduleByChannel(ChannelSchedule channel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Show getUpComingScheduleByChannel(ChannelSchedule channel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getAllChannelSchedule() {
		// TODO Auto-generated method stub
		channels = channelManager.getAllChannelsByRegion("india");
		schedules = new HashMap<ChannelSchedule, List<Show>>();
		final ObjectMapper mapper = new ObjectMapper(); // can reuse, share //
														// globally
		numberOfPendingRequests = channels.size();
		for (int i = 0; i < channels.size(); ++i) {
			AsyncInvokeURLTask task = null;
			try {
				final int index = i;
				task = new AsyncInvokeURLTask(channels.get(i).getChannelName(),
						context, null,
						new AsyncInvokeURLTask.OnPostExecuteListener() {
							@Override
							public void onPostExecute(String result) {
								try {
									ChannelSchedule schedule = mapper
											.readValue(result,
													ChannelSchedule.class);
									Show nowPlaying = getNowPlayingShow(schedule
											.getListOfShows());
									Show upComing = getUpComingShow(schedule
											.getListOfShows());
									if (nowPlaying != null) {
										new DownloadImageTask(context,
												imageLoader, nowPlaying,
												schedule, true)
												.execute(nowPlaying
														.getShowThumb());
									}
									if (upComing != null) {
										new DownloadImageTask(context,
												imageLoader, upComing,
												schedule, false)
												.execute(upComing
														.getShowThumb());
									}
									schedules.put(schedule,
											schedule.getListOfShows());
									ScheduleManager.channelData.put(schedule,
											channels.get(index));

									numberOfPendingRequests--;
									if (numberOfPendingRequests == 0) {

									}
								} catch (JsonParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JsonMappingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			task.execute();
		}
	}

	public static Show getNowPlayingShow(List<Show> shows) {
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Collections.sort(shows, new TimeComparator());
		// Timestamp currentTime = null;
		Show rtrnShow = null;
		Calendar c = Calendar.getInstance();
		Timestamp currentTime = Timestamp
				.valueOf(sdfCurrent.format(c.getTime()) // get the current date
														// as String
				);
		for (int i = 0; i < shows.size(); ++i) {
			Timestamp prevShowTime = null;
			Timestamp nxtShowTime = null;
			if (i == shows.size() - 1) {
				showDuration.put(shows.get(i), 0L);
				return shows.get(i);
			}
			prevShowTime = Timestamp.valueOf(sdf.format(c.getTime()).concat(
					" " + shows.get(i).getShowTime()));
			nxtShowTime = Timestamp.valueOf(sdf.format(c.getTime()).concat(
					" " + shows.get(i + 1).getShowTime()));
			if (currentTime.compareTo(prevShowTime) == 0) {
				rtrnShow = shows.get(i);
				showDuration.put(shows.get(i), nxtShowTime.getTime()
						- prevShowTime.getTime());
				break;
			} else if (currentTime.compareTo(prevShowTime) > 0
					&& currentTime.compareTo(nxtShowTime) < 0) {
				rtrnShow = shows.get(i);
				showDuration.put(shows.get(i), nxtShowTime.getTime()
						- prevShowTime.getTime());
				break;
			} else if (currentTime.compareTo(prevShowTime) < 0
					&& currentTime.compareTo(nxtShowTime) < 0) {
				rtrnShow = shows.get(i);
				showDuration.put(shows.get(i), nxtShowTime.getTime()
						- prevShowTime.getTime());
				break;
			}
		}

		return rtrnShow;
	}

	public static Show getUpComingShow(List<Show> shows) {
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Collections.sort(shows, new TimeComparator());
		Date date = new Date();
		Show rtrnShow = null;
		Calendar c = Calendar.getInstance();
		Timestamp currentTime = Timestamp
				.valueOf(sdfCurrent.format(c.getTime()) // get the current date
														// as String
				);
		for (int i = 0; i < shows.size(); ++i) {
			Timestamp prevShowTime = null;
			Timestamp nxtShowTime = null;
			if (i == shows.size() - 1)
				return shows.get(i);
			// prevShowTime = new Timestamp(sdf.parse(
			// shows.get(i).getShowTime()).getTime());
			prevShowTime = Timestamp.valueOf(sdf.format(c.getTime()).concat(
					" " + shows.get(i).getShowTime()));
			nxtShowTime = Timestamp.valueOf(sdf.format(c.getTime()).concat(
					" " + shows.get(i + 1).getShowTime()));
			if (currentTime.compareTo(prevShowTime) == 0) {
				rtrnShow = shows.get(i + 1);
				break;
			} else if (currentTime.compareTo(prevShowTime) > 0
					&& currentTime.compareTo(nxtShowTime) < 0) {
				rtrnShow = shows.get(i + 1);
				break;
			} else if (currentTime.compareTo(prevShowTime) < 0
					&& currentTime.compareTo(nxtShowTime) < 0) {
				rtrnShow = shows.get(i);
				break;
			}
		}

		return rtrnShow;
	}

	public static HashMap<Show, Bitmap> getBmpImages() {
		return bmpImages;
	}

	public static int getShowRunningTime(Show show) {
		Long duration = ScheduleManager.showDuration.get(show);
		return (int) (duration / (1000 * 60));
	}

	public synchronized static void AddShowToCurrentMedia(Show show,
			ChannelSchedule schedule, boolean isNowPlaying) {
		if (isNowPlaying) {
			ScheduleManager.nowPlayingMedia.put(show, schedule);
		} else {
			ScheduleManager.upComingMedia.put(show, schedule);
		}
	}

	public static void DisplayNowPlaying() {
		Iterator itNowPlaying = ScheduleManager.nowPlayingMedia.entrySet()
				.iterator();
		// set data on now playing
		for (int i = 0; i < FragmentTvGuide.btnImagesNowPlaying.length
				&& itNowPlaying.hasNext(); ++i) {
			Map.Entry mEntry = (Map.Entry) itNowPlaying.next();
			Show mShowNowPlaying = (Show) mEntry.getKey();
			FragmentTvGuide.btnImagesNowPlaying[i]
					.setImageBitmap(ScheduleManager.bmpImages
							.get(mShowNowPlaying));
			FragmentTvGuide.showTitleNowPlaying[i].setText(mShowNowPlaying
					.getShowTitle());
			int minutes = ScheduleManager.getShowRunningTime(mShowNowPlaying);
			FragmentTvGuide.showDuration[i].setText("+"
					+ String.valueOf(minutes));

			// set channel logo
			// get schedule from show
			ChannelSchedule sch = ScheduleManager.nowPlayingMedia
					.get(mShowNowPlaying);
			Channel ch = ScheduleManager.channelData.get(sch);
			FragmentTvGuide.imgIconsNowPlaying[i].setImageBitmap(ch
					.getChannelIcon());
		}
	}

	public static void DisplayUpComing() {
		Iterator itUpcoming = ScheduleManager.upComingMedia.entrySet()
				.iterator();
		// set data on now playing
		for (int i = 0; i < FragmentTvGuide.btnImagesUpComing.length
				&& itUpcoming.hasNext(); ++i) {
			Map.Entry mEntry = (Map.Entry) itUpcoming.next();
			Show mShowUpComing = (Show) mEntry.getKey();
			FragmentTvGuide.btnImagesUpComing[i]
					.setImageBitmap(ScheduleManager.bmpImages
							.get(mShowUpComing));
			FragmentTvGuide.showTitleUpComing[i].setText(mShowUpComing
					.getShowTitle());

			// set channel logo
			// get schedule from show
			ChannelSchedule sch = ScheduleManager.upComingMedia
					.get(mShowUpComing);
			Channel ch = ScheduleManager.channelData.get(sch);
			FragmentTvGuide.imgIconsUpComing[i].setImageBitmap(ch
					.getChannelIcon());
		}
	}

	public static List<Show> GetAllUpComingShows(List<Show> shows) {
		Show nowPlayingShow = getNowPlayingShow(shows);
		int index = shows.indexOf(nowPlayingShow);
		List<Show> upComingShows = new ArrayList<Show>();

		if (index > -1) {
			index++;
			for (; index < shows.size(); ++index) {
				upComingShows.add(shows.get(index));
			}
		}

		return upComingShows;
	}

	public synchronized static void RenderSchedulesOnDash() {

		DisplayNowPlaying();
		DisplayUpComing();

		// execute nowplaying async task
		new UpdateMediaTask(FragmentTvGuide.GetParentActivity(),
				FragmentTvGuide.btnImagesNowPlaying,
				FragmentTvGuide.btnImagesUpComing,
				ScheduleManager.nowPlayingMedia, ScheduleManager.upComingMedia,
				FragmentTvGuide.showDuration,
				FragmentTvGuide.showTitleNowPlaying,
				FragmentTvGuide.showTitleUpComing, FragmentTvGuide.progBar,
				false).execute();
	}

	public static int getCurrentPlayingShowStatus(Show show) {
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Long duration = ScheduleManager.showDuration.get(show);

		Timestamp showStartTime = null;

		Calendar c = Calendar.getInstance();
		Timestamp currentTime = Timestamp
				.valueOf(sdfCurrent.format(c.getTime()) // get the current date
														// as String
				);
		// Timestamp timestamp = Timestamp.valueOf("2007-09-23 10:10:10.0");
		showStartTime = Timestamp.valueOf(sdf.format(c.getTime()).concat(
				" " + show.getShowTime()));
		// showStartTime = new Timestamp(sdf.parse(show.getShowTime())
		// .getTime());

		return (int) (((double) (currentTime.getTime() - showStartTime
				.getTime()) / duration) * 100);
	}

	public static void setBmpImages(HashMap<Show, Bitmap> bmpImages) {
		ScheduleManager.bmpImages = bmpImages;
	}

	public static void getScheduleByDate(String date, final Channel ch) {
		ChannelManager channelManager = new ChannelManager(
				ScheduleManager.context);
		// final ArrayList<Channel> channels = channelManager
		// .getAllChannelsByRegion("india");
		chData = new HashMap<ChannelSchedule, Channel>();
		final ObjectMapper mapper = new ObjectMapper(); // can reuse, share //

		// numTasks = channels.size();
		asyncWorker = true;
		// for (int i = 0; i < channels.size(); ++i) {
		AsyncInvokeURLTask task = null;
		try {
			// final int index = i;
			task = new AsyncInvokeURLTask(ch.getChannelName(), context, date,
					new AsyncInvokeURLTask.OnPostExecuteListener() {
						@Override
						public void onPostExecute(String result) {
							try {
								ChannelSchedule schedule = mapper.readValue(
										result, ChannelSchedule.class);
								chData.put(schedule, ch);
								// numTasks--;
								// if (numTasks == 0) {
								Iterator itChannels = ScheduleManager.chData
										.entrySet().iterator();
								ChannelSchedule chSchedule = null;
								while (itChannels.hasNext()) {
									Map.Entry mEntry = (Map.Entry) itChannels
											.next();
									Channel mChannel = (Channel) mEntry
											.getValue();
									// check for the channel name
									// selected
									if (mChannel.getChannelName().equals(
											ch.getChannelName())) {
										// get schedule of the selected
										// channel
										chSchedule = (ChannelSchedule) mEntry
												.getKey();
										FragmentTvGuideMain.selectedChannel = ch
												.getChannelName();
										break;
									}							
								}
								ArrayList<Channel> lstChannel = new ArrayList<Channel>();
								lstChannel.add(ch);
								ArrayList<Show> upComing = new ArrayList<Show>();
								for (int i = 0; i < chSchedule.getListOfShows().size(); ++i){
									upComing.add(chSchedule.getListOfShows().get(i));
								}
								GridView gv = (GridView) FragmentTvGuideMain
										.GetParentActivity().findViewById(
												R.id.gridView);
								UpcomingAdapter imageAdapter = new UpcomingAdapter(
										FragmentTvGuideMain
												.GetParentActivity(),
										upComing,
										lstChannel, true);
								gv.setAdapter(imageAdapter);
							} catch (JsonParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JsonMappingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			task.execute();
		}
	}
}
