package com.example.android_application;

import java.util.LinkedList;
import java.util.Vector;

import android.R.string;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Base_dedatosdel_jugador extends SQLiteOpenHelper{

	String sqlCreate = "CREATE TABLE Usuarios (puntaje TEXT, nombre TEXT, nivel TEXT)";
	public Base_dedatosdel_jugador(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creaci�n de la tabla
        db.execSQL(sqlCreate);
    }
	//M�todos de AlmacenPuntuaciones
    public void guardarPuntuacion(String puntos,String nombre,String nivel) {
          SQLiteDatabase db = getWritableDatabase();
          db.execSQL("INSERT INTO Usuarios VALUES ('"+puntos+"', '"+nombre+"','"+nivel+"')");
          db.close();
    }
    public void borrar() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Usuarios"); 
		  onCreate(db); 
  }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Log.w("Base_dedatosdel_jugador", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data"); 
		  db.execSQL("DROP TABLE IF EXISTS Usuarios"); 
		  onCreate(db); 
	}
	
	 public LinkedList<LinkedList<String>> listaPuntuaciones(String Nivel) {
		
		 LinkedList<LinkedList<String>> result = new LinkedList<LinkedList<String>>();
         SQLiteDatabase db = getReadableDatabase();
         Cursor cursor = db.rawQuery("SELECT puntaje, nombre FROM " +
          "Usuarios Where nivel='"+Nivel+"'ORDER BY puntaje",null);
         while (cursor.moveToNext()){
        	           LinkedList<String> jugador = new LinkedList<String>();
                       jugador.add(cursor.getString(1));
                       jugador.add(cursor.getString(0));
                       result.add(jugador);
                       
          }
         cursor.close();
         db.close();
         return result;
   } 
}
