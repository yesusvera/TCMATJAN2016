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
import br.com.diabetesvirtual.activity.ExerciciosActivity;
import br.com.diabetesvirtual.adapter.ExerciciosAdapter;
import br.com.diabetesvirtual.dao.ExerciciosDao;
import br.com.diabetesvirtual.grafico.ExercicioGrafico;
import br.com.diabetesvirtual.model.Detalhes;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.pdf.ExerciciosPdf;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.Mensagem;

public class ExerciciosListActivity extends ActivityBase implements OnItemClickListener,OnClickListener{
	ListView listView;
	public static List<Exercicios> lista;
	private Handler handler = new Handler();
	private ProgressDialog alerta;
	private ExerciciosDao exerciciosDao;
	private ExerciciosAdapter adapter;
	ProgressDialog progressBar;	
	final String path = Environment.getExternalStorageDirectory().getPath();
	final String pasta = "Diabetes Virtual";
	final String pasta_criada = path+"/"+pasta;
	Mensagem msg;
	public static Exercicios exercicio;
	Detalhes detalhes;
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());	
	Dialog dialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercicios_relatorio_geral);
		listView = (ListView) findViewById(R.id.exe_lista);
		listView.setOnItemClickListener(this);
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carregar lista.");
		}
	}
	
	public void ThreadCarregarLista() {
		new Thread() {
			@Override
			public void run() {
				try {
					exerciciosDao = new ExerciciosDao(ExerciciosListActivity.this);
					lista = exerciciosDao.getAll();
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
						msg.mensagemToast(ExerciciosListActivity.this, "Sem dados.");	
						lista = new ArrayList<Exercicios>();
					} 
					adapter = new ExerciciosAdapter(ExerciciosListActivity.this, R.layout.exercicios_item_da_lista, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();
				} catch (Exception e) {
					msg = new Mensagem();
					msg.mensagemToast(ExerciciosListActivity.this, "Erro ao carregar registros.");
				}							
			}
		});
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
				ExerciciosPdf pdf = new ExerciciosPdf(this, "Relatório Atividade física", lista);
				pdf.gerarPdf();
				break;
			case 1:	
				startActivity(new Intent(this, ExercicioGrafico.class));
				break;
		default:
			break;
		}
		return true;
	}

	public void carregar() {    //carregar dados nas variaveis	
		detalhes.getTipo().setText("Descrição");
		detalhes.getDados().setText(exercicio.getDescricao());
		detalhes.getObs().setText("Duração: "+exercicio.getDuracao()+" minutos"+
				//"\n"+"Intensidade: "+ExerciciosIntensidade.getNome(this, exercicio.getIntensidade().getCod())+
				"\n"+"Intensidade: "+exercicio.getIntensidade()+
				"\n"+"Modalidade: "+exercicio.getModalidade()+
				"\n"+"Tipo: "+exercicio.getTipo());
		detalhes.getData().setText(exercicio.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+format_dia.format(exercicio.getData().getTime()));
		detalhes.getHora().setText(format_hora.format(exercicio.getData().getTime()));
		detalhes.getEditar().setOnClickListener(this);
		detalhes.getVoltar().setOnClickListener(this);
		detalhes.getExcluir().setOnClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		exercicio = new Exercicios();
		exercicio = lista.get(arg2);
		detalhes = new Detalhes(this);			
		dialog =  detalhes.carregar();
		this.carregar();
		dialog.show();		
	}

	@Override
	public void onClick(View v) {
		try {
			if (v == detalhes.getEditar()) {
				Intent i = new Intent(this, ExerciciosActivity.class);
				i.putExtra("extra", "editar");
				startActivityForResult(i, 0);
				return;
			} else if (v == detalhes.getExcluir()) {
				dialog.dismiss();
				alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
				exerciciosDao = new ExerciciosDao(this);
				exerciciosDao.deletar(exercicio);
				
				new SyncRESTBo().deleteExercicioREST(exercicio, this, null);
				
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0 ) {
			dialog.dismiss();
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista();
		}
	}
}
