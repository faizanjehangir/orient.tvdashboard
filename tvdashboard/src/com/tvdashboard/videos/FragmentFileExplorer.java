package com.tvdashboard.videos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.main.XmlParser;
import com.tvdashboard.tmdb.wrapper.TMDb;
import com.tvdashboard.tmdb.wrapper.collections.MovieList;
import com.tvdashboard.utility.Preferences;

public class FragmentFileExplorer extends Fragment{
	
	private static GridView gv;
	private static CustomAdapterVideoFileManager adapter;
	public static Context context;
	private static TextView txtPath;
	
	public static FragmentFileExplorer newInstance() {
		FragmentFileExplorer fragment = new FragmentFileExplorer();
        
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		context = getActivity();
		txtPath = (TextView) getActivity().findViewById(R.id.rootPath);
		ArrayList<VideoFileItem> list = initFileManager(Environment
				.getExternalStorageDirectory());
	
		gv = (GridView) getActivity().findViewById(R.id.gridView);
		adapter = new CustomAdapterVideoFileManager(getActivity(), list);
		gv.setAdapter(adapter);
		gv.setFocusable(true);
		
		super.onActivityCreated(savedInstanceState);
	}
	
	public static void resetGVAdapter(File file) {
		ArrayList<VideoFileItem> lstFiles = initFileManager(file);
		adapter = new CustomAdapterVideoFileManager(context, lstFiles);
		gv.setAdapter(adapter);
		gv.setFocusable(true);
	}

	private static ArrayList<VideoFileItem> initFileManager(File rootPath) {
		// get files from directory
		ArrayList<VideoFileItem> items = new ArrayList<VideoFileItem>();
		ArrayList<String> extensions = XmlParser.parseXml(context,
				"Extensions.xml", "Videos");
		List<File> files = getListFiles(rootPath, extensions);
//		for (int i = 0; i < files.size(); ++i) {
//			Log.v("file:", files.get(i).getName());
//		}
		InputStream bitmap = null;
		VideoFileItem rootItem = new VideoFileItem();
		if (!(rootPath.getParent().equals("/"))){
			rootItem.setFile(rootPath.getParentFile());
			
			try {
				bitmap = context.getAssets().open(
						Preferences.FILE_MANAGER_FOLDER_ICON_2);
				rootItem.setFileIcon(BitmapFactory.decodeStream(bitmap));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			items.add(rootItem);
		}	
		for (File obj : files) {
			if (!obj.canRead())
				continue;
			VideoFileItem item = new VideoFileItem();
			bitmap = null;
			if (obj.isDirectory()) {
				try {
					bitmap = context.getAssets().open(
							Preferences.FILE_MANAGER_FOLDER_ICON_1);
					item.setFileIcon(BitmapFactory.decodeStream(bitmap));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				try {
					bitmap = context.getAssets().open(
							Preferences.FILE_MANAGER_VIDEO_ICON);
					item.setFileIcon(BitmapFactory.decodeStream(bitmap));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			item.setFile(obj);
			items.add(item);
		}
		
		//set path here
		setFilePath(rootPath.getAbsolutePath());

		return items;
	}
	
	public static void setFilePath(String path){
		txtPath.setText("PATH: " + path);
	}

	private static List<File> getListFiles(File parentDir,
			ArrayList<String> extensions) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		if (files == null)
			return inFiles;
		for (File file : files) {
			if (file.isDirectory()) {
				inFiles.add(file);
			} else {
				for (int i = 0; i < extensions.size(); ++i) {
					String name = file.getName().toLowerCase();
					if (name.endsWith("." + extensions.get(i).toLowerCase())) {
						inFiles.add(file);
					}
				}

			}
		}
		return inFiles;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_videos_filemanager_layout, container, false);
		return view;
	}

}
