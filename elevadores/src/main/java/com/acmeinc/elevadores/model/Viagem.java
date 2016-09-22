package com.acmeinc.elevadores.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

public class Viagem {
		
	private int capacidade;
	private TreeMap<Integer,List<Usuario>> paradas;
	private Integer andarAtual;
	private Calendar dataSaida;
	private StatusElevador status;
	private boolean partiu;
	private int qtdUsuarios;
	
	/**
	 * Classe que representa um viagem de elevador 
	 * 
	 * @param capacidade capacidade do elevador
	 */
	public Viagem(int capacidade) {
		this.capacidade = capacidade;
		paradas = new TreeMap<>();
		andarAtual = 1;
		qtdUsuarios =0;
		status = StatusElevador.IDLE;
		partiu = false;
	}
	
	/**
	 * Aciona um usuário a essa viagem de elevador
	 * 
	 * @param usuario
	 */
	public void addUsuario(Usuario usuario) {

		if (this.getParadas().get(usuario.getAndarDestino()) == null) {
			this.getParadas().put(usuario.getAndarDestino(), new ArrayList<>());
		}

		this.getParadas().get(usuario.getAndarDestino()).add(usuario);
		qtdUsuarios++;
	}
	
	/**
	 * Validação para verificar se o elevador está cheio
	 * 
	 * @return true caso o elevador já estiver cheio
	 */
	public boolean isCheio(){
		return qtdUsuarios >= capacidade;
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

	/**
	 * Próxima andar que o elevador irá parar
	 * 
	 * @return o andar ou null caso ja esteja no último
	 */
	public Integer getProximaParada(){
		Integer proxima =  paradas.ceilingKey(this.andarAtual);
		
		if(this.andarAtual == paradas.lastKey()){
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

	/**
	 * Status que se encontra essa viagem de elevador
	 * 
	 * @return StatusElevador
	 */
	public StatusElevador getStatus() {
		return status;
	}
	
	public void setStatus(StatusElevador status) {
		this.status = status;
	}	
	
	/**
	 * flag para indicar que o elevador dessa viagem está em transito
	 * 
	 * @return true caso o elevador dessa viagem já estiver em transito
	 */
	public boolean isPartiu() {
		return partiu;
	}
	
	public void setPartiu(boolean partiu) {
		this.partiu = partiu;
	}
	
	public int getQtdUsuarios() {
		return qtdUsuarios;
	}
}
