package br.com.diabetesvirtual.model;

import android.content.Context;
import br.com.diabetesvirtual.R;

public enum ExerciciosIntensidade {
	//MUITO_LEVE(R.array.intensidade_exercicio),
	
	
	MUITO_LEVE(R.string.exercicio_muito_leve),
	LEVE(R.string.exercicio_leve),
	MODERADO(R.string.exercicio_moderado),
	INTENSO(R.string.exercicio_intenso),
	MUITO_INTENSO(R.string.exercicio_muito_intenso);
	
	private int cod;
	
	private ExerciciosIntensidade(int cod){
		this.cod = cod;
	}
	
	public int getCod() {
		return cod;
	}

	public int getOrdem() {
		int x=0;
		switch (getCod()) {
		case R.string.exercicio_muito_leve:
			x= 1;
			break;
		case R.string.exercicio_leve:
			x= 2;
			break;
		case R.string.exercicio_moderado:
			x= 3;
			break;
		case R.string.exercicio_intenso:
			x= 4;
			break;
		case R.string.exercicio_muito_intenso:
			x= 5;
			break;
		}
		return x;
	}
	
	public static String getNome(Context ctx, int a) {
		return ctx.getResources().getString(a);
	}
		
	public static ExerciciosIntensidade getIntensidade(int id){
		for (final ExerciciosIntensidade nome : ExerciciosIntensidade.values()) {
			if (id == nome.cod) {
				return nome;
			}
		}
		return null;
	}	
}
