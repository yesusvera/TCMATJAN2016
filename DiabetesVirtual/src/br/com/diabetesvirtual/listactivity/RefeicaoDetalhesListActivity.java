package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.RefeicaoDetalhadaAdapter;
import br.com.diabetesvirtual.dao.RefeicaoDao;
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.pdf.RefeicaoDetalhadaPdf;
import br.com.diabetesvirtual.util.Mensagem;

public class RefeicaoDetalhesListActivity extends ActivityBase{

	List<ItensRefeicao> lista;
	RefeicaoDao refeicaoDao;
	ListView listView;
	RefeicaoDetalhadaAdapter adapter;
	Handler handler = new Handler();
	private ProgressDialog alerta;
	Mensagem msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refeicao_detalhes_geral);
		listView = (ListView) findViewById(R.id.ref_det_lista);
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carrregar lista.");
		}
	}
	
	public void ThreadCarregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					lista = new ArrayList<ItensRefeicao>();
					refeicaoDao = new RefeicaoDao(RefeicaoDetalhesListActivity.this);
					lista = refeicaoDao.getAllItens();
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
					if (lista == null || lista.size()==0) { //caso a lista esteja vazia ainda
						Mensagem msg = new Mensagem();
						msg.mensagemToast(RefeicaoDetalhesListActivity.this, "Sem dados.");	
						lista = new ArrayList<ItensRefeicao>();
					}
					adapter = new RefeicaoDetalhadaAdapter(RefeicaoDetalhesListActivity.this, R.layout.refeicao_por_item_linha, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();
				} catch (Exception e) {
					msg = new Mensagem();
					msg.mensagemToast(RefeicaoDetalhesListActivity.this,"Erro ao carrregar dados.");
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				RefeicaoDetalhadaPdf pdf = new RefeicaoDetalhadaPdf(this,"Relatório Refeicao Detalhes",lista);
				pdf.gerarPdf();		
				break;
		default:
			break;
		}
		return true;
	}
}
