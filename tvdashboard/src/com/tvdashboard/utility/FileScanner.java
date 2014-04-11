package com.tvdashboard.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.tvdashboard.main.XmlParser;
import com.tvdashboard.model.Video;

public class FileScanner {
	
	Context context;
	
	public FileScanner(Context context)
	{		
		this.context = context;
	}

	public List<String> listFiles(String directoryName,ArrayList<String> extensions, ArrayList<String> filter) {

		Pattern pattern;
		Matcher matcher;
		List<String> files = new ArrayList<String>();		
		File directory = new File(directoryName);
		
//		pattern = Pattern.compile(filter);
		File[] fList = directory.listFiles();
		
		for (File file : fList) {
			
			if (file.isFile()) 
			{
				for (int k = 0; k < extensions.size(); k++)
				{
					if (file.getName().endsWith("." + extensions.get(k).toLowerCase()))
					{
						String name = file.getName();
//						matcher = pattern.matcher(name);
						files.add(file.getAbsolutePath());
						break;
					}					
				}		
			} 
			else if (file.isDirectory())
			{
				listFiles(file.getAbsolutePath(), extensions, filter);
			}			
		}		
		return files;
	}

}
