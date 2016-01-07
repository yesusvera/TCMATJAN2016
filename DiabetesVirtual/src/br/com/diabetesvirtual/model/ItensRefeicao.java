package br.com.diabetesvirtual.model;

public class ItensRefeicao {

	int id;
	int id_refeicao;
	Refeicao refeicao; //apenas para faciliar na hora de afzr o relatotio de ref detalhado
	Alimento alimento;
	double qtd;
	
	public Refeicao getRefeicao() {
		return refeicao;
	}
	
	public void setRefeicao(Refeicao refeicao) {
		this.refeicao = refeicao;
	}
	
	public ItensRefeicao() {
		alimento = new Alimento();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_refeicao() {
		return id_refeicao;
	}
	public void setId_refeicao(int id_refeicao) {
		this.id_refeicao = id_refeicao;
	}
	public Alimento getAlimento() {
		return alimento;
	}
	public void setAlimento(Alimento alimento) {
		this.alimento = alimento;
	}
	public double getQtd() {
		return qtd;
	}
	public void setQtd(Double qtd) {
		this.qtd = qtd;
	}
	
	
	
	
}
