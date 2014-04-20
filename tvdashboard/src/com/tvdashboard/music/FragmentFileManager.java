package com.tvdashboard.music;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tvdashboard.channelsetup.CustomListAdapter;
import com.tvdashboard.database.R;
import com.tvdashboard.main.XmlParser;
import com.tvdashboard.utility.Preferences;

public class FragmentFileManager extends Fragment {

	private static GridView gv;
	private static CustomAdapterMusicFileManager adapter;
	public static Context context;
	private static TextView txtPath;

	public static FragmentFileManager newInstance(String num) {
		FragmentFileManager fragment = new FragmentFileManager();

		Bundle b = new Bundle();
		b.putString("fragment#", num);
		fragment.setArguments(b);

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String num = getArguments().getString("fragment#");

		context = getActivity();
		txtPath = (TextView) getActivity().findViewById(R.id.rootPath);
		ArrayList<FileManagerItem> list = initFileManager(Environment
				.getExternalStorageDirectory());
	
		gv = (GridView) getActivity().findViewById(R.id.gridView);
		adapter = new CustomAdapterMusicFileManager(getActivity(), list);
		gv.setAdapter(adapter);
		gv.setFocusable(true);

		super.onActivityCreated(savedInstanceState);
	}

	public static void resetGVAdapter(File file) {
		ArrayList<FileManagerItem> lstFiles = initFileManager(file);
		adapter = new CustomAdapterMusicFileManager(context, lstFiles);
		gv.setAdapter(adapter);
		gv.setFocusable(true);
	}

	private static ArrayList<FileManagerItem> initFileManager(File rootPath) {
		// get files from directory
		ArrayList<FileManagerItem> items = new ArrayList<FileManagerItem>();
		ArrayList<String> extensions = XmlParser.parseXml(context,
				"Extensions.xml", "Music");
		List<File> files = getListFiles(rootPath, extensions);
//		for (int i = 0; i < files.size(); ++i) {
//			Log.v("file:", files.get(i).getName());
//		}
		InputStream bitmap = null;
		FileManagerItem rootItem = new FileManagerItem();
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
			FileManagerItem item = new FileManagerItem();
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
							Preferences.FILE_MANAGER_MUSIC_ICON);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_tab_music_filemanager_layout, container,
				false);
		return view;
	}

}
