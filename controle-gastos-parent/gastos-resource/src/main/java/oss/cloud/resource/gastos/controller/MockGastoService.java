package oss.cloud.resource.gastos.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import oss.cloud.resource.gastos.model.Gasto;
import oss.cloud.resource.gastos.model.TipoConta;
import oss.cloud.resource.gastos.model.TipoDespesa;

@Component
public class MockGastoService {
	
	private static List<Gasto> gastos = new ArrayList<>();
	private static long idSeq = 1;
	static {
		Gasto g = new Gasto();
		g.setId(idSeq++);
		g.setLugar("Halin");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.ALMOCO);
		g.setValor(BigDecimal.valueOf(15.5D));

		gastos.add(g);

		g = new Gasto();
		g.setId(idSeq++);
		g.setLugar("Prime auto posto");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.CARRO);
		g.setValor(BigDecimal.valueOf(130.35D));

		gastos.add(g);
	}
	
	public List<Gasto> mockfindAllGastos() {
		return gastos;
	}
	
	public void mockCreateGasto(Gasto gasto) {
		
		System.out.println("######################### Inclindo gasto idSeq: " + idSeq);
		gasto.setId(idSeq++);
		gastos.add(gasto);
	}

	public Gasto detailGasto(Long id) {
		for (Gasto gasto : gastos) {
			if(gasto.getId().equals(id))
				return gasto;
		}
		
		return null;
	}

	public void removeGasto(Gasto toRemove) {
		for (Gasto gasto : gastos) {
			if(gasto.getId().equals(toRemove.getId())){
				gastos.remove(gasto);
				return;
			}
		}
	}


}
