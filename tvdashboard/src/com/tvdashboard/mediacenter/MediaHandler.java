package com.tvdashboard.mediacenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.model.VideoCategory;

public class MediaHandler {
	
	public MediaHandler(){

	}
	
	public List<VideoCategory> InitVideoCategories(Context context){
		List<VideoCategory> lstVc = new ArrayList<VideoCategory>();
		VideoCategory vcMovies = new VideoCategory();
		vcMovies.setName("Movie");
		VideoCategory vcShows = new VideoCategory();
		vcShows.setName("TVShow");
		VideoCategory vcMusic = new VideoCategory();
		vcMusic.setName("Music Video");
		
		DatabaseHelper helper = new DatabaseHelper(context);
		/*vcMovies.setId((int) helper.createVideoCategory(vcMovies));
		vcShows.setId((int) helper.createVideoCategory(vcShows));
		vcMusic.setId((int) helper.createVideoCategory(vcMusic));*/
	
		lstVc.add(vcMovies);
		lstVc.add(vcShows);
		lstVc.add(vcMusic);

		return lstVc;
	}
}
