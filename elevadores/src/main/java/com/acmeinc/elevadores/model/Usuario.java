package com.acmeinc.elevadores.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Usuario {
	
	private String id;
	private String nome;
	private Calendar dataChegada;
	private Integer andarDestino;
	private Integer numeroElevador;
	private Calendar dataSaidaElevador;
	private Calendar dataDesembarque;
	
	public Usuario(int andar) {
		dataChegada = GregorianCalendar.getInstance();
		andarDestino = andar;
	}
	
	public Usuario(String id, String nome, Calendar dataChegada, int andar) {
		this.id = id;
		this.nome = nome;
		this.dataChegada = dataChegada;
		this.andarDestino = andar;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getNumeroElevador() {
		return numeroElevador;
	}

	public void setNumeroElevador(Integer numeroElevador) {
		this.numeroElevador = numeroElevador;
	}

	public Calendar getDataSaidaElevador() {
		return dataSaidaElevador;
	}

	public void setDataSaidaElevador(Calendar dataSaidaElevador) {
		this.dataSaidaElevador = dataSaidaElevador;
	}

	public Calendar getDataDesembarque() {
		return dataDesembarque;
	}

	public void setDataDesembarque(Calendar dataDesembarque) {
		this.dataDesembarque = dataDesembarque;
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
