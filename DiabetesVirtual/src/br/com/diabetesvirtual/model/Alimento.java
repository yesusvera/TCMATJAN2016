package br.com.diabetesvirtual.model;

public class Alimento{

	private int id_alimentos;
	private int peso;
	private int carboidrato;
	private int favorito;
	private String medida;
	private String descricao;
	
	
	
	public static Alimento getAlimento(Alimento x) {
		if (x==null) {
			return new Alimento();
		}else {
			return x;
		}
	}
	
	public int getFavorito() {
		return favorito;
	}
	
	public void setFavorito(int favorito) {
		this.favorito = favorito;
	}
	
	public int getId_alimentos() {
		return id_alimentos;
	}
	public void setId_alimentos(int id_alimentos) {
		this.id_alimentos = id_alimentos;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public int getCarboidrato() {
		return carboidrato;
	}
	public void setCarboidrato(int carboidrato) {
		this.carboidrato = carboidrato;
	}
	public String getMedida() {
		return medida;
	}
	public void setMedida(String medida) {
		this.medida = medida;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
}
