package com.difed.util;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.difed.service.model.User;
import com.difed.princessgo.R;
//import difed.christmas.christmasrun.R;
//import difed.christmas.service.model.User;



public class PuntuacionGlobalAdapter extends ArrayAdapter<User> {

	
	private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	
	Context context;
	int layoutResourceId;
	List<User> data = null;
	
	int positionUser;
	
	

	public int getPositionUser() {
		return positionUser;
	}

	public void setPositionUser(int positionUser) {
		this.positionUser = positionUser;
	}

	public List<User> getData() {
		return data;
	}

	public void setData(List<User> data) {
		this.data = data;
	}

	public PuntuacionGlobalAdapter(Context context, int layoutResourceId, List<User> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	@Override
    public int getItemViewType(int position) {
		User user = data.get(position);
		return user.getTime() == 0 ? TYPE_SEPARATOR : TYPE_ITEM;
		
    }

	@SuppressLint("WrongViewCast")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		puntos holder = null;
		
		int type = getItemViewType(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new puntos();
			
			switch (type) {
            	case TYPE_ITEM:
					holder.txtNumero = (TextView) row.findViewById(R.id.txtNumero);
					holder.txtNombre = (TextView) row.findViewById(R.id.txtNombre);
					holder.txtTiempo = (TextView) row.findViewById(R.id.txtTiempo);
					holder.rlyLayout = (RelativeLayout) row.findViewById(R.id.rltlayout);
					Util util = new Util();
					
					User lstPuntuacion = data.get(position);
					holder.txtNumero.setText(Integer.toString(lstPuntuacion.getPosition()));
					
					if ((positionUser>0) && (positionUser==lstPuntuacion.getPosition())) {
						holder.rlyLayout.setBackgroundColor(Color.YELLOW);
					}
					
					holder.txtNombre.setText(lstPuntuacion.getUsername());
					
					holder.txtTiempo.setText(util.transformarMilisegundos(lstPuntuacion.getTime()));
					break;
            	case TYPE_SEPARATOR:
            		holder.txtNombre = (TextView) row.findViewById(R.id.txtNombre);
            		holder.rlyLayout = (RelativeLayout) row.findViewById(R.id.rltlayoutseparator);
            		holder.txtNombre.setText("------------------");
            		
            		break;
			}
			
			row.setTag(holder);
		} else {
			holder = (puntos) row.getTag();
		}
		
		
//		if (lstPuntuacion.getActivo()==1) {
//			holder.rlyLayout.setBackgroundColor(Color.YELLOW);
//		}
		

		return row;
	}
	
	
	
	@Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }
	
	@Override
	public int getCount() {
		if (data==null){
			return 0;
		}else{ 
			return data.size();
		}	
	}

	static class puntos {
		TextView txtNumero;
		TextView txtNombre;
		TextView txtTiempo;
		RelativeLayout rlyLayout;
	}
}
