package br.com.docedesafio.model;

import java.io.Serializable;
import java.util.List;

//Tabela Usuario
public class Usuario implements Serializable {

	private static final long serialVersionUID = 4750295249399409677L;
	
	private Integer id;
	private String nome;
	private String senha;
	private String email;
	private String nivelAcesso;
	private String login;
	
	private Perfil perfil = new Perfil();
	
	private List<Alimento> listaAlimentos;
	
	private List<Exercicio> listaExercicios;
	
	private List<Glicemia> listaGlicemia;
	
	private List<Insulina> listaInsulina;
	
	private List<Refeicao> listaRefeicoes;
	
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
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public List<Alimento> getListaAlimentos() {
		return listaAlimentos;
	}
	public void setListaAlimentos(List<Alimento> listaAlimentos) {
		this.listaAlimentos = listaAlimentos;
	}
	public List<Exercicio> getListaExercicios() {
		return listaExercicios;
	}
	public void setListaExercicios(List<Exercicio> listaExercicios) {
		this.listaExercicios = listaExercicios;
	}
	public List<Glicemia> getListaGlicemia() {
		return listaGlicemia;
	}
	public void setListaGlicemia(List<Glicemia> listaGlicemia) {
		this.listaGlicemia = listaGlicemia;
	}
	public List<Insulina> getListaInsulina() {
		return listaInsulina;
	}
	public void setListaInsulina(List<Insulina> listaInsulina) {
		this.listaInsulina = listaInsulina;
	}
	public List<Refeicao> getListaRefeicoes() {
		return listaRefeicoes;
	}
	public void setListaRefeicoes(List<Refeicao> listaRefeicoes) {
		this.listaRefeicoes = listaRefeicoes;
	}
}