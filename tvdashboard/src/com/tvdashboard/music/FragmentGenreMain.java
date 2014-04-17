package com.tvdashboard.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tvdashboard.database.R;
import com.tvdashboard.tmdb.wrapper.TMDb;

public class FragmentGenreMain extends Fragment implements OnClickListener{
	
	public LinearLayout layoutContent1;
	public LinearLayout layoutOptions;	
	private ImageButton [] images;
	private RelativeLayout [] rl;
	private TextView [] txtName;
	private TMDb tmdb;
	private ImageLoader imageLoader;
	private int j;
	private int fragmentNum;
	private View view;
	
	private static final int[] imageidArray =
		{
			R.id.image1,R.id.image2,R.id.image3,
			R.id.image4,R.id.image5,R.id.image6,
			R.id.image7,R.id.image8,R.id.image9,
			R.id.image10,R.id.image11,R.id.image12 };
	
	private static final int[] rlidArray =
		{
			R.id.rl1,R.id.rl2,R.id.rl3,
			R.id.rl4,R.id.rl5,R.id.rl6,
			R.id.rl7,R.id.rl8,R.id.rl9,
			R.id.rl10,R.id.rl11,R.id.rl12 };
	
	private static final int[] txtNameIdArray =
		{
			R.id.txtName1,R.id.txtName2,R.id.txtName3,
			R.id.txtName4,R.id.txtName5,R.id.txtName6,
			R.id.txtName7,R.id.txtName8,R.id.txtName9,
			R.id.txtName10,R.id.txtName11,R.id.txtName12 };
	
	public FragmentGenreMain newInstance(String num) {
		FragmentGenreMain fragment = new FragmentGenreMain();

		Bundle b = new Bundle();
        b.putString("fragment#", num);
        fragment.setArguments(b);
		
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		fragmentNum = Integer.valueOf(getArguments().getString("fragment#"));
        j=0;
        tmdb = new TMDb();
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
		ImageLoader.getInstance().init(config);
		
		if (fragmentNum == 0)
		{
			layoutContent1.setVisibility(View.GONE);
		}
		else
		{
			layoutOptions.setVisibility(View.GONE);
		}	
		
		if (fragmentNum == 0)
		{
			j=3;
		}
		for (int i = (fragmentNum * MusicSection.totalItems); i< TabGenre.listGenre.size();i++,j++)
		{
//			imageLoader.displayImage(TabAlbum.allAlbums.getAlbum().get(i).getStrAlbumThumb()+"/preview", images[j]);
			rl[j].setVisibility(View.VISIBLE);
			txtName[j].setText(TabGenre.listGenre.get(i));		
		}
		j = checkJ(fragmentNum);
		for (int i = 0; i< TabGenre.listGenre.size();i++,j++)
		{
			final int temp = i;
			final ImageButton imageBtn = images[j];
			imageBtn.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), MusicGenreDetails.class);
					intent.putExtra("strGenre", TabGenre.listGenre.get((fragmentNum * MusicSection.totalItems)+temp));
					startActivity(intent);
				}
			});
		}
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_music_genre_layout, container, false);
		layoutContent1 = (LinearLayout)view.findViewById(R.id.LayoutContent1);
		layoutOptions = (LinearLayout)view.findViewById(R.id.MusicOptions);
		rl = new RelativeLayout[MusicSection.totalItems];
		txtName = new TextView[MusicSection.totalItems];
		if (TabGenre.listGenre.size() > (MusicSection.totalItems * (fragmentNum+1)))
		{
			images = new ImageButton[MusicSection.totalItems];
		}
		else
		{
			if (fragmentNum==0)
			{
				images = new ImageButton[(MusicSection.totalItems * fragmentNum)+TabGenre.listGenre.size()+3];
			}
			else{
			images = new ImageButton[(MusicSection.totalItems * fragmentNum)+TabGenre.listGenre.size()];
			}
		}
		declareImageButtons(images, view);
		declareRelativeLayouts (rl, view);
		declareTextViews (txtName, view);
		return view;
	}	
	
	public void declareImageButtons (ImageButton [] images, View view)
	{
		for (int i=0; i<images.length;i++)
		{
			images[i] = (ImageButton)view.findViewById(imageidArray[i]);
		}
	}
	
	public void declareRelativeLayouts (RelativeLayout [] rl, View view)
	{
		for (int i=0; i<rl.length;i++)
		{
			rl[i] = (RelativeLayout)view.findViewById(rlidArray[i]);
		}
	}	
	
	public void declareTextViews (TextView [] txtName, View view)
	{
		for (int i=0; i<txtName.length;i++)
		{
			txtName[i] = (TextView)view.findViewById(txtNameIdArray[i]);
		}
	}
	public int checkJ(int fragmentNum)
	{
		if (fragmentNum == 0)
			return 3;
		else
			return 0;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
