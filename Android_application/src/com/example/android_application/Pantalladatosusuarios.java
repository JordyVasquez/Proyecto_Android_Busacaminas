package com.example.android_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Pantalladatosusuarios extends Activity implements OnClickListener {

	RadioGroup rg;
	int id_rbgroup;
	Button facil, medio, dificil;
	Button jugar, volver;
	int nivel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pantalladatosusuario);
		facil = (Button) findViewById(R.id.btFacil);
		medio = (Button) findViewById(R.id.btIntermedio);
		dificil = (Button) findViewById(R.id.btDificil);

		volver = (Button) findViewById(R.id.volver);

		facil.setOnClickListener(this);
		medio.setOnClickListener(this);
		dificil.setOnClickListener(this);
		volver.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btFacil:

			Intent llama_pantalla_facil = new Intent(
					"com.example.android_application.Pantalla_facil");
			llama_pantalla_facil.putExtra("Nivel_juego","Facil");
			startActivity(llama_pantalla_facil);
			break;
		case R.id.btIntermedio:

			Intent llama_pantalla_intermedio = new Intent(
					"com.example.android_application.Pantalla_facil");
			llama_pantalla_intermedio.putExtra("Nivel_juego","Medio");
			startActivity(llama_pantalla_intermedio);
			break;
		case R.id.btDificil:

	
			Intent llama_pantalla_dificil = new Intent(
					"com.example.android_application.Pantalla_facil");
			llama_pantalla_dificil.putExtra("Nivel_juego","Dificil");
			startActivity(llama_pantalla_dificil);
			break;

		case R.id.volver:
			Intent volverMenu = new Intent(this, MainActivity.class);
			startActivity(volverMenu);
			break;
		}
	}

}