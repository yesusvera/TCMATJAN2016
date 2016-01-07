package br.com.diabetesvirtual.model;

public class Metas {

	int id;
	int g_inicial;
	int g_final;
	int i_inicial;
	int i_final;
	int c_inicial;
	int c_final;

	public final static String COLUNA_G_INICIAL = "g_inicial";
	public final static String COLUNA_G_FINAL = "g_final";
	public final static String COLUNA_I_INICIAL = "i_inicial";
	public final static String COLUNA_I_FINAL = "i_final";
	public final static String COLUNA_C_INICIAL = "c_inicial";
	public final static String COLUNA_C_FINAL = "c_final";

	public static Metas getMetas(Metas m) {
		if (m == null) {
			return new Metas();
		} else {
			return m;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getG_inicial() {
		return g_inicial;
	}

	public void setG_inicial(int g_inicial) {
		this.g_inicial = g_inicial;
	}

	public int getG_final() {
		return g_final;
	}

	public void setG_final(int g_final) {
		this.g_final = g_final;
	}

	public int getI_inicial() {
		return i_inicial;
	}

	public void setI_inicial(int i_inicial) {
		this.i_inicial = i_inicial;
	}

	public int getI_final() {
		return i_final;
	}

	public void setI_final(int i_final) {
		this.i_final = i_final;
	}

	public int getC_inicial() {
		return c_inicial;
	}

	public void setC_inicial(int c_inicial) {
		this.c_inicial = c_inicial;
	}

	public int getC_final() {
		return c_final;
	}

	public void setC_final(int c_final) {
		this.c_final = c_final;
	}

}
