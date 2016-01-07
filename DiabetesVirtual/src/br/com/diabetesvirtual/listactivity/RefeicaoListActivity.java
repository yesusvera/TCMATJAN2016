package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.activity.RefeicaoActivity;
import br.com.diabetesvirtual.adapter.ItensRefeicaoAdapter;
import br.com.diabetesvirtual.adapter.RefeicaoAdapter;
import br.com.diabetesvirtual.dao.RefeicaoDao;
import br.com.diabetesvirtual.grafico.RefeicaoGrafico;
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.pdf.RefeicaoPdf;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;

public class RefeicaoListActivity extends ActivityBase implements OnItemClickListener{
	ListView listView;
	ListView listView_itens;
	TextView textView_data;
	TextView textView_hora;
	TextView textView_carb_total;
	TextView textView_peso_total;
	TextView textView_obs;
	TextView textView_tipo;
	public static List<Refeicao> lista;
	public static Refeicao refeicao;
	private List<ItensRefeicao> itens;
	private ItensRefeicaoAdapter adapter_itens;
	private Handler handler = new Handler();
	private ProgressDialog alerta;
	private RefeicaoDao refeicaoDao;
	private RefeicaoAdapter adapter;
	ProgressDialog progressBar;
	Mensagem msg;
	LinearLayout tela1;
	LinearLayout tela2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refeicao_relatorio_geral);
		listView = (ListView) findViewById(R.id.listView_refeicao);
		listView.setOnItemClickListener(this);	
		listView_itens = (ListView) findViewById(R.id.ListView_itens_ref);
		textView_data = (TextView) findViewById(R.id.textView_data_ref);
		textView_carb_total = (TextView) findViewById(R.id.textView_carb_ref);
		textView_peso_total  = (TextView) findViewById(R.id.textView_peso_ref);
		textView_obs = (TextView) findViewById(R.id.textView_obs_ref);		
		textView_hora = (TextView) findViewById(R.id.textView_hora_ref);
		textView_tipo = (TextView) findViewById(R.id.textView_tipo_ref);
		tela1 = (LinearLayout) findViewById(R.id.ref_relatorio_tela1);
		tela2 = (LinearLayout) findViewById(R.id.ref_relatorio_tela2);
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(RefeicaoListActivity.this, "Erro ao efetuar operação.");
		}
	}
	
	public void ThreadCarregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					refeicaoDao = new RefeicaoDao(RefeicaoListActivity.this);
					lista = refeicaoDao.getAll();
					atualizaTela();
				} catch (Exception e) {
					msg = new Mensagem();
					msg.mensagemToast(RefeicaoListActivity.this, "Erro ao efetuar operação.");
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
						msg.mensagemToast(RefeicaoListActivity.this, "Sem dados.");
						lista = new ArrayList<Refeicao>();
					} 
					adapter = new RefeicaoAdapter(RefeicaoListActivity.this, R.layout.refeicao_item_da_lista, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();
				} catch (Exception e) {
					msg = new Mensagem();
					msg.mensagemToast(RefeicaoListActivity.this, "Erro ao efetuar operação.");
				}							
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		refeicao = new Refeicao();
		try {
			refeicao = lista.get(arg2);
			refeicaoDao = new RefeicaoDao(this);
			itens = refeicaoDao.getItens(refeicao.getId());
			if (itens == null || itens.size() < 1) {
				msg = new Mensagem();
				msg.mensagemToast(RefeicaoListActivity.this, "Erro - Nenhum item foi encontrado.");
			}else {
				adapter_itens = new ItensRefeicaoAdapter(this, R.layout.itens_refeicao_linha, itens);
				listView_itens.setAdapter(adapter_itens);
				String data = refeicao.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", " +
						""+refeicao.getData().get(Calendar.DAY_OF_MONTH)+" de "+refeicao.getData().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
						+" de "+refeicao.getData().get(Calendar.YEAR)+"";
				String hora = refeicao.getData().get(Calendar.HOUR_OF_DAY)+" horas e "+refeicao.getData().get(Calendar.MINUTE)+" minutos";
				textView_data.setText(data); 
				textView_hora.setText(hora);
				textView_carb_total.setText(Formatos.formataDouble(refeicao.getCarboidrato())+"g"); 
				textView_peso_total.setText(Formatos.formataDouble(refeicao.getPeso())+"g"); 
				textView_tipo.setText(refeicao.getTipo());
				//textView_tipo.setText(RefeicaoTipos.getNome(RefeicaoListActivity.this, refeicao.getTipo().getCod()));
				if (refeicao.getObs() == null || refeicao.getObs().length() == 0) {
					textView_obs.setText("Nenhuma");
				} else {
					textView_obs.setText(refeicao.getObs());
				}
				this.alternaTela();
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(RefeicaoListActivity.this, "Erro ao efetuar operação.");
		}
	}

	public void alternaTela() {
		if (tela1.getVisibility() == View.VISIBLE) {
			tela1.setVisibility(View.GONE);
			tela2.setVisibility(View.VISIBLE);
		} else {
			tela1.setVisibility(View.VISIBLE);
			tela2.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (tela2.getVisibility() == View.VISIBLE) {
			this.alternaTela();
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Gerar PDF");
		menu.add(0, 1, 1, "Gráfico");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				RefeicaoPdf pdf = new RefeicaoPdf(this,"Relatório Refeição - Geral",lista);
				pdf.gerarPdf();		
				break;
			case 1:	
				Intent i = new Intent(this, RefeicaoGrafico.class);
				i.putExtra("extra", "RefeicaoListActivity");
				startActivity(i);
				break;
		default:
			break;
		}
		return true;
	}
		
	public void voltar(View view) {
		this.alternaTela();
	}
	
	public void editar(View view) {
		Intent i = new Intent(this, RefeicaoActivity.class);
		i.putExtra("extra", "editar");
		startActivityForResult(i, 0);
		return;
	}

	public void excluir(View view) {
		try {
			refeicaoDao = new RefeicaoDao(this);
			refeicaoDao.deletar(refeicao);
			
			new SyncRESTBo().deleteRefeicaoREST(refeicao, this, null);
			
			this.alternaTela();
			this.ThreadCarregarLista();
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao excluir.");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0 ) {
			this.alternaTela();
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		}
	}

}
