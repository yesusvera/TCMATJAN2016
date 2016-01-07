package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.GlicemiaAdapter;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.util.Mensagem;


public class GlicemiaPorTipoListActivity extends ActivityBase implements android.widget.AdapterView.OnItemSelectedListener{

	List<Glicemia> lista;
	ListView listView;
	GlicemiaDao glicemiaDao;
	ProgressDialog alerta;
	GlicemiaAdapter adapter;
	Handler handler = new Handler();
	Spinner listaTipo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glicemia_relatorio_por_tipo);
		ArrayAdapter<CharSequence> adapter_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_glicemia, R.layout.spinner_item);
		listaTipo = (Spinner) findViewById(R.id.tipo_glicemia_relatorio_spinner);
		listaTipo.setAdapter(adapter_tipo);
		listView = (ListView) findViewById(R.id.lista);
		OnItemSelectedListener teste = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getSelectedItem().toString();
				try {
					if(item.equals("Nenhum")) {
						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
						lista = glicemiaDao.listarPorTipo("Nenhum");
						atualizaTela();
					}else if(item.equals("Em Jejum")) {
//						Toast.makeText(getApplicationContext(), "entrou if", Toast.LENGTH_SHORT).show();
						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
						lista = glicemiaDao.listarPorTipo("Em Jejum");
//						Toast.makeText(getApplicationContext(), "passou dao", Toast.LENGTH_SHORT).show();
						atualizaTela();
					}else if(item.equals("Pré Prandial")) {
						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
						lista = glicemiaDao.listarPorTipo("Pré Prandial");
						atualizaTela();
					}else {
						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
						lista = glicemiaDao.listarPorTipo("Pós Prandial");
						atualizaTela();
					}	
				} catch (Exception e) {
					lista=null;
					atualizaTela();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
		
		
		listaTipo.setOnItemSelectedListener(teste);
		
		
		
		
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			//this.ThreadCarregarLista(R.id.todos);
		} catch (Exception e) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carregar lista.");		
		}
	}
		
	
	
//	public void ThreadCarregarLista(final int id) {
//		new Thread() {
//			private Resources res;
//
//			@Override
//			public void run() {
//				lista = new ArrayList<Glicemia>();
//				res = getResources();
//				String[] vetorTipo = res.getStringArray(R.array.tipo_glicemia);
//				
//				try {
//					if(vetorTipo.equals("Nenhum")) {
//						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
//						lista = glicemiaDao.listarPorTipo("Nenhum");
//						atualizaTela();
//					}else if(vetorTipo.equals("Em Jejum")) {
//						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
//						lista = glicemiaDao.listarPorTipo("Em Jejum");
//						atualizaTela();
//					}else if(vetorTipo.equals("Pré Prandial")) {
//						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
//						lista = glicemiaDao.listarPorTipo("Pré Prandial");
//						atualizaTela();
//					}else {
//						glicemiaDao = new GlicemiaDao(GlicemiaPorTipoListActivity.this);
//						lista = glicemiaDao.listarPorTipo("Pós Prandial");
//						atualizaTela();
//					}
//						
//				} catch (Exception e) {
//					lista=null;
//					atualizaTela();
//				}
//			}
//		}.start();
//	};
		
		
	public void atualizaTela() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				try{
					if (lista == null) { //caso a lista esteja vazia ainda
						Mensagem msg = new Mensagem();
						msg.mensagemToast(GlicemiaPorTipoListActivity.this, "Dados insuficientes.");
						lista = new ArrayList<Glicemia>();
					} 
					adapter = new GlicemiaAdapter(GlicemiaPorTipoListActivity.this, R.layout.glicemia_item_da_lista_1, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();	
				} catch (Exception e) {
					Mensagem msg = new Mensagem();
					msg.mensagemToast(GlicemiaPorTipoListActivity.this, "Erro carregar lista.");
				}							
			}
		});
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Gerar PDF");
		return true;
	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		super.onOptionsItemSelected(item);
//		switch (item.getItemId()) {
//			case 0:
//				InsulinaPdf pdf = new InsulinaPdf(this,"Relatório Insulina por Tipo",lista);
//				pdf.gerarPdf();		
//				break;
//		default:
//			break;
//		}
//		return true;
//	}



	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	
}
