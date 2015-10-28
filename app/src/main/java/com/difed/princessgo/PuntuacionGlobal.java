package com.difed.princessgo;


//import difed.christmas.service.RankingServiceFactory;
//import difed.christmas.service.model.Result;
//import difed.christmas.service.model.User;
//import difed.christmas.service.utils.DeviceID;
//import difed.christmas.util.PuntuacionGlobalAdapter;


import retrofit.RetrofitError;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.difed.service.RankingServiceFactory;
import com.difed.service.model.Result;
import com.difed.service.model.User;
import com.difed.service.utils.DeviceID;
import com.difed.util.PuntuacionGlobalAdapter;

import java.util.Collections;
import java.util.List;


public class PuntuacionGlobal extends Activity {

	final Context context = this;

	public static final int NIVEL_FACIL = 1;
	public static final int NIVEL_MEDIO = 2;
	public static final int NIVEL_DIFICIL = 3;

	private static PuntuacionGlobalTask task;

	String nombreJugador;
	int tiempo;
	String deviceId;
	int nivel = NIVEL_MEDIO;

	private ListView mainListView;
	PuntuacionGlobalAdapter ptAdapter;

	ProgressBar myProgresG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.puntuacionglobal);

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

		deviceId = DeviceID.getID(context);

		task = new PuntuacionGlobalTask();

		task.execute();

		ptAdapter = new PuntuacionGlobalAdapter(context,
				R.layout.puntuacionrow, null);

		mainListView = (ListView) findViewById(R.id.listViewGlobal);

		View header = (View) getLayoutInflater().inflate(
				R.layout.puntuacion_header, null);

		TextView txtHeader = (TextView) header.findViewById(R.id.txtHeader);
		txtHeader.setText(getString(R.string.puntuacionglobal));

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

		myProgresG = (ProgressBar) findViewById(R.id.myProgressG);

		mainListView.addHeaderView(header);

		mainListView.setAdapter(ptAdapter);

	}

	class PuntuacionGlobalTask extends AsyncTask<Void, Boolean, List<User>> {
		int positionUser;

		@Override
		protected List<User> doInBackground(Void... params) {
			List<User> lstFinal = null;

			int contador;
			contador = 0;
			Result result = null;
			while (contador <= 3) {

				try {
					result = RankingServiceFactory.createRankingService()
							.listRanking(nivel, deviceId);

				} catch (RetrofitError e) {

				}
				if (result == null) {
					try {
						Thread.sleep(2000);
					} catch (Exception ex) {
						
					}
					contador++;
				} else {
					contador = 4;
				}
			}

			// VALIDO !!!!!
            try {
                if (result != null) {
                    lstFinal = result.getRanking().getUserList();

                    User user_separator = new User();
                    user_separator.setTime(0);
                    lstFinal.add(user_separator);

                    //
                    if (result.getUserRanking().getAboveList()!=null){
                        if (result.getUserRanking().getAboveList().size()==2){
                            result.getUserRanking().getAboveList().get(0).setPosition(result.getUserRanking().getAboveList().get(1).getPosition());
                            result.getUserRanking().getAboveList().get(1).setPosition(result.getUserRanking().getAboveList().get(0).getPosition()-1);
                        }
                        Collections.reverse(result.getUserRanking().getAboveList());
                    }

                    lstFinal.addAll(result.getUserRanking().getAboveList());
                    //

                    if ((result.getUserRanking().getAboveList().size() > 0)
                            || (result.getUserRanking().getBelowList().size() > 0)) {
                        lstFinal.add(result.getUser());
                    }
                    positionUser = result.getUser().getPosition();

                    //
                    lstFinal.addAll(result.getUserRanking().getBelowList());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lstFinal;

			// Response response = RankingServiceFactory
			// .createRankingService().listRanking(nivel, deviceId);
			//
			// // CustomLog.debug("Error", "Exception: " + result);
			// BufferedReader reader = null;
			// StringBuilder sb = new StringBuilder();
			// try {
			//
			// reader = new BufferedReader(new InputStreamReader(response
			// .getBody().in()));
			//
			// String line;
			//
			// try {
			// while ((line = reader.readLine()) != null) {
			// sb.append(line);
			// }
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			//
			// String result = sb.toString();
			// CustomLog.debug("Error", "Exception: " + result);

			// return null;

			// } catch (Exception e) {
			//
			// try {
			// Thread.sleep(500);
			// } catch (InterruptedException ex) {
			// // TODO Auto-generated catch block
			// ex.printStackTrace();
			// }
			//
			// // Recuperamos la posicion del user
			// Result result = RankingServiceFactory.createRankingService()
			// .listRanking(nivel, deviceId);
			//
			// // VALIDO !!!!!
			// lstFinal = result.getRanking().getUserList();
			//
			// User user_separator =new User();
			// user_separator.setTime(0);
			// lstFinal.add(user_separator);
			//
			// //
			// lstFinal.addAll(result.getUserRanking().getAboveList());
			// //
			//
			//
			//
			// if ((result.getUserRanking().getAboveList().size()>0) ||
			// (result.getUserRanking().getBelowList().size()>0)){
			// lstFinal.add(result.getUser());
			// }
			// positionUser = result.getUser().getPosition();
			//
			// //
			// lstFinal.addAll(result.getUserRanking().getBelowList());
			//
			// return lstFinal;
			//
			// }

		}

		@Override
		protected void onPostExecute(List<User> lstFinal) {

			myProgresG.setVisibility(View.INVISIBLE);

			ptAdapter.setPositionUser(positionUser);
			ptAdapter.setData(lstFinal);

			ptAdapter.notifyDataSetChanged();

		}

	}

}
