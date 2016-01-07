package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.ExerciciosAdapter;
import br.com.diabetesvirtual.dao.ExerciciosDao;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.pdf.ExerciciosPdf;
import br.com.diabetesvirtual.util.Mensagem;


public class ExercicioPorTipoListActivity extends ActivityBase implements OnCheckedChangeListener{

	List<Exercicios> lista;
	ListView listView;
	ExerciciosDao exercicioDao;
	ProgressDialog alerta;
	ExerciciosAdapter adapter;
	Handler handler = new Handler();
	RadioGroup radioGroup;
	RadioButton anaerobico;
	RadioButton aerobico;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercicios_relatorio_por_tipo);
		radioGroup = (RadioGroup) findViewById(R.id.exercicio_radio_group);
		anaerobico = (RadioButton) findViewById(R.id.anaerobico);
		aerobico = (RadioButton) findViewById(R.id.aerobico);
		listView = (ListView) findViewById(R.id.lista_exercicios);
		anaerobico.setText("Anaerobico");
		aerobico.setText("Aerobico");
		radioGroup.setOnCheckedChangeListener(this);
		
		try {
//			Toast.makeText(getApplicationContext(), "entra try1", Toast.LENGTH_SHORT).show();
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
//			Toast.makeText(getApplicationContext(), "thread antes", Toast.LENGTH_SHORT).show();
			this.ThreadCarregarLista(R.id.exercicios_todos);
//			Toast.makeText(getApplicationContext(), "thread depois", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carregar lista.");		
		}
	}
		
	public void ThreadCarregarLista(final int id) {
//		new Thread() {
//			@Override
//			public void run() {
				lista = new ArrayList<Exercicios>();
				try {
//					Toast.makeText(getApplicationContext(), "entra try", Toast.LENGTH_SHORT).show();
					switch (id) {
						case R.id.anaerobico:
//							Toast.makeText(getApplicationContext(), "antes dao ana", Toast.LENGTH_SHORT).show();
							exercicioDao = new ExerciciosDao(ExercicioPorTipoListActivity.this);
							lista = exercicioDao.listarPorTipo("Anaeróbico");
							atualizaTela();;
							break;
						case R.id.aerobico:
//							Toast.makeText(getApplicationContext(), "antes dao aerobico", Toast.LENGTH_SHORT).show();
							exercicioDao = new ExerciciosDao(ExercicioPorTipoListActivity.this);
							lista = exercicioDao.listarPorTipo("Aeróbico");
							atualizaTela();
							break;
						default:
//							Toast.makeText(getApplicationContext(), "antes dao", Toast.LENGTH_SHORT).show();
							exercicioDao = new ExerciciosDao(ExercicioPorTipoListActivity.this);
							Log.i("Teste", "Passou DAO");
							lista = exercicioDao.getAll();
							Log.i("Teste", "Passou Lista DAO");
							atualizaTela();;
					}
				} catch (Exception e) {
					e.printStackTrace();
					lista=null;
					atualizaTela();
				}
//			}
//		}.start();
	}
		
		
	public void atualizaTela() {
//		handler.post(new Runnable() {			
//			@Override
//			public void run() {
				try{
					if (lista == null) { //caso a lista esteja vazia ainda
						Mensagem msg = new Mensagem();
//						msg.mensagemToast(ExercicioPorTipoListActivity.this, "Dados insuficientes teste.");
						lista = new ArrayList<Exercicios>();
					} 
					adapter = new ExerciciosAdapter(ExercicioPorTipoListActivity.this, R.layout.exercicios_item_da_lista, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();	
				} catch (Exception e) {
					Mensagem msg = new Mensagem();
					msg.mensagemToast(ExercicioPorTipoListActivity.this, "Erro carregar lista.");
				}							
//			}
//		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		this.ThreadCarregarLista(checkedId);
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
				ExerciciosPdf pdf = new ExerciciosPdf(this,"Relatório Exercicio por Tipo",lista);
				pdf.gerarPdf();		
				break;
		default:
			break;
		}
		return true;
	}

	
}
