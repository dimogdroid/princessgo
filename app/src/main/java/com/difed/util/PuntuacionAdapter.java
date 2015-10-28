package com.difed.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.difed.entidades.ListaPuntuacion;
import com.difed.princessgo.R;
//
//import difed.christmas.christmasrun.R;
//import difed.christmas.entidades.ListaPuntuacion;




public class PuntuacionAdapter extends ArrayAdapter<ListaPuntuacion> {

	Context context;
	int layoutResourceId;
	ListaPuntuacion data[] = null;

	public PuntuacionAdapter(Context context, int layoutResourceId, ListaPuntuacion[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		puntos holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new puntos();
			holder.txtNumero = (TextView) row.findViewById(R.id.txtNumero);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtNombre);
			holder.txtTiempo = (TextView) row.findViewById(R.id.txtTiempo);
			
			holder.rlyLayout = (RelativeLayout) row.findViewById(R.id.rltlayout);

			row.setTag(holder);
		} else {
			holder = (puntos) row.getTag();
		}

		ListaPuntuacion lstPuntuacion = data[position];
		holder.txtNumero.setText(Integer.toString(lstPuntuacion.getNumero()));
		holder.txtTitle.setText(lstPuntuacion.getNombre());
		holder.txtTiempo.setText(lstPuntuacion.getTiempo());
		
		if (lstPuntuacion.getActivo()==1) {
			holder.rlyLayout.setBackgroundColor(Color.YELLOW);
		}
		

		return row;
	}

	static class puntos {
		TextView txtNumero;
		TextView txtTitle;
		TextView txtTiempo;
		RelativeLayout rlyLayout;
	}
}
