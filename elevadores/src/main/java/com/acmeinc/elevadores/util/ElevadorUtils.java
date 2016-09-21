package com.acmeinc.elevadores.util;

import java.util.Calendar;

import com.acmeinc.elevadores.model.Elevador;
import com.acmeinc.elevadores.model.Viagem;

public class ElevadorUtils {
	
	
	/**
	 * Calculo do próximo horário de embarque do elevador
	 * 
	 * @param elevador
	 * @return data do próximo embarque
	 */
	public static Calendar estimarProximaSaida(Elevador elevador){
		
		Viagem viagem = elevador.getViagemAtual();
		Calendar dataSaida = viagem.getDataSaida();
		Integer ultimoAndar = viagem.getUltimaParada();
		Integer tempoParadas = viagem.getParadas().size() * elevador.getTempoParadaAndar();
		Integer tempoSubida = (ultimoAndar * elevador.getTempoEntreAndares());
		Integer tempoDescida = tempoSubida; 
		Calendar proximoHorarioEmbarque = (Calendar) dataSaida.clone();
		proximoHorarioEmbarque.add(Calendar.MILLISECOND, (tempoSubida + tempoParadas + tempoDescida + elevador.getTempoParadaAndar()));
		
		System.out.println(String.format("Elevador %s Tempo Paradas: %s", elevador.getNumber(), tempoParadas));
		System.out.println(String.format("Elevador %s Tempo Subida: %s", elevador.getNumber(), tempoSubida));
		System.out.println(String.format("Elevador %s Tempo Descida: %s", elevador.getNumber(), tempoDescida));
		System.out.println(String.format("Elevador %s próxima saída: %s", elevador.getNumber(), proximoHorarioEmbarque.getTime()));
		return proximoHorarioEmbarque;
	}

}
