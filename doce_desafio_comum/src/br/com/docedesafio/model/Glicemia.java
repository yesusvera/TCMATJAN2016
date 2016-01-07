package br.com.docedesafio.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Glicemia implements Serializable {

	private static final long serialVersionUID = -4726062957543215998L;
	
	private int id;
	private int idLogin;
	private String nomeUsuario;
	private String tipo;
	private String observacao;
	private int medida;
	private Timestamp data;
	private int codigo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdLogin() {
		return idLogin;
	}
	public void setIdLogin(int idLogin) {
		this.idLogin = idLogin;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public int getMedida() {
		return medida;
	}
	public void setMedida(int medida) {
		this.medida = medida;
	}
	public Timestamp getData() {
		return data;
	}
	public void setData(Timestamp data) {
		this.data = data;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}
