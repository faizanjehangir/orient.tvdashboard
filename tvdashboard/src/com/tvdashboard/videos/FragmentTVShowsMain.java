package com.tvdashboard.videos;

import java.io.File;
import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.tmdb.wrapper.TMDb;
import com.tvdashboard.tmdb.wrapper.collections.MovieList;

public class FragmentTVShowsMain extends Fragment{
	
	public static LinearLayout layoutContent01;
	public static LinearLayout layoutOptions;
	private DatabaseHelper dbHelper;
	public static ImageButton [] images;
	private static final int[] idArray =
		{
		R.id.image1,R.id.image2,R.id.image3,
		R.id.image4,R.id.image5,R.id.image6,
		R.id.image7,R.id.image8,R.id.image9,
		R.id.image10,R.id.image11,R.id.image12 };
	
	private TMDb tmdb;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private static int index;
	
	public static FragmentTVShowsMain newInstance(String num) {
		FragmentTVShowsMain fragment = new FragmentTVShowsMain();
		
		Bundle b = new Bundle();
        b.putString("fragment#", num);
        fragment.setArguments(b);
        
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String num = getArguments().getString("fragment#");
		if (num.equals("0"))
		{
			layoutContent01.setVisibility(View.GONE);
		}
		else
		{
			layoutOptions.setVisibility(View.GONE);
		}
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		tmdb = new TMDb();
		dbHelper = new DatabaseHelper(getActivity());
		if (TabTVShows.allTvShows.size() > (12 * Integer.valueOf(getArguments().getString("fragment#")+1)))
		{
			images = new ImageButton[12];
		}
		else
		{
			images = new ImageButton[(12 * Integer.valueOf(getArguments().getString("fragment#")))+TabTVShows.allTvShows.size()];
		}
		declareImageButtons(images);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
		ImageLoader.getInstance().init(config);
//		getImages();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_tvshows_layout, container, false);		
		layoutContent01 = (LinearLayout)view.findViewById(R.id.LayoutContent01);
		layoutOptions = (LinearLayout)view.findViewById(R.id.VideosOptions);
		return view;
	}
	
	public void declareImageButtons (ImageButton [] images)
	{
		for (int i=0; i<images.length;i++)
		{
			images[i] = (ImageButton)getActivity().findViewById(idArray[i]);
		} 
	}
	
	public void getImages()
	{
		for (int i = 0; i < images.length; i++)
		{
			File file = new File (TabTVShows.allTvShows.get(i).getPath());
			new GetMovieResult().execute(file.getName().toString());
		}
	}
	
	private class GetMovieResult extends AsyncTask<String, Void, String> {
		TMDb tmdb = new TMDb();

		@Override
		protected String doInBackground(String... params) {
			String data = "";
			String movieTitle = params[0];
			try {
				data = tmdb.SearchMovieByTitle(movieTitle);
				Log.d("Movies", data);
				return data;
			} catch (IOException e) {
				Log.d("ERROR", e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			ObjectMapper mapper = new ObjectMapper();
			MovieList moviesList = null;
			try {
				if (!result.isEmpty())
					moviesList = mapper.readValue(result, MovieList.class);
			} catch (JsonParseException e) {
				Log.d("ERROR", e.getMessage());
				e.printStackTrace();
			} catch (JsonMappingException e) {
				Log.d("ERROR", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("ERROR", e.getMessage());
				e.printStackTrace();
			}
			
			String url = tmdb.GetMoviePosterUrl(moviesList.getResults().get(1));		
			imageLoader.displayImage(url, images[index]);
		}

	}

}
