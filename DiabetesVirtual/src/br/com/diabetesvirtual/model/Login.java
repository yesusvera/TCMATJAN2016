package br.com.diabetesvirtual.model;

import java.io.Serializable;

//Tabela Usuário
public class Login implements Serializable {

	private static final long serialVersionUID = 4750295249399409677L;
	
	private Integer id;
	private String nome;
	private String senha;
	private String email;
	private String nivelAcesso;
	private String login;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		if(nome==null) return "";
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		if(senha==null) return "";
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getEmail() {
		if(email==null) return "";
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNivelAcesso() {
		if(nivelAcesso==null) return "";
		return nivelAcesso;
	}
	public void setNivelAcesso(String nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}
	public String getLogin() {
		if(login==null) return "";
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
}