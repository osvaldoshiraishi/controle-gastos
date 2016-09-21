package com.acmeinc.elevadores.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.acmeinc.elevadores.util.ElevadorUtils;

public class Elevador implements Runnable{
	
	private int number;
	private int tempoEntreAndares = 500; //2000;
	private int tempoParadaAndar = 3000; //20000;
	private ConcurrentLinkedQueue<Viagem> viagens = new ConcurrentLinkedQueue<>();
	private Viagem viagemAtual = null;
	private Viagem viagemProxima = null;
	
	public Elevador(int number) {
		this.number = number;
		viagemAtual = new Viagem();
		
	}

	
	@Override
	public void run() {
		while(true){ //NOSONAR
			if(viagemAtual != null 
					&& viagemAtual.getParadas().size() > 0  
					&& viagemAtual.getDataSaida().getTimeInMillis() < System.currentTimeMillis()){ //NOSONAR
				
				viagemAtual.setPartiu(true);
				//subindo
				while(viagemAtual.getProximaParada() != null){
					viagemAtual.setStatus(StatusElevador.SUBINDO);
					printStatus();
					try {
						Thread.sleep(tempoEntreAndares);						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(viagemAtual.getProximaParada().equals(viagemAtual.getAndarAtual())){
						try {
							viagemAtual.setStatus(StatusElevador.PARADO);
							printStatus();
							Thread.sleep(tempoParadaAndar);
							//remove a parada atual da lista de paradas
							viagemAtual.getParadas().remove(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					viagemAtual.setAndarAtual(viagemAtual.getAndarAtual() + 1);
				}
				//descendo
				int andar = viagemAtual.getAndarAtual() -1;
				viagemAtual.setAndarAtual(viagemAtual.getAndarAtual() - 1);
				while(andar != 1){
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
				
				viagemAtual = viagemProxima;
				viagemProxima = null;
				
			}else{
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//printStatus();
			}			
		}		
	}
	
	public int getNumber() {
		return number;
	}
	
	public void addUsuario(Usuario usuario){
		
		if(viagemAtual.getDataSaida() == null){
			Calendar dataSaida = GregorianCalendar.getInstance();
			dataSaida.add(Calendar.MILLISECOND, (int)tempoParadaAndar);
			viagemAtual.setDataSaida(dataSaida);
			System.out.println(String.format("Elevador %s próxima saída: %s", number, dataSaida.getTime()));
		}
		
		if(!viagemAtual.isPartiu()){
			if(viagemAtual.getParadas().get(usuario.getAndarDestino()) == null){
				viagemAtual.getParadas().put(usuario.getAndarDestino(), new ArrayList<>());
			}
			
			viagemAtual.getParadas().get(usuario.getAndarDestino()).add(usuario);			
			
			
		}else{
			if(viagemProxima == null){
				viagemProxima = new Viagem();
			}
			
			if(viagemProxima.getParadas().get(usuario.getAndarDestino()) == null){
				viagemProxima.getParadas().put(usuario.getAndarDestino(), new ArrayList<>());
			}
			
			viagemProxima.getParadas().get(usuario.getAndarDestino()).add(usuario);
			if(viagemProxima.getDataSaida() == null){
				viagemProxima.setDataSaida(ElevadorUtils.estimarProximaSaida(this));
			}
			
		}
		
	}
	
	public int getTempoEntreAndares() {
		return tempoEntreAndares;
	}
	
	public int getTempoParadaAndar() {
		return tempoParadaAndar;
	}
	
	public Viagem getViagemAtual() {
		return viagemAtual;
	}
	
	private void printStatus(){
		System.out.println(String.format("Elevador %s %s Andar: %s Status: %s \n", this.number, new Date(), viagemAtual.getAndarAtual(), viagemAtual.getStatus()));
	}

}
