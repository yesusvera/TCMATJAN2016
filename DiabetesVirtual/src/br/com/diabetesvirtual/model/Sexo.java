package br.com.diabetesvirtual.model;

public enum Sexo {

	MASCULINO(0, "M"),
	FEMININO(1, "F");
	
	private int cod;
	private String nome;
	
	private Sexo(int a, String b) {
		this.cod = a;
		this.nome = b;
	}
	
	public static Sexo getSexo(int cod2){
		for (final Sexo nome : Sexo.values()) {
			if (nome.cod == cod2) {
				return nome;
			}
		}
		return null;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	


}
