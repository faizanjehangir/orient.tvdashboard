package com.tvdashboard.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.tvdashboard.main.XmlParser;
import com.tvdashboard.model.TrackDummy;
import com.tvdashboard.model.Video;

public class FileScanner {

	Context context;

	public FileScanner(Context context) {
		this.context = context;
	}

	public List<TrackDummy> listFiles(String directoryName,	ArrayList<String> extensions, ArrayList<String> regExp) {
		Pattern pattern;
		Matcher matcher;
		List<TrackDummy> files = new ArrayList<TrackDummy>();
		ArrayList<String> baseRegExp = XmlParser.parseXml(context,
				"RegExp.xml", "BaseName");
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();

		for (File file : fList) {

			if (file.isFile()) {
				for (int k = 0; k < extensions.size(); k++) {
					if (file.getName().endsWith("." + extensions.get(k).toLowerCase())) {

						for (int i = 0; i < regExp.size(); i++) {
							pattern = Pattern.compile(regExp.get(i));
							String track =fileName(file.getName(), '.');
							matcher = pattern.matcher(track);
							if (matcher.find()) {
								String[] parts = matcher.group(0).split("-");								
								files.add(new TrackDummy(file.getParent(),	parts[0].trim(), parts[1].trim(), file.getPath()));
								break;
							}
						}
					}
				}
			} else if (file.isDirectory()) {
				listFiles(file.getAbsolutePath(), extensions, regExp);
			}
		}
		return files;
	}

	public String fileName(String fileName, char sep) { // gets filename without extension
		int dot = fileName.lastIndexOf(sep);
		return fileName.substring(0, dot);
	}

}
