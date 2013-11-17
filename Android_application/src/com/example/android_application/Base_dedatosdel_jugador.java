package com.example.android_application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Base_dedatosdel_jugador extends SQLiteOpenHelper{

	String sqlCreate = "CREATE TABLE Usuarios (codigo INTEGER, nombre TEXT)";
	public Base_dedatosdel_jugador(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS Usuarios");
		 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
	}
	
}
