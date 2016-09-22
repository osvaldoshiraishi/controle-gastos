package com.acmeinc.elevador.manager.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.acmeinc.elevadores.manager.ElevadorManager;
import com.acmeinc.elevadores.model.Usuario;

public class ElevadorManagerTest {
	
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	
	
	public static void main(String[] args) throws URISyntaxException, IOException, ParseException, InterruptedException {	
		List<Usuario> usuarios = loadUsuarios();
		long fusoHorario = sdf.parse("31/08/2016 10:00:00").getTime() - System.currentTimeMillis();
		ElevadorManager elevadorManager = new ElevadorManager(4, fusoHorario);
		
		for (Usuario usuario : usuarios) {
			Calendar dataAtual = GregorianCalendar.getInstance();
			dataAtual.setTimeInMillis((System.currentTimeMillis()+fusoHorario));
			if(usuario.getDataChegada().compareTo(dataAtual) > 0){
				Thread.sleep(usuario.getDataChegada().getTimeInMillis() - dataAtual.getTimeInMillis());
			}
			elevadorManager.addUsuario(usuario);
		}
	}
	
	private static List<Usuario> loadUsuarios() throws URISyntaxException, IOException, ParseException {
		List<String> linhas = Files.readAllLines(Paths.get(ElevadorManagerTest.class.getResource("/elevadores.csv").toURI()));
		List<Usuario> usuarios = new ArrayList<>();
		
		for (String linha : linhas) {
			String[] tokens = linha.split(";");
			String id = tokens[0];
			String nome = tokens[1];
			Calendar dataChegada = Calendar.getInstance();
			Date aux = sdf.parse(tokens[2]);
			dataChegada.setTime(aux);
			Integer andarDestino = Integer.parseInt(tokens[3]);
			usuarios.add(new Usuario(id, nome, dataChegada, andarDestino));
			
		}
		return usuarios;
		
	}
	
}
