package com.difed.bd;

import com.difed.entidades.Puntuacion;

import java.util.List;



public interface ProveedorBD {
	
	public long Insertar(Puntuacion puntuacion, int nivel);
	
	public void Eliminar(Puntuacion puntuacion, int nivel);
	
	public Puntuacion ultimoValor(int nivel);
	
	public Puntuacion primerValor(int nivel);
	
	public List<Puntuacion> lstListaCompleta(int nivel);
	
	public int numJugadores(int nivel);
	
	
	//TODO Hay q indica el nivel
	public void borrarBdCompleta(int nivel);
	

}
