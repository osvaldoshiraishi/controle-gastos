package com.acmeinc.elevadores.manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.acmeinc.elevadores.model.Elevador;
import com.acmeinc.elevadores.model.Usuario;
import com.acmeinc.elevadores.util.ElevadorUtils;

public class ElevadorManager implements ElevadorEventListener {

	private long fusoHorario;
	private List<Elevador> elevadores;
	private BufferedWriter writer;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private List<Long> tempoEspera = Collections.synchronizedList(new ArrayList<>());
	private List<Long> tempoElevador = Collections.synchronizedList(new ArrayList<>());
	private List<Long> tempoTotal = Collections.synchronizedList(new ArrayList<>());

	public ElevadorManager(int qtdElevadores, long fusoHorario) throws IOException {
		writer = Files.newBufferedWriter(Paths.get("c:/Temp", "resultado_elevador.csv"), Charset.forName("UTF-8"),
				StandardOpenOption.SYNC, StandardOpenOption.WRITE, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		writer.write(
				"Ordem Chegada;Nome;Data Chegada;andar;elevador;Data Saída Elevador;Data Chegada Destino;Tempo médio Espera;Tempo médio Elevador;Tempo médio Total");
		writer.newLine();
		writer.flush();
		this.fusoHorario = fusoHorario;
		this.elevadores = new ArrayList<>();
		if (qtdElevadores <= 0) {
			throw new IllegalStateException("Quantidade de Elevadores deve ser maior que zero!");
		}
		for (int i = 0; i < qtdElevadores; i++) {
			Elevador elevador = new Elevador(i + 1, fusoHorario, this);
			Thread t = new Thread(elevador);
			t.start();
			elevadores.add(elevador);

		}
	}

	public Integer addUsuario(Usuario usuario) {
		Elevador elevadorSelecionado = ElevadorUtils.selecionarElevador(usuario.getAndarDestino(), this.elevadores,
				this.fusoHorario);
		usuario.setNumeroElevador(elevadorSelecionado.getNumber());
		usuario.setDataSaidaElevador(elevadorSelecionado.proximaViagem().getDataSaida());
		elevadorSelecionado.addUsuario(usuario);
		return elevadorSelecionado.getNumber();
	}

	@Override
	public void publish(Usuario usuario) {

		synchronized (writer) {
			Long tempoEspUs = usuario.getDataSaidaElevador().getTimeInMillis()
					- usuario.getDataChegada().getTimeInMillis();
			Long tempoElvUs = usuario.getDataDesembarque().getTimeInMillis()
					- usuario.getDataSaidaElevador().getTimeInMillis();
			tempoEspera.add(tempoEspUs);
			tempoElevador.add(tempoElvUs);
			tempoTotal.add(tempoElvUs + tempoEspUs);

			String linha = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s", usuario.getId(), usuario.getNome(),
					sdf.format(usuario.getDataChegada().getTime()), usuario.getAndarDestino(),
					usuario.getNumeroElevador(), sdf.format(usuario.getDataSaidaElevador().getTime()),
					sdf.format(usuario.getDataDesembarque().getTime()), calcularTempoMedio(tempoEspera),
					calcularTempoMedio(tempoElevador), calcularTempoMedio(tempoTotal));

			try {
				writer.write(linha);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println(linha);
		}
		

	}

	/**
	 * Tempo médio em segundos
	 * 
	 * @param tempos
	 * @return
	 */
	private LocalTime calcularTempoMedio(List<Long> tempos) {
		Long sum = 0L;
		if (!tempos.isEmpty()) {
			Iterator<Long> it = tempos.iterator();
			while (it.hasNext()) {
				sum += (Long) it.next();
			}
			return LocalTime.ofSecondOfDay((sum / tempos.size()) / 1000);
		}
		return LocalTime.ofSecondOfDay(sum);
	}

}
