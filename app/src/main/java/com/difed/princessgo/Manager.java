package com.difed.princessgo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
 
public class Manager extends Activity { 

	private static final String AD_UNIT_ID = "ca-app-pub-7866637665636353/7609794629";
	                                          

	final private static int DIALOG_LOGIN = 1;
	public static final int NIVEL_FACIL = 1;
	public static final int NIVEL_MEDIO = 2;
	public static final int NIVEL_DIFICIL = 3;
	Button btnNuevo;
	Button btnPuntuacion;
	Button btnAjustes;

	String nombreJugador;

	Context context;

	//final String nivel[] = { "Fácil", "Medio", "Difícil" };
//String nivel[] = { getResources().getString(R.string.facil), getResources().getString(R.string.medio), getResources().getString(R.string.dificil) };

	private InterstitialAd interstitial;

	@Override
	protected void onDestroy() {
		
		
		super.onDestroy();
		
		displayInterstitial();
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		// handler = new UpdateCardsHandler();
		// loadImages();
		setContentView(R.layout.main);

		context = this;

		btnNuevo = (Button) findViewById(R.id.btn_nuevo);
		btnNuevo.setOnClickListener(new View.OnClickListener() {

			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				showDialog(DIALOG_LOGIN);

				// botonPulsado("Nuevo");

			}

		});
		
		ArrayList<String> niveles  = new ArrayList<String>();
        niveles.add(getString(R.string.facil));
        niveles.add(getString(R.string.medio));
        niveles.add(getString(R.string.dificil));
        
        final CharSequence[] cs = niveles.toArray(new CharSequence[niveles.size()]);
       
       
		btnPuntuacion = (Button) findViewById(R.id.btn_puntuacion);
		btnPuntuacion.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder ab = new AlertDialog.Builder(Manager.this);
				ab.setTitle(getString(R.string.puntuacion));
				ab.setItems(cs, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface d, int choice) {
						if (choice == 0) {

							SharedPreferences settings = context
									.getSharedPreferences("ajustes",
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = settings.edit();
							editor.putInt("puntuacion", NIVEL_FACIL);
							editor.apply();

						} else if (choice == 1) {

							SharedPreferences settings = context
									.getSharedPreferences("ajustes",
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = settings.edit();
							editor.putInt("puntuacion", NIVEL_MEDIO);
							editor.apply();
						} else if (choice == 2) {

							SharedPreferences settings = context
									.getSharedPreferences("ajustes",
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = settings.edit();
							editor.putInt("puntuacion", NIVEL_DIFICIL);
							editor.apply();
						}

						startActivity(new Intent(context, PuntuacionTotal.class));
					}
				});
				ab.show();

			}
		}); 

		btnAjustes = (Button) findViewById(R.id.btn_ajustes);
		btnAjustes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				botonPulsado("Ajustes");
				

			}

		});
		
		
		

		// backImage = getResources().getDrawable(R.drawable.fondo144b);
		//
		//
		//
		// buttonListener = new ButtonListener();
		//
		// mainTable = (TableLayout)findViewById(R.id.TableLayout03);
		//
		//
		// context = mainTable.getContext();
		//
		// newGame(2,2);
		
		
		// Create the interstitial.
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(AD_UNIT_ID);
		
		// Create ad request.
	 //TODO EL bueno
	   AdRequest adRequest = new AdRequest.Builder().build();

		// Check the logcat output for your hashed device ID to get test ads on a physical device.
        //TEST
//	    AdRequest adRequest = new AdRequest.Builder()
//	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//	       .addTestDevice("A31D280E26B4043E35F3B89D12C66B27")
//	        .build();

	    // Load the interstitial ad.
	    interstitial.loadAd(adRequest);
		
	  
		
	}
	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;

		switch (id) {
		case DIALOG_LOGIN:
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogview = inflater.inflate(R.layout.nombre_dialog, null);

			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setTitle(getString(R.string.introducenombre));
			dialogbuilder.setView(dialogview);
			dialogDetails = dialogbuilder.create();

			break;
		}

		return dialogDetails;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case DIALOG_LOGIN:
			final AlertDialog alertDialog = (AlertDialog) dialog;
			Button loginbutton = (Button) alertDialog
					.findViewById(R.id.btn_login);
			Button cancelbutton = (Button) alertDialog
					.findViewById(R.id.btn_cancel);
			final EditText userName = (EditText) alertDialog
					.findViewById(R.id.txt_name);

			loginbutton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();

					// JUGANDO
					SharedPreferences settings = context.getSharedPreferences(
							"ajustes", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("puntuacion", -1);
					editor.apply();
					editor.putString("nombre", userName.getText().toString());
					editor.apply();

					// nombreJugador = userName.getText().toString();
					// Intent i = new Intent(context, Pantallas.class);
					// i.putExtra("nombre", nombreJugador);
					// editor.commit();

					// SharedPreferences settings =
					// context.getSharedPreferences("ajustes",
					// Context.MODE_PRIVATE);
					// String tipoCartas = settings.getString("cartas",
					// "frozen");
					// String nivel = settings.getString("nivel", "medio");
					//
					// Bundle bundle = getIntent().getExtras();
					//
					// if (bundle != null) {
					// String tipoCartas = bundle.getString("cartas");
					// i.putExtra("cartas", tipoCartas);
					//
					// String nivel = bundle.getString("nivel");
					// i.putExtra("nivel", nivel);
					//
					// }

					Intent i = new Intent(context, Pantallas.class);
					startActivity(i);

				}
			});

			cancelbutton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			break;
		}
	}

	private void botonPulsado(String boton) {

		if (boton.equalsIgnoreCase("Nuevo")) {

			Bundle bundle = getIntent().getExtras();

			if (bundle != null) {
				if (!bundle.containsKey("nombre")) {
					mostrarNombre();
				} else {
					nombreJugador = bundle.getString("nombre");
				}
			} else {
				mostrarNombre();
			}

		}

		if (boton.equalsIgnoreCase("Ajustes")) {
			startActivity(new Intent(this, Ajustes.class));
		}

	}

	private void mostrarNombre() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.nombre));

		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton(getString(R.string.comenzar),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						nombreJugador = input.getText().toString();
						Intent i = new Intent(context, Pantallas.class);
						i.putExtra("nombre", nombreJugador);
						startActivity(i);
					}
				});

		builder.setNegativeButton(getString(R.string.cancelar),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}

}