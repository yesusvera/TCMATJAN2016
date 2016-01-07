package br.com.docedesafio.model;

import java.io.Serializable;

public class Alimento implements Serializable {

	private static final long serialVersionUID = 3145956286247070579L;
	
	private int id;
	private int idLogin;
	private String nomeUsuario;
	private String nome;
	private String medida;
	private int peso;
	private int carboidratos;
	private int favoritos;
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
	public String getNome() {
		if(nome!=null){
			String [] arrTmp = nome.split(",");
			if(arrTmp.length==2){
				nome = arrTmp[1].trim() + " " +arrTmp[0].trim();
			}
		}
		
		if(nome!=null && nome.length() > 2){
			nome = nome.substring(0,1).toUpperCase() + nome.substring(1);
		}
		return nome; 
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMedida() {
		return medida;
	}
	public void setMedida(String medida) {
		this.medida = medida;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public int getCarboidratos() {
		return carboidratos;
	}
	public void setCarboidratos(int carboidratos) {
		this.carboidratos = carboidratos;
	}
	public int getFavoritos() {
		return favoritos;
	}
	public void setFavoritos(int favoritos) {
		this.favoritos = favoritos;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}
