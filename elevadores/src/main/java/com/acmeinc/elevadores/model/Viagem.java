package com.acmeinc.elevadores.model;

import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

public class Viagem {
		
	private int capacidade = 8;
	private TreeMap<Integer,List<Usuario>> paradas;
	private Integer andarAtual;
	private Calendar dataSaida;
	private StatusElevador status;
	private boolean partiu;
	
	public Viagem() {
		paradas = new TreeMap<>();
		andarAtual = 1;
		status = StatusElevador.PARADO;
		partiu = false;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}
	
	public TreeMap<Integer, List<Usuario>> getParadas() {
		return paradas;
	}
	
	public void setParadas(TreeMap<Integer, List<Usuario>> paradas) {
		this.paradas = paradas;
	}

	public Integer getProximaParada(){
		Integer proxima =  paradas.ceilingKey(this.andarAtual);
		
		if(this.andarAtual > paradas.lastKey()){
			return null;
		}
		return proxima;
	}
	
	public Integer getUltimaParada(){
		return paradas.size() > 0 ? paradas.lastKey() : null;
	}
	
	public int getAndarAtual() {
		return andarAtual;
	}

	public void setAndarAtual(Integer andarAtual) {
		this.andarAtual = andarAtual;
	}

	public Calendar getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Calendar dataSaida) {
		this.dataSaida = dataSaida;
	}

	public StatusElevador getStatus() {
		return status;
	}
	
	public void setStatus(StatusElevador status) {
		this.status = status;
	}	
	
	public boolean isPartiu() {
		return partiu;
	}
	
	public void setPartiu(boolean partiu) {
		this.partiu = partiu;
	}
}
