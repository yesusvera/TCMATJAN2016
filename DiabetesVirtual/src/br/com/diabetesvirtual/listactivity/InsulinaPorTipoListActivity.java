package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.InsulinaAdapter;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.InsulinaTipos;
import br.com.diabetesvirtual.pdf.InsulinaPdf;
import br.com.diabetesvirtual.util.Mensagem;


public class InsulinaPorTipoListActivity extends ActivityBase implements OnCheckedChangeListener{

	List<Insulina> lista;
	ListView listView;
	InsulinaDao insulinaDao;
	ProgressDialog alerta;
	InsulinaAdapter adapter;
	Handler handler = new Handler();
	RadioGroup radioGroup;
	RadioButton lenta;
	RadioButton rapida;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insulina_relatorio_por_tipo);
		radioGroup = (RadioGroup) findViewById(R.id.insulina_radio_group);
		lenta = (RadioButton) findViewById(R.id.lenta);
		rapida = (RadioButton) findViewById(R.id.rapida);
		listView = (ListView) findViewById(R.id.lista);
		lenta.setText(InsulinaTipos.LENTA.getNome());
		rapida.setText(InsulinaTipos.RAPIDA.getNome());
		radioGroup.setOnCheckedChangeListener(this);
		try {
			alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
			this.ThreadCarregarLista(R.id.todos);
		} catch (Exception e) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carregar lista.");		
		}
	}
		
	public void ThreadCarregarLista(final int id) {
//		new Thread() {
//			@Override
//			public void run() {
				lista = new ArrayList<Insulina>();
				try {
					switch (id) {
						case R.id.lenta:
							insulinaDao = new InsulinaDao(InsulinaPorTipoListActivity.this);
							lista = insulinaDao.listarPorTipo(InsulinaTipos.forInsulina(0));
							atualizaTela();;
							break;
						case R.id.rapida:
							insulinaDao = new InsulinaDao(InsulinaPorTipoListActivity.this);
							lista = insulinaDao.listarPorTipo(InsulinaTipos.forInsulina(1));
							atualizaTela();;
							break;
						default:
							insulinaDao = new InsulinaDao(InsulinaPorTipoListActivity.this);
							lista = insulinaDao.getAll();
							atualizaTela();;
					}
				} catch (Exception e) {
					lista=null;
					atualizaTela();
				}
//			}
//		}.start();
	};
		
		
	public void atualizaTela() {
//		handler.post(new Runnable() {			
//			@Override
//			public void run() {
				try{
					if (lista == null) { //caso a lista esteja vazia ainda
						Mensagem msg = new Mensagem();
						msg.mensagemToast(InsulinaPorTipoListActivity.this, "Dados insuficientes.");
						lista = new ArrayList<Insulina>();
					} 
					adapter = new InsulinaAdapter(InsulinaPorTipoListActivity.this, R.layout.insulina_item_da_lista, lista);
					listView.setAdapter(adapter);
					alerta.dismiss();	
				} catch (Exception e) {
					Mensagem msg = new Mensagem();
					msg.mensagemToast(InsulinaPorTipoListActivity.this, "Erro carregar lista.");
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
				InsulinaPdf pdf = new InsulinaPdf(this,"Relatório Insulina por Tipo",lista);
				pdf.gerarPdf();		
				break;
		default:
			break;
		}
		return true;
	}

	
}
