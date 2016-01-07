package br.com.diabetesvirtual.model;

import java.util.Calendar;

import android.content.res.Resources;
import br.com.diabetesvirtual.R;

public class Exercicios {

	int id;
	String descricao;
	String intensidade;
	String modalidade;
	String tipo;
	int duracao;
	Calendar data;
	private Resources res;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public Exercicios() {
		data = Calendar.getInstance();
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	// public ExerciciosIntensidade getIntensidade() {
	// return intensidade;
	// }
	// public void setIntensidade(ExerciciosIntensidade intensidade) {
	// this.intensidade = intensidade;
	// }

	public void setIntensidade(String intensidade) {
		this.intensidade = intensidade;
	}

	public String getIntensidade() {
		return intensidade;
	}

	// public ExerciciosTipos getTipo() {
	// return tipo;
	// }
	// public void setTipo(ExerciciosTipos tipo) {
	// this.tipo = tipo;
	// }
	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public static Exercicios getExercicios(Exercicios x) {
		if (x == null) {
			return new Exercicios();
		} else {
			return x;
		}
	}

	public int getOrdem() {
		int x = 0;

		res = null;

		String[] intensidade2 = res.getStringArray(R.array.intensidade_exercicio);

		if (intensidade2.equals("Muito Leve")) {
			x = 1;
		} else if (intensidade2.equals("Leve")) {
			x = 2;
		} else if (intensidade2.equals("Moderado")) {
			x = 3;
		} else if (intensidade2.equals("Intenso")) {
			x = 4;
		} else {
			x = 5;
		}

		// switch (getCod()) {
		// case R.string.exercicio_muito_leve:
		// x= 1;
		// break;
		// case R.string.exercicio_leve:
		// x= 2;
		// break;
		// case R.string.exercicio_moderado:
		// x= 3;
		// break;
		// case R.string.exercicio_intenso:
		// x= 4;
		// break;
		// case R.string.exercicio_muito_intenso:
		// x= 5;
		// break;
		// }
		return x;
	}

}
