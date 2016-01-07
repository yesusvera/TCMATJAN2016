package br.com.diabetesvirtual.model;

import java.io.Serializable;

public class SyncREST implements Serializable {

	private static final long serialVersionUID = 8868315001958272435L;
	
	private Integer id;
	private Integer tipoTabela;
	private Integer codigo;
	private Integer operacao;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTipoTabela() {
		return tipoTabela;
	}
	public void setTipoTabela(Integer tipoTabela) {
		this.tipoTabela = tipoTabela;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public Integer getOperacao() {
		return operacao;
	}
	public void setOperacao(Integer operacao) {
		this.operacao = operacao;
	}
}
