package br.com.diabetesvirtual.model;

import java.util.Calendar;

public class Refeicao {

	int id;
	Calendar data;
	double carboidrato;
	double peso;
	String obs;
	String tipo;

	// RefeicaoTipos tipo;
	//
	// public RefeicaoTipos getTipo() {
	// return tipo;
	// }
	//
	// public void setTipo(RefeicaoTipos tipo) {
	// this.tipo = tipo;
	// }
	//
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setCarboidrato(double carboidrato) {
		this.carboidrato = carboidrato;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public Refeicao() {
		data = Calendar.getInstance();
		carboidrato = 0;
		peso = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public double getCarboidrato() {
		return carboidrato;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public static Refeicao getRefeicao(Refeicao r) {
		if (r == null) {
			return new Refeicao();
		} else {
			return r;
		}
	}

}
