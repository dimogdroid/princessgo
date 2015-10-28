package com.difed.princessgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.difed.bd.ProveedorBD;
import com.difed.bd.ProveedorBDImplemen;
import com.difed.entidades.Card;
import com.difed.entidades.Puntuacion;
import com.difed.util.Chronometer;


public class Pantallas extends Activity {

	private static int ROW_COUNT = -1;
	private static int COL_COUNT = -1;
	public static final int NIVEL_FACIL = 1;
	public static final int NIVEL_MEDIO = 2;
	public static final int NIVEL_DIFICIL = 3;

	private Context context;
	private Drawable backImage;
	private int[][] cards;
	private List<Drawable> images144;
	private List<Drawable> images96;
	private List<Drawable> images70;
	private Card firstCard;
	private Card seconedCard;
	private ButtonListener buttonListener;

	int maxPantallas = 0;

	private static Object lock = new Object();

	int turns;
	int pixeles;
	int numCartas = 0;
	private TableLayout mainTable;

	private UpdateCardsHandler handler;

	Chronometer cronometro;
	int memoCronometro;

	ProveedorBD proveedor;

	String nombreJugador;
	String tipoCartas = "miaandme";
	int nivel = NIVEL_MEDIO;

	TextView txtPantallas;
	TextView txtNivel;

	// private static PuntuacionGuardarTask taskGuarda;
	String deviceId;

    String sonido;
    private SoundPool soundPool;
    int soundID[] = new int[10];
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		handler = new UpdateCardsHandler();

		setContentView(R.layout.pantalla);

		SharedPreferences settings = getSharedPreferences("ajustes",
				Context.MODE_PRIVATE);

		tipoCartas = settings.getString("cartas", "miaandme");
		nivel = settings.getInt("nivel", NIVEL_MEDIO);
		nombreJugador = settings.getString("nombre", "");

        sonido = settings.getString("sonido", "on");

		mainTable = (TableLayout) findViewById(R.id.TableLayout03);

		if ((tipoCartas == null) || (tipoCartas.equalsIgnoreCase("miaandme"))) {
			// fondo
			try {
				mainTable.setBackgroundResource(R.drawable.fon);
			} catch (OutOfMemoryError E) {

			}

			loadImages144();
			loadImages96();
			loadImages70();

		} /*else {

			if (tipoCartas.equalsIgnoreCase("animales")) {
				try {
					mainTable.setBackgroundResource(R.drawable.fondoanimales);
				} catch (OutOfMemoryError E) {

				}
				loadImagesAnimales144();
				loadImagesAnimales96();
				loadImagesAnimales70();

			} else {

				*//*
				 * mainTable.setBackgroundResource(R.drawable.fondobob);
				 * loadImagesBob144(); loadImagesBob96(); loadImagesBob70();
				 *//*

			}

		}*/

		proveedor = ProveedorBDImplemen.getProveedor(this);

		buttonListener = new ButtonListener();

		context = mainTable.getContext();

		pixeles = 144;

		txtNivel = (TextView) findViewById(R.id.txtnivelp);
		if (nivel == NIVEL_FACIL) {
			txtNivel.setText(getString(R.string.nivel) + " "
					+ getString(R.string.facil));
			maxPantallas = 3;
		}
		if (nivel == NIVEL_MEDIO) {
			txtNivel.setText(getString(R.string.nivel) + " "
					+ getString(R.string.medio));
			maxPantallas = 5;
		}
		if (nivel == NIVEL_DIFICIL) {
			txtNivel.setText(getString(R.string.nivel) + " "
					+ getString(R.string.dificil));
			maxPantallas = 6;
		}

		txtPantallas = (TextView) findViewById(R.id.txtpantallas);
		newGame(2, 2);


        // Sonido
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        // Hardware buttons setting to adjust the media sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Load the sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID[0] = soundPool.load(context, R.raw.ok, 1);
        soundID[1] = soundPool.load(context, R.raw.error, 1);


