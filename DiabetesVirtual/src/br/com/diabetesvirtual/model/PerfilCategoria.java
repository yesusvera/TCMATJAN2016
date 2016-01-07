package br.com.diabetesvirtual.model;


public enum PerfilCategoria {
	CAT_1(0.5),
	CAT_2(0.6),
	CAT_3(0.7),
	CAT_4(0.8),
	CAT_5(0.9),
	CAT_6(1);
	
	double valor;
	
	private PerfilCategoria(double x) {
		this.valor = x;
	}
	
	public double getValor() {
		return valor;
	}
	
	public static PerfilCategoria getTipo(double id){
		for (final PerfilCategoria tipo : PerfilCategoria.values()) {
			if (id == tipo.valor) {
				return tipo;
			}
		}
		return null;
	}
}
