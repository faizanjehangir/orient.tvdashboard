package com.tvdashboard.channelsetup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tvdashboard.androidwebservice.ChannelManager;
import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.database.R;

public class FragmentTvGuideMain extends Fragment {

	ListView list;
	public static CustomListAdapter adapter;
	public static Context context;

	View view;
	private static Button txtDate;
	static final int DATE_DIALOG_ID = 999;

	public static String selectedChannel = null;
	static String selectedDate;
	public static boolean dateChanged = false;
	public static boolean isTodayDate = true;
	
	private Spinner spinnerRegions;
	 CustomSpinnerAdapter spinnerAdapter;
	 ChannelManager channelManager;
	 List<String> regions;

	// public FragmentTvGuideMain CustomListView = null;
	public ArrayList<Channel> CustomListViewValuesArr = new ArrayList<Channel>();

	public static FragmentTvGuideMain newInstance(String num) {
		FragmentTvGuideMain fragment = new FragmentTvGuideMain();

		Bundle b = new Bundle();
		b.putString("fragment#", num);
		fragment.setArguments(b);

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		context = getActivity();
		channelManager = new ChannelManager(context);
		regions = channelManager.getAllRegions();
		spinnerRegions = (Spinner)getActivity().findViewById(R.id.Spinner_Region);
		  Resources res = getResources(); 
		  spinnerAdapter = new CustomSpinnerAdapter(context, R.layout.spinner_region_row, regions,res);
		  spinnerRegions.setAdapter(spinnerAdapter);
		spinnerRegions.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v,
					int position, long id) {
				ImageView image = (ImageView) getActivity().findViewById(
						R.id.image);
				image.setVisibility(View.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// get current date time with Date()
		Date date = new Date();
		txtDate = (Button) getActivity().findViewById(R.id.datepicker);
		txtDate.setText(dateFormat.format(date));
		txtDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(view);

			}
		});

		String num = getArguments().getString("fragment#");

		setListData();
		res = getResources();
		list = (ListView) getActivity().findViewById(R.id.list);
		adapter = new CustomListAdapter(getActivity(), CustomListViewValuesArr,
				res);
		list.setAdapter(adapter);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	/****** Function to set data in ArrayList *************/
	public void setListData() {
		Iterator itSchedule = ScheduleManager.schedules.entrySet().iterator();
		while (itSchedule.hasNext()) {
			Map.Entry mEntry = (Map.Entry) itSchedule.next();
			ChannelSchedule mChannelSchedule = (ChannelSchedule) mEntry
					.getKey();
			CustomListViewValuesArr.add(ScheduleManager.channelData
					.get(mChannelSchedule));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tab_tvguide_layout,
				container, false);
		return view;
	}

	public static Activity GetParentActivity() {
		return (Activity) context;
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker

			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			
			Calendar now = Calendar.getInstance();
			Date dt = new Date();
			if (now.get(Calendar.DAY_OF_MONTH) == day && now.get(Calendar.MONTH) == month
					&& now.get(Calendar.YEAR) == year)
				FragmentTvGuideMain.isTodayDate = true;
			else
				FragmentTvGuideMain.isTodayDate = false;

			month++;
			String strDay = String.valueOf(day);
			String strMonth = String.valueOf(month);
			if (day < 10)
				strDay = "0" + strDay;
			if (month < 10)
				strMonth = "0" + strMonth;
			txtDate.setText(strDay + "/" + strMonth + "/" + year);
			dateChanged = true;
			selectedDate = strDay + strMonth + String.valueOf(year);
			// if channel is selected get channel data on that date
			if (FragmentTvGuideMain.selectedChannel != null) {
				// generate click event on the selected channel
				adapter.getView(CustomListAdapter.selPosition, null, null)
						.performClick();
			}
		}
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getChildFragmentManager(), "datePicker");
	}

}
