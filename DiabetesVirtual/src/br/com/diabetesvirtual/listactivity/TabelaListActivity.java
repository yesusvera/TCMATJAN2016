package br.com.diabetesvirtual.listactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.TabelaAdapter;
import br.com.diabetesvirtual.dao.TabelaDao;
import br.com.diabetesvirtual.model.Alimento;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.Mensagem;

public class TabelaListActivity extends ActivityBase
		implements OnItemClickListener, TextWatcher, OnCheckedChangeListener, OnClickListener {

	private ListView lista;
	private EditText busca;
	private List<Alimento> alimentos;
	private TabelaDao alimentoDao;
	private ProgressDialog alerta;
	private TabelaAdapter adapter;
	private Alimento alimento;
	Handler handler = new Handler();
	RadioGroup radioGroup;
	RadioButton geral;
	RadioButton favorito;
	Mensagem msg;
	Dialog dialog2;
	EditText medida;
	EditText descricao;
	EditText peso;
	EditText carboidrato;
	CheckBox chek_favorito;
	Button b_salvar;
	Button b_cancelar;
	Button b_excluir;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.i("ON CREATE", "CREATE CHAMADO");
		try {
			setContentView(R.layout.tabela);
			lista = (ListView) findViewById(R.id.lista_classe);
			dialog2 = new Dialog(this, R.style.tema_dialogo);
			dialog2.setContentView(R.layout.dialog_tabela_inserir);
			dialog2.setTitle("Detalhes");
			medida = (EditText) dialog2.findViewById(R.id.tabela_medida);
			peso = (EditText) dialog2.findViewById(R.id.tabela_peso);
			carboidrato = (EditText) dialog2.findViewById(R.id.tabela_carb);
			descricao = (EditText) dialog2.findViewById(R.id.tabela_desc);
			chek_favorito = (CheckBox) dialog2.findViewById(R.id.tabela_chec_favorito);
			b_cancelar = (Button) dialog2.findViewById(R.id.tabela_cancelar);
			b_cancelar.setOnClickListener(this);
			b_salvar = (Button) dialog2.findViewById(R.id.tabela_editar);
			b_salvar.setOnClickListener(this);
			b_excluir = (Button) dialog2.findViewById(R.id.tabela_excluir);
			b_excluir.setOnClickListener(this);
			busca = (EditText) findViewById(R.id.filtro_tabela);
			busca.addTextChangedListener(this);
			radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
			radioGroup.setOnCheckedChangeListener(this);
			geral = (RadioButton) findViewById(R.id.tabela_normal);
			favorito = (RadioButton) findViewById(R.id.tabela_favorito);
			alerta = ProgressDialog.show(this, "Carregando", "A lista está sendo carregada, aguarde..", false, true);
			msg = new Mensagem();
			msg.mensagemToast(this, "*Tabela de alimentos disponibilizada pela Sociedade Brasileira de Diabetes.");
			this.ThreadCarregarLista(false);
			lista.setOnItemClickListener(this);
		} catch (Exception e) {
			Log.i("ERRO", e.getMessage().toString());
		}
	}

	public void ThreadCarregarLista(final Boolean favorito) {
		new Thread() {
			@Override
			public void run() {
				Log.i("THREAD", "THREAD CHAMADA");
				try {
					alimentoDao = new TabelaDao(TabelaListActivity.this);
					alimentos = alimentoDao.listar(favorito);
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
				try {
					if (alimentos == null || alimentos.size() == 0) {
						alimentos = new ArrayList<Alimento>();
						msg = new Mensagem();
						msg.mensagemToast(TabelaListActivity.this, "Sem dados");
					}
					adapter = new TabelaAdapter(TabelaListActivity.this, R.layout.tabela_linha, alimentos);
					lista.setAdapter(adapter);
					busca.setText("");
					alerta.dismiss();
				} catch (Exception e) {
					msg = new Mensagem();
					msg.mensagemToast(TabelaListActivity.this, "Erro ao listar dados.");
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int posicao, long id) {
		alimento = new Alimento();
		alimento = adapter.getItemFilter(posicao);
		medida.setText(alimento.getMedida() + "");
		peso.setText(alimento.getPeso() + "");
		descricao.setText(alimento.getDescricao() + "");
		carboidrato.setText(alimento.getCarboidrato() + "");
		if (alimento.getFavorito() == 1) {
			chek_favorito.setChecked(true);
		} else {
			chek_favorito.setChecked(false);
		}
		b_excluir.setVisibility(View.VISIBLE);
		dialog2.show();
	}

	@Override
	public void afterTextChanged(Editable s) {
		if(adapter!=null){
			adapter.getFilter().filter(s.toString());
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		alimentos = new ArrayList<Alimento>();
		switch (checkedId) {
		case R.id.tabela_normal:
			alerta = ProgressDialog.show(this, "Atualizando", "A lista está sendo atualizada, aguarde..", false, true);
			ThreadCarregarLista(false);
			break;
		case R.id.tabela_favorito:
			alerta = ProgressDialog.show(this, "Atualizando", "A lista está sendo atualizada, aguarde..", false, true);
			ThreadCarregarLista(true);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		try {
			if (v == b_cancelar) {
				alimento = null;
				dialog2.dismiss();
			} else if (v == b_salvar) {
				if (this.validarDados()) {
					alimento = Alimento.getAlimento(alimento);
					alimento.setCarboidrato(Integer.parseInt(carboidrato.getText().toString()));
					alimento.setDescricao(descricao.getText().toString());
					if (chek_favorito.isChecked() == true) {
						alimento.setFavorito(1);
					} else {
						alimento.setFavorito(0);
					}
					alimento.setMedida(medida.getText().toString());
					alimento.setPeso(Integer.parseInt(peso.getText().toString()));
					alimentoDao = new TabelaDao(this);
					alimentoDao.inserir(alimento, false);
					dialog2.dismiss();
					alerta = ProgressDialog.show(this, "Atualizando", "A lista está sendo atualizada, aguarde..", false,
							true);
					ThreadCarregarLista(favorito.isChecked());

					new SyncRESTBo().insertAlimentoREST(alimento, TabelaListActivity.this, null);

				} else {
					msg = new Mensagem();
					msg.mensagemToast(this, "Preencha todos os campos.");
				}
			} else if (v == b_excluir) {
				alimentoDao = new TabelaDao(this);
				if (alimentoDao.deletar(alimento)) {
					msg = new Mensagem();
					msg.mensagemToast(this, "Impossivel deletar, este item está sendo usado em uma refeição salva.");
				} else {
					dialog2.dismiss();
					AlertDialog.Builder msg = new AlertDialog.Builder(this);
					msg.setTitle("Excluir item");
					msg.setMessage(
							"Ao confirmar a exclusão o item será permanentemente deletado do aplicatico. Deseja continuar?");
					msg.setNeutralButton("Não", null);
					msg.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog2.dismiss();
							alerta = ProgressDialog.show(TabelaListActivity.this, "Atualizando",
									"A lista está sendo atualizada, aguarde..", false, true);
							
							new SyncRESTBo().deleteAlimentoREST(alimento, TabelaListActivity.this, null);
							
							ThreadCarregarLista(favorito.isChecked());
						}
					});
					msg.show();
				}
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao salvar dados");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Inserir Novo");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			alimento = new Alimento();
			this.limparDialog();
			b_excluir.setVisibility(View.GONE);
			dialog2.show();
			break;
		default:
			break;
		}
		return true;
	}

	public void limparDialog() {
		medida.setText("");
		peso.setText("");
		carboidrato.setText("");
		descricao.setText("");
		chek_favorito.setChecked(false);
	}

	public Boolean validarDados() {
		if (medida.getText().toString().equals("") || peso.getText().toString().equals("")
				|| carboidrato.getText().toString().equals("") || descricao.getText().toString().equals("")) {
			return false;
		} else {
			return true;
		}
	}
}
