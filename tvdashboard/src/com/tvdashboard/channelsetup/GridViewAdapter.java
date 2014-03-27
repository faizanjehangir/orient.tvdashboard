package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tvdashboard.database.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private final List<GVShow> lstGVShow;
	

	public GridViewAdapter(Context context, List<GVShow> lstGVShow) {
		this.context = context;
		this.lstGVShow = lstGVShow;
		
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = convertView;//new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.gridview_tile_layout,parent, false);
			
			TextView txtShowName = (TextView) gridView.findViewById(R.id.ShowTitle);
			TextView txtShowDuration = (TextView) gridView.findViewById(R.id.ShowTime);
			ImageButton imgBtnShow = (ImageButton) gridView.findViewById(R.id.image);
			ProgressBar progress = (ProgressBar)gridView.findViewById(R.id.progressBar);
			if (position != 0)
			{
				progress.setVisibility(View.GONE);
				
			}
			
			FragmentTvGuideMain.viewHolder.add(new ViewHolder(txtShowName, txtShowDuration, imgBtnShow, progress));

			GVShow show = lstGVShow.get(position);
			txtShowName.setText(show.getShowName());
			ImageLoader.getInstance().displayImage(show.getShowImage(), imgBtnShow);

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return lstGVShow.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
