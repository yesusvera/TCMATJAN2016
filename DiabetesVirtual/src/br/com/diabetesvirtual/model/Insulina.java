package br.com.diabetesvirtual.model;

import java.util.Calendar;



public class Insulina {

	private int id;
//	private InsulinaTipos tipo;
	private String tipo;
	
	private double qtd;
	private Calendar data;
	private String obs;
	
	public Insulina() {
		data = Calendar.getInstance();
	}
	
	public static Insulina getInsulina(Insulina i) {
		if (i==null) {
			return new Insulina();
		}else {
			return i;
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
//	public InsulinaTipos getTipo() {
//		return tipo;
//	}
//	public void setTipo(InsulinaTipos tipo) {
//		this.tipo = tipo;
//	}
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getQtd() {
		return qtd;
	}
	public void setQtd(double qtd) {
		this.qtd = qtd;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public Calendar getData() {
		return data;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	
	
	
}
