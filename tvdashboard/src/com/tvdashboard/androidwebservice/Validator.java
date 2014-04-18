package com.tvdashboard.androidwebservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.tvdashboard.channelsetup.Channel;
import com.tvdashboard.channelsetup.FragmentTvGuideMain;
import com.tvdashboard.channelsetup.SectionChannelSetup;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class Validator implements AutoCompleteTextView.Validator {

	private String[] validList;
	private String valType;
	private Context con;
	private HashMap<String, ArrayList<String>> regionChannels;

	public Validator(Context context, String validatorType, String[] lstString) {
		this.validList = lstString;
		this.valType = validatorType;
		this.con = context;
		
		if (this.valType.equals("region")) {
			ChannelManager cm = new ChannelManager(this.con);
			this.regionChannels = cm.getAllChannelNamesByRegion();
		}
	}

	@Override
	public CharSequence fixText(CharSequence arg0) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean isValid(CharSequence text) {
		// TODO Auto-generated method stub
		Arrays.sort(this.validList);
		if (Arrays.binarySearch(this.validList, text.toString()) > -1) {
			// load channels for the region
			if (this.valType.equals("region")) {
				String[] chName = null;
				if (text.toString().equals("All")) {
					ArrayList<String> allChannels = new ArrayList<String>();
					Iterator it = this.regionChannels.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry mEntry = (Map.Entry) it.next();
						String strRegion = (String) mEntry.getKey();
						ArrayList<String> regionChannels = this.regionChannels.get(strRegion);
						for (int i = 0; i < regionChannels.size(); ++i){
							allChannels.add(regionChannels.get(i));
						}
					}
					chName = allChannels.toArray(new String[allChannels.size()]);

				} else {
					ArrayList<String> channels = this.regionChannels.get(text.toString());
					chName = channels.toArray(new String[channels.size()]);
				}
				ArrayAdapter autocompletetextAdapter = new ArrayAdapter<String>(
						this.con, android.R.layout.simple_dropdown_item_1line,
						chName);
				SectionChannelSetup.selectChannel
						.setAdapter(autocompletetextAdapter);
				SectionChannelSetup.selectChannel.setValidator(new Validator(
						this.con, "channel", chName));
			}
			return true;
		}

		return false;
	}

}
