package com.difed.princessgo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.difed.bd.ProveedorBD;
import com.difed.bd.ProveedorBDImplemen;


public class Ajustes extends Activity {
	
	public static final int NIVEL_FACIL = 1;
	public static final int NIVEL_MEDIO = 2;
	public static final int NIVEL_DIFICIL = 3;

	Button btnBorrar;
	Button btnNivel;

    Button btnEmail;
    Button btnPlay;
    Button btnCompartir;

	final Context context = this;
	ProveedorBD proveedor;

	//final String items[] = { this.getString(R.string.frozen), this.getString(R.string.animales), this.getString(R.string.bob) };
	//final String nivel[] = { this.getString(R.string.facil), this.getString(R.string.medio), this.getString(R.string.dificil) };
	
	String items[];
	String nivel[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.ajustes);

		proveedor = ProveedorBDImplemen.getProveedor(this);
		
		
		items = getResources().getStringArray(R.array.cartas); 
		nivel = getResources().getStringArray(R.array.nivel);
		

		btnBorrar = (Button) findViewById(R.id.btn_borrarbd);
		btnBorrar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder ab = new AlertDialog.Builder(Ajustes.this);
				ab.setTitle(getString(R.string.borrarbasedatos));
				ab.setItems(nivel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface d, int choice) {
						if (choice == 0) {
							
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);

							// set title
							alertDialogBuilder.setTitle(getString(R.string.eliminarpuntuacion));

							// set dialog message
							alertDialogBuilder
									.setMessage(
											getString(R.string.preguntaeliminarfacil))
									.setCancelable(false)
									.setPositiveButton(getString(R.string.si),
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													
													proveedor.borrarBdCompleta(NIVEL_FACIL);

													
												}
											})
									.setNegativeButton("No",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													// if this button is clicked, just close
													// the dialog box and do nothing
													dialog.cancel();
												}
											});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();

							// show it
							alertDialog.show();
							
							
						} else if (choice == 1) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);

							// set title
							alertDialogBuilder.setTitle(getString(R.string.eliminarpuntuacion));

							// set dialog message
							alertDialogBuilder
									.setMessage(
											getString(R.string.preguntaeliminarmedio))
									.setCancelable(false)
									.setPositiveButton(getString(R.string.si),
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													
													proveedor.borrarBdCompleta(NIVEL_MEDIO);

													
												}
											})
									.setNegativeButton("No",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													// if this button is clicked, just close
													// the dialog box and do nothing
													dialog.cancel();
												}
											});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();

							// show it
							alertDialog.show();							
//							
							
						}else if (choice == 2) {
							
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);

							// set title
							alertDialogBuilder.setTitle(getString(R.string.eliminarpuntuacion));

							// set dialog message
							alertDialogBuilder
									.setMessage(
											getString(R.string.preguntaeliminardificil))
									.setCancelable(false)
									.setPositiveButton(getString(R.string.si),
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													
													proveedor.borrarBdCompleta(NIVEL_DIFICIL);

													
												}
											})
									.setNegativeButton("No",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int id) {
													// if this button is clicked, just close
													// the dialog box and do nothing
													dialog.cancel();
												}
											});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();

							// show it
							alertDialog.show();				
							
//							
						}
					}
				});
				ab.show();

			}
		});

