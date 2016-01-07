package br.com.docedesafio.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Insulina implements Serializable {
	
	private static final long serialVersionUID = -7126941946493390608L;
	
	private int id;
	private int idLogin;
	private String nomeUsuario;
	private String observacao;
	private String tipo;
	private int quantidade;
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
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
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
