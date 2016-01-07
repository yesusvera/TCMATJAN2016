package br.com.docedesafio.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Refeicao implements Serializable {

	private static final long serialVersionUID = 8971840567471463864L;
	
	private int id;
	private int idLogin;
	private String nomeUsuario;
	private int carboidrato;
	private int peso;
	private String observacao;
	private String tipo;
	private Timestamp data;
	private int codigo;
	
	private List<ItemRefeicao> itemRefeicao;
	
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
	public int getCarboidrato() {
		return carboidrato;
	}
	public void setCarboidrato(int carboidrato) {
		this.carboidrato = carboidrato;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
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
	public List<ItemRefeicao> getItemRefeicao() {
		return itemRefeicao;
	}
	public void setItemRefeicao(List<ItemRefeicao> itemRefeicao) {
		this.itemRefeicao = itemRefeicao;
	}
}