package br.com.diabetesvirtual.model;

import java.util.Calendar;

public class Perfil {
	int id;
	String nome;
	double peso;
	double altura;
	double fatorGlicemia;
	double fatorCarboidrato;
	PerfilCategoria categoria;
	Calendar data_nasc;
	String obs;
	Sexo sexo;
	String email;
	String senha;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public PerfilCategoria getCategoria() {
		return categoria;
	}

	public void setCategoria(PerfilCategoria categoria) {
		this.categoria = categoria;
	}

	public double getFatorGlicemia() {
		return fatorGlicemia;
	}

	public void setFatorGlicemia(double fatorGlicemia) {
		this.fatorGlicemia = fatorGlicemia;
	}

	public double getFatorCarboidrato() {
		return fatorCarboidrato;
	}

	public void setFatorCarboidrato(double fatorCarboidrato) {
		this.fatorCarboidrato = fatorCarboidrato;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public Calendar getData_nasc() {
		return data_nasc;
	}

	public void setData_nasc(Calendar data_nasc) {
		this.data_nasc = data_nasc;
	}

	public static int getIdade(Calendar nasc) {
		Calendar c = Calendar.getInstance();
		int idade;
		idade = c.get(Calendar.YEAR) - nasc.get(Calendar.YEAR);
		if (nasc.get(Calendar.DAY_OF_YEAR) > c.get(Calendar.DAY_OF_YEAR)) {
			idade = idade - 1;
		}
		return idade;
	}

	public static Perfil getPerfil(Perfil p) {
		if (p == null) {
			return new Perfil();
		} else {
			return p;
		}
	}

}
