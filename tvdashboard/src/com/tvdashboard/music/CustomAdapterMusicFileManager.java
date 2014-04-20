package com.tvdashboard.music;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.database.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomAdapterMusicFileManager extends BaseAdapter {
	private Context mContext;
	LayoutInflater inflater;
	private ArrayList<FileManagerItem> items;

	public CustomAdapterMusicFileManager(Context context,
			ArrayList<FileManagerItem> items) {
		this.mContext = context;
		this.items = items;
		inflater = (LayoutInflater) this.mContext
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	static class ViewHolder {

		public ImageButton image;
		public TextView txtFile;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.music_gridview_filemanager,
					null);
			holder = new ViewHolder();
			holder.image = (ImageButton) convertView.findViewById(R.id.imgIcon);
			holder.txtFile = (TextView) convertView
					.findViewById(R.id.txtFilename);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final int i = position;
		holder.image.setImageBitmap(this.items.get(position).getFileIcon());
		holder.image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// get file here
				if (items.get(i).getFile().isFile()) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.fromFile(items.get(i).getFile()),
							URLConnection.guessContentTypeFromName(items.get(i)
									.getFile().getName()));
					mContext.startActivity(intent);
				} else {
					// call fragment to reset adapter
					FragmentFileManager.resetGVAdapter(items.get(i).getFile());
				}
			}
		});
		if (position == 0) {
			holder.txtFile.setText("...");
		} else
			holder.txtFile
					.setText(this.items.get(position).getFile().getName());

		return convertView;
	}
}
