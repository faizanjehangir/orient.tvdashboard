package com.tvdashboard.channelsetup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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

import com.orient.menu.animations.ExpandAnimationRTL;
import com.tvdashboard.androidwebservice.ChannelManager;
import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.androidwebservice.Validator;
import com.tvdashboard.androidwebservice.XMLChannelManager;
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
	public static boolean isValueValid = false;

	private Spinner spinnerRegions;
	CustomSpinnerAdapter spinnerAdapter;
	ChannelManager channelManager;
	List<String> regions;

	// public FragmentTvGuideMain CustomListView = null;
	public ArrayList<Channel> CustomListViewValuesArr;

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
		spinnerRegions = (Spinner) getActivity().findViewById(
				R.id.Spinner_Region);
		Resources res = getResources();
		spinnerAdapter = new CustomSpinnerAdapter(context,
				R.layout.spinner_region_row, regions, res);
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

//		setListData();

		res = getResources();
		list = (ListView) getActivity().findViewById(R.id.list);
		this.CustomListViewValuesArr = this.channelManager.getAllChannels();
		adapter = new CustomListAdapter(getActivity(), CustomListViewValuesArr,
				res);
		list.setAdapter(adapter);

		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		final int screenWidth = metrics.widthPixels;
		final int screenHeight = metrics.heightPixels;

		setChannelManager();

//		final View activityRootView = getActivity().findViewById(
//				R.id.activityRoot);
//		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
//				new OnGlobalLayoutListener() {
//					@Override
//					public void onGlobalLayout() {
//						int heightDiff = activityRootView.getRootView()
//								.getHeight() - activityRootView.getHeight();
//						if (heightDiff > 100) { // if more than 100 pixels, its
//												// probably a keyboard...
//							Log.v("keyboard", "opened");
//						}
//					}
//				});

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	public void setChannelManager() {
		final ChannelManager channelMgr = new ChannelManager(getActivity());
		ArrayList<String> hmRegions = channelMgr.getAllRegions();

		ArrayAdapter autocompletetextChannelAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				hmRegions);
		SectionChannelSetup.selectRegion
				.setAdapter(autocompletetextChannelAdapter);
		String[] regionsArr = hmRegions.toArray(new String[hmRegions.size()]);
		SectionChannelSetup.selectRegion.setValidator(new Validator(
				getActivity(), "region", regionsArr));
		SectionChannelSetup.selectRegion
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus)
							SectionChannelSetup.selectRegion.showDropDown();
						else {
							SectionChannelSetup.selectRegion
									.performValidation();
						}
					}
				});

		SectionChannelSetup.selectChannel
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus)
							SectionChannelSetup.selectChannel.showDropDown();
						else {
							SectionChannelSetup.selectChannel
									.performValidation();
						}
					}
				});

		// get channel numbers
		ArrayList<String> channelNumbers = channelMgr.getAllChannelNumbers();
		String[] channelNumberArr = channelNumbers
				.toArray(new String[channelNumbers.size()]);
		ArrayAdapter autocompletetextChannelNumberAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				channelNumbers);
		SectionChannelSetup.selectChannelNumber
				.setAdapter(autocompletetextChannelNumberAdapter);
		SectionChannelSetup.selectChannelNumber.setValidator(new Validator(
				getActivity(), "chnumber", channelNumberArr));

		SectionChannelSetup.selectChannelNumber
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus)
							SectionChannelSetup.selectChannelNumber
									.showDropDown();
						else {
							SectionChannelSetup.selectChannelNumber
									.performValidation();
						}
					}
				});

		SectionChannelSetup.btnChannelManagerOK
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (SectionChannelSetup.selectChannel.getText()
								.length() != 0
								&& SectionChannelSetup.selectChannelNumber
										.getText().length() != 0
								&& SectionChannelSetup.selectRegion.getText()
										.length() != 0) {
							channelMgr.setChannelNumber(
									SectionChannelSetup.selectChannel.getText()
											.toString(),
									SectionChannelSetup.selectChannelNumber
											.getText().toString());
							ArrayList<Channel> newChList = new ArrayList<Channel>();
							if (SectionChannelSetup.selectRegion.getText()
									.toString().equals("All"))
								newChList = channelMgr.getAllChannels();
							else
								newChList = channelMgr
										.getAllChannelsByRegion(SectionChannelSetup.selectRegion
												.getText().toString());
							adapter = new CustomListAdapter(getActivity(), newChList,
									getResources());
							list.setAdapter(adapter);
							adapter.notifyDataSetChanged();

						}
					}
				});
		// SectionChannelSetup.selectRegion
	}

	/****** Function to set data in ArrayList *************/
	public void setListData() {
		Iterator itSchedule = ScheduleManager.schedules.entrySet().iterator();
		this.CustomListViewValuesArr = new ArrayList<Channel>();
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
			if (now.get(Calendar.DAY_OF_MONTH) == day
					&& now.get(Calendar.MONTH) == month
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		final ChannelManager channelMgr = new ChannelManager(getActivity());
		ArrayList<Channel> newChList = new ArrayList<Channel>();
		if (SectionChannelSetup.selectRegion.getText()
				.toString().equals("All"))
			newChList = channelMgr.getAllChannels();
		else
			newChList = channelMgr
					.getAllChannelsByRegion(SectionChannelSetup.selectRegion
							.getText().toString());
		adapter = new CustomListAdapter(getActivity(), newChList,
				getResources());
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		if (FragmentTvGuideMain.selectedChannel != null) {
			// generate click event on the selected channel
			adapter.getView(CustomListAdapter.selPosition, null, null)
					.performClick();
		}
		super.onResume();
	}

}
