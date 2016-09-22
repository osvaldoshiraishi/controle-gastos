package com.acmeinc.elevadores.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.acmeinc.elevadores.manager.ElevadorEventListener;
import com.acmeinc.elevadores.util.ElevadorUtils;

public class Elevador implements Runnable {
	
	private long fusoHorario;
	private Integer number;
	private Integer capacidade = 8;
	private Integer tempoEntreAndares = 2000;
	private Integer tempoParadaAndar = 20000;
	private List<Viagem> viagens = Collections.synchronizedList(new ArrayList<>());
	private ElevadorEventListener elevadorEventListener = null;

	public Elevador(Integer number, long fusoHorario, ElevadorEventListener elevadorEventListener) throws IOException {
		
		this.fusoHorario = fusoHorario;
		this.number = number;
		viagens.add(new Viagem(this.capacidade));
		this.elevadorEventListener = elevadorEventListener;

	}

	/**
	 * Controlador de subida, descida e paradas do elevador
	 * 
	 */
	@Override
	public void run() {
		while (true) { // NOSONAR
			Viagem viagemAtual = this.getViagemAtual();
			if (viagemAtual != null && viagemAtual.getParadas().size() > 0 && viagemAtual.getDataSaida() != null
					&& viagemAtual.getDataSaida().getTimeInMillis() < (System.currentTimeMillis() + fusoHorario)) { // NOSONAR

				viagemAtual.setPartiu(true);
				
				printStatus();
				// subindo
				while (viagemAtual.getProximaParada() != null) {
					
					try {
						viagemAtual.setStatus(StatusElevador.SUBINDO);
						Thread.sleep(tempoEntreAndares);
						viagemAtual.setAndarAtual(viagemAtual.getAndarAtual() + 1);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (viagemAtual.getProximaParada() == null || viagemAtual.getProximaParada().equals(viagemAtual.getAndarAtual())) {
						try {
							long init = System.currentTimeMillis();
							viagemAtual.setStatus(StatusElevador.PARADO);
							Calendar dataAtual = GregorianCalendar.getInstance();
							dataAtual.setTimeInMillis((System.currentTimeMillis() + fusoHorario));
							printStatus();
							for (Usuario u : viagemAtual.getParadas().get(viagemAtual.getAndarAtual())) {
								u.setDataDesembarque(dataAtual);
								this.elevadorEventListener.publish(u);
							}							
							Thread.sleep(tempoParadaAndar - (System.currentTimeMillis()-init));
							// remove a parada atual da lista de paradas
							viagemAtual.getParadas().remove(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else{
						printStatus();
					}
					
				}
				// descendo
				int andar = viagemAtual.getAndarAtual();
				viagemAtual.setAndarAtual(viagemAtual.getAndarAtual() - 1);
				while (andar != 1) {
					viagemAtual.setStatus(StatusElevador.DESCENDO);
					printStatus();	
					try {
						Thread.sleep(tempoEntreAndares);
						andar--;
						viagemAtual.setAndarAtual(andar);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				viagemAtual.setStatus(StatusElevador.FIM);
				printStatus();
				//remove a viagem de elevador da fila de viangens.
				viagens.remove(0);

			} 
		}
	}

	public Integer getNumber() {
		return number;
	}

	/**
	 * Retorna a próxima viagem do elevador
	 * 
	 * @return
	 */
	public Viagem proximaViagem() {
		Viagem viagemAtual = this.getUltimaViagem();
		if (viagemAtual.getDataSaida() == null) {
			Calendar dataSaida = GregorianCalendar.getInstance();
			dataSaida.setTimeInMillis((System.currentTimeMillis() + fusoHorario));
			dataSaida.add(Calendar.MILLISECOND, (int) tempoParadaAndar);
			viagemAtual.setDataSaida(dataSaida);
		}

		if (!viagemAtual.isPartiu() && !viagemAtual.isCheio()) {
			return viagemAtual;
		} else {
			Viagem ultimo = this.getUltimaViagem();
			if (ultimo.isPartiu() || !ultimo.isPartiu() && ultimo.isCheio()) {
				Viagem proximo = new Viagem(this.capacidade);
				Calendar dataSaida = ElevadorUtils.estimarProximaSaida(ultimo, this.tempoEntreAndares, this.tempoParadaAndar);
				proximo.setDataSaida(dataSaida);
				viagens.add(proximo);
			}
			return this.getUltimaViagem();
		}
	}

	/**
	 * Adiciona um usuário na fila do elevador
	 * 
	 * @param usuario
	 */
	public void addUsuario(Usuario usuario) {
		getUltimaViagem().addUsuario(usuario);
	}

	public int getTempoEntreAndares() {
		return tempoEntreAndares;
	}

	public int getTempoParadaAndar() {
		return tempoParadaAndar;
	}

	public Viagem getViagemAtual() {
		if(viagens.isEmpty()){
			viagens.add(new Viagem(this.capacidade));
		}
		return viagens.get(0);
	}
	
	public Viagem getUltimaViagem() {
		return viagens.get(viagens.size()-1);
	}
	
	public List<Viagem> getViagens() {
		return viagens;
	}

	private void printStatus() {
		int qtdOut = getViagemAtual().getParadas().get(getViagemAtual().getAndarAtual()) != null ? getViagemAtual().getParadas().get(getViagemAtual().getAndarAtual()).size() :0;
		 System.out.println(String.format("Elevador %s %s Andar: %s Qtd Usr: %s Qtd Out: %s Status: %s ", this.number, new Date((System.currentTimeMillis()+fusoHorario)),
				 getViagemAtual().getAndarAtual(),getViagemAtual().getQtdUsuarios(), qtdOut ,getViagemAtual().getStatus()));
	}

}
