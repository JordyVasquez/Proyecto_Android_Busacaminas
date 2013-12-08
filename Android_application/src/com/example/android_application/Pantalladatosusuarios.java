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

		volver = (Button) findViewById(R.id.volver);

		facil.setOnClickListener(this);
		volver.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btFacil:

			// Toast.makeText(this, "Ha elegio el nivel Facil",
			// Toast.LENGTH_SHORT).show();
			Intent llama_pantalla_facil = new Intent(
					"com.example.android_application.Pantalla_facil");
			startActivity(llama_pantalla_facil);
			break;

		case R.id.volver:
			Intent volverMenu = new Intent(this, MainActivity.class);
			startActivity(volverMenu);
			break;
		}
	}

}