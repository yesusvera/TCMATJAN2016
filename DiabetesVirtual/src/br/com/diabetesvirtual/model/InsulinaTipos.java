package br.com.diabetesvirtual.model;



public enum InsulinaTipos {
	LENTA(0, "Basal"),
	RAPIDA(1,"Ultra-rápida");
	
	private int cod;
	private String nome;
	
	private InsulinaTipos(int a, String b) {
		this.cod = a;
		this.nome = b;
	}
	
	public static InsulinaTipos forInsulina(int cod2){
		for (final InsulinaTipos nome : InsulinaTipos.values()) {
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


