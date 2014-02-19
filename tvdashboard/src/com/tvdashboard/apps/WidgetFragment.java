package com.tvdashboard.apps;

import android.app.Activity;
import android.app.Fragment;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import com.tvdashboard.database.R;

/**
 * Created by Shahzeb on 12/10/13.
 */
public class WidgetFragment extends android.support.v4.app.Fragment {

    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;
    ViewGroup mainlayout;
    static final String TAG = "WidgetHostExampleActivity";
    public WidgetFragment()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_widget,container,false);
        Button addWidget =(Button) view.findViewById(R.id.add);
        Button removeWidget =(Button) view.findViewById(R.id.remove);

        return view;
    }

    public void add(View view)
    {
    }

    public void remove(View view)
    {
    }




}
