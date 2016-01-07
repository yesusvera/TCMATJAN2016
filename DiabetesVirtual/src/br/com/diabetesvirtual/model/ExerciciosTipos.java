package br.com.diabetesvirtual.model;

import android.content.Context;
import br.com.diabetesvirtual.R;

public enum ExerciciosTipos {
	AERO(R.string.exercicio_aerobico),
	ANAERO(R.string.exercicio_anaerobico);
	
	private int cod;
	
	private ExerciciosTipos(int cod){
		this.cod = cod;
	}
	
	public int getCod() {
		return cod;
	}

	public static String getNome(Context ctx, int a) {
		return ctx.getResources().getString(a);
	}
		
//	public static ExerciciosTipos getTipo(int id){
//		for (final ExerciciosTipos nome : ExerciciosTipos.values()) {
//			if (id == nome.cod) {
//				return nome;
//			}
//		}
//		return null;
//	}	
}
