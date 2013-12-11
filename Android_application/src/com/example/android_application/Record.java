package com.example.android_application;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Record extends Activity implements OnClickListener{

	TableLayout tabla;
	TableLayout cabecera;
	TableRow.LayoutParams layoutFila;
	TableRow.LayoutParams layoutId;
	TableRow.LayoutParams layoutTexto,layouttexto2;
	Base_dedatosdel_jugador base;
	LinkedList<LinkedList<String>> bases;
	private int MAX_FILAS = 13;
	String variable_nivel;
	View inicio;
	Resources rs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
		rs = this.getResources();
		tabla = (TableLayout) findViewById(R.id.tabla);
		inicio = (View) findViewById(R.id.bthome);
		cabecera = (TableLayout) findViewById(R.id.cabecera);
		layoutFila = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		layoutId = new TableRow.LayoutParams(85,
				TableRow.LayoutParams.WRAP_CONTENT);
		layoutTexto = new TableRow.LayoutParams(85,
				TableRow.LayoutParams.WRAP_CONTENT);
		layouttexto2 = new TableRow.LayoutParams(85,
				TableRow.LayoutParams.WRAP_CONTENT);
		
		variable_nivel = getIntent().getStringExtra("Nivel_juego");
		base = new Base_dedatosdel_jugador(this, "DBUsuarios", null, 1);
		inicio.setOnClickListener(this);
		agregarCabecera();
		agregarFilasTabla();
	}
	protected void onDraw(Canvas canvas) {}


	public void agregarCabecera() {
		TableRow fila;
		TextView txtId;
		TextView txtNombre,txtPuntaje;

		fila = new TableRow(this);
		fila.setLayoutParams(layoutFila);

		txtId = new TextView(this);
		txtNombre = new TextView(this);
		txtPuntaje = new TextView(this);
//
		txtId.setText(rs.getString(R.string.Posicion));
		txtId.setGravity(Gravity.CENTER_HORIZONTAL);
		txtId.setTextAppearance(this, R.style.etiqueta);
		txtId.setBackgroundResource(R.layout.tabla_celda_cabecera);
		txtId.setLayoutParams(layoutId);

		txtNombre.setText("Jugador");
		txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
		txtNombre.setTextAppearance(this, R.style.etiqueta);
		txtNombre.setBackgroundResource(R.layout.tabla_celda_cabecera);
		txtNombre.setLayoutParams(layoutTexto);
		
		txtPuntaje.setText("Puntaje");
		txtPuntaje.setGravity(Gravity.CENTER_HORIZONTAL);
		txtPuntaje.setTextAppearance(this, R.style.etiqueta);
		txtPuntaje.setBackgroundResource(R.layout.tabla_celda_cabecera);
		txtPuntaje.setLayoutParams(layouttexto2);

		fila.addView(txtId);
		fila.addView(txtNombre);
		fila.addView(txtPuntaje);
		cabecera.addView(fila);
	}

	public void agregarFilasTabla() {

		TableRow fila;
		TextView txtId;
		TextView txtNombre;
		TextView txtPuntaje;
		

		bases = base.listaPuntuaciones(variable_nivel);
		for (int i = 0; i < bases.size(); i++) {
			int posicion = i + 1;
			fila = new TableRow(this);
			fila.setLayoutParams(layoutFila);

			txtId = new TextView(this);
			txtNombre = new TextView(this);
			txtPuntaje = new TextView(this);

			txtId.setText(String.valueOf(posicion));
			txtId.setGravity(Gravity.CENTER_HORIZONTAL);
			txtId.setTextAppearance(this, R.style.etiqueta);
			txtId.setBackgroundResource(R.layout.tabla_celda);
			txtId.setLayoutParams(layoutId);

			txtNombre.setText(bases.get(i).get(0).toString());
			txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
			txtNombre.setTextAppearance(this, R.style.etiqueta);
			txtNombre.setBackgroundResource(R.layout.tabla_celda);
			txtNombre.setLayoutParams(layoutTexto);
			
			txtPuntaje.setText(bases.get(i).get(1).toString());
			txtPuntaje.setGravity(Gravity.CENTER_HORIZONTAL);
			txtPuntaje.setTextAppearance(this, R.style.etiqueta);
			txtPuntaje.setBackgroundResource(R.layout.tabla_celda);
			txtPuntaje.setLayoutParams(layoutId);

			fila.addView(txtId);
			fila.addView(txtNombre);
			fila.addView(txtPuntaje);
			
			tabla.addView(fila);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.bthome :
			Intent volverMenu = new Intent(this, MainActivity.class);
			startActivity(volverMenu);
		}
	}

}
