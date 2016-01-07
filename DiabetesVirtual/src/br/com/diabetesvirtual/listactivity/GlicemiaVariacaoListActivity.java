package br.com.diabetesvirtual.listactivity;


import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.GlicemiaAdapter;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.grafico.GlicemiaGrafico;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.pdf.GlicemiaPdf;
import br.com.diabetesvirtual.util.DialogDetalhes;
import br.com.diabetesvirtual.util.Mensagem;

public class GlicemiaVariacaoListActivity extends ActivityBase implements TextWatcher{

	EditText glic_inicial;
	EditText glic_final;
	GlicemiaDao glicemiaDao;
	ListView listview;
	GlicemiaAdapter adapter;
	ProgressDialog alerta;
	Mensagem msg; 
	public static List<Glicemia> lista;
	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glicemia_variacao);		
		glic_inicial = (EditText) findViewById(R.id.glic_inicial);
		glic_inicial.addTextChangedListener(this);
		glic_final = (EditText) findViewById(R.id.glic_final);
		glic_final.addTextChangedListener(this);
		listview = (ListView) findViewById(R.id.glicemia_lista);
	}
	
	public void pesquisar(View view) {
		try {
			if (glic_inicial.length()==0 || glic_final.length()==0) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Informe a glicemia Incial e final.");
			} else {
				alerta = ProgressDialog.show(this, "Carregando..", "Carregando dados, esta operação pode levar alguns segundos..");
				this.ThreadCarregarLista();								
			}
		} catch (Exception e) {
			Log.e("ERRO", "Erro ao pesquisar");
		}
	}
	
	public void ThreadCarregarLista() {
		new Thread() {
			@Override
			public void run() {
				lista = new ArrayList<Glicemia>();
				try {
					int inicio = Integer.parseInt(glic_inicial.getText().toString());
					int fim = Integer.parseInt(glic_final.getText().toString());
					glicemiaDao = new GlicemiaDao(GlicemiaVariacaoListActivity.this);
					lista =  glicemiaDao.bucarPorIntervaloGlic(inicio, fim);
					atualizaTela();
				} catch (Exception e) {
					Log.e("Erro ao pesquisar", e.getMessage());
				}	
			}
		}.start();	
	}
	
	public void atualizaTela() {
		handler.post(new Runnable() {		
			@Override
			public void run() {			
				adapter = new GlicemiaAdapter(GlicemiaVariacaoListActivity.this, R.layout.glicemia_item_da_lista_1, lista);
				listview.setAdapter(adapter);
				if (lista.size() == 0 ) { //mostra a lista ou some
					msg = new Mensagem();
					msg.mensagemToast(GlicemiaVariacaoListActivity.this, "Nenhum registro encontrado.");
				} 
				alerta.dismiss();
			}
		});
	}

	public void voltar(View view) {
		finish();
	}
	@Override
	public void afterTextChanged(Editable s) {
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {	
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) { //aumenta a letra ao digitar
		if (s.length()==0) {
			if (glic_final.length()==0) {
				glic_final.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			}
			if (glic_inicial.length()==0) {
				glic_inicial.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			}
		} else {
			if (glic_final.length()>0) {
				glic_final.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			}
			if (glic_inicial.length()>0) {
				glic_inicial.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			}
		}
	}	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Gerar PDF");
		menu.add(0, 1, 1, "Gráfico");
		menu.add(0, 2, 2, "Estatística");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				GlicemiaPdf pdf = new GlicemiaPdf(this,"Relatório Glicemia Variação",Integer.parseInt(glic_inicial.getText().toString()),Integer.parseInt(glic_final.getText().toString()),lista);
				pdf.gerarPdf();				
				break;
			case 1:	
				Intent i = new Intent(this, GlicemiaGrafico.class);
				i.putExtra("extra", "GlicemiaVariacaoListActivity");
				startActivity(i);
				break;
			case 2:
				DialogDetalhes d = new DialogDetalhes(this);
				d.DialogEstatistica(lista);
			break;
		default:
			break;
		}
		return true;
	}
}
