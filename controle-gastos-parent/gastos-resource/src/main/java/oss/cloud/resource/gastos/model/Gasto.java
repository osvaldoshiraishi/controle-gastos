package oss.cloud.resource.gastos.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Gasto {
	
	public Long id;
	public String lugar;
	public Timestamp data;
	public TipoConta tipo;
	public TipoDespesa tipoDespesa;
	public BigDecimal valor;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public Timestamp getData() {
		return data;
	}
	public void setData(Timestamp data) {
		this.data = data;
	}
	public TipoConta getTipo() {
		return tipo;
	}
	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
	}
	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}
	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
