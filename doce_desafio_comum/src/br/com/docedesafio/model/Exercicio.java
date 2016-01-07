package br.com.docedesafio.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Exercicio implements Serializable{

	private static final long serialVersionUID = 8975076107972082528L;
	
	private int id;
	private int idLogin;
	private String nomeUsuario;
	private String descricao;
	private String tipo;
	private String modalidade;
	private String intensidade;
	private int duracao;
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getModalidade() {
		return modalidade;
	}
	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}
	public String getIntensidade() {
		return intensidade;
	}
	public void setIntensidade(String intensidade) {
		this.intensidade = intensidade;
	}
	public int getDuracao() {
		return duracao;
	}
	public void setDuracao(int duracao) {
		this.duracao = duracao;
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
