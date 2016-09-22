package com.acmeinc.elevadores.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.acmeinc.elevadores.model.Elevador;
import com.acmeinc.elevadores.model.Usuario;
import com.acmeinc.elevadores.util.ElevadorUtils;

public class ElevadorManager {
	private long fusoHorario;
	private List<Elevador> elevadores;
	public ElevadorManager(int qtdElevadores, long fusoHorario) throws IOException {
		this.fusoHorario = fusoHorario;
		this.elevadores = new ArrayList<>();
		if(qtdElevadores <=0){
			throw new IllegalStateException("Quantidade de Elevadores deve ser maior que zero!");
		}
		for (int i = 0; i < qtdElevadores; i++) {
			Elevador elevador = new Elevador(i + 1,fusoHorario);	
			Thread t = new Thread(elevador);
		    t.start();
		    elevadores.add(elevador);
		    
		}
	}

	public Integer addUsuario(Usuario usuario){
		Elevador elevadorSelecionado = ElevadorUtils.selecionarElevador(usuario.getAndarDestino(), this.elevadores, this.fusoHorario);
		usuario.setNumeroElevador(elevadorSelecionado.getNumber());
		usuario.setDataSaidaElevador(elevadorSelecionado.proximaViagem().getDataSaida());
		elevadorSelecionado.addUsuario(usuario);
		return elevadorSelecionado.getNumber();
	}
	
	

}
