package br.com.diabetesvirtual.model;



public enum GlicemiaTipos {
	NENHUM(0, "Nenhum"),
	JEJUM(1, "Em jejum"),
	PRE_PRANDIAL(2, "Pré prandial"),
	POS_PRANDIAL(3,"Pós prandial");
	
	private int cod;
	private String nome;
	
	private GlicemiaTipos(int a, String b) {
		this.cod = a;
		this.nome = b;
	}
	
	public static GlicemiaTipos forGlicemia(int cod2){
		for (final GlicemiaTipos nome : GlicemiaTipos.values()) {
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


