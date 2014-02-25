package com.tvdashboard.main;

import java.io.File;
import java.util.ArrayList;

import com.orient.menu.directorylist.DirectoryItems;
import com.orient.menu.directorylist.DirectoryListAdapter;
import com.tvdashboard.database.R;
import com.tvdashboard.music.MusicSection;
import com.tvdashboard.pictures.PictureSection;
import com.tvdashboard.videos.VideoSection;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectedDirectoryListFragment extends Fragment {

	private static ListView lv;
	private static DirectoryListAdapter adapter;
	private static ArrayList<DirectoryItems> allFiles;
	public static File file;
	public static String path;
	public static View view;
	public static String calledBy;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		allFiles = new ArrayList<DirectoryItems>();
		file = new File(Environment.getExternalStorageDirectory().toString());
		path = file.getAbsolutePath();
		File[] list = file.listFiles();
		initializeDirList(list);		
		lv = (ListView) getActivity().findViewById(R.id.directoryList);
		adapter = new DirectoryListAdapter(this.getActivity(),R.id.directoryList, allFiles);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (position == 0) {
					if (!(path.equals("/"))) {
						path = file.getParent();
						file = new File(path);
						File temp_file = new File(path);
						File list[] = temp_file.listFiles();
						initializeDirList(list);
						adapter = new DirectoryListAdapter(getActivity(),
								R.id.directoryList, allFiles);
						lv.setAdapter(adapter);
					}
				} 
				else 
				{
					File temp_file = new File(file,allFiles.get(position).getTitle());
					if (!temp_file.isFile()) 
					{
						file = new File(file, allFiles.get(position).getTitle());
						if(calledBy=="PictureSection"){
							PictureSection.browseText.setText(file.getAbsolutePath());
						}
						else if(calledBy=="VideoSection"){
							VideoSection.browseText.setText(file.getAbsolutePath());
						}
						else if(calledBy=="MusicSection"){
							MusicSection.browseText.setText(file.getAbsolutePath());
						}
						path = file.getAbsolutePath();
						File list[] = file.listFiles();	
						initializeDirList(list);			
						adapter = new DirectoryListAdapter(getActivity(),R.id.directoryList, allFiles);
						lv.setAdapter(adapter);
					}
				}

			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.selected_dir, container, false);		
		return view;
	}
	
	public void introduce(String intro){
		calledBy = intro;
	}
	
	public void updateDirectoryList (File [] list){
		if (list != null) {
			for (File f : list) {
				if (!f.isFile())
					allFiles.add(new DirectoryItems(f.getName()));
			}
		}
	}
	
	public void refresh()
	{
		File[] list = file.listFiles();
		initializeDirList(list);
		adapter = new DirectoryListAdapter(this.getActivity(),R.id.directoryList, allFiles);
		lv.setAdapter(adapter);
	}
	
	public void initializeDirList(File [] list)
	{
		allFiles.clear();
		allFiles.add(new DirectoryItems("..."));
		updateDirectoryList(list);
	}
	

}
