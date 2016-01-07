package br.com.diabetesvirtual.listactivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.activity.GlicemiaActivity;
import br.com.diabetesvirtual.adapter.GlicemiaAdapter;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.grafico.GlicemiaGrafico;
import br.com.diabetesvirtual.model.Detalhes;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.pdf.GlicemiaPdf;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.DialogDetalhes;
import br.com.diabetesvirtual.util.Mensagem;


public class GlicemiaListActivity extends ActivityBase implements OnItemClickListener, OnClickListener{

	ListView listView;
	public static List<Glicemia> lista;
	private Handler handler = new Handler();
	private ProgressDialog alerta;
	private GlicemiaDao glicemiaDao;
	private GlicemiaAdapter adapter;
	ProgressDialog progressBar;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());	
	final String path = Environment.getExternalStorageDirectory().getPath();
	final String pasta = "Diabetes Virtual";
	final String pasta_criada = path+"/"+pasta;
	Dialog dialog;
	Detalhes detalhes;
	Mensagem msg;
	public static Glicemia glicemia;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glicemia_lista_1);
		listView = (ListView) findViewById(R.id.glicemia_lista);
		listView.setOnItemClickListener(this);
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		} catch (Exception e) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carrregar lista.");
		}
	}
	
	public void ThreadCarregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					glicemiaDao = new GlicemiaDao(GlicemiaListActivity.this);
					lista = glicemiaDao.getAll();
					atualizaTela();
				} catch (Exception e) {
					lista = null;
					atualizaTela();
				}
			}
		}.start();
	};
	
	public void atualizaTela() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				try{
					if (lista == null) { //caso a lista esteja vazia ainda
						Mensagem msg = new Mensagem();
						msg.mensagemToast(GlicemiaListActivity.this, "Você ainda não adicionou dados da glicemia.");	
						lista = new ArrayList<Glicemia>();
					}
					adapter = new GlicemiaAdapter(GlicemiaListActivity.this, R.layout.glicemia_item_da_lista_1, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();
				} catch (Exception e) {
					msg = new Mensagem();
					msg.mensagemToast(GlicemiaListActivity.this,"Erro ao carrregar dados.");
				}							
			}
		});
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
				GlicemiaPdf pdf = new GlicemiaPdf(this,"Relatório Glicemia Geral",lista);
				pdf.gerarPdf();		
				break;
			case 1:	
				Intent i = new Intent(this, GlicemiaGrafico.class);
				i.putExtra("extra", "GlicemiaListActivity");
				startActivity(i);
				break;
			case 2:
				DialogDetalhes d = new DialogDetalhes(this);
				d.DialogEstatistica(lista);
			break;
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		glicemia = new Glicemia();
		glicemia = lista.get(arg2);
		detalhes = new Detalhes(this);			
		dialog =  detalhes.carregar();
		this.carregar();
		dialog.show();		
	}
	
	public void carregar() {    //carregar dados nas variaveis	
		detalhes.getTipo().setText("Medida");
		detalhes.getDados().setText(glicemia.getMedida()+" md/dL");
		detalhes.getObs().setText("Obs.: "+glicemia.getObs());
		detalhes.getData().setText(glicemia.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+format_dia.format(glicemia.getData().getTime()));
		detalhes.getHora().setText(format_hora.format(glicemia.getData().getTime()));
		detalhes.getEditar().setOnClickListener(this);
		detalhes.getVoltar().setOnClickListener(this);
		detalhes.getExcluir().setOnClickListener(this);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0 ) {
			dialog.dismiss();
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			if (v == detalhes.getEditar()) {
				Intent i = new Intent(this, GlicemiaActivity.class);
				i.putExtra("extra", "editar");
				startActivityForResult(i, 0);
				return;
			} else if (v == detalhes.getExcluir()) {
				dialog.dismiss();
				alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
				glicemiaDao = new GlicemiaDao(this);
				glicemiaDao.deletar(glicemia);
				
				new SyncRESTBo().deleteGlicemiaREST(glicemia, this, null);
				
				this.ThreadCarregarLista();
			} else {
				dialog.dismiss();
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao efetuar operação");
		}
	}	
}


