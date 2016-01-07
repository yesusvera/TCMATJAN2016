package br.com.diabetesvirtual.model;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import br.com.diabetesvirtual.R;

public class Detalhes {

	TextView tipo;
	TextView dados;
	TextView obs;
	TextView data;
	TextView hora;
	Context context;
	Button voltar;
	Button editar;
	Button excluir;
	
	public Detalhes(Context ctx) {
		context = ctx;
	}

	public Dialog carregar() {
		Dialog dialog = new Dialog(context, R.style.tema_dialogo);
		dialog.setContentView(R.layout.dialog_detalhes);
		dialog.setTitle("Detalhes");
		tipo = (TextView) dialog.findViewById(R.id.detalhes_tipo);
		dados = (TextView) dialog.findViewById(R.id.detalhes_dados);
		obs = (TextView) dialog.findViewById(R.id.detalhes_obs);
		data = (TextView) dialog.findViewById(R.id.detalhes_data);
		hora = (TextView) dialog.findViewById(R.id.detalhes_hora);
		voltar = (Button) dialog.findViewById(R.id.detalhes_voltar);
		editar = (Button) dialog.findViewById(R.id.detalhes_editar);
		excluir = (Button) dialog.findViewById(R.id.detalhes_excluir);
		return dialog;
	}
	
	
	
	public Button getVoltar() {
		return voltar;
	}

	public void setVoltar(Button voltar) {
		this.voltar = voltar;
	}

	public Button getEditar() {
		return editar;
	}

	public void setEditar(Button editar) {
		this.editar = editar;
	}

	public Button getExcluir() {
		return excluir;
	}

	public void setExcluir(Button excluir) {
		this.excluir = excluir;
	}

	public TextView getTipo() {
		return tipo;
	}

	public void setTipo(TextView tipo) {
		this.tipo = tipo;
	}

	public TextView getDados() {
		return dados;
	}

	public void setDados(TextView dados) {
		this.dados = dados;
	}

	public TextView getObs() {
		return obs;
	}

	public void setObs(TextView obs) {
		this.obs = obs;
	}

	public TextView getData() {
		return data;
	}

	public void setData(TextView data) {
		this.data = data;
	}

	public TextView getHora() {
		return hora;
	}

	public void setHora(TextView hora) {
		this.hora = hora;
	}
	
	
}
