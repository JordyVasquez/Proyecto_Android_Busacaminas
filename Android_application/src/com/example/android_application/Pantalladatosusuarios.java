package com.example.android_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class Pantalladatosusuarios extends Activity implements OnClickListener{
    RadioButton facil,medio,dificil;
    Button jugar,volver;
    int nivel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pantalladatosusuario);
		facil=(RadioButton)findViewById(R.id.rdfacil);
		medio=(RadioButton)findViewById(R.id.rdmedio);
		dificil=(RadioButton)findViewById(R.id.rddificil);
		jugar=(Button)findViewById(R.id.Jugar);
		volver=(Button)findViewById(R.id.volver);
		jugar.setOnClickListener(this);
		
		
		facil.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
            	
    			nivel=1;
            }
        });
	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.Jugar:
			if(nivel==1){
			Intent llama_pantalla_facil=new Intent("com.example.android_application.Pantalla_facil");
			startActivity(llama_pantalla_facil);
			}
			
			
		}
	}

}