//		btnTipoCarta = (Button) findViewById(R.id.btn_tipocarta);
//		btnTipoCarta.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				AlertDialog.Builder ab = new AlertDialog.Builder(Ajustes.this);
//				ab.setTitle(getString(R.string.tipocartas));
//				ab.setItems(items, new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface d, int choice) {
//						if (choice == 0) {
//
//							SharedPreferences settings = context.getSharedPreferences("ajustes",
//				            		Context.MODE_PRIVATE);
//				            SharedPreferences.Editor editor = settings.edit();
//				            editor.putString("cartas", "frozen");
//				            editor.apply();
//
//
//
//
////							Intent mainIntent = new Intent(Ajustes.this,
////									Manager.class);
////							mainIntent.putExtra("cartas", "frozen");
////							//Ajustes.this.startActivity(mainIntent);
//
//						} else if (choice == 1) {
//
//							SharedPreferences settings = context.getSharedPreferences("ajustes",
//				            		Context.MODE_PRIVATE);
//				            SharedPreferences.Editor editor = settings.edit();
//				            editor.putString("cartas", "animales");
//				            editor.apply();
//
//
////							Intent mainIntent = new Intent(Ajustes.this,
////									Manager.class);
////							mainIntent.putExtra("cartas", "animales");
//							//Ajustes.this.startActivity(mainIntent);
//
//						} else if (choice == 2) {
//
//							SharedPreferences settings = context.getSharedPreferences("ajustes",
//				            		Context.MODE_PRIVATE);
//				            SharedPreferences.Editor editor = settings.edit();
//				            editor.putString("cartas", "bob");
//				            editor.apply();
//
//
////							Intent mainIntent = new Intent(Ajustes.this,
////									Manager.class);
////							mainIntent.putExtra("cartas", "animales");
//							//Ajustes.this.startActivity(mainIntent);
//
//						}
//					}
//				});
//				ab.show();
//
//			}
//		});
		
		
		btnNivel = (Button) findViewById(R.id.btn_nivel);
		btnNivel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder ab = new AlertDialog.Builder(Ajustes.this);
				ab.setTitle(getString(R.string.niveljuego));
				ab.setItems(nivel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface d, int choice) {
						if (choice == 0) {
							
							SharedPreferences settings = context.getSharedPreferences("ajustes",
				            		Context.MODE_PRIVATE);
				            SharedPreferences.Editor editor = settings.edit();
				            editor.putInt("nivel", NIVEL_FACIL);
				            editor.apply();
							
//							Intent mainIntent = new Intent(Ajustes.this,
//									Manager.class);
//							mainIntent.putExtra("nivel", "facil");
							//Ajustes.this.startActivity(mainIntent);
							
						} else if (choice == 1) {
							
							SharedPreferences settings = context.getSharedPreferences("ajustes",
				            		Context.MODE_PRIVATE);
				            SharedPreferences.Editor editor = settings.edit();
				            editor.putInt("nivel", NIVEL_MEDIO);
				            editor.apply();
							
//							Intent mainIntent = new Intent(Ajustes.this,
//									Manager.class);
//							mainIntent.putExtra("nivel", "medio");
							//Ajustes.this.startActivity(mainIntent);
							
						}  else if (choice == 2) {
							
							SharedPreferences settings = context.getSharedPreferences("ajustes",
				            		Context.MODE_PRIVATE);
				            SharedPreferences.Editor editor = settings.edit();
				            editor.putInt("nivel", NIVEL_DIFICIL);
				            editor.apply();
							
//							Intent mainIntent = new Intent(Ajustes.this,
//									Manager.class);
//							mainIntent.putExtra("nivel", "dificil");
							//Ajustes.this.startActivity(mainIntent);
							
						}
					}
				});
				ab.show();

			}
		});
		
		
//		btnacercade = (Button) findViewById(R.id.btn_acercade);
//		btnacercade.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				startActivity(new Intent(context, Acercade.class));
//
//			}
//		});


        btnEmail = (Button) findViewById(R.id.about_like_mail);
        btnEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                botonPulsado("email");

            }
        });

        btnPlay = (Button) findViewById(R.id.about_like_market);
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                botonPulsado("play");

            }
        });

        btnCompartir = (Button) findViewById(R.id.about_like_compartir);
        btnCompartir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                botonPulsado("compartir");

            }
        });
	}

    private void botonPulsado(String boton) {

        if (boton.equalsIgnoreCase("email")) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.about_problem_mail)});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_problem_subject));
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(intent);

        }
        if (boton.equalsIgnoreCase("play")) {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName()));
            startActivity(intent);

        }
        if (boton.equalsIgnoreCase("compartir")) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.about_twitter_msg));
            startActivity(intent);

        }
    }

}
