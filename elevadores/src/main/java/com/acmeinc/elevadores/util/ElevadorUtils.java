package com.acmeinc.elevadores.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.acmeinc.elevadores.model.Elevador;
import com.acmeinc.elevadores.model.Viagem;

public class ElevadorUtils {
	
	/**
	 * Calculo do próximo horário de embarque do elevador
	 * 
	 * @param elevador
	 * @return data do próximo embarque
	 */
	public static Calendar estimarProximaSaida(Viagem viagem,Integer tempoEntreAndares, Integer tempoParadaAndar){
		
		Calendar dataSaida = viagem.getDataSaida();
		if(viagem.getParadas().size() == 0){
			return dataSaida;
		}
		Integer ultimoAndar = viagem.getUltimaParada();
		Integer tempoParadas = viagem.getParadas().size() * tempoParadaAndar;
		Integer tempoSubida = ((ultimoAndar-1) * tempoEntreAndares);
		Integer tempoDescida = tempoSubida; 
		Calendar proximoHorarioEmbarque = (Calendar) dataSaida.clone();
		proximoHorarioEmbarque.add(Calendar.MILLISECOND, (tempoSubida + tempoParadas + tempoDescida + tempoParadaAndar));
		
		//System.out.println(String.format("Elevador %s Tempo Paradas: %s", elevador.getNumber(), tempoParadas));
		//System.out.println(String.format("Elevador %s Tempo Subida: %s", elevador.getNumber(), tempoSubida));
		//System.out.println(String.format("Elevador %s Tempo Descida: %s", elevador.getNumber(), tempoDescida));
		//System.out.println(String.format("Elevador %s próxima saída: %s", elevador.getNumber(), proximoHorarioEmbarque.getTime()));
		return proximoHorarioEmbarque;
	}

	/**
	 * Seleciona o elevador que irá chegar ao destino antes.
	 * @param andar Andar destino
	 * @param elevadores lista de elevadores selecionados
	 * 
	 * @return Elevador que chegará ao andar antes
	 */
	public static Elevador selecionarElevador(Integer andar, List<Elevador> elevadores, long fusuHorario) {
		Calendar melhorHorarioProximaSaida = null;
		Elevador elevadorSelecionado = null;
		Calendar data = GregorianCalendar.getInstance();
		
		for (Elevador elevador : elevadores) {

			if(elevador.proximaViagem().getParadas().containsKey(andar)){
				return elevador;
			}
			
			Calendar horarioSaida = estimarProximaSaida(elevador.getUltimaViagem(),elevador.getTempoEntreAndares(), elevador.getTempoParadaAndar());
			if(horarioSaida == null){
				data.setTimeInMillis((System.currentTimeMillis() + fusuHorario +elevadorSelecionado.getTempoParadaAndar()));
				horarioSaida = data;
			}
			
			if(melhorHorarioProximaSaida == null){
				melhorHorarioProximaSaida = horarioSaida;
				elevadorSelecionado = elevador;
			}else if(melhorHorarioProximaSaida.after(horarioSaida)){
				melhorHorarioProximaSaida = horarioSaida;
				elevadorSelecionado = elevador;
			}
		}
		
		if(elevadorSelecionado.proximaViagem().getDataSaida() == null){
			data.setTimeInMillis((System.currentTimeMillis() + fusuHorario +elevadorSelecionado.getTempoParadaAndar()));
			elevadorSelecionado.proximaViagem().setDataSaida(data);
		}
		return elevadorSelecionado;
	}
	

}
