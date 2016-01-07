package br.com.diabetesvirtual.util;

import java.util.ArrayList;
import java.util.List;

public class OrdenaListaASCtoDESC<T> {

	private List<T> lista;
	
	public OrdenaListaASCtoDESC(List<T> lista) {
		this.lista = lista;
	}
	
	public List<T> ordenar() {
		final List<T> listaOrdenada = new ArrayList<T>();
		if (lista != null || lista.size()>0) {
			for (int i  = lista.size()-1;  i >= 0; i--) {
				listaOrdenada.add(lista.get(i));
			}
		}		
		return listaOrdenada;
	}
	
}
