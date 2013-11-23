package com.example.android_application;

import java.util.LinkedList;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Pantalla_facil extends Activity implements OnTouchListener,
		OnDragListener {
	private Tablero tabla;
	private Casilla[][] casillas;
	int x, y, primerintento = 0;
	LinkedList<Coordenada> cor_Bom;
	View reiniciar;
	Coordenada primera;
	private boolean activo = true;
	private boolean moviendoBan = false;
	ImageView flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablerofacil);
		reiniciar=(View)findViewById(R.id.button1);

		LinearLayout layout = (LinearLayout) findViewById(R.id.tableroG);
		tabla = new Tablero(this);
		tabla.setOnTouchListener(this);
		tabla.setOnDragListener(this);
		
		layout.addView(tabla);
		casillas = new Casilla[8][8];
		for (int f = 0; f < 8; f++) {
			for (int c = 0; c < 8; c++) {
				casillas[f][c] = new Casilla();
			}
		}

		flag = (ImageView) findViewById(R.id.flag);
		// set touch listeners
		flag.setOnTouchListener(new ChoiceTouchListener());

	}

	private final class ChoiceTouchListener implements OnTouchListener {

		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				// setup drag
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				// start dragging the item touched
				view.startDrag(data, shadowBuilder, view, 0);
				moviendoBan = true;
				return true;
			} else {
				return false;
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
		primerintento = 0;
		activo = true;
		reiniciar.setBackgroundResource(R.drawable.carafeliz);

		tabla.invalidate();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (activo) {
				int X = (int) event.getX();
				int Y = (int) event.getY();
				for (int f = 0; f < 8; f++) {
					for (int c = 0; c < 8; c++) {
						if (casillas[f][c].dentro(X, Y)) {
							if (casillas[f][c].destapado == false) {
								if (casillas[f][c].conBandera) {
									casillas[f][c].conBandera = false;
									moviendoBan = false;
									tabla.invalidate();
									return true;
								} else {
									if (primerintento == 0) {
										primera = new Coordenada(f, c);
										cor_Bom = this.disponerBombas(primera);
										this.contarBombasPerimetro(cor_Bom);

										primerintento = 1;
										reiniciar.setBackgroundResource(R.drawable.carasorpresa);
									}
									casillas[f][c].destapado = true;

									if (casillas[f][c].contenido == 9) {
										Destaparbombas(cor_Bom);
										Toast.makeText(this,
												"LOSER........PERDISTES",
												Toast.LENGTH_LONG).show();
										activo = false;
										reiniciar.setBackgroundResource(R.drawable.caratriste);
									} else if (casillas[f][c].contenido == 0) {
										recorrer(f, c);
									}
								}
								tabla.invalidate();
							}
							// Deberia salir de los for
						}
					}
				}
			}
			if (gano() && activo) {
				Toast.makeText(this, "Ganaste", Toast.LENGTH_LONG).show();
				reiniciar.setBackgroundResource(R.drawable.caraganador);
				activo = false;
			}
		}

		return true;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// handle drag events
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			// no action necessary
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			// no action necessary
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			// no action necessary
			break;
		case DragEvent.ACTION_DROP:
			if (activo && moviendoBan) {
				moviendoBan = false;
				int X = (int) event.getX();
				int Y = (int) event.getY();
				for (int f = 0; f < 8; f++) {
					for (int c = 0; c < 8; c++) {
						if (casillas[f][c].dentro(X, Y)) {
							if (primerintento != 0) {// Si no es el primer
														// intento coloca la
														// bandera
								if (casillas[f][c].destapado == false
										&& casillas[f][c].conBandera == false) {
									casillas[f][c].conBandera = true;
									tabla.invalidate();
								}
							}
							return true;
						}
					}
				}
			}
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			// no action necessary
			break;
		default:
			break;
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
		public int x, y, ancho;
		public int contenido = 0;
		public boolean destapado = false;
		public boolean conBandera = false;

		public void fijarxy(int x, int y, int ancho) {
			this.x = x;
			this.y = y;
			this.ancho = ancho;
		}

		public boolean dentro(int xx, int yy) {
			if (xx >= this.x && xx <= this.x + ancho && yy >= this.y
					&& yy <= this.y + ancho)
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
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.ficha);
			Bitmap bmp0 = BitmapFactory.decodeResource(getResources(),
					R.drawable.ficha0);
			Bitmap bmpmin = BitmapFactory.decodeResource(getResources(),
					R.drawable.fichamina);

			Bitmap bandera = BitmapFactory.decodeResource(getResources(),
					R.drawable.fichaflag);

			int ancho = 0;
			if (canvas.getWidth() < canvas.getHeight())
				ancho = tabla.getWidth();
			else
				ancho = tabla.getHeight();
			int anchocua = ancho / 8;
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
				for (int c = 0; c < 8; c++) {
					casillas[f][c]
							.fijarxy(c * anchocua, filaact + 30, anchocua);
					if (casillas[f][c].destapado == false)
						if (casillas[f][c].conBandera == true) {
							canvas.drawBitmap(bandera, c * anchocua + 6,
									filaact + 6 + 30, null);
						} else {
							canvas.drawBitmap(bmp0, c * anchocua + 6,
									filaact + 6 + 30, null);
						}
					else
						canvas.drawBitmap(bmp, c * anchocua + 6,
								filaact + 6 + 30, null);

					// linea blanca
					canvas.drawLine(c * anchocua, filaact + 30, c * anchocua
							+ anchocua, filaact + 30, paintlinea1);
					canvas.drawLine(c * anchocua + anchocua - 1, filaact + 30,
							c * anchocua + anchocua - 1, filaact + anchocua
									+ 30, paintlinea1);
					if (c == 7) {
						canvas.drawLine(8 * anchocua, filaact + 10 + anchocua,
								8 * anchocua + anchocua, filaact + 10
										+ anchocua, paintlinea1);

					}

					pintarnume = new Coordenada(f, c);
					if (casillas[f][c].contenido >= 1
							&& casillas[f][c].contenido <= 8
							&& casillas[f][c].destapado) {
						colornumeros(paint2, pintarnume);
						canvas.drawText(
								String.valueOf(casillas[f][c].contenido), c
										* anchocua + 2 + (anchocua / 2) - 8,
								filaact + 2 + +30 + anchocua / 2, paint2);
					}
					if (casillas[f][c].contenido == 9
							&& casillas[f][c].destapado) {
						Paint bomba = new Paint();
						canvas.drawBitmap(bmpmin, c * anchocua + 6,
								filaact + 6 + 30, bomba);
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
			if (casillas[fila][columna].contenido == 0
					&& !casillas[primerinten.x][primerinten.y]
							.equals(casillas[fila][columna])) {
				casillas[fila][columna].contenido = 9;
				cor = new Coordenada(fila, columna);
				lista.add(cor);
				cantidad = cantidad - 1;
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
			int x = Cordenadas.get(f).x;
			int y = Cordenadas.get(f).y;

			numeroAlrededorBomba(x, y);

		}
	}

	private void colornumeros(Paint pa, Coordenada cor) {
		if (casillas[cor.x][cor.y].contenido == 1)
			pa.setARGB(200, 0, 0, 200);
		if (casillas[cor.x][cor.y].contenido == 2)
			pa.setARGB(200, 0, 200, 0);
		if (casillas[cor.x][cor.y].contenido == 3)
			pa.setARGB(200, 200, 0, 0);
		if (casillas[cor.x][cor.y].contenido == 4)
			pa.setARGB(200, 200, 0, 200);
		if (casillas[cor.x][cor.y].contenido == 5)
			pa.setARGB(200, 0, 0, 100);
		if (casillas[cor.x][cor.y].contenido == 6)
			pa.setARGB(200, 0, 100, 0);
		if (casillas[cor.x][cor.y].contenido == 7)
			pa.setARGB(200, 100, 0, 200);
		if (casillas[cor.x][cor.y].contenido == 8)
			pa.setARGB(200, 200, 200, 200);

	}

	private void Destaparbombas(LinkedList<Coordenada> Cordenadas) {
		for (int f = 0; f < Cordenadas.size(); f++) {
			int x = Cordenadas.get(f).x;
			int y = Cordenadas.get(f).y;

			casillas[x][y].destapado = true;
		}
	}

	void numeroAlrededorBomba(int fila, int columna) {
		if (fila - 1 >= 0 && columna - 1 >= 0) {
			if (casillas[fila - 1][columna - 1].contenido != 9) {

				if (casillas[fila - 1][columna - 1].contenido > 0
						&& casillas[fila - 1][columna - 1].contenido < 9)
					casillas[fila - 1][columna - 1].contenido = casillas[fila - 1][columna - 1].contenido + 1;
				else
					casillas[fila - 1][columna - 1].contenido = 1;
			}
		}

		if (fila - 1 >= 0) {
			if (casillas[fila - 1][columna].contenido != 9) {

				if (casillas[fila - 1][columna].contenido > 0
						&& casillas[fila - 1][columna].contenido < 9)
					casillas[fila - 1][columna].contenido = casillas[fila - 1][columna].contenido + 1;
				else
					casillas[fila - 1][columna].contenido = 1;
			}

		}
		if (fila - 1 >= 0 && columna + 1 < 8) {
			if (casillas[fila - 1][columna + 1].contenido != 9) {

				if (casillas[fila - 1][columna + 1].contenido > 0
						&& casillas[fila - 1][columna + 1].contenido < 9)
					casillas[fila - 1][columna + 1].contenido = casillas[fila - 1][columna + 1].contenido + 1;
				else
					casillas[fila - 1][columna + 1].contenido = 1;
			}

		}

		if (columna + 1 < 8) {
			if (casillas[fila][columna + 1].contenido != 9) {

				if (casillas[fila][columna + 1].contenido > 0
						&& casillas[fila][columna + 1].contenido < 9)
					casillas[fila][columna + 1].contenido = casillas[fila][columna + 1].contenido + 1;
				else
					casillas[fila][columna + 1].contenido = 1;
			}
		}
		if (fila + 1 < 8 && columna + 1 < 8) {
			if (casillas[fila + 1][columna + 1].contenido != 9) {

				if (casillas[fila + 1][columna + 1].contenido > 0
						&& casillas[fila + 1][columna + 1].contenido < 9)
					casillas[fila + 1][columna + 1].contenido = casillas[fila + 1][columna + 1].contenido + 1;
				else
					casillas[fila + 1][columna + 1].contenido = 1;
			}
		}

		if (fila + 1 < 8) {
			if (casillas[fila + 1][columna].contenido != 9) {

				if (casillas[fila + 1][columna].contenido > 0
						&& casillas[fila + 1][columna].contenido < 9)
					casillas[fila + 1][columna].contenido = casillas[fila + 1][columna].contenido + 1;
				else
					casillas[fila + 1][columna].contenido = 1;
			}
		}
		if (fila + 1 < 8 && columna - 1 >= 0) {
			if (casillas[fila + 1][columna - 1].contenido != 9) {

				if (casillas[fila + 1][columna - 1].contenido > 0
						&& casillas[fila + 1][columna - 1].contenido < 9)
					casillas[fila + 1][columna - 1].contenido = casillas[fila + 1][columna - 1].contenido + 1;
				else
					casillas[fila + 1][columna - 1].contenido = 1;
			}
		}
		if (columna - 1 >= 0) {
			if (casillas[fila][columna - 1].contenido != 9) {

				if (casillas[fila][columna - 1].contenido > 0
						&& casillas[fila][columna - 1].contenido < 9)
					casillas[fila][columna - 1].contenido = casillas[fila][columna - 1].contenido + 1;
				else
					casillas[fila][columna - 1].contenido = 1;
			}
		}

	}

	private void recorrer(int fil, int col) {
		if (fil >= 0 && fil < 8 && col >= 0 && col < 8) {
			if (casillas[fil][col].conBandera == false) {
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
				} else if (casillas[fil][col].contenido < 9) {
					casillas[fil][col].destapado = true;
				}
			}
		}
	}
}