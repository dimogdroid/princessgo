package com.difed.util;

public class Util {
	
	public String transformarMilisegundos(long milisegundos) {
		int minuto = (int) (milisegundos / 60000);
		int restominuto = (int) (milisegundos % 60000);
		int segundos = restominuto / 1000;
		int restosegundos = restominuto % 1000;

		String sMinuto;
		if (minuto < 10) {
			sMinuto = "0" + String.valueOf(minuto);
		} else {
			sMinuto = String.valueOf(minuto);
		}
		String sSegundo;
		if (segundos < 10) {
			sSegundo = "0" + String.valueOf(segundos);
		} else {
			sSegundo = String.valueOf(segundos);
		}
		String sRestoSegundo;
		if (restosegundos < 10) {
			sRestoSegundo = "00" + String.valueOf(restosegundos);
		} else {
			if (restosegundos < 99) {
				sRestoSegundo = "0" + String.valueOf(restosegundos);
			} else {
				sRestoSegundo = String.valueOf(restosegundos);
			}
		}

		String sTime = sMinuto + ":" + sSegundo + ":" + sRestoSegundo;
		
		return sTime;

	}

}
