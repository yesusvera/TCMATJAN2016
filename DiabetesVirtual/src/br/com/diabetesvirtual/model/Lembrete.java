package br.com.diabetesvirtual.model;

import java.util.Calendar;

public class Lembrete {

	int id;
	double tipo;
	Calendar hora;
	int ativo;
	String descricao;
	
	
	public Lembrete() {
		hora = Calendar.getInstance();
	}
	
	public static Lembrete getLembrete(Lembrete l) {
		if (l==null) {
			return new Lembrete();
		} else {
			return l;
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getTipo() {
		return tipo;
	}
	public void setTipo(double tipo) {
		this.tipo = tipo;
	}

	public Calendar getHora() {
		return hora;
	}
	
	public void setHora(Calendar hora) {
		this.hora = hora;
	}	
	
	public int getAtivo() {
		return ativo;
	}
	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
}
