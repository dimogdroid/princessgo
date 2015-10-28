package com.difed.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BdMiaAndMeRun extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "memprincessrun.db";
	private static final int DATABASE_VERSION = 3;

	public final static String TABLA_PUNTUACION_FACIL = "puntuacion_facil";
	public static final String COL_ID = "id";
	public static final String COL_NOMBRE = "nombre";
	public static final String COL_TIEMPO = "tiempo";

	public final static String TABLA_PUNTUACION_MEDIA = "puntuacion_media";
	public final static String TABLA_PUNTUACION_DIFICIL = "puntuacion_dificil";

	public BdMiaAndMeRun(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLA_PUNTUACION_FACIL
				+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_NOMBRE + " TEXT NOT NULL, " + COL_TIEMPO + " LONG);";
		db.execSQL(sql);

		sql = "CREATE TABLE IF NOT EXISTS " + TABLA_PUNTUACION_MEDIA + " ("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOMBRE
				+ " TEXT NOT NULL, " + COL_TIEMPO + " LONG);";
		db.execSQL(sql);

		sql = "CREATE TABLE IF NOT EXISTS " + TABLA_PUNTUACION_DIFICIL + " ("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOMBRE
				+ " TEXT NOT NULL, " + COL_TIEMPO + " LONG);";
		db.execSQL(sql);
		
		
		//-----caché
		
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		onCreate(db);

	}

}
