package com.tvdashboard.androidwebservice;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.tvdashboard.channelsetup.Channel;
import com.tvdashboard.utility.Preferences;
import com.tvdashboard.utility.Preferences.TVGuideFiles;

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
	private ArrayList<String> channelNumbers;
	private TVGuideFiles enumChannel;
	private HashMap<String, ArrayList<String>> channelNames;

	public XMLChannelManager(Context context, TVGuideFiles enumChannel) {
		this.context = context;
		this.enumChannel = enumChannel;
		this.fileName = this.enumChannel.getFileName();
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

	private void parseXMLAndStoreIt(XmlPullParser myParser) {
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
					if (name.equals("channel")) {
						channel = new Channel();
					}
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;

				case XmlPullParser.END_TAG:
					if (name.equals("region")) {
						this.channelListData.put(region, regionChannels);
					} else if (name.equals("name")) {
						channel.setChannelName(text);
					} else if (name.equals("icon")) {
						// get channel icon from name
						channel.setChannelIcon(getChannelIcon(text));
					} else if (name.equals("number")) {
						channel.setChannelNum(text);
					} else if (name.equals("channel")) {
						regionChannels.add(channel);
					} else {
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

	private void parseChannelNamesXMLAndStore(XmlPullParser myParser) {
		int event;
		String text = null;
		this.channelNames = new HashMap<String, ArrayList<String>>();
		ArrayList<String> lstChannels = null;
		String region = null;
		try {
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:
					if (name.equals("region")) {
						region = myParser.getAttributeValue(null, "value");
						lstChannels = new ArrayList<String>();
					}
					break;
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;

				case XmlPullParser.END_TAG:
					if (name.equals("region")) {
						this.channelNames.put(region, lstChannels);
					}
					if (name.equals("name")) {
						lstChannels.add(text);
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

	public void channelXMLNumberStore(Channel channel) {
		FileOutputStream fOut = null;
		File file = context.getFileStreamPath(fileName);
		InputStream stream = null;
		boolean isUpdate = true;
		// if file does not exits
		if (!file.exists()) {
			isUpdate = false;
			try {
				fOut = context.openFileOutput(fileName,
						context.MODE_WORLD_READABLE);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stream = context.getAssets().open(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			isUpdate = true;
			Log.v("error", "file already exists");
			FileInputStream fIn = null;
			try {
				fIn = context.openFileInput(fileName);
				stream = new BufferedInputStream(fIn);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Document doc = null;
		try {
			doc = docBuilder.parse(stream);
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// edit the stream to change the channel number
		// Change the content of node
		NodeList nodeList = doc.getElementsByTagName("*");
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node node = nodeList.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            // do something with the current element
	            if (node.getTextContent().equals(channel.getChannelName())){
	            	node.getNextSibling().getNextSibling().setTextContent(channel.getChannelNum());
	            	break;
	            }
	        }
	    }
	    
	    Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	    // initialize StreamResult with File object to save to file
	    StreamResult result = new StreamResult(file);
	    
//	    if (!isUpdate){
//	    	OutputStreamWriter osw = new OutputStreamWriter(fOut);
//
//			// Write the string to the file
//			try {
//				osw.write(result.toString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			/*
//			 * ensure that everything is really written out and close
//			 */
//			try {
//				osw.flush();
//				osw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	    }
//	    else{
	    	DOMSource source = new DOMSource(doc);
		    try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
//	    }
	}

	private void parseChannelXMLAndStore(XmlPullParser myParser) {
		int event;
		String text = null;
		this.channelNumbers = new ArrayList<String>();
		try {
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event) {
				case XmlPullParser.TEXT:
					text = myParser.getText();
					break;

				case XmlPullParser.END_TAG:
					if (name.equals("number")) {
						this.channelNumbers.add(text);
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

	public HashMap<String, ArrayList<String>> getAllChannelNamesByRegions() {
		return this.channelNames;
	}

	public ArrayList<String> getAllChannelNumbers() {
		return this.channelNumbers;
	}

	private Bitmap getChannelIcon(String imageName) {
		String fileList[] = null;
		try {
			fileList = this.context.getAssets().list(
					Preferences.CHANNEL_ICONS_FOLDER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				Log.d("", fileList[i]);
				if (fileList[i].equals(imageName)) {
					InputStream bitmap = null;
					try {
						bitmap = this.context.getAssets().open(
								Preferences.CHANNEL_ICONS_FOLDER + "/"
										+ imageName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return BitmapFactory.decodeStream(bitmap);
				}
			}
		}

		return null;
	}

	public void fetchXML() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream stream = null;
					File file = context.getFileStreamPath(fileName);
					// if file does not exist in system
					if (!file.exists()) {
						stream = context.getAssets().open(fileName);
					} else {
						stream = new FileInputStream(file);
					}
					
//					InputStream temp = stream;
//					java.util.Scanner s = new java.util.Scanner(temp).useDelimiter("\\A");
//					Log.v("stream", s.hasNext() ? s.next() : "");
//					
//					if (!file.exists()) {
//						stream = context.getAssets().open(fileName);
//					} else {
//						stream = new FileInputStream(file);
//					}
					

					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myparser = xmlFactoryObject.newPullParser();

					myparser.setFeature(
							XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myparser.setInput(stream, null);
					switch (enumChannel) {
					case CHANNEL_TVGUIDE_LIST:
						parseXMLAndStoreIt(myparser);
						break;
					case CHANNEL_TVSET_NUMBERS:
						parseChannelXMLAndStore(myparser);
						break;
					case CHANNEL_TVGUIDE_NAMES:
						parseChannelNamesXMLAndStore(myparser);
						break;
					default:
						break;
					}
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}
}
