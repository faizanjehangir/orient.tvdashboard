package com.tvdashboard.music.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.helper.Media_source;
import com.tvdashboard.helper.Source;
import com.tvdashboard.model.TrackDummy;
import com.tvdashboard.utility.Preferences;

public class MusicController {

	private List<TrackDummy> files;
	private static MusicTracks tracks;
	private static DatabaseHelper db;
	private Context context;

	public MusicController(List<TrackDummy> files, Context context) {
		this.files = files;
		tracks = new MusicTracks();		
		this.context = context;
		db = new DatabaseHelper(context);
		new parseTrackData(files, context).execute();
	}

	public class parseTrackData extends AsyncTask<Void, Void, String> {
		private List<TrackDummy> files;
		private TrackDummy musicObj;
		private MusicAlbums albums;
		private MusicArtist artists;
		private Context context;

		public parseTrackData(List<TrackDummy> files, Context context) {
			this.files = files;
			this.context = context;			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
//			MusicTracks allT = db.getAllMusicTracks();
//			MusicAlbums allAl = db.getAllMusicAlbums();
//			MusicArtist allAr = db.getAllMusicArtists();
			for (int i = 0; i < files.size(); i++) {				
				musicObj = encodeParameters(files.get(i));
				String result = "";
				String trackUrl = Preferences.MUSIC_THEAUDIODB_BASEURL + Preferences.MUSIC_THEAUDIODB_APIKEY+ "/searchtrack.php?s=" + this.musicObj.getArtist() + "&t="	+ musicObj.getTrackName();
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();

				HttpGet httpget = new HttpGet(trackUrl);

				try {
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream inStream = entity.getContent();
						result = convertStreamToString(inStream);
						final ObjectMapper mapper = new ObjectMapper();
						try {
							JSONObject json = new JSONObject(result);
							String array = json.getString("track");
							array.length();
							if (array.length() > 4) {
								tracks = mapper.readValue(result, MusicTracks.class);
								String albumUrl = Preferences.MUSIC_THEAUDIODB_BASEURL + Preferences.MUSIC_THEAUDIODB_APIKEY+ "/album.php?m="+tracks.getTrack().get(0).getIdAlbum();
								String artistUrl = Preferences.MUSIC_THEAUDIODB_BASEURL + Preferences.MUSIC_THEAUDIODB_APIKEY+ "/artist.php?i="+tracks.getTrack().get(0).getIdArtist();
								albums = new MusicAlbums();
								artists = new MusicArtist();
								albums =(MusicAlbums)getAlbumNArtist (albumUrl, "Album");
								artists =(MusicArtist)getAlbumNArtist(artistUrl, "Artist");
								db.addMusicTrack(tracks,files.get(i).getRealPath(), 0, 1);
								db.addMusicAlbum(albums, 0, 1);
								db.addMusicArtist(artists, 0, 1);
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
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

//		private String ConstructHTTPGetParams(String trackUrl) {
//
//			return trackUrl + Preferences.MUSIC_THEAUDIODB_APIKEY+ "/searchtrack.php?s=" + this.musicObj.getArtist() + "&t="	+ musicObj.getTrackName();
//		}

	}
	
	private static Object getAlbumNArtist (String url, String category)
	{
		MusicAlbums albums = new MusicAlbums();
		MusicArtist artists = new MusicArtist();
		String result = "";
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(url);

		try {
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inStream = entity.getContent();
				result = convertStreamToString(inStream);
				final ObjectMapper mapper = new ObjectMapper();
				try {
					if (category == "Album")
					{
						try {
						albums = mapper.readValue(result, MusicAlbums.class);
						return albums;
						}catch (Exception e)
						{}
					}
					if (category == "Artist")
					{
						artists = mapper.readValue(result, MusicArtist.class);
						return artists;
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
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
				
	}
	
	
	private static String convertStreamToString(InputStream is) 
	{
		BufferedReader streamReader = null;
		try {
			streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuilder responseStrBuilder = new StringBuilder();

		String inputStr;
		try {
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return responseStrBuilder.toString();
	}
	
	private static TrackDummy encodeParameters(TrackDummy track)
	{
		try {
			track.setArtist(URLEncoder.encode(track.getArtist(), "UTF-8"));
			track.setTrackName(URLEncoder.encode(track.getTrackName(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return track;
	}
	

}
