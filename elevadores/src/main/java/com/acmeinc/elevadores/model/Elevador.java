package com.acmeinc.elevadores.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.acmeinc.elevadores.util.ElevadorUtils;

public class Elevador implements Runnable {

	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private BufferedWriter writer;
	private long fusoHorario;
	private Integer number;
	private Integer capacidade = 8;
	private Integer tempoEntreAndares = 2000;
	private Integer tempoParadaAndar = 20000;
	private List<Viagem> viagens = Collections.synchronizedList(new ArrayList<>());

	public Elevador(Integer number, long fusoHorario) throws IOException {
		writer = Files.newBufferedWriter(Paths.get("c:/Temp", "resultado"+number+"csv"), StandardOpenOption.SYNC,StandardOpenOption.WRITE,StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		this.fusoHorario = fusoHorario;
		this.number = number;
		viagens.add(new Viagem());

	}
	
	

	@Override
	public void run() {
		while (true) { // NOSONAR
			Viagem viagemAtual = this.getViagemAtual();
			if (viagemAtual != null && viagemAtual.getParadas().size() > 0 && viagemAtual.getDataSaida() != null
					&& viagemAtual.getDataSaida().getTimeInMillis() < (System.currentTimeMillis() + fusoHorario)) { // NOSONAR

				viagemAtual.setPartiu(true);
				viagemAtual.setStatus(StatusElevador.SUBINDO);
				printStatus();
				// subindo
				while (viagemAtual.getProximaParada() != null) {
					
					try {
						Thread.sleep(tempoEntreAndares);
						viagemAtual.setAndarAtual(viagemAtual.getAndarAtual() + 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (viagemAtual.getProximaParada() == null || viagemAtual.getProximaParada().equals(viagemAtual.getAndarAtual())) {
						try {
							viagemAtual.setStatus(StatusElevador.PARADO);
							Calendar dataAtual = GregorianCalendar.getInstance();
							dataAtual.setTimeInMillis((System.currentTimeMillis() + fusoHorario));
							for (Usuario u : viagemAtual.getParadas().get(viagemAtual.getAndarAtual())) {
								u.setDataDesembarque(dataAtual);
							}

							printStatus();
							Thread.sleep(tempoParadaAndar);
							// remove a parada atual da lista de paradas
							viagemAtual.getParadas().remove(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
				// descendo
				int andar = viagemAtual.getAndarAtual();
				viagemAtual.setAndarAtual(viagemAtual.getAndarAtual() - 1);
				while (andar != 1) {
					viagemAtual.setStatus(StatusElevador.DESCENDO);
					//printStatus();
					try {
						Thread.sleep(tempoEntreAndares);
						andar--;
						viagemAtual.setAndarAtual(andar);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				sumarizar(viagemAtual);
				viagemAtual.setStatus(StatusElevador.FIM);
				printStatus();
				//remove a viagem de elevador da fila de viangens.
				viagens.remove(0);

			} else {
				/*try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				// printStatus();
			}
		}
	}

	private void sumarizar(Viagem viagem) {
		viagem.getParadas().forEach((k, v) -> v.forEach(c -> printSumarizacaoConsumidor(c)));
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printSumarizacaoConsumidor(Usuario usuario) {
		String linha = String.format("%s;%s;%s;%s;%s;%s;%s", usuario.getId(), usuario.getNome(),
				sdf.format(usuario.getDataChegada().getTime()), usuario.getAndarDestino(), usuario.getNumeroElevador(),
				sdf.format(usuario.getDataSaidaElevador().getTime()),
				sdf.format(usuario.getDataDesembarque().getTime()));
		try {
			writer.write(linha);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(linha);
		
		
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
			// System.out.println(String.format("Elevador %s próxima saída: %s",
			// number, dataSaida.getTime()));
		}

		if (!viagemAtual.isPartiu() && !viagemAtual.isCheio()) {
			return viagemAtual;
		} else {
			Viagem ultimo = this.getUltimaViagem();
			if (ultimo.isPartiu() || !ultimo.isPartiu() && ultimo.isCheio()) {
				Viagem proximo = new Viagem();
				Calendar dataSaida = ElevadorUtils.estimarProximaSaida(ultimo, this.tempoEntreAndares, this.tempoParadaAndar);
				proximo.setDataSaida(dataSaida);
				viagens.add(proximo);
			}
			return this.getUltimaViagem();
		}
	}

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
		if(viagens.size() == 0){
			viagens.add(new Viagem());
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
