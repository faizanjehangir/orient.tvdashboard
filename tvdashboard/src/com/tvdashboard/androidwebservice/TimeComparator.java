package com.tvdashboard.androidwebservice;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class TimeComparator implements Comparator<Show> {

	public SimpleDateFormat sdf;
	
	@Override
	public int compare(Show prevShow, Show nxtShow) {
		sdf = new SimpleDateFormat("HH:mm:ss");
		Timestamp timeShowPrev = null;
		Timestamp timeShowNxt = null;
		try {
			timeShowPrev = new Timestamp(sdf.parse(prevShow.getShowTime()).getTime());
			timeShowNxt = new Timestamp(sdf.parse(nxtShow.getShowTime()).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		int cmpVal = timeShowPrev.compareTo(timeShowNxt);
		return cmpVal < 0 ? -1 : (cmpVal == 0 ? 0 : 1); 
	}

}
