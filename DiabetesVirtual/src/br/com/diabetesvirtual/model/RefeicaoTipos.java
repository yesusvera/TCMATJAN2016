package br.com.diabetesvirtual.model;

import android.content.Context;
import br.com.diabetesvirtual.R;

public enum RefeicaoTipos {
	CAFE(R.string.ref_cafe),
	ALMOCO(R.string.ref_almoco),
	JANTA(R.string.ref_janta),
	LANCHE(R.string.ref_lanche);
	
	private int cod;
	
	private RefeicaoTipos(int cod) {
		this.cod = cod;
	}
	
	public int getCod() {
		return cod;
	}

	public static String getNome(Context ctx, int a) {
		return ctx.getResources().getString(a);
	}
		
	public static RefeicaoTipos getTipo(int id){
		for (final RefeicaoTipos nome : RefeicaoTipos.values()) {
			if (id == nome.cod) {
				return nome;
			}
		}
		return null;
	}
}
