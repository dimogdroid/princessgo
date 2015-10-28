package com.difed.bd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.difed.entidades.Puntuacion;


public class ProveedorBDImplemen implements ProveedorBD {
	
	public static final int NIVEL_FACIL = 1;
	public static final int NIVEL_MEDIO = 2;
	public static final int NIVEL_DIFICIL = 3;
	
	private BdMiaAndMeRun bdMem;
	private static ProveedorBDImplemen db;

	private ProveedorBDImplemen(Context context) {
		bdMem = new BdMiaAndMeRun(context);
		bdMem.getWritableDatabase();
	}

	public static ProveedorBD getProveedor(Context context) {
		if (db == null) {
			db = new ProveedorBDImplemen(context);
		}
		return db;
	}

	@SuppressWarnings("static-access")
	@Override
	public long Insertar(Puntuacion puntuacion, int nivel) {
		
		long id =0;
		
		SQLiteDatabase db = bdMem.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(bdMem.COL_NOMBRE, puntuacion.getNombre().trim());
		values.put(bdMem.COL_TIEMPO, puntuacion.getTiempo());
		
		if (nivel==NIVEL_FACIL){
			id = db.insert(bdMem.TABLA_PUNTUACION_FACIL, null, values);
		}
		if (nivel==NIVEL_MEDIO){
			id = db.insert(bdMem.TABLA_PUNTUACION_MEDIA, null, values);
		}
		if (nivel==NIVEL_DIFICIL){
			id = db.insert(bdMem.TABLA_PUNTUACION_DIFICIL, null, values);
		}
		
		
		return id;
	}

	@SuppressWarnings({ "unused", "static-access" })
	@Override
	public void Eliminar(Puntuacion puntuacion, int nivel) {
		
		int uno = 0;
		
		SQLiteDatabase db = bdMem.getWritableDatabase();
		if (nivel==NIVEL_FACIL){
			uno = db.delete(bdMem.TABLA_PUNTUACION_FACIL, bdMem.COL_ID + " = ?",
					new String[] { Integer.toString(puntuacion.getId()) });
		}
		if (nivel==NIVEL_MEDIO){
			uno = db.delete(bdMem.TABLA_PUNTUACION_MEDIA, bdMem.COL_ID + " = ?",
					new String[] { Integer.toString(puntuacion.getId()) });
		}
		if (nivel==NIVEL_DIFICIL){
			uno = db.delete(bdMem.TABLA_PUNTUACION_DIFICIL, bdMem.COL_ID + " = ?",
					new String[] { Integer.toString(puntuacion.getId()) });
		}
		
		
	}

	@SuppressWarnings("static-access")
	@Override
	public Puntuacion ultimoValor(int nivel) {
		
		String nombreTabla = null;
		
		if (nivel==NIVEL_FACIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_FACIL;
		}
		if (nivel==NIVEL_MEDIO){
			nombreTabla = bdMem.TABLA_PUNTUACION_MEDIA;
		}
		if (nivel==NIVEL_DIFICIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_DIFICIL;
		}
		
		
		SQLiteDatabase db = bdMem.getWritableDatabase();

		Cursor c = db.rawQuery("Select " + bdMem.COL_ID + ", "
				+ bdMem.COL_NOMBRE + "," + bdMem.COL_TIEMPO + " FROM "
				+ nombreTabla + " " + "ORDER BY TIEMPO DESC", null);

		Puntuacion pnt = new Puntuacion();
		if (c != null) {
			if (c.moveToNext()) {
				pnt.setId(c.getInt(0));
				pnt.setNombre(c.getString(1));
				pnt.setTiempo(c.getLong(2));
			}
		}

		closeResource(c);

		return pnt;
	}
	
	@Override
	public Puntuacion primerValor(int nivel) {
		
		String nombreTabla = null;
		
		if (nivel==NIVEL_FACIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_FACIL;
		}
		if (nivel==NIVEL_MEDIO){
			nombreTabla = bdMem.TABLA_PUNTUACION_MEDIA;
		}
		if (nivel==NIVEL_DIFICIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_DIFICIL;
		}
		
		
		SQLiteDatabase db = bdMem.getWritableDatabase();

		Cursor c = db.rawQuery("Select " + bdMem.COL_ID + ", "
				+ bdMem.COL_NOMBRE + "," + bdMem.COL_TIEMPO + " FROM "
				+ nombreTabla + " " + "ORDER BY TIEMPO ASC", null);

		Puntuacion pnt = new Puntuacion();
		if (c != null) {
			if (c.moveToNext()) {
				pnt.setId(c.getInt(0));
				pnt.setNombre(c.getString(1));
				pnt.setTiempo(c.getLong(2));
			}
		}

		closeResource(c);

		return pnt;
	}

	@SuppressWarnings("static-access")
	@Override
	public List<Puntuacion> lstListaCompleta(int nivel) {
		String nombreTabla = null;
		
		SQLiteDatabase db = bdMem.getWritableDatabase();
		
		if (nivel==NIVEL_FACIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_FACIL;
		}
		if (nivel==NIVEL_MEDIO){
			nombreTabla = bdMem.TABLA_PUNTUACION_MEDIA;
		}
		if (nivel==NIVEL_DIFICIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_DIFICIL;
		}

		Cursor c = db.rawQuery("Select " + bdMem.COL_ID + ", "
				+ bdMem.COL_NOMBRE + "," + bdMem.COL_TIEMPO + " FROM "
				+ nombreTabla + " " + "ORDER BY TIEMPO ASC", null);
		
		
		List<Puntuacion> lstPuntuacion = new ArrayList<Puntuacion>();
		if (c != null) {
			Puntuacion pnt;
			int contador = 1; 
			while (c.moveToNext()){
				pnt = new Puntuacion();
				pnt.setPosicion(contador);
				contador++;
				pnt.setNombre(c.getString(1));
				pnt.setTiempo(c.getLong(2));
				
				lstPuntuacion.add(pnt);
			}
		}
		
		
		closeResource(c);
		return lstPuntuacion;
	}

	private static void closeResource(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public int numJugadores(int nivel) {
		
		String nombreTabla = null;
		
		SQLiteDatabase db = bdMem.getWritableDatabase();
		
		if (nivel==NIVEL_FACIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_FACIL;
		}
		if (nivel==NIVEL_MEDIO){
			nombreTabla = bdMem.TABLA_PUNTUACION_MEDIA;
		}
		if (nivel==NIVEL_DIFICIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_DIFICIL;
		}
		
		int numJugadores = 0;

		Cursor c = db.rawQuery("Select count(*) from " + nombreTabla , null);

		if (c != null) {
			if (c.moveToNext()) {
				numJugadores = c.getInt(0);
			}
		}

		closeResource(c);

		return numJugadores;
	}

	@SuppressWarnings("static-access")
	@Override
	public void borrarBdCompleta(int nivel) {
		String nombreTabla = null;
		
		SQLiteDatabase db = bdMem.getWritableDatabase();
		
		if (nivel==NIVEL_FACIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_FACIL;
		}
		if (nivel==NIVEL_MEDIO){
			nombreTabla = bdMem.TABLA_PUNTUACION_MEDIA;
		}
		if (nivel==NIVEL_DIFICIL){
			nombreTabla = bdMem.TABLA_PUNTUACION_DIFICIL;
		}
		
		db.delete(nombreTabla, null,null);
		
	}

}
