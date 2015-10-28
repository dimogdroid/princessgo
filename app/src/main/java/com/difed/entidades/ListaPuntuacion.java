package com.difed.entidades;

public class ListaPuntuacion {

	private int numero;
	private String nombre;
	private String tiempo;
	
	private int activo;
	

	public ListaPuntuacion() {
		super();
	}

	public ListaPuntuacion(int numero, String nombre, String tiempo) {
		super();
		this.numero = numero;
		this.nombre = nombre;
		this.tiempo = tiempo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}
	
	
	
	

}
