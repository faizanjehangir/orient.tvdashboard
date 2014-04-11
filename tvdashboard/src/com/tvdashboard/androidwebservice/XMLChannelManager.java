package com.tvdashboard.androidwebservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.tvdashboard.channelsetup.Channel;
import com.tvdashboard.utility.Preferences;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class XMLChannelManager {

	private Context context;
	private String fileName;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;
	private HashMap<String, ArrayList<Channel>> channelListData;

	public XMLChannelManager(Context context, String channelListFileName) {
		this.context = context;
		this.fileName = channelListFileName;
	}

	public ArrayList<Channel> getChannelListByRegion(String region) {
		return this.channelListData.get(region);
	}

	public ArrayList<Channel> getAllChannels() {
		ArrayList<Channel> allChannels = new ArrayList<Channel>();
		for (String key : this.channelListData.keySet()) {
			ArrayList<Channel> regionChannels = this.channelListData.get(key);
			for (int i = 0; i < regionChannels.size(); ++i)
				allChannels.add(regionChannels.get(i));
		}
		return allChannels;
	}
	
	public List<String> getAllRegions()
	 {
	  List<String> regions = new ArrayList<String>();
	  regions.add("All");
	  ArrayList<Channel> allChannels = new ArrayList<Channel>();
	  for (String key : this.channelListData.keySet()) {
	   regions.add(key);
	  }
	  return regions;
	 }

	public void parseXMLAndStoreIt(XmlPullParser myParser) {
		int event;
		String text = null;
		channelListData = new HashMap<String, ArrayList<Channel>>();
		try {
			event = myParser.getEventType();
			String region = null;
			ArrayList<Channel> regionChannels = null;
			String currentNode = null;
			Channel channel = null;
			
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					if (name.equals("region")) {
						region = myParser.getAttributeValue(null, "value");
						regionChannels = new ArrayList<Channel>();
					}
					if (name.equals("channel")){
						channel = new Channel();
					}
					break;
				case XmlPullParser.TEXT:
					 text = myParser.getText();
					break;

				case XmlPullParser.END_TAG:
					if (name.equals("region")) {
						this.channelListData.put(region, regionChannels);
					} 
					else if (name.equals("name")){
						channel.setChannelName(text);
					}
					else if (name.equals("icon")){
						//get channel icon from name				
						channel.setChannelIcon(getChannelIcon(text));
					}
					else if (name.equals("number")){
						channel.setChannelNum(text);
					}
					else if (name.equals("channel")){
						regionChannels.add(channel);
					}
					else {
					}
					break;
				}
				event = myParser.next();

			}
			parsingComplete = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Bitmap getChannelIcon(String imageName) {
//		File file = new File(Environment.getExternalStorageDirectory()
//				+ Preferences.CHANNEL_ICONS_FOLDER);
        String fileList[] = null;
		try {
			fileList = this.context.getAssets().list(Preferences.CHANNEL_ICONS_FOLDER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				Log.d("", fileList[i]);
				if (fileList[i].equals(imageName)){
					InputStream bitmap = null;
					try {
						bitmap = this.context.getAssets().open(Preferences.CHANNEL_ICONS_FOLDER + "/" + imageName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return BitmapFactory.decodeStream(bitmap);
				}
			}
		}
//		File imageList[] = file.listFiles();
//
//		for (int i = 0; i < imageList.length; i++) {
//			Log.e("Image: " + i + ": path", imageList[i].getAbsolutePath());
//			if (imageList[i].getName().equals(imageName))
//				return BitmapFactory.decodeFile(imageList[i].getAbsolutePath());
//		}
		return null;
	}

	public void fetchXML() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream stream = context.getAssets().open(fileName);

					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myparser = xmlFactoryObject.newPullParser();

					myparser.setFeature(
							XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myparser.setInput(stream, null);
					parseXMLAndStoreIt(myparser);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

}
