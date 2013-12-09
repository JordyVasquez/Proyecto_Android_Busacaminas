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
	SoundEffects cancion1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btStart = (Button) findViewById(R.id.btStart);
		btInstrucciones = (Button) findViewById(R.id.btInstrucciones);
		btRecord = (Button) findViewById(R.id.btRecord);
		btExit = (Button) findViewById(R.id.btExit);
		
		cancion1= new SoundEffects(getApplicationContext(), R.raw.mistery);
		cancion1.iniciar();

		btStart.setOnClickListener(this);
		btRecord.setOnClickListener(this);
		btExit.setOnClickListener(this);
		btInstrucciones.setOnClickListener(this);

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
			cancion1.detener();
			Intent llama_pantalla_datos = new Intent(
					"com.example.android_application.Pantalladatosusuarios");
			startActivity(llama_pantalla_datos);
			break;
		case R.id.btRecord:
			cancion1.detener();
			Intent record=new Intent("com.example.android_application.Record");
			startActivity(record);
			break;

		case R.id.btInstrucciones:
			cancion1.detener();
			Intent llama_Instrucciones = new Intent("com.example.android_application.Instrucciones");
			startActivity(llama_Instrucciones);
			break;

		case R.id.btExit:
			cancion1.detener();
			// TODO Auto-generated method stub
			this.finish();// try activityname.finish instead of this
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			break;
		}

	}

}
