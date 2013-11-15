package com.example.android_application;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Pantalla_facil extends Activity implements OnTouchListener {
        private Tablero tabla;
        private Casilla[][] casillas;
        int x, y,primerintento=0;
        LinkedList<Coordenada> cor_Bom;
        Coordenada primera;
        private boolean activo = true;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.tablerofacil);

                LinearLayout layout = (LinearLayout) findViewById(R.id.layout2);
                tabla= new Tablero(this);
                tabla.setOnTouchListener(this);                                  
                layout.addView(tabla);
                casillas = new Casilla[8][8];
                for (int f = 0; f < 8; f++) {
                        for (int c = 0; c < 8; c++) {
                                casillas[f][c] = new Casilla();
                        }
                }
        }

        

        public void presionado(View v) {
                casillas = new Casilla[8][8];
                for (int f = 0; f < 8; f++) {
                        for (int c = 0; c < 8; c++) {
                                casillas[f][c] = new Casilla();
                        }
                }
                primerintento=0;
                activo = true;

                tabla.invalidate();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
                if (activo)
                        for (int f = 0; f < 8; f++) {
                                for (int c = 0; c < 8; c++) {
                                        if (casillas[f][c].dentro((int) event.getX(),
                                                        (int) event.getY())) {
                                                if(primerintento==0){
                                                        primera=new Coordenada(f, c);
                                                        cor_Bom = this.disponerBombas(primera);
                                                        this.contarBombasPerimetro(cor_Bom);
                                                        
                                                        primerintento=1;
                                                }
                                                casillas[f][c].destapado = true;
                                                
                                                if (casillas[f][c].contenido == 9) {
                                                        Destaparbombas(cor_Bom);
                                                        Toast.makeText(this, "Booooooooommmmmmmmmmmm",
                                                                        Toast.LENGTH_LONG).show();
                                                        activo = false;
                                                } else if (casillas[f][c].contenido == 0)
                                                        recorrer(f, c);
                                                tabla.invalidate();
                                        }
                                }
                        }
                if (gano() && activo) {
                        Toast.makeText(this, "Ganaste", Toast.LENGTH_LONG).show();
                        activo = false;
                }

                return true;
        }

        class Coordenada {
                public int x, y;

                public Coordenada(int x1, int y1) {
                        x = x1;
                        y = y1;
                }
        }
         class Casilla {
                    public int x,y,ancho;
                    public int contenido=0;
                    public boolean destapado=false;
                    public void fijarxy(int x,int y, int ancho) {
                        this.x=x;
                        this.y=y;
                        this.ancho=ancho;
                    }
                    
                    public boolean dentro(int xx,int yy) {
                        if (xx>=this.x && xx<=this.x+ancho && yy>=this.y && yy<=this.y+ancho) 
                            return true;
                        else
                            return false;
                    }
                }
        class Tablero extends View {

                public Tablero(Context context) {
                        super(context);
                }

                protected void onDraw(Canvas canvas) {
                        canvas.drawRGB(20, 0, 0);
                        int ancho = 0;
                        if (canvas.getWidth() < canvas.getHeight())
                                ancho = tabla.getWidth();
                        else
                                ancho = tabla.getHeight();
                        int anchocua = ancho /8;
                        Paint paint = new Paint();
                        paint.setTextSize(20);
                        Paint paint2 = new Paint();
                        paint2.setTextSize(20);
                        paint2.setTypeface(Typeface.DEFAULT_BOLD);
                        
                        
                        Paint paintlinea1 = new Paint();
                        paintlinea1.setARGB(255, 255, 255, 255);
                        Coordenada pintarnume;
                        int filaact = 0;
                        for (int f = 0; f < 8; f++) {
                                for (int c = 0; c <8; c++) {
                                        casillas[f][c].fijarxy(c * anchocua, filaact, anchocua);
                                        if (casillas[f][c].destapado == false)
                                                paint.setARGB(153, 204, 204, 204);
                                        else
                                                paint.setARGB(255, 153, 153, 153);
                                        canvas.drawRect(c * anchocua, filaact, c * anchocua
                                                        + anchocua - 2, filaact + anchocua - 2, paint);
                                        // linea blanca
                                        canvas.drawLine(c * anchocua, filaact, c * anchocua
                                                        + anchocua, filaact, paintlinea1);
                                        canvas.drawLine(c * anchocua + anchocua - 1, filaact, c
                                                        * anchocua + anchocua - 1, filaact + anchocua,
                                                        paintlinea1);
                                        pintarnume=new Coordenada(f, c);
                                        if (casillas[f][c].contenido >= 1
                                                        && casillas[f][c].contenido <= 8
                                                        && casillas[f][c].destapado){
                                                colornumeros(paint2, pintarnume);
                                                canvas.drawText(
                                                                String.valueOf(casillas[f][c].contenido), c
                                                                                * anchocua + (anchocua / 2) - 8,
                                                                filaact + anchocua / 2, paint2);
                                        }
                                        if (casillas[f][c].contenido == 9
                                                        && casillas[f][c].destapado) {
                                                Paint bomba = new Paint();
                                                bomba.setARGB(255, 255, 0, 0);
                                                canvas.drawCircle(c * anchocua + (anchocua / 2),
                                                                filaact + (anchocua / 2), 8, bomba);
                                        }

                                }
                                filaact = filaact + anchocua;
                        }
                }
        }

        private LinkedList<Coordenada> disponerBombas(Coordenada primerinten) {
                int cantidad = 8;
                LinkedList<Coordenada> lista = new LinkedList<Coordenada>();
                Coordenada cor;
                do {
                        int fila = (int) (Math.random() * 8);
                        int columna = (int) (Math.random() * 8);
                        if (casillas[fila][columna].contenido == 0 && !casillas[primerinten.x][primerinten.y].equals(casillas[fila][columna])) {
                                casillas[fila][columna].contenido = 9;
                                cor = new Coordenada(fila, columna);
                                lista.add(cor);
                                cantidad=cantidad-1;
                        }
                } while (cantidad != 0);
                return lista;
        }
        private boolean gano() {
                int cant = 0;
                for (int f = 0; f < 8; f++)
                        for (int c = 0; c < 8; c++)
                                if (casillas[f][c].destapado)
                                        cant++;
                if (cant == 56)
                        return true;
                else
                        return false;
        }

        
        private void contarBombasPerimetro(LinkedList<Coordenada> Cordenadas) {
                for (int f = 0; f < Cordenadas.size(); f++) {
                        int x=Cordenadas.get(f).x;
                        int y=Cordenadas.get(f).y;
                                
                                 numeroAlrededorBomba(x, y);
                                
                }
        }
        
        private void colornumeros(Paint pa, Coordenada cor) {
                if(casillas[cor.x][cor.y].contenido == 1)pa.setARGB(200, 0,0, 200);
                if(casillas[cor.x][cor.y].contenido == 2)pa.setARGB(200, 0,200, 0);
                if(casillas[cor.x][cor.y].contenido == 3)pa.setARGB(200, 200,0,0);
                if(casillas[cor.x][cor.y].contenido == 4)pa.setARGB(200, 200,0, 200);
                if(casillas[cor.x][cor.y].contenido == 5)pa.setARGB(200, 0,0, 100);
                if(casillas[cor.x][cor.y].contenido == 6)pa.setARGB(200, 0,100,0);
                if(casillas[cor.x][cor.y].contenido == 7)pa.setARGB(200, 100,0, 200);
                if(casillas[cor.x][cor.y].contenido == 8)pa.setARGB(200, 200,200, 200);
                
                
        }
        private void Destaparbombas(LinkedList<Coordenada> Cordenadas) {
                for (int f = 0; f < Cordenadas.size(); f++) {
                        int x=Cordenadas.get(f).x;
                        int y=Cordenadas.get(f).y;
                                
                                 casillas[x][y].destapado=true;        
                }
        }

        void numeroAlrededorBomba(int fila, int columna) {
                if (fila - 1 >= 0 && columna - 1 >= 0) {
                        if (casillas[fila - 1][columna - 1].contenido !=9){
                        
                        if(casillas[fila - 1][columna - 1].contenido>0 && casillas[fila - 1][columna - 1].contenido<9)
                                casillas[fila - 1][columna - 1].contenido =casillas[fila - 1][columna - 1].contenido +1;
                        else casillas[fila - 1][columna - 1].contenido =1;
                        }
                }
                     
                if (fila - 1 >= 0) {
                        if (casillas[fila - 1][columna].contenido !=9){
                         
                        if(casillas[fila - 1][columna].contenido>0 && casillas[fila - 1][columna].contenido<9)
                                casillas[fila - 1][columna].contenido=casillas[fila - 1][columna].contenido+1;
                        else casillas[fila - 1][columna].contenido=1;
                        }
                                
                }
                if (fila - 1 >= 0 && columna + 1 < 8) {
                        if (casillas[fila - 1][columna + 1].contenido !=9){
                                
                        if(casillas[fila - 1][columna + 1].contenido>0 && casillas[fila - 1][columna + 1].contenido<9)
                                casillas[fila - 1][columna + 1].contenido=casillas[fila - 1][columna + 1].contenido+1;
                        else casillas[fila - 1][columna + 1].contenido =1;
                        }
                                
                }

                if (columna + 1 < 8) {
                        if (casillas[fila][columna + 1].contenido !=9){
                                
                        if(casillas[fila][columna + 1].contenido >0 && casillas[fila][columna + 1].contenido <9)
                                casillas[fila][columna + 1].contenido =casillas[fila][columna + 1].contenido +1;
                        else casillas[fila][columna + 1].contenido =1;
                        }
                }
                if (fila + 1 < 8 && columna + 1 < 8) {
                        if (casillas[fila + 1][columna + 1].contenido !=9){
                                
                        if(casillas[fila + 1][columna + 1].contenido>0 && casillas[fila + 1][columna + 1].contenido<9)
                                casillas[fila + 1][columna + 1].contenido=casillas[fila + 1][columna + 1].contenido+1;
                        else casillas[fila + 1][columna + 1].contenido=1;
                        }
                }

                if (fila + 1 < 8) {
                        if (casillas[fila + 1][columna].contenido !=9){
                                
                        if(casillas[fila + 1][columna].contenido>0 && casillas[fila + 1][columna].contenido<9)
                                casillas[fila + 1][columna].contenido=casillas[fila + 1][columna].contenido+1;
                        else casillas[fila + 1][columna].contenido=1;
                        }
                }
                if (fila + 1 < 8 && columna - 1 >= 0) {
                        if (casillas[fila + 1][columna - 1].contenido !=9){
                                
                        if(casillas[fila + 1][columna - 1].contenido>0 && casillas[fila + 1][columna - 1].contenido<9)
                                casillas[fila + 1][columna - 1].contenido=casillas[fila + 1][columna - 1].contenido+1;
                        else casillas[fila + 1][columna - 1].contenido=1;
                        }
                }
                if (columna - 1 >= 0) {
                        if (casillas[fila][columna - 1].contenido !=9){
                                
                        if(casillas[fila][columna - 1].contenido>0 && casillas[fila][columna - 1].contenido<9)
                                casillas[fila][columna - 1].contenido=casillas[fila][columna - 1].contenido+1;
                        else casillas[fila][columna - 1].contenido=1;
                        }
                }
                
        }

        private void recorrer(int fil, int col) {
                if (fil >= 0 && fil < 8 && col >= 0 && col < 8) {
                        if (casillas[fil][col].contenido == 0) {
                                casillas[fil][col].destapado = true;
                                casillas[fil][col].contenido = 10;
                                recorrer(fil, col + 1);
                                recorrer(fil, col - 1);
                                recorrer(fil + 1, col);
                                recorrer(fil - 1, col);
                                recorrer(fil - 1, col - 1);
                                recorrer(fil - 1, col + 1);
                                recorrer(fil + 1, col + 1);
                                recorrer(fil + 1, col - 1);
                        } else if (casillas[fil][col].contenido >= 1
                                        && casillas[fil][col].contenido <= 8) {
                                casillas[fil][col].destapado = true;
                        }
                }
        }



}