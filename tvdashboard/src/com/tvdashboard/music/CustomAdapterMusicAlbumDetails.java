package com.tvdashboard.music;

import com.tvdashboard.database.R;
import com.tvdashboard.music.manager.MusicTracks;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapterMusicAlbumDetails extends BaseAdapter implements OnClickListener{
	
	public Resources res;
	private static LayoutInflater inflater=null;
	private Activity activity;
	private MusicTracks allTracks;
	
	public CustomAdapterMusicAlbumDetails(MusicTracks allTracks, Activity activity, Resources res)
	{
		this.allTracks = allTracks;
		this.activity = activity;
		this.res = res;
		inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return allTracks.getTrack().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static class ViewHolder{
        
        public TextView title;
 
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        ViewHolder holder;
         
        if(convertView==null){
             
            vi = inflater.inflate(R.layout.music_album_detail_list_row, null);
             

            holder = new ViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.title);
            vi.setTag( holder );
        }
        else 
        	
            holder=(ViewHolder)vi.getTag();
            holder.title.setText(allTracks.getTrack().get(position).getStrTrack());
            
            vi.setOnClickListener(new OnItemClickListener( position ));
            
        return vi;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private class OnItemClickListener  implements OnClickListener{           
        private int mPosition;
         
        OnItemClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View arg0) {
   
          MusicAlbumDetails albumDetails = (MusicAlbumDetails)activity;

          albumDetails.onItemClick(mPosition);
        }               
    }  
	
}
