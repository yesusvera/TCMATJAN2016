package br.com.diabetesvirtual.model;

import java.util.Calendar;

public class Glicemia {

	int id;
	private String tipo;
	int medida;
	String obs;
	Calendar data;

	public Glicemia() {
		data = Calendar.getInstance();
	}

	public static Glicemia getGlicemia(Glicemia g) {
		if (g == null) {
			return new Glicemia();
		} else {
			return g;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	// public GlicemiaTipos getTipo() {
	// return tipo;
	// }
	// public void setTipo(GlicemiaTipos tipo) {
	// this.tipo = tipo;
	// }
	public int getMedida() {
		return medida;
	}

	public void setMedida(int medida) {
		this.medida = medida;
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
