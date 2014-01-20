package com.tvdashboard.utility;

import com.tvdashboard.database.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FileManagerForm extends Activity {
	
	Button browseBtn;
	public static EditText browseText;
	Context context;
	public static String dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form1);
        
        context = this.getApplicationContext();
        browseBtn = (Button)findViewById(R.id.btn_browse);
        browseText = (EditText)findViewById(R.id.text_browse);
        dir = "";
        
        browseText.setText(dir);
        String s = getIntent().getStringExtra("chosenDir");
        
        browseBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent intent = new Intent(FileManagerForm.this, DirectoryPicker.class);
				startActivity(intent);							
			}
		});
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.form1, menu);
        return true;
    }
    
}