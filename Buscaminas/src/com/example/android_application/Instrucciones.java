package com.example.android_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Instrucciones extends Activity implements OnClickListener {
	Button volver;
	TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instrucciones);
		volver = (Button) findViewById(R.id.back);
		volver.setOnClickListener(this);
		textView1 = (TextView)findViewById(R.id.textView1);
		textView1.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubIntent llama_pantalla_facil = new Intent("com.example.android_application.Pantalla_facil");
		Intent volverMenu = new Intent(this,MainActivity.class);
		startActivity(volverMenu);
				
		
	}
}
