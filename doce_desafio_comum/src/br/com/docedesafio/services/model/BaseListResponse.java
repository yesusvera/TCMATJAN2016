package br.com.docedesafio.services.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListResponse<T> {

	private List<T> lista = new ArrayList<T>();

	public List<T> getLista() {
		return lista;
	}

	public void setLista(List<T> lista) {
		this.lista = lista;
	}

}
