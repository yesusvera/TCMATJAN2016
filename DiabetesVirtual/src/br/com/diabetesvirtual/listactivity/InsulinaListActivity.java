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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.activity.InsulinaActivity;
import br.com.diabetesvirtual.adapter.InsulinaAdapter;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.model.Detalhes;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.pdf.InsulinaPdf;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.Mensagem;

public class InsulinaListActivity extends ActivityBase implements OnItemClickListener, OnClickListener{
	
	ListView listView;
	public static List<Insulina> lista;
	private Handler handler = new Handler();
	private ProgressDialog alerta;
	private InsulinaDao insulinaDao;
	private InsulinaAdapter adapter;
	private Mensagem msg;
	public static Insulina insulina;
	Detalhes detalhes;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());	
	Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insulina_relatorio_geral);
		listView = (ListView) findViewById(R.id.insulina_relatorio_geral);
		listView.setOnItemClickListener(this);
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		} catch (Exception e) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(InsulinaListActivity.this, "Erro ao carregar lista.");		
		}
	}
		
	public void ThreadCarregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					insulinaDao = new InsulinaDao(InsulinaListActivity.this);
					lista = insulinaDao.getAll();
					atualizaTela();
				} catch (Exception e) {
					lista = new ArrayList<Insulina>();
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
						lista = new ArrayList<Insulina>();
						Mensagem msg = new Mensagem();
						msg.mensagemToast(InsulinaListActivity.this, "Você ainda não adicionou dados da insulina.");						
					} 
					adapter = new InsulinaAdapter(InsulinaListActivity.this, R.layout.insulina_item_da_lista, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();	
				} catch (Exception e) {
					msg = new Mensagem();
					msg.mensagemToast(InsulinaListActivity.this, "Erro ao listar.");
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
				InsulinaPdf pdf = new InsulinaPdf(this,"Relatório Insulina Geral",lista);
				pdf.gerarPdf();		
				break;
		default:
			break;
		}
		return true;
	}

	
	public void carregar() {    //carregar dados nas variaveis	
		detalhes.getTipo().setText("Quantidade");
		detalhes.getDados().setText(insulina.getQtd()+" UI");
		detalhes.getObs().setText("Tipo: "+insulina.getTipo()+"\n"+"Obs.: "+insulina.getObs());
		detalhes.getData().setText(insulina.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+format_dia.format(insulina.getData().getTime()));
		detalhes.getHora().setText(format_hora.format(insulina.getData().getTime()));
		detalhes.getEditar().setOnClickListener(this);
		detalhes.getVoltar().setOnClickListener(this);
		detalhes.getExcluir().setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		try {
			if (v == detalhes.getEditar()) {
				Intent i = new Intent(this, InsulinaActivity.class);
				i.putExtra("extra", "editar");
				startActivityForResult(i, 0);
				return;
			} else if (v == detalhes.getExcluir()) {
				dialog.dismiss();
				alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
				insulinaDao = new InsulinaDao(this);
				insulinaDao.deletar(insulina);
				
				new SyncRESTBo().deleteInsulinaREST(insulina, this, null);
				
				this.ThreadCarregarLista();
			} else {
				dialog.dismiss();
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao efetuar operação");
		}		
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		insulina = new Insulina();
		insulina = lista.get(arg2);
		detalhes = new Detalhes(this);
		dialog = detalhes.carregar();
		this.carregar();	
		dialog.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1 ) {
			dialog.dismiss();
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		}
	}
	
}