		// Cronometro
		cronometro = (Chronometer) findViewById(R.id.cronometro);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/reloj.ttf");
		cronometro.setTypeface(tf);
		cronometro.setBase(SystemClock.elapsedRealtime());
		cronometro.start();

	}

	private void newGame(int c, int r) {
		ROW_COUNT = r;
		COL_COUNT = c;

		numCartas = ROW_COUNT * COL_COUNT;

		cards = new int[COL_COUNT][ROW_COUNT];

		if ((ROW_COUNT == 2) && (COL_COUNT == 2)) {
		//	if ((tipoCartas == null) || (tipoCartas.equalsIgnoreCase("christmas"))) {
				backImage = getResources().getDrawable(R.drawable.back144);
//			} else {
//				if (tipoCartas.equalsIgnoreCase("animales")) {
//					backImage = getResources().getDrawable(
//							R.drawable.fondo144anm);
//				} else {
//					if (tipoCartas.equalsIgnoreCase("bob")) {
//						// backImage = getResources().getDrawable(
//						// R.drawable.fondo144bob);
//					}
//				}
//			}
			pixeles = 144;
			txtPantallas.setText("1/" + maxPantallas);
		} else {
			if ((ROW_COUNT == 3) && (COL_COUNT == 2)) {
		//		if ((tipoCartas == null)
		//				|| (tipoCartas.equalsIgnoreCase("christmas"))) {
					backImage = getResources().getDrawable(
							R.drawable.back110);
//				} else {
//					if (tipoCartas.equalsIgnoreCase("animales")) {
//						backImage = getResources().getDrawable(
//								R.drawable.fondo110anm);
//					} else {
//						if (tipoCartas.equalsIgnoreCase("bob")) {
//							// backImage = getResources().getDrawable(
//							// R.drawable.fondo110bob);
//						}
//					}
//				}

				pixeles = 110;
				txtPantallas.setText("2/" + maxPantallas);
			} else {
				if ((ROW_COUNT == 4) && (COL_COUNT == 2)) {
//					if ((tipoCartas == null)
//							|| (tipoCartas.equalsIgnoreCase("christmas"))) {
						backImage = getResources().getDrawable(
								R.drawable.back110);
//					} else {
//						if (tipoCartas.equalsIgnoreCase("animales")) {
//							backImage = getResources().getDrawable(
//									R.drawable.fondo110anm);
//						} else {
//							if (tipoCartas.equalsIgnoreCase("bob")) {
//								// backImage = getResources().getDrawable(
//								// R.drawable.fondo110bob);
//							}
//						}
//					}

					pixeles = 110;
					txtPantallas.setText("3/" + maxPantallas);
				} else {
					if ((ROW_COUNT == 4) && (COL_COUNT == 3)) {
//						if ((tipoCartas == null)
//								|| (tipoCartas.equalsIgnoreCase("christmas"))) {
							backImage = getResources().getDrawable(
									R.drawable.back110);
//						} else {
//							if (tipoCartas.equalsIgnoreCase("animales")) {
//								backImage = getResources().getDrawable(
//										R.drawable.fondo110anm);
//							} else {
//								if (tipoCartas.equalsIgnoreCase("bob")) {
//									// backImage = getResources().getDrawable(
//									// R.drawable.fondo110bob);
//								}
//							}
//						}
						pixeles = 110;
						txtPantallas.setText("4/" + maxPantallas);
					} else {
						if ((ROW_COUNT == 3) && (COL_COUNT == 4)) {
		//					if ((tipoCartas == null)
		//							|| (tipoCartas.equalsIgnoreCase("christmas"))) {
								backImage = getResources().getDrawable(
										R.drawable.back90);
//							} else {
//								if (tipoCartas.equalsIgnoreCase("animales")) {
//									backImage = getResources().getDrawable(
//											R.drawable.fondo90anm);
//								} else {
//									if (tipoCartas.equalsIgnoreCase("bob")) {
//										// backImage =
//										// getResources().getDrawable(
//										// R.drawable.fondo90bob);
//									}
//								}
//							}
							pixeles = 90;
							txtPantallas.setText("5/" + maxPantallas);
						} else {
							// 4 X 4
//							if ((tipoCartas == null)
//									|| (tipoCartas.equalsIgnoreCase("christmas"))) {
								backImage = getResources().getDrawable(
										R.drawable.back90);
//							} else {
//								if (tipoCartas.equalsIgnoreCase("animales")) {
//									backImage = getResources().getDrawable(
//											R.drawable.fondo90anm);
//								} else {
//									if (tipoCartas.equalsIgnoreCase("bob")) {
//										// backImage =
//										// getResources().getDrawable(
//										// R.drawable.fondo90bob);
//									}
//								}
//							}
							pixeles = 90;
							txtPantallas.setText("6/" + maxPantallas);

						}
					}
				}
			}

		}

		// mainTable.removeView(findViewById(R.id.TableRow01));
		// mainTable.removeView(findViewById(R.id.TableRow02));

		TableRow tr = ((TableRow) findViewById(R.id.TableRow03));
		tr.removeAllViews();

		mainTable = new TableLayout(context);

		tr.addView(mainTable);

		for (int y = 0; y < ROW_COUNT; y++) {
			mainTable.addView(createRow(y));
		}

		firstCard = null;
		loadCards();

		turns = 0;

	}

	private void loadCards() {
		try {

			int size = ROW_COUNT * COL_COUNT;

			Log.i("loadCards()", "size=" + size);

			ArrayList<Integer> list = new ArrayList<Integer>();

			list = rellenaList(size);

			// for (int i = 0; i < size; i++) {
			// list.add(new Integer(i));
			// }

			Random r = new Random();
			for (int i = size - 1; i >= 0; i--) {
				int t = 0;

				if (i > 0) {
					t = r.nextInt(i);
				}

				t = list.remove(t).intValue();

				// cards[i % COL_COUNT][i / COL_COUNT] = t % (size / 2);
				cards[i % COL_COUNT][i / COL_COUNT] = t;

				Log.i("loadCards()", "card[" + (i % COL_COUNT) + "]["
						+ (i / COL_COUNT) + "]="
						+ cards[i % COL_COUNT][i / COL_COUNT]);
			}
		} catch (Exception e) {
			Log.e("loadCards()", e + "");
		}

	}

	private ArrayList<Integer> rellenaList(int size) {

		ArrayList<Integer> l = new ArrayList<Integer>();
		ArrayList<Integer> lstImagenes = new ArrayList<Integer>();

        //Sustituido por 14
        // for (int j = 0; j < 10; j++) {
        for (int j = 0; j < 15; j++) {
			l.add(j);
		}
		Collections.shuffle(l);

		for (int i = 0; i < (size / 2); i++) {
			lstImagenes.add(l.get(i));
		}

		for (int i = 0; i < (size / 2); i++) {
			lstImagenes.add(lstImagenes.get(i));
		}

		return lstImagenes;

	}

	private TableRow createRow(int y) {
		TableRow row = new TableRow(context);
		row.setHorizontalGravity(Gravity.CENTER);

		for (int x = 0; x < COL_COUNT; x++) {
			row.addView(createImageButton(x, y));
		}
		return row;
	}

	private View createImageButton(int x, int y) {
		Button button = new Button(context);
		if (Build.VERSION.SDK_INT >= 16) {
			button.setBackground(backImage);
		}else{
			button.setBackgroundDrawable(backImage);
		}
		
		button.setId(100 * x + y);
		button.setOnClickListener(buttonListener);

		LinearLayout ll = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		params.setMargins(8, 8, 8, 8); // left, top, right, bottom
		button.setLayoutParams(params);

		ll.addView(button);

		return ll;
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			synchronized (lock) {
				if (firstCard != null && seconedCard != null) {
					return;
				}
				int id = v.getId();
				int x = id / 100;
				int y = id % 100;
				turnCard((Button) v, x, y);
			}

		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void turnCard(Button button, int x, int y) {

           // int sdk = android.os.Build.VERSION.SDK_INT;
			if (pixeles == 144) {

                if (Build.VERSION.SDK_INT >= 16) {
                    button.setBackground(images144.get(cards[x][y]));
                } else {
                    button.setBackgroundDrawable(images144.get(cards[x][y]));
                }

//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    button.setBackgroundDrawable(images144.get(cards[x][y]));
//                } else {
//                    button.setBackground(images144.get(cards[x][y]));
//                }
                //button.setBackground(images144.get(cards[x][y]));
			}
			if (pixeles == 110) {

                if (Build.VERSION.SDK_INT >= 16) {
                    button.setBackground(images96.get(cards[x][y]));
                } else {
                    button.setBackgroundDrawable(images96.get(cards[x][y]));
                }

//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    button.setBackgroundDrawable(images96.get(cards[x][y]));
//                } else {
//                    button.setBackground(images96.get(cards[x][y]));
//                }
			}
			if (pixeles == 90) {

                if (Build.VERSION.SDK_INT >= 16) {
                    button.setBackground(images70.get(cards[x][y]));
                } else {
                    button.setBackgroundDrawable(images70.get(cards[x][y]));
                }
//
//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    button.setBackgroundDrawable(images70.get(cards[x][y]));
//                } else {
//                    button.setBackground(images70.get(cards[x][y]));
//                }
			}

			if (firstCard == null) {
				firstCard = new Card(button, x, y);
			} else {

				if (firstCard.x == x && firstCard.y == y) {
					return; // the user pressed the same card
				}

				seconedCard = new Card(button, x, y);

				turns++;
				// ((TextView)findViewById(R.id.tv1)).setText("Tries: "+turns);

				TimerTask tt = new TimerTask() {

					@Override
					public void run() {
						try {
							synchronized (lock) {
								handler.sendEmptyMessage(0);
							}
						} catch (Exception e) {
							Log.e("E1", e.getMessage());
						}
					}
				};

				Timer t = new Timer(false);
				t.schedule(tt, 500);

			}

		}

	}

	class UpdateCardsHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			synchronized (lock) {
				checkCards();
			}
		}

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public void checkCards() {

           // int sdk = android.os.Build.VERSION.SDK_INT;

			if (cards[seconedCard.x][seconedCard.y] == cards[firstCard.x][firstCard.y]) {
				firstCard.button.setVisibility(View.INVISIBLE);
				seconedCard.button.setVisibility(View.INVISIBLE);

				numCartas = numCartas - 2;

				// BORRAR !!
				// numCartas = 0;

				// Si es 0, pasamos de pantalla
				if (numCartas == 0) {

					// VALIDO !!
					if (comprobarPasoPantalla() == 1) {

						newGame(COL_COUNT, ROW_COUNT);

					} else {
						// -----------------

						// Mostrar para insertar nombre, o no
						// Final de partida.
						// Paramos cronometro.
						memoCronometro = (int) (SystemClock.elapsedRealtime() - cronometro
								.getBase());
						cronometro.stop();

						// Insertamos el jugador y su tiempo, si procede.

						Puntuacion p = new Puntuacion();
						p.setNombre(nombreJugador);
						p.setTiempo(memoCronometro);
						if (proveedor.numJugadores(nivel) < 10) {

							proveedor.Insertar(p, nivel);

						} else {

							Puntuacion pPeor = new Puntuacion();
							pPeor = proveedor.ultimoValor(nivel);

							if (pPeor.getTiempo() > memoCronometro) {
								proveedor.Eliminar(pPeor, nivel);

								proveedor.Insertar(p, nivel);
							}

						}

						// Insertamos en RED
						// User user = new User();
						// user.setUsername(nombreJugador);
						// user.setTime(memoCronometro);
						// deviceId = DeviceID.getID(context);
						// taskGuarda = new PuntuacionGuardarTask();
						// taskGuarda.setUser(user);
						// taskGuarda.execute();

						// try {
						// Thread.sleep(500);
						// } catch (InterruptedException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }

						// MOSTRAMOS PUNTUACION
						Intent i = new Intent(context, PuntuacionTotal.class);
						i.putExtra("tiempo", memoCronometro);
						startActivity(i);
						finish();

						// valido !!
					}
				}

			} else {

                if (Build.VERSION.SDK_INT >= 16) {
                    seconedCard.button.setBackground(backImage);
                } else {
                    seconedCard.button.setBackgroundDrawable(backImage);
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    firstCard.button.setBackground(backImage);
                } else {
                    firstCard.button.setBackgroundDrawable(backImage);
                }

//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    seconedCard.button.setBackgroundDrawable(backImage);
//                } else {
//                    seconedCard.button.setBackground(backImage);
//                }
//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    firstCard.button.setBackgroundDrawable(backImage);
//                } else {
//                    firstCard.button.setBackground(backImage);
//                }
//				seconedCard.button.setBackground(backImage);
//				firstCard.button.setBackground(backImage);
			}

			firstCard = null;
			seconedCard = null;

		}

	}

	private int comprobarPasoPantalla() {

		int volver = 0;
		boolean bandera = false;

		if ((ROW_COUNT == 2) && (COL_COUNT == 2)) {
			ROW_COUNT = 3;
			volver = 1;
			bandera = true;
		}

		if ((ROW_COUNT == 3) && (COL_COUNT == 2) && !bandera) {
			ROW_COUNT = 4;
			COL_COUNT = 2;
			volver = 1;
			bandera = true;
		}

		if ((ROW_COUNT == 4) && (COL_COUNT == 2) && (nivel != NIVEL_FACIL)
				&& !bandera) {
			ROW_COUNT = 4;
			COL_COUNT = 3;
			volver = 1;
			bandera = true;
		}

		if ((ROW_COUNT == 4) && (COL_COUNT == 3) && (nivel != NIVEL_FACIL)
				&& !bandera) {
			ROW_COUNT = 3;
			COL_COUNT = 4;
			volver = 1;
			bandera = true;
		}

		if ((ROW_COUNT == 3) && (COL_COUNT == 4) && (nivel != NIVEL_MEDIO)
				&& !bandera) {
			ROW_COUNT = 4;
			COL_COUNT = 4;
			volver = 1;
			bandera = true;
		}

		if ((ROW_COUNT == 4) && (COL_COUNT == 4) && !bandera) {

			volver = 0;
			bandera = true;
		}
		return volver;

	}


	// imagenes
	private void loadImages144() {
		images144 = new ArrayList<Drawable>();

		images144.add(getResources().getDrawable(R.drawable.mia1441));
		images144.add(getResources().getDrawable(R.drawable.mia1442));
		images144.add(getResources().getDrawable(R.drawable.mia1443));
		images144.add(getResources().getDrawable(R.drawable.mia1444));
		images144.add(getResources().getDrawable(R.drawable.mia1445));
		images144.add(getResources().getDrawable(R.drawable.mia1446));
		images144.add(getResources().getDrawable(R.drawable.mia1447));
		images144.add(getResources().getDrawable(R.drawable.mia1448));
		images144.add(getResources().getDrawable(R.drawable.mia1449));
		images144.add(getResources().getDrawable(R.drawable.mia14410));
        images144.add(getResources().getDrawable(R.drawable.mia14411));
        images144.add(getResources().getDrawable(R.drawable.mia14412));
        images144.add(getResources().getDrawable(R.drawable.mia14413));
        images144.add(getResources().getDrawable(R.drawable.mia14414));
        images144.add(getResources().getDrawable(R.drawable.mia14415));

	}

	private void loadImages96() {
		images96 = new ArrayList<Drawable>();

		images96.add(getResources().getDrawable(R.drawable.mia1101));
		images96.add(getResources().getDrawable(R.drawable.mia1102));
		images96.add(getResources().getDrawable(R.drawable.mia1103));
		images96.add(getResources().getDrawable(R.drawable.mia1104));
		images96.add(getResources().getDrawable(R.drawable.mia1105));
		images96.add(getResources().getDrawable(R.drawable.mia1106));
		images96.add(getResources().getDrawable(R.drawable.mia1107));
		images96.add(getResources().getDrawable(R.drawable.mia1108));
		images96.add(getResources().getDrawable(R.drawable.mia1109));
		images96.add(getResources().getDrawable(R.drawable.mia11010));
        images96.add(getResources().getDrawable(R.drawable.mia11011));
        images96.add(getResources().getDrawable(R.drawable.mia11012));
        images96.add(getResources().getDrawable(R.drawable.mia11013));
        images96.add(getResources().getDrawable(R.drawable.mia11014));
        images96.add(getResources().getDrawable(R.drawable.mia11015));
	}

	private void loadImages70() {
		images70 = new ArrayList<Drawable>();

		images70.add(getResources().getDrawable(R.drawable.mia901));
		images70.add(getResources().getDrawable(R.drawable.mia902));
		images70.add(getResources().getDrawable(R.drawable.mia903));
		images70.add(getResources().getDrawable(R.drawable.mia904));
		images70.add(getResources().getDrawable(R.drawable.mia905));
		images70.add(getResources().getDrawable(R.drawable.mia906));
		images70.add(getResources().getDrawable(R.drawable.mia907));
		images70.add(getResources().getDrawable(R.drawable.mia908));
		images70.add(getResources().getDrawable(R.drawable.mia909));
		images70.add(getResources().getDrawable(R.drawable.mia9010));
        images70.add(getResources().getDrawable(R.drawable.mia9011));
        images70.add(getResources().getDrawable(R.drawable.mia9012));
        images70.add(getResources().getDrawable(R.drawable.mia9013));
        images70.add(getResources().getDrawable(R.drawable.mia9014));
        images70.add(getResources().getDrawable(R.drawable.mia9015));

	}


}
