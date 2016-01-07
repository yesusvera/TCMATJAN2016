package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.ItensRefeicaoAdapter;
import br.com.diabetesvirtual.adapter.RefeicaoAdapter;
import br.com.diabetesvirtual.dao.RefeicaoDao;
import br.com.diabetesvirtual.grafico.RefeicaoGrafico;
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.pdf.RefeicaoPdf;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;

public class RefeicaoVariacaoListActivity extends ActivityBase implements TextWatcher, OnItemClickListener {
	EditText ref_inicial;
	EditText ref_final;
	ListView listView_itens;
	TextView textView_data;
	TextView textView_hora;
	TextView textView_carb_total;
	TextView textView_peso_total;
	TextView textView_obs;
	TextView textView_tipo;
	RelativeLayout tela1;
	LinearLayout tela2;
	RefeicaoDao refeicaoDao;
	Refeicao refeicao;
	ListView listview;
	RefeicaoAdapter adapter;
	ProgressDialog alerta;
	Mensagem msg; 
	public static List<Refeicao> lista;
	private List<ItensRefeicao> itens;
	private ItensRefeicaoAdapter adapter_itens;
	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refeicao_variacao);		
		ref_inicial = (EditText) findViewById(R.id.ref_inicial);
		ref_inicial.addTextChangedListener(this);
		ref_final = (EditText) findViewById(R.id.ref_final);
		ref_final.addTextChangedListener(this);
		listview = (ListView) findViewById(R.id.refeicao_lista);
		listview.setOnItemClickListener(this);	
		listView_itens = (ListView) findViewById(R.id.ListView_itens_ref);
		textView_data = (TextView) findViewById(R.id.textView_data_ref);
		textView_carb_total = (TextView) findViewById(R.id.textView_carb_ref);
		textView_peso_total  = (TextView) findViewById(R.id.textView_peso_ref);
		textView_obs = (TextView) findViewById(R.id.textView_obs_ref);		
		textView_hora = (TextView) findViewById(R.id.textView_hora_ref);
		textView_tipo = (TextView) findViewById(R.id.textView_tipo_ref);
		tela1 = (RelativeLayout) findViewById(R.id.ref_relatorio_tela1);
		tela2 = (LinearLayout) findViewById(R.id.ref_relatorio_tela2);
	}
	
	public void pesquisar(View view) {
		try {
			if (ref_inicial.length()==0 || ref_inicial.length()==0) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Informe a refeicao Incial e final.");
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
				lista = new ArrayList<Refeicao>();
				try {
					int inicio = Integer.parseInt(ref_inicial.getText().toString());
					int fim = Integer.parseInt(ref_final.getText().toString());
					refeicaoDao = new RefeicaoDao(RefeicaoVariacaoListActivity.this);
					lista =  refeicaoDao.bucarPorIntervaloCarb(inicio, fim);
					atualizaTela();
				} catch (Exception e) {
					lista = null;
					atualizaTela();
				}	
			}
		}.start();	
	}
	
	public void atualizaTela() {
		handler.post(new Runnable() {		
			@Override
			public void run() {					
				if (lista==null || lista.size() == 0 ) { //mostra a lista ou some
					lista = new ArrayList<Refeicao>();
					msg = new Mensagem();
					msg.mensagemToast(RefeicaoVariacaoListActivity.this, "Nenhum registro encontrado.");
				} 
				adapter = new RefeicaoAdapter(RefeicaoVariacaoListActivity.this, R.layout.refeicao_item_da_lista, lista);
				listview.setAdapter(adapter);
				alerta.dismiss();
			}
		});
	}
	
	public void voltar(View view) {
		finish();
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
				msg.mensagemToast(RefeicaoVariacaoListActivity.this, "Erro - Nenhum item foi encontrado.");
			}else {
				adapter_itens = new ItensRefeicaoAdapter(this, R.layout.itens_refeicao_linha, itens);
				listView_itens.setAdapter(adapter_itens);
				String data = refeicao.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", " +
						""+refeicao.getData().get(Calendar.DAY_OF_MONTH)+" de "+refeicao.getData().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
						+" de "+refeicao.getData().get(Calendar.YEAR)+"";
				String hora = refeicao.getData().get(Calendar.HOUR_OF_DAY)+" horas e "+refeicao.getData().get(Calendar.MINUTE)+" minutos";
				textView_data.setText(data); 
				textView_hora.setText(hora);
				textView_tipo.setText(refeicao.getTipo());
				//textView_tipo.setText(RefeicaoTipos.getNome(this, refeicao.getTipo().getCod()));
				textView_carb_total.setText(Formatos.formataDouble(refeicao.getCarboidrato())+"g"); 
				textView_peso_total.setText(Formatos.formataDouble(refeicao.getPeso())+"g"); 
				if (refeicao.getObs() == null || refeicao.getObs().length() == 0) {
					textView_obs.setText("Nenhuma");
				} else {
					textView_obs.setText(refeicao.getObs());
				}
				this.alternaTela();
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(RefeicaoVariacaoListActivity.this, "Erro ao efetuar operação.");
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
	public void afterTextChanged(Editable s) {	
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {	
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) { //aumenta a letra ao digitar
		if (s.length()==0) {
			if (ref_final.length()==0) {
				ref_final.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			}
			if (ref_inicial.length()==0) {
				ref_inicial.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			}
		} else {
			if (ref_final.length()>0) {
				ref_final.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			}
			if (ref_inicial.length()>0) {
				ref_inicial.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			}
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
				RefeicaoPdf pdf = new RefeicaoPdf(this,"Relatório Refeição - Variação",Integer.parseInt(ref_inicial.getText().toString()),Integer.parseInt(ref_final.getText().toString()),lista);
				pdf.gerarPdf();		
				break;
			case 1:	
				Intent i = new Intent(this, RefeicaoGrafico.class);
				i.putExtra("extra", "RefeicaoVariacaoListActivity");
				startActivity(i);
				break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		if (tela2.getVisibility() == View.VISIBLE) {
			this.alternaTela();
		} else {
			super.onBackPressed();
		}
	}
}
