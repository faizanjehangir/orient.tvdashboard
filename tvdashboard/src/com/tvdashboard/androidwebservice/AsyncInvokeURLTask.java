package com.tvdashboard.androidwebservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncInvokeURLTask extends AsyncTask<Void, Void, String> {
	// private final String mNoteItWebUrl =
	// "https://indian-television-guide.appspot.com/indian_television_guide?channel=star-movies&date=27022014";
	private String tvAPIIndiaURL = "https://indian-television-guide.appspot.com/indian_television_guide";
	private String mParams;
	private OnPostExecuteListener mPostExecuteListener = null;
	private Context context;
	private String chDate;

	public static interface OnPostExecuteListener {
		void onPostExecute(String result);
	}

	AsyncInvokeURLTask(String params, Context context, String date,
			OnPostExecuteListener postExecuteListener)
			throws Exception {

		this.chDate = date;
		this.context = context;
		mParams = params;
		mPostExecuteListener = postExecuteListener;
		if (mPostExecuteListener == null)
			throw new Exception("Param cannot be null.");
	}
	
	

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
	}



	@Override
	protected String doInBackground(Void... params) {

		String result = "";

		tvAPIIndiaURL = ConstructHTTPGetParams(tvAPIIndiaURL);
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(tvAPIIndiaURL);

		try {
			// Add parameters
			// httpget.setEntity(new UrlEncodedFormEntity(mParams));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inStream = entity.getContent();
				result = convertStreamToString(inStream);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String ConstructHTTPGetParams(String url) {
		String todayAsString = null;

		if (chDate == null) {
			todayAsString = new SimpleDateFormat("ddMMyyyy").format(new Date());
			url = url + "?channel=" + mParams + "&date=" + todayAsString;
		}

		else{
			url = url + "?channel=" + mParams + "&date=" + chDate;
		}
		return url;
	}

	@Override
	protected void onPostExecute(String result) {
		if (mPostExecuteListener != null) {
			try {
				JSONObject json = new JSONObject(result);
				// dump json to file
				CreateJSONSchedule(json);
				mPostExecuteListener.onPostExecute(ReadFile(json.toString()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private String ReadFile(String jsonString) {
		String todayAsString = new SimpleDateFormat("ddMMyyyy")
				.format(new Date());
		String fileName;
		if (chDate != null)
			fileName = mParams + "-" + chDate + ".json";
		else
			fileName = mParams + "-" + todayAsString + ".json";
		FileInputStream fIn = null;
		try {
			fIn = context.openFileInput(fileName);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStreamReader isr = new InputStreamReader(fIn);

		/*
		 * Prepare a char-Array that will hold the chars we read back in.
		 */
		char[] inputBuffer = new char[jsonString.length()];

		// Fill the Buffer with data from the file
		try {
			isr.read(inputBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Transform the chars to a String
		return new String(inputBuffer);

		// Check if we read back the same chars that we had written out

	}

	private void CreateJSONSchedule(JSONObject result) {
		FileOutputStream fOut = null;
		String todayAsString = new SimpleDateFormat("ddMMyyyy")
				.format(new Date());
		String fileName = null;
		if (chDate != null)
			fileName = mParams + "-" + chDate + ".json";
		else
			fileName = mParams + "-" + todayAsString + ".json";
		File file = context.getFileStreamPath(fileName);
		if (!file.exists()) {
			try {
				fOut = context.openFileOutput(fileName,
						context.MODE_WORLD_READABLE);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// Write the string to the file
			try {
				osw.write(result.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * ensure that everything is really written out and close
			 */
			try {
				osw.flush();
				osw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.v("error", "file already exists");
		}

	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader streamReader = null;
		try {
			streamReader = new BufferedReader(
					new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuilder responseStrBuilder = new StringBuilder();

		String inputStr;
		try {
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return responseStrBuilder.toString();
	}
} // AsyncInvokeURLTask
