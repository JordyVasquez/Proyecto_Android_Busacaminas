package com.example.android_application;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundEffects {
	
	MediaPlayer mp;
	int nombre;
	
	SoundEffects(Context app,int nombre)
	{
		this.nombre=nombre; 
		mp = MediaPlayer.create(app, nombre);
		
	}
	
	public void iniciar()
	{
		mp.start();
	}
	
	public void pausar()
	{
		mp.pause();
	}
	
	public void detener()
	{
		mp.stop();
	}
	
	public void resetear()
	{
		mp.reset();
	}
		
	

}
