package com.example.android_application;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


	
	public class Record extends Activity {  
		  
	    TableLayout tabla;  
	    TableLayout cabecera;  
	    TableRow.LayoutParams layoutFila;  
	    TableRow.LayoutParams layoutId;  
	    TableRow.LayoutParams layoutTexto;  
	    Base_dedatosdel_jugador base;
	    LinkedList<String>bases;
	    private int MAX_FILAS = 13;  
	  
	    Resources rs;  
	  
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.record);  
	        rs = this.getResources();  
	        tabla = (TableLayout)findViewById(R.id.tabla);  
	        cabecera = (TableLayout)findViewById(R.id.cabecera);  
	 layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,  
	                                               TableRow.LayoutParams.WRAP_CONTENT);  
	 layoutId = new TableRow.LayoutParams(85,TableRow.LayoutParams.WRAP_CONTENT);  
	 layoutTexto = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);  
	
	 base=new Base_dedatosdel_jugador(this, "DBUsuarios", null, 1);
	   
	 
	
	
	 agregarCabecera();  
	 agregarFilasTabla(); 
	    }  
	    public void presionado2(View v) {
	      	Intent volverMenu = new Intent(this,MainActivity.class);
				startActivity(volverMenu);
	  }
	  
	    public void agregarCabecera(){  
	     TableRow fila;  
	     TextView txtId;  
	     TextView txtNombre;  
	  
	     fila = new TableRow(this);  
	 fila.setLayoutParams(layoutFila);  
	  
	 txtId = new TextView(this);  
	 txtNombre = new TextView(this);  
	  
	 txtId.setText(rs.getString(R.string.Posicion));  
	 txtId.setGravity(Gravity.CENTER_HORIZONTAL);  
	 txtId.setTextAppearance(this,R.style.etiqueta);  
	 txtId.setBackgroundResource(R.layout.tabla_celda_cabecera);  
	 txtId.setLayoutParams(layoutId);  
	  
	 txtNombre.setText(rs.getString(R.string.Jugador_puntaje));  
	 txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);  
	 txtNombre.setTextAppearance(this,R.style.etiqueta);  
	 txtNombre.setBackgroundResource(R.layout.tabla_celda_cabecera);  
	 txtNombre.setLayoutParams(layoutTexto);  
	  
	 fila.addView(txtId);  
	 fila.addView(txtNombre);  
	 cabecera.addView(fila);  
	    }  
	  
	    public void agregarFilasTabla(){  
	  
	     TableRow fila;  
	     TextView txtId;  
	     TextView txtNombre; 
	     
	     bases=base.listaPuntuaciones();
	     for(int i = 0;i<bases.size();i++){  
	         int posicion = i + 1;  
	         fila = new TableRow(this);  
	         fila.setLayoutParams(layoutFila);  
	        
	         txtId = new TextView(this);  
	         txtNombre = new TextView(this);  
	  
	         txtId.setText(String.valueOf(posicion));  
	         txtId.setGravity(Gravity.CENTER_HORIZONTAL);  
	         txtId.setTextAppearance(this,R.style.etiqueta);  
	         txtId.setBackgroundResource(R.layout.tabla_celda);  
	         txtId.setLayoutParams(layoutId);  
	  
	         txtNombre.setText(bases.get(i));  
	         txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);  
	         txtNombre.setTextAppearance(this,R.style.etiqueta);  
	         txtNombre.setBackgroundResource(R.layout.tabla_celda);  
	         txtNombre.setLayoutParams(layoutTexto);  
	  
	         fila.addView(txtId);  
	         fila.addView(txtNombre);  
	  
	         tabla.addView(fila);  
	     }  
	    }  
	  

}
