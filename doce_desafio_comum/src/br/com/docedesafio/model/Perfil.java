package br.com.docedesafio.model;

import java.io.Serializable;
import java.sql.Date;

public class Perfil implements Serializable  {

	private static final long serialVersionUID = 960752828909258897L;
	
	private Integer idPerfil;
	private Integer idLogin;
	private Integer peso;
	private Date dataNascimento;
	private String observacao;
	private Integer altura;
	private byte sexo;
	private Integer fatorGlicemia;
	private Integer fatorCarboidrato;
	private Byte categoria;
	public Integer getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}
	public Integer getIdLogin() {
		return idLogin;
	}
	public void setIdLogin(Integer idLogin) {
		this.idLogin = idLogin;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getObservacao() {
		if(observacao==null) return "";
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Integer getAltura() {
		return altura;
	}
	public void setAltura(Integer altura) {
		this.altura = altura;
	}
	public byte getSexo() {
		return sexo;
	}
	public void setSexo(byte sexo) {
		this.sexo = sexo;
	}
	public Integer getFatorGlicemia() {
		return fatorGlicemia;
	}
	public void setFatorGlicemia(Integer fatorGlicemia) {
		this.fatorGlicemia = fatorGlicemia;
	}
	public Integer getFatorCarboidrato() {
		return fatorCarboidrato;
	}
	public void setFatorCarboidrato(Integer fatorCarboidrato) {
		this.fatorCarboidrato = fatorCarboidrato;
	}
	public Byte getCategoria() {
		return categoria;
	}
	public void setCategoria(Byte categoria) {
		this.categoria = categoria;
	}
}
