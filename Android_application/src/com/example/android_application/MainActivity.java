package com.example.android_application;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button btStart, btInstrucciones, btRecord, btExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btStart = (Button) findViewById(R.id.btStart);
		btInstrucciones = (Button) findViewById(R.id.btInstrucciones);
		btRecord = (Button) findViewById(R.id.btRecord);
		btExit = (Button) findViewById(R.id.btExit);

		btStart.setOnClickListener(this);
		btExit.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btStart:
			Intent llama_pantalla_datos = new Intent(
					"com.example.android_application.Pantalladatosusuarios");
			startActivity(llama_pantalla_datos);
			break;

		case R.id.btExit:

			// TODO Auto-generated method stub
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			super.onDestroy();
			// System.exit(1);

			break;
		}

	}

}
