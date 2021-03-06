package com.tvdashboard.utility;

public class Preferences {

	public static String CHANNEL_ICONS_FOLDER = "icons";
	public static String FILE_MANAGER_FOLDER_ICON_1 = "icons/folder1.png";
	public static String FILE_MANAGER_FOLDER_ICON_2 = "icons/folder2.png";
	public static String FILE_MANAGER_MUSIC_ICON = "icons/sound.png";
	public static String FILE_MANAGER_VIDEO_ICON = "icons/video.png";
	public static String MUSIC_THEAUDIODB_APIKEY = "234621276b2d731671156g";
	public static String MUSIC_THEAUDIODB_BASEURL = "http://www.theaudiodb.com/api/v1/json/";
//	public static String CHANNEL_LIST_FILENAME = "tvguidechannellist.xml";

	public enum TVGuideFiles {
		CHANNEL_TVGUIDE_LIST {
			@Override
			public String getFileName() {
				return "tvguidechannellist.xml";
			}
		},
		CHANNEL_TVGUIDE_SET_NUMBER {
			@Override
			public String getFileName() {
				return "tvguidechannellist.xml";
			}
		},
		CHANNEL_TVGUIDE_NAMES {
			@Override
			public String getFileName() {
				return "tvguidechannellist.xml";
			}
		},
		CHANNEL_TVSET_NUMBERS {
			@Override
			public String getFileName() {
				return "Channels.xml";
			}
		};
		
		public abstract String getFileName();
	}
	
	
}
