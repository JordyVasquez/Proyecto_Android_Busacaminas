package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent= getIntent();
		String message= intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		TextView textview = new TextView(this);
		textview.setTextSize(40);
		textview.setText(message);
		
		setContentView(textview);
		
	}

	

}
