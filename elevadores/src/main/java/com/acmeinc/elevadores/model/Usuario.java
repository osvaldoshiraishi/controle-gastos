package com.acmeinc.elevadores.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Usuario {
	
	private Calendar dataChegada;
	private Integer andarDestino;
	
	public Usuario(int andar) {
		dataChegada = GregorianCalendar.getInstance();
		andarDestino = andar;
	}
	
	public Calendar getDataChegada() {
		return dataChegada;
	}
	public void setDataChegada(Calendar dataChegada) {
		this.dataChegada = dataChegada;
	}
	public Integer getAndarDestino() {
		return andarDestino;
	}
	public void setAndarDestino(Integer andarDestino) {
		this.andarDestino = andarDestino;
	}	
	
	
}
