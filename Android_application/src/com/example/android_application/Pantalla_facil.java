package com.example.android_application;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputFilter;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Pantalla_facil extends Activity implements OnTouchListener,
		OnDragListener, OnCompletionListener, OnClickListener {
	private Tablero tabla;
	private Casilla[][] casillas;
	int x, y, primerintento = 0;
	LinkedList<Coordenada> cor_Bom;
	View reiniciar, config, btmas, btmenos, inicio;
	Coordenada primera;
	Base_dedatosdel_jugador base;
	private boolean activo = true;
	private boolean moviendoBan = false;
	ImageView flag;
	Chronometer crono;

	MediaPlayer player;
	String estado = "inactivo";
	String chronoText;
	int anchos, ancho2, anchocua, bombas, Filas, columnas;
	int valor;
	int conbanderas = 0;
	TextView ba;
	String nivel;
	String variable_nivel;

	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablerofacil);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		variable_nivel = getIntent().getStringExtra("Nivel_juego");
		reiniciar = (View) findViewById(R.id.button1);

		btmas = (View) findViewById(R.id.btmas);
		btmenos = (View) findViewById(R.id.btmenos);
		btmas.setOnClickListener(this);
		btmenos.setOnClickListener(this);

		crono = (Chronometer) findViewById(R.id.crono);
		inicio = (View) findViewById(R.id.bthome);
		ba = (TextView) findViewById(R.id.NUMERObanderas);
		LinearLayout layout = (LinearLayout) findViewById(R.id.tableroG);
		tabla = new Tablero(this, variable_nivel, null);
		tabla.setOnTouchListener(this);
		tabla.setOnDragListener(this);
		inicio.setOnClickListener(this);

		base = new Base_dedatosdel_jugador(this, "DBUsuarios", null, 1);

		layout.addView(tabla);
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
		casillas = new Casilla[Filas][columnas];
		for (int f = 0; f < Filas; f++) {
			for (int c = 0; c < columnas; c++) {
				casillas[f][c] = new Casilla();
			}
		}
		flag = (ImageView) findViewById(R.id.flag);
		primerintento = 0;
		activo = true;
		reiniciar.setBackgroundResource(R.drawable.carafeliz);
		crono.stop();
		crono.setBase(SystemClock.elapsedRealtime());
		estado = "inactivo";
		tabla.invalidate();
		conbanderas = 0;
		ba.setText(String.valueOf(conbanderas));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.btmas:
			Animation anim = AnimationUtils.loadAnimation(this, R.anim.zoom);
			anim.reset();
			tabla.startAnimation(anim);

			break;

		case R.id.btmenos:
			Animation animout = AnimationUtils.loadAnimation(this,
					R.anim.zoomout);
			animout.reset();
			tabla.startAnimation(animout);

			break;

		case R.id.bthome:
			Intent volverMenu = new Intent(this, MainActivity.class);
			startActivity(volverMenu);
			break;

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		AssetFileDescriptor descriptor;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (activo) {
				
				int X = (int) event.getX();
				int Y = (int) event.getY();
				for (int f = 0; f < Filas; f++) {
					for (int c = 0; c < columnas; c++) {
						if (casillas[f][c].dentro(X, Y)) {
							if (casillas[f][c].destapado == false) {
								if (casillas[f][c].conBandera) {
									casillas[f][c].conBandera = false;
									moviendoBan = false;
									conbanderas--;
									ba.setText(String.valueOf(conbanderas));
									tabla.invalidate();
									return true;
								} else {
									if (primerintento == 0) {
										primera = new Coordenada(f, c);
										cor_Bom = this.disponerBombas(primera,
												bombas);
										this.contarBombasPerimetro(cor_Bom);

										primerintento = 1;
										reiniciar
												.setBackgroundResource(R.drawable.carasorpresa);
										if (estado == "inactivo") {
											crono.setBase(SystemClock.elapsedRealtime());
											crono.start();
											estado = "activo";
										}
									}
									casillas[f][c].destapado = true;

									if (casillas[f][c].contenido == 9) {
										Destaparbombas(Filas, columnas);
										crono.stop();
										estado = "inactivo";

										AssetManager manager = this.getAssets();
										player = new MediaPlayer();
										try {
											descriptor = manager
													.openFd("bombaExplosion.mp3");
											player.setDataSource(
													descriptor
															.getFileDescriptor(),
													descriptor.getStartOffset(),
													descriptor.getLength());
											player.prepare();
											player.start();
											player.release();
											player.setOnCompletionListener(this);

										} catch (Exception e) {
										}
										;

										Toast.makeText(this,
												"LOSER........PERDISTES    ",
												Toast.LENGTH_LONG).show();
										activo = false;
										reiniciar
												.setBackgroundResource(R.drawable.caratriste);
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
			if (gano(Filas, columnas, bombas) && activo) {
				Toast.makeText(this, "Ganaste", Toast.LENGTH_LONG).show();

				crono.stop();
				estado = "inactivo";
				chronoText = crono.getText().toString();
				reiniciar.setBackgroundResource(R.drawable.caraganador);

				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Puntaje:" + chronoText); // aqui escribes lo que
															// quieras
				alert.setMessage("Introduce tu nombre para guardar la partida"); // mensajito
																					// bonito
				alert.setIcon(android.R.drawable.ic_dialog_info); // si quieres
																	// un icono
				final EditText input = new EditText(this); // creas un Edit Text
				int maxLength = 15; // si quieres ponerle caracteristicas al
									// EditText
				InputFilter[] FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(maxLength);
				input.setFilters(FilterArray); // por ejemplo maximo 10
												// caracteres
				alert.setView(input); // añades el edit text a la vista del
										// AlertDialog
				alert.setPositiveButton("Guardar",
						new DialogInterface.OnClickListener() { // si 
							// si
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								base.guardarPuntuacion(chronoText, input.getText().toString(),variable_nivel);
								// u
							}

						});

				alert.setNegativeButton("Atras", null);
				alert.show();
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
				for (int f = 0; f < Filas; f++) {
					for (int c = 0; c < columnas; c++) {
						if (casillas[f][c].dentro(X, Y)) {
							if (primerintento != 0) {// Si no es el primer
														// intento coloca la
														// bandera
								if (casillas[f][c].destapado == false
										&& casillas[f][c].conBandera == false
										&& conbanderas < bombas) {
									casillas[f][c].conBandera = true;
									conbanderas++;
									ba.setText(String.valueOf(conbanderas));
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
		int Divisor, Divisor2;
//
		public Tablero(Context context, String Nivel, LinkedList<Integer> datos) {
			super(context);
			if (Nivel.equals("Facil")) {
				Filas = 8;
				columnas = 8;
				Divisor = 10;
				Divisor2 = 10;
				bombas = 10;

			}
			if (Nivel.equals("Medio")) {
				Filas = 16;
				columnas = 16;
				Divisor = 16;
				Divisor2 = 16;
				bombas = 40;
			}
			if (Nivel.equals("Dificil")) {
				Filas = 24;
				columnas = 20;
				Divisor = 25;
				Divisor2 = 21;
				bombas = 80;
			}
			
			casillas = new Casilla[Filas][columnas];
			for (int f = 0; f < Filas; f++) {
				for (int c = 0; c < columnas; c++) {
					casillas[f][c] = new Casilla();
				}
			}
		}

		protected void onDraw(Canvas canvas) {
			canvas.drawRGB(20, 0, 0);
			int medio, medio2;

			if (canvas.getWidth() < canvas.getHeight()) {

				if (Filas > columnas)
					anchocua = tabla.getHeight() / Divisor;
				else
					anchocua = tabla.getWidth() / Divisor2;

				medio = (tabla.getWidth() / 2) - ((columnas / 2) * anchocua);
				medio2 = (tabla.getHeight() / 2) - ((Filas / 2) * anchocua);

			} else {
				if (Filas > columnas)
					anchocua = tabla.getWidth() / Divisor;
				else
					anchocua = tabla.getHeight() / Divisor2;

				medio = (tabla.getWidth() / 2) - ((columnas / 2) * anchocua);
				medio2 = (tabla.getHeight() / 2) - ((Filas / 2) * anchocua);
			}
			ba.setText(String.valueOf(conbanderas));
			Bitmap bandera1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.fichaflag);
			flag.setImageBitmap(redimensionarImagenMaximo(bandera1, anchocua,
					anchocua));
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.ficha);
			Bitmap bmp0 = BitmapFactory.decodeResource(getResources(),
					R.drawable.ficha0);
			Bitmap bmpmin = BitmapFactory.decodeResource(getResources(),
					R.drawable.fichamina);

			Bitmap bandera = BitmapFactory.decodeResource(getResources(),
					R.drawable.fichaflag);
			bmp = redimensionarImagenMaximo(bmp, anchocua, anchocua);
			bmp0 = redimensionarImagenMaximo(bmp0, anchocua, anchocua);
			bmpmin = redimensionarImagenMaximo(bmpmin, anchocua, anchocua);
			bandera = redimensionarImagenMaximo(bandera, anchocua, anchocua);

			Paint paint = new Paint();
			paint.setTextSize(anchocua);
			Paint paint2 = new Paint();
			paint2.setTextSize(anchocua);
			paint2.setTypeface(Typeface.DEFAULT_BOLD);
			Paint X = new Paint();
			X.setTextSize(anchocua);
			X.setTypeface(Typeface.DEFAULT_BOLD);
			Paint paintlinea1 = new Paint();
			paintlinea1.setARGB(255, 100, 255, 255);
			Coordenada pintarnume;
			int filaact = 0;
			//

			for (int f = 0; f < Filas; f++) {
				for (int c = 0; c < columnas; c++) {
					casillas[f][c].fijarxy(c * anchocua + medio, filaact
							+ medio2, anchocua);
					if (casillas[f][c].destapado == false)
						if (casillas[f][c].conBandera == true) {
							canvas.drawBitmap(bandera, c * anchocua + medio,
									filaact + medio2, null);
						} else {
							canvas.drawBitmap(bmp0, c * anchocua + medio,
									filaact + medio2, null);
						}
					else
						canvas.drawBitmap(bmp, c * anchocua + medio, filaact
								+ medio2, null);

					// linea blanca
					canvas.drawLine(c * anchocua + medio, filaact + medio2, c
							* anchocua + anchocua + medio, filaact + medio2,
							paintlinea1);
					canvas.drawLine(c * anchocua + anchocua + medio, filaact
							+ medio2, c * anchocua + anchocua + medio, filaact
							+ anchocua + medio2, paintlinea1);
					if (f == (Filas - 1)) {
						canvas.drawLine(c * anchocua + medio, filaact + medio2
								+ anchocua, c * anchocua + anchocua + medio,
								filaact + medio2 + anchocua, paintlinea1);

					}
					if (c == 0) {
						canvas.drawLine(c * anchocua + medio, filaact + medio2,
								c * anchocua + medio, filaact + anchocua
										+ medio2, paintlinea1);
					}

					pintarnume = new Coordenada(f, c);

					if (casillas[f][c].contenido >= 1
							&& casillas[f][c].contenido <= 8
							&& casillas[f][c].destapado) {
						colornumeros(paint2, pintarnume);
						canvas.drawText(String.valueOf(casillas[f][c].contenido), c
										* anchocua + (anchocua / 3) + medio,
								filaact + medio2 + (anchocua / 3)
										+ (anchocua / 2), paint2);
					}
					if (casillas[f][c].contenido == 9
							&& casillas[f][c].destapado) {
						Paint bomba = new Paint();
						canvas.drawBitmap(bmpmin, c * anchocua + medio, filaact
								+ medio2, bomba);
					}
					if (casillas[f][c].contenido == 10
							&& casillas[f][c].destapado) {

						colornumeros(X, pintarnume);
						canvas.drawText("X", c * anchocua + (anchocua / 5)
								+ medio, filaact + medio2 + (anchocua / 3)
								+ (anchocua / 2), X);
					}

				}

				filaact = filaact + anchocua;
			}

		}
	}

	public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth,
			float newHeigth) {
		// Redimensionamos
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeigth) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		
		return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
		
	}

	
	
	private LinkedList<Coordenada> disponerBombas(Coordenada primerinten,
			int bombas) {
		int cantidad = bombas;
		LinkedList<Coordenada> lista = new LinkedList<Coordenada>();
		Coordenada cor;
		do {
			int fila = (int) (Math.random() * Filas);
			int columna = (int) (Math.random() * columnas);
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
	//h

	public boolean gano(int Filas, int columnas, int bombas) {
		int cant = 0;

		for (int f = 0; f < Filas; f++) {
			for (int c = 0; c < columnas; c++) {
				if (casillas[f][c].destapado)
					cant = cant + 1;
			}
		}
		if (cant == ((Filas * columnas) - bombas))
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
		if (casillas[cor.x][cor.y].contenido == 10)
			pa.setARGB(200, 200, 0, 0);

	}

	private void Destaparbombas(int fi, int co) {
		for (int f = 0; f < fi; f++) {
			for (int c = 0; c < co; c++) {
				if (casillas[f][c].conBandera == false
						&& casillas[f][c].contenido == 9)
					casillas[f][c].destapado = true;
				if (casillas[f][c].conBandera == true
						&& !(casillas[f][c].contenido == 9)) {
					casillas[f][c].contenido = 10;
					casillas[f][c].conBandera = false;
					casillas[f][c].destapado = true;

				}
			}
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
		if (fila - 1 >= 0 && columna + 1 < columnas) {
			if (casillas[fila - 1][columna + 1].contenido != 9) {

				if (casillas[fila - 1][columna + 1].contenido > 0
						&& casillas[fila - 1][columna + 1].contenido < 9)
					casillas[fila - 1][columna + 1].contenido = casillas[fila - 1][columna + 1].contenido + 1;
				else
					casillas[fila - 1][columna + 1].contenido = 1;
			}

		}

		if (columna + 1 < columnas) {
			if (casillas[fila][columna + 1].contenido != 9) {

				if (casillas[fila][columna + 1].contenido > 0
						&& casillas[fila][columna + 1].contenido < 9)
					casillas[fila][columna + 1].contenido = casillas[fila][columna + 1].contenido + 1;
				else
					casillas[fila][columna + 1].contenido = 1;
			}
		}
		if (fila + 1 < Filas && columna + 1 < columnas) {
			if (casillas[fila + 1][columna + 1].contenido != 9) {

				if (casillas[fila + 1][columna + 1].contenido > 0
						&& casillas[fila + 1][columna + 1].contenido < 9)
					casillas[fila + 1][columna + 1].contenido = casillas[fila + 1][columna + 1].contenido + 1;
				else
					casillas[fila + 1][columna + 1].contenido = 1;
			}
		}

		if (fila + 1 < Filas) {
			if (casillas[fila + 1][columna].contenido != 9) {

				if (casillas[fila + 1][columna].contenido > 0
						&& casillas[fila + 1][columna].contenido < 9)
					casillas[fila + 1][columna].contenido = casillas[fila + 1][columna].contenido + 1;
				else
					casillas[fila + 1][columna].contenido = 1;
			}
		}
		if (fila + 1 < Filas && columna - 1 >= 0) {
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
		if (fil >= 0 && fil < Filas && col >= 0 && col < columnas) {
			if (casillas[fil][col].conBandera == false) {
				if (casillas[fil][col].contenido == 0) {
					casillas[fil][col].destapado = true;
					casillas[fil][col].contenido = 15;
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

	public Bitmap resizeImage(int resId, int w, int h) {

		// load the original Bitmap
		Bitmap BitmapOrg = BitmapFactory.decodeResource(getResources(), resId);

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);

		// make a Drawable from Bitmap to allow to set the Bitmap
		// to the ImageView, ImageButton or what ever
		return resizedBitmap;

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		player.stop();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

	}

}