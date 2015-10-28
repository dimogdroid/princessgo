package com.difed.princessgo;

import java.util.ArrayList;
import java.util.List;

//import difed.christmas.bd.ProveedorBD;
//import difed.christmas.bd.ProveedorBDImplemen;
//import difed.christmas.entidades.ListaPuntuacion;
//import difed.christmas.entidades.Puntuacion;
//import difed.christmas.service.RankingServiceFactory;
//import difed.christmas.service.model.User;
//import difed.christmas.service.utils.DeviceID;
//import difed.christmas.util.PuntuacionAdapter;
//import difed.christmas.util.Util;
import retrofit.RetrofitError;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
//
import com.difed.bd.ProveedorBD;
import com.difed.bd.ProveedorBDImplemen;
import com.difed.entidades.ListaPuntuacion;
import com.difed.entidades.Puntuacion;
import com.difed.service.RankingServiceFactory;
import com.difed.service.model.User;
import com.difed.service.utils.DeviceID;
import com.difed.util.PuntuacionAdapter;
import com.difed.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class PuntuacionTotal extends Activity {

	private static final String MY_AD_UNIT_ID = "ca-app-pub-7866637665636353/6133061428";

	final Context context = this;
	public static final int NIVEL_FACIL = 1;
	public static final int NIVEL_MEDIO = 2;
	public static final int NIVEL_DIFICIL = 3;

	private static PuntuacionTotalTask task;

	private ListView mainListView;
	ArrayList<String> lstPuntuacion;
	List<Puntuacion> lsttemp;

	ListaPuntuacion[] listado;

	ProveedorBD proveedor;

	int nivel = NIVEL_MEDIO;
	String sNivel = "";
	String nombreJugador;
	int tiempo;

	RankingServiceFactory rkService;
	String deviceId;

	TextView txtNumeroG;
	TextView txtNombreG;
	TextView txtTiempoG;

	ProgressBar myPB;

	Button btnvermas;

	Util util;

	private AdView adView;

	Puntuacion mejorTiempo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.puntuacion);

		proveedor = ProveedorBDImplemen.getProveedor(this);

		rkService = new RankingServiceFactory();

		lstPuntuacion = new ArrayList<String>();
		lsttemp = new ArrayList<Puntuacion>();

		SharedPreferences settings = getSharedPreferences("ajustes",
				Context.MODE_PRIVATE);
		nivel = settings.getInt("puntuacion", NIVEL_MEDIO);
		nombreJugador = settings.getString("nombre", "");

		if (nivel == -1) { // Estabamos jugando
			nivel = settings.getInt("nivel", NIVEL_MEDIO);
		}

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			if (bundle.containsKey("tiempo")) {
				tiempo = bundle.getInt("tiempo");
			}
		}
		// Recuperamos el mejor tiempo
		mejorTiempo = new Puntuacion();
		mejorTiempo = proveedor.primerValor(nivel);

		// Publicidad
		// ---------------------------------------------------------
		// Crear adView.

		adView = new AdView(this);
		adView.setAdUnitId(MY_AD_UNIT_ID);
		adView.setAdSize(AdSize.BANNER);

		// Buscar LinearLayout suponiendo que se le ha asignado
		// el atributo android:id="@+id/mainLayout".
		FrameLayout frmlayout = (FrameLayout) findViewById(R.id.publicidad);

		// Añadirle adView.
		frmlayout.addView(adView);

		// Iniciar una solicitud genérica. VALIDA
		AdRequest adRequest = new AdRequest.Builder().build();

		// Test
		// AdRequest adRequest = new AdRequest.Builder()
		// .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) // Todos los emuladores
		// .addTestDevice("A31D280E26B4043E35F3B89D12C66B27") // Mi teléfono de
		// prueba Galaxy Nexus
		// .build();

		// Cargar adView con la solicitud de anuncio.
		adView.loadAd(adRequest);
		// --------------------------------------------------------------------------

		myPB = (ProgressBar) findViewById(R.id.myProgress);
		txtNumeroG = (TextView) findViewById(R.id.txtNumeroG);
		txtNombreG = (TextView) findViewById(R.id.txtNombreG);
		txtTiempoG = (TextView) findViewById(R.id.txtTiempoG);

		util = new Util();

		// Llamamos al Hilo

		deviceId = DeviceID.getID(context);

		User user = new User();
		user.setUsername(nombreJugador);
		user.setTime(tiempo);

		task = new PuntuacionTotalTask();
		task.setUser(user);
		task.execute();

		lsttemp = proveedor.lstListaCompleta(nivel);

		listado = new ListaPuntuacion[lsttemp.size()];

		if (lsttemp != null) {
			for (Puntuacion ptn : lsttemp) {
				// int minuto = (int) (ptn.getTiempo() / 60000);
				// int restominuto = (int) (ptn.getTiempo() % 60000);
				// int segundos = restominuto / 1000;
				// int restosegundos = restominuto % 1000;
				//
				// String sMinuto;
				// if (minuto < 10) {
				// sMinuto = "0" + String.valueOf(minuto);
				// } else {
				// sMinuto = String.valueOf(minuto);
				// }
				// String sSegundo;
				// if (segundos < 10) {
				// sSegundo = "00" + String.valueOf(segundos);
				// } else {
				// sSegundo = String.valueOf(segundos);
				// }
				// String sRestoSegundo;
				// if (restosegundos < 99) {
				// sRestoSegundo = "0" + String.valueOf(restosegundos);
				// } else {
				// sRestoSegundo = String.valueOf(restosegundos);
				// }
				//
				// String nombre = ptn.getNombre();
				// int posicion = ptn.getPosicion();
				//
				// String sTime = sMinuto + ":" + sSegundo + ":" +
				// sRestoSegundo;

				String sTime = util.transformarMilisegundos(ptn.getTiempo());

				ListaPuntuacion puntos = new ListaPuntuacion();
				puntos.setNombre(ptn.getNombre());
				puntos.setNumero(ptn.getPosicion());
				puntos.setTiempo(sTime);

				if (tiempo == ptn.getTiempo()) {
					puntos.setActivo(1);
				} else {
					puntos.setActivo(0);
				}

				listado[ptn.getPosicion() - 1] = puntos;

				// lstPuntuacion.add(nombre + "     " + sTime);
			}
		}

		PuntuacionAdapter adapter = new PuntuacionAdapter(context,
				R.layout.puntuacionrow, listado);

		mainListView = (ListView) findViewById(R.id.listView1);

		View header = (View) getLayoutInflater().inflate(
				R.layout.puntuacion_header, null);

		TextView txtNivel = (TextView) header.findViewById(R.id.txtNivel);

		if (nivel == NIVEL_FACIL) {
			txtNivel.setText(getString(R.string.facil));
		}
		if (nivel == NIVEL_MEDIO) {
			txtNivel.setText(getString(R.string.medio));
		}
		if (nivel == NIVEL_DIFICIL) {
			txtNivel.setText(getString(R.string.dificil));
		}

		mainListView.addHeaderView(header);

		mainListView.setAdapter(adapter);

		// user = RankingServiceFactory.createRankingService().postTime(nivel,
		// tiempo, deviceId, nombreJugador);

		btnvermas = (Button) findViewById(R.id.btn_vermas);
		btnvermas.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(context, PuntuacionGlobal.class);
				i.putExtra("tiempo", tiempo);
				startActivity(i);

			}

		});

	}

	class PuntuacionTotalTask extends AsyncTask<Void, Boolean, User> {

		private User user;

		public void setUser(User user) {
			this.user = user;
		}

		@Override
		protected User doInBackground(Void... params) {

			// !!Guardamos si es nuevo, es decir, si el tiempo > 0

			if (user.getTime() > 0) {
				int cont;
				cont = 0;
				while (cont <= 3) {
					try {
						user = RankingServiceFactory.createRankingService()
								.postGuarda(nivel, deviceId, user.getTime(),
										user.getUsername());
					} catch (RetrofitError e) {

					}
					if ((user.getPosition() == -1)) {
						user.setUsername(nombreJugador);
						try {
							Thread.sleep(2000);
						} catch (Exception e) {

						}
						cont++;
					} else {
						cont = 4;
					}
				}

			}

			int contador;
			contador = 0;

			while (contador <= 3) {

				// Recuperamos la posicion del user
				// ----- x 2 veces, por si acaso....
				try {
					user = RankingServiceFactory.createRankingService()
							.postTime(nivel, deviceId);
				} catch (RetrofitError e) {

				}

				if (user.getPosition() <= 0) {

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					contador++;
				} else {
					contador = 4;
				}

			}

			return user;
		}

		@Override
		protected void onPostExecute(User user) {

			myPB.setVisibility(View.INVISIBLE);
			if (user.getPosition() > 0) {
				txtNumeroG.setText(Integer.toString(user.getPosition()));
				txtNombreG.setText(user.getUsername());
				txtTiempoG
						.setText(util.transformarMilisegundos(user.getTime()));
			} else {
				txtNombreG.setText(getString(R.string.sindatos));
			}

			// txtPGlobal.setText(user.getPosition() + " " + user.getUsername()
			// + " " + user.getTime());

		}

	}

	@Override
	public void onPause() {
		adView.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		adView.resume();
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

}
