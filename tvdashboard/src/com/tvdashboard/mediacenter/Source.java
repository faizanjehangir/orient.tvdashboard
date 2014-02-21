package com.tvdashboard.mediacenter;

public class Source {
	
		public static String source_path;
		public static String source_name;
		Media_source mSource;
		
		public static void main(String[] args) {
			 
			System.out.println(Media_source.Pitures.get_media_source());
	 
		}
		
		
		public Source(String name, String path) {
			Source.source_name = name;
			Source.source_path = path;
		}
		public static String getSource_path() {
			return source_path;
		}
		public static void setSource_path(String source_path) {
			Source.source_path = source_path;
		}
		public static String getSource_name() {
			return source_name;
		}
		public static void setSource_name(String source_name) {
			Source.source_name = source_name;
		}
		
		
		
		
}
