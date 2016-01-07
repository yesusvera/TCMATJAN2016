package br.com.docedesafio.model;

import java.io.Serializable;

public class ItemRefeicao implements Serializable {

	private static final long serialVersionUID = -4633225860379438156L;
	
	private int id;
	private int idRefeicao;
	private int idAlimento;
	private int qtde;
	private int codigo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdRefeicao() {
		return idRefeicao;
	}
	public void setIdRefeicao(int idRefeicao) {
		this.idRefeicao = idRefeicao;
	}
	public int getIdAlimento() {
		return idAlimento;
	}
	public void setIdAlimento(int idAlimento) {
		this.idAlimento = idAlimento;
	}
	public int getQtde() {
		return qtde;
	}
	public void setQtde(int qtde) {
		this.qtde = qtde;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}