package br.com.diabetesvirtual.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.TabelaAdapter;
import br.com.diabetesvirtual.adapter.TabelaAddAdapter;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.dao.PerfilDao;
import br.com.diabetesvirtual.dao.RefeicaoDao;
import br.com.diabetesvirtual.dao.TabelaDao;
import br.com.diabetesvirtual.listactivity.RefeicaoListActivity;
import br.com.diabetesvirtual.model.Alimento;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.model.Perfil;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.DialogLembretes;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;

public class RefeicaoActivity extends Activity implements OnItemClickListener, OnClickListener, TextWatcher,
		OnDateChangedListener, OnTimeChangedListener, OnCheckedChangeListener {

	RelativeLayout layout_add;
	RelativeLayout layout_data_hora;
	RelativeLayout layout_tabela;
	LinearLayout sugestao_layout;
	Button b_qtd_ok;
	Button b_qtd_cancel;
	ListView listView_add;
	ListView listView_tabela;
	List<Alimento> alimentos;
	List<ItensRefeicao> itens;
	TextView textView_carb_total;
	TextView textView_peso_total;
	TextView textView_carb_item;
	TextView textView_peso_item;
	TextView textView_desc_item;
	TextView textView_medida_item;
	TextView dia_selecionado;
	TextView hora_selecionada;
	TextView ultima_glicemia;
	TextView ultima_glicemia_data;
	EditText editText_qtd;
	DatePicker datePicker;
	TimePicker timePicker;
	EditText pesquisa;
	EditText obs;
	RadioGroup radioGroup1;
	RadioGroup radioGroup2;
	RadioButton cafe;
	RadioButton almoco;
	RadioButton jantar;
	RadioButton lanche;
	CheckBox checkBox;
	CheckBox checkBoxNovo;
	Glicemia glicemia;
	Button sug_cancel;
	Button sug_nova;
	Button sug_usar;
	Button ins_cancel;
	Button ins_insere;
	Button ins_lembrete;
	Button recalcular;
	Dialog dialogGlicemia;
	public static Dialog dialogInsulina;
	EditText sug_insulina;
	EditText info1;
	TextView info2;
	TextView info3;
	TextView info4;
	TextView info5;
	ItensRefeicao item;
	Refeicao refeicao;
	TabelaDao alimentoDao;
	RefeicaoDao refeicaoDao;
	private Dialog dialog_qtd;
	private ProgressDialog alerta;
	private TabelaAdapter adapter;
	private TabelaAddAdapter adapterAdd;
	private Handler handler = new Handler();
	private Mensagem msg;
	private static final int DIALOG_QTD = 1;
	private static final int TABELA = 0;
	private Perfil perfil;
	private PerfilDao perfilDao;
	private Boolean editar = false;
	private int textWatcher = TABELA; // variavel para diferenciar o textwatcher
	RadioGroup radioGroup;
	RadioButton geral;
	RadioButton favorito;
	Spinner tipo_refeicao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refeicao_inserir);
		this.inicializarVariaveis();
		this.inicializarTelaDataHora();
		this.getExtra();
	}

	public void getExtra() {
		try {
			Intent i = getIntent();
			String x = i.getStringExtra("extra");
			if (x != null && x.equals("editar")) {
				this.refeicao = RefeicaoListActivity.refeicao;
				obs.setText(refeicao.getObs());
				textView_carb_total.setText(refeicao.getCarboidrato() + "g");
				textView_peso_total.setText(refeicao.getPeso() + "g");
				timePicker.setCurrentHour(refeicao.getData().get(Calendar.HOUR_OF_DAY)); // setando
																							// a
																							// hora
																							// ATUAL
																							// no
																							// formato
																							// 24h
																							// no
																							// timepick
				timePicker.setCurrentMinute(refeicao.getData().get(Calendar.MINUTE));
				datePicker.init(refeicao.getData().get(Calendar.YEAR), refeicao.getData().get(Calendar.MONTH),
				refeicao.getData().get(Calendar.DAY_OF_MONTH), this);			
				refeicaoDao = new RefeicaoDao(this);
				itens = this.getLista(itens);
				itens = refeicaoDao.getItens(refeicao.getId());
				adapterAdd = new TabelaAddAdapter(RefeicaoActivity.this, R.layout.tabela_add_linha, itens);
				listView_add.setAdapter(adapterAdd);
				editar = true;
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carregar informaões.");
		}

	}

	public void add(View view) { // ao clicar em adicionar carrega a tabela de
									// alimento
		this.alternarVisibilidade();
		if (alimentos == null) {
			alerta = ProgressDialog.show(this, "Carregando", "A lista está sendo carregada, aguarde..", false, true);
			this.ThreadCarregarLista(false);
		}
	}

	public void alternarVisibilidade() { // alterna entre a tabela e a lista add
		if (layout_add.getVisibility() == View.VISIBLE) {
			layout_add.setVisibility(View.GONE);
			//sugestao_layout.setVisibility(View.GONE);
			layout_tabela.setVisibility(View.VISIBLE);
		} else {
			layout_add.setVisibility(View.VISIBLE);
			//sugestao_layout.setVisibility(View.VISIBLE);
			layout_tabela.setVisibility(View.GONE);
		}
	}

	public void ThreadCarregarLista(final Boolean favorito) {
		new Thread() {
			@Override
			public void run() {
				Log.i("THREAD", "THREAD CHAMADA");
				try {
					alimentoDao = new TabelaDao(RefeicaoActivity.this);
					alimentos = alimentoDao.listar(favorito);
					atualizaTela();
				} catch (Exception e) {
					alerta.dismiss();
					msg = new Mensagem();
					msg.mensagemToast(RefeicaoActivity.this, "Erro ao carregar lista!");
				}
			}
		}.start();
	};

	public void atualizaTela() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					adapter = new TabelaAdapter(RefeicaoActivity.this, R.layout.tabela_linha, alimentos);
					listView_tabela.setAdapter(adapter);
					alerta.dismiss();
				} catch (Exception e) {
					alerta.dismiss();
					msg = new Mensagem();
					msg.mensagemToast(RefeicaoActivity.this, "Erro ao carregar lista!");
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { // ao
																					// clicar
																					// no
																					// item
																					// da
																					// tabela
																					// de
																					// alimentos
		if (arg0.equals(listView_tabela)) {
			item = new ItensRefeicao();
			item.setAlimento(adapter.getItemFilter(arg2));
			textView_carb_item.setText(item.getAlimento().getCarboidrato() + "g");
			textView_peso_item.setText(item.getAlimento().getPeso() + "g");
			textView_desc_item.setText(item.getAlimento().getDescricao() + "");
			textView_medida_item.setText(item.getAlimento().getMedida() + "");
			textWatcher = DIALOG_QTD;
			dialog_qtd.show();
		} else if (arg0.equals(listView_add)) {
			this.deletarItem(itens.get(arg2));
		}
	}

	public void deletarItem(final ItensRefeicao x) {
		AlertDialog.Builder msg = new AlertDialog.Builder(this);
		msg.setTitle("Excluir");
		msg.setMessage("Deseja excluir o item da refeição?");
		msg.setNeutralButton("Voltar", null);
		msg.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				itens.remove(x);
				adapterAdd = new TabelaAddAdapter(RefeicaoActivity.this, R.layout.tabela_add_linha, itens);
				listView_add.setAdapter(adapterAdd);
				double c = x.getQtd() * x.getAlimento().getCarboidrato();
				double c2 = refeicao.getCarboidrato();
				refeicao.setCarboidrato(c2 - c);
				double p = x.getQtd() * x.getAlimento().getPeso();
				double p2 = refeicao.getPeso();
				refeicao.setPeso(p2 - p);
				textView_carb_total.setText(Formatos.formataDouble(refeicao.getCarboidrato()) + "g");
				textView_peso_total.setText(Formatos.formataDouble(refeicao.getPeso()) + "g");
			}
		});
		msg.show();
	}

	public void sugestao(View view) {
		try {
			glicemia = new Glicemia();
			if (checkBoxNovo.isChecked() == true) {
				GlicemiaDao glicemiaDao = new GlicemiaDao(this);
				glicemia = glicemiaDao.getUltima();
				if (glicemia == null) {
					msg = new Mensagem();
					msg.mensagemToast(this, "Informe a glicemia para utilizar esta opção.");
					checkBoxNovo.setChecked(false);
				} else {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
					dialogGlicemia = new Dialog(this, R.style.tema_dialogo);
					dialogGlicemia.setCancelable(false);
					dialogGlicemia.setTitle("última glicemia");
					dialogGlicemia.setContentView(R.layout.dialog_sugestao_insulina_glic);
					ultima_glicemia = (TextView) dialogGlicemia.findViewById(R.id.ultima_glicemia);
					ultima_glicemia_data = (TextView) dialogGlicemia.findViewById(R.id.ultima_glicecmia_data);
					sug_cancel = (Button) dialogGlicemia.findViewById(R.id.sug_b_cancel);
					sug_nova = (Button) dialogGlicemia.findViewById(R.id.sug_b_nova);
					sug_usar = (Button) dialogGlicemia.findViewById(R.id.sug_b_usar);
					sug_cancel.setOnClickListener(this);
					sug_nova.setOnClickListener(this);
					sug_usar.setOnClickListener(this);
					ultima_glicemia.setText(glicemia.getMedida() + "mg/dL");
					ultima_glicemia_data.setText(
							glicemia.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
									+ ", " + format.format(glicemia.getData().getTime()));
					dialogGlicemia.show();

				}
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao sugerir insulina");
		}

	}

	// public void avancar(View view) {
	// layout_add.setVisibility(View.GONE);
	// sugestao_layout.setVisibility(View.GONE);
	// layout_data_hora.setVisibility(View.VISIBLE);
	// try {
	// if (checkBox.isChecked() == true) {
	// perfilDao = new PerfilDao(this);
	// perfil = new Perfil();
	// perfil = perfilDao.getPerfil();
	// dialogInsulina = new Dialog(this, R.style.tema_dialogo);
	// dialogInsulina.setCancelable(false);
	// dialogInsulina.setTitle("Sugestão de insulina");
	// dialogInsulina.setContentView(R.layout.dialog_sugestao_insulina);
	//// EDITEXT E TEXT do dialog
	// info1 = (EditText) dialogInsulina.findViewById(R.id.sug_insulina_info1);
	// //glicemia
	// sug_insulina = (EditText)
	// dialogInsulina.findViewById(R.id.sug_insulina_info_qtd); //sugestao de
	// insulina
	// info2 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info2);
	// info3 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info3);
	// info4 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info4);
	// info5 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info5);
	//// botoes do dialog
	// recalcular = (Button) dialogInsulina.findViewById(R.id.ins_b_recalcular);
	// recalcular.setOnClickListener(this);
	// ins_cancel = (Button) dialogInsulina.findViewById(R.id.ins_b_cancel);
	// ins_cancel.setOnClickListener(this);
	// ins_insere = (Button) dialogInsulina.findViewById(R.id.ins_b_insere);
	// ins_insere.setOnClickListener(this);
	// ins_lembrete = (Button) dialogInsulina.findViewById(R.id.ins_b_lembrar);
	// ins_lembrete.setOnClickListener(this);
	//// carrega variaveis
	// info1.setText("100");
	// info2.setText(glicemia.getMedida()+" mg/dL");
	// if (refeicao!=null) {
	// info3.setText(refeicao.getCarboidrato()+" g");
	// } else {
	// info3.setText("0 g");
	// }
	// info4.setText(Formatos.formataDoubleCasaDec(perfil.getFatorGlicemia()));
	// info5.setText(Formatos.formataDoubleCasaDec(perfil.getFatorCarboidrato()));
	// this.calcular();
	// dialogInsulina.show();
	// }
	// } catch (Exception e) {
	// msg = new Mensagem();
	// msg.mensagemToast(this, "Erro ao sugerir insulina");
	// }
	// }

	public void avancar(View view) {
		layout_add.setVisibility(View.GONE);
		//sugestao_layout.setVisibility(View.GONE);
		layout_data_hora.setVisibility(View.VISIBLE);

	}

	public void calcular() throws Exception {
		int glicAtual = glicemia.getMedida();
		int glicMeta = Integer.parseInt(info1.getText().toString());
		double insulinaGlic = 0;
		double insulinaCarb = 0;
		double insulinaTotal = 0;
		if (glicAtual > glicMeta) { // para baixar a glicemia
			// Acaba com a difereça entre a meta e a real
			double x = glicAtual - glicMeta;
			insulinaGlic = x / perfil.getFatorGlicemia(); // variravel da
															// insulina (QTD)
			if (refeicao != null) {
				// Acaba com o carboidrato
				double y = refeicao.getCarboidrato(); // total carb
				insulinaCarb = y / perfil.getFatorCarboidrato();
			}
			// ARREDONDAR
			// ????????????????????????????????????????????????????????????????????????????
			double soma = insulinaGlic + insulinaCarb;
			insulinaTotal = Formatos.formataUmaCasa(soma);
		} else if (glicAtual <= glicMeta) {
			// Fator de carb proporcional
			double glicFutura = 0;
			double baixarGLic = 0;
			if (refeicao != null && refeicao.getCarboidrato() >= 0) { // se carb
																		// for
																		// maior
																		// q
																		// zero
																		// possivel
																		// aumentar
																		// a
																		// glic
				glicFutura = glicAtual
						+ (refeicao.getCarboidrato() / perfil.getFatorCarboidrato()) * perfil.getFatorGlicemia();
				baixarGLic = glicFutura - glicMeta;
				if (baixarGLic >= 0) {
					insulinaGlic = baixarGLic / perfil.getFatorGlicemia();
				} else {
					insulinaGlic = 0;
					msg = new Mensagem();
					msg.mensagemToast(this, "Impossivel atingir a meta apenas com essa quantidade de carboidrato.");
				}
			} else {
				insulinaGlic = 0;
				msg = new Mensagem();
				msg.mensagemToast(this, "Impossivel atingir a meta apenas com essa quantidade de carboidrato.");
			}
			// ARREDONDAR
			// ????????????????????????????????????????????????????????????????????????????
			insulinaTotal = Formatos.formataUmaCasa(insulinaGlic);
		} else {
			msg = new Mensagem();
			msg.mensagemToast(this, "Informe um valor válido.");
		}
		sug_insulina.setText(insulinaTotal + "");
	}

	public void voltarDataHora(View view) {
		layout_data_hora.setVisibility(View.GONE);
		layout_add.setVisibility(View.VISIBLE);
		//sugestao_layout.setVisibility(View.VISIBLE);
	}

	public void salvarRefeicao(View view) {

		if (itens == null || itens.size() == 0) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Adicione pelo menos um alimento na refeição.");
		} else {

			Calendar c = this.setData(timePicker, datePicker);
			refeicao.setData(c);
			refeicao.setObs(obs.getText().toString());
			refeicao.setTipo(tipo_refeicao.getSelectedItem().toString());
			
			refeicaoDao = new RefeicaoDao(this);
			refeicaoDao.inserir(refeicao, itens);
			
			new SyncRESTBo().insertRefeicaoREST(refeicao, this, null);

			if (editar == true) {
				setResult(0);
				finish();
				return;
			}

			finish();
		}
	}

	public <T> List<T> getLista(List<T> lista) { // Se a lista for null retorna
													// ele inicializada
		if (lista == null) {
			return new ArrayList<T>();
		}
		return lista;
	}

	public void insereItem() { // insere item selecionado na tabela da lista de
								// itens
		refeicao = Refeicao.getRefeicao(refeicao);
		itens = getLista(itens);
		// add item na lista
		String v = editText_qtd.getText().toString();
		v.replace(",", ".");
		item.setQtd(Double.parseDouble(v));
		itens.add(item);
		// seta os valores da refeição
		double c = item.getQtd() * item.getAlimento().getCarboidrato();
		double c2 = refeicao.getCarboidrato();
		refeicao.setCarboidrato(c + c2);
		double p = item.getQtd() * item.getAlimento().getPeso();
		double p2 = refeicao.getPeso();
		refeicao.setPeso(p + p2);
		refeicao.getTipo();
		//refeicao.setTipo(RefeicaoTipos.CAFE); // caso naos eja alterado
		// atualiza a tela
		dialog_qtd.dismiss();
		adapterAdd = new TabelaAddAdapter(RefeicaoActivity.this, R.layout.tabela_add_linha, itens);
		listView_add.setAdapter(adapterAdd);
		this.atualizaTotais();
		this.alternarVisibilidade();
	}

	public void atualizaTotais() {
		textView_carb_total.setText(Formatos.formataDouble(refeicao.getCarboidrato()) + "g");
		textView_peso_total.setText(Formatos.formataDouble(refeicao.getPeso()) + "g");
		editText_qtd.setText("");
	}

	public void inicializarVariaveis() { // inicializa os totais
		layout_add = (RelativeLayout) findViewById(R.id.layout_add);
		layout_tabela = (RelativeLayout) findViewById(R.id.layout_tabela);
		layout_data_hora = (RelativeLayout) findViewById(R.id.layout_data_hora);
		//sugestao_layout = (LinearLayout) findViewById(R.id.checkBox_sugestao2);
		listView_add = (ListView) findViewById(R.id.lista_add);
		listView_add.setOnItemClickListener(this);
		listView_tabela = (ListView) findViewById(R.id.lista_tabela);
		textView_carb_total = (TextView) findViewById(R.id.textView_total_carb);
		textView_peso_total = (TextView) findViewById(R.id.textView_total_peso);
		dialog_qtd = new Dialog(this, R.style.tema_dialogo);
		dialog_qtd.setContentView(R.layout.dialog_refeicao_qtd);
		dialog_qtd.setCancelable(false);
		textView_carb_item = (TextView) dialog_qtd.findViewById(R.id.textView_carb_item);
		textView_peso_item = (TextView) dialog_qtd.findViewById(R.id.textView_peso_item);
		textView_desc_item = (TextView) dialog_qtd.findViewById(R.id.textView_desc_item);
		textView_medida_item = (TextView) dialog_qtd.findViewById(R.id.textView_medida_item);
		dialog_qtd.setTitle("Quantidade de Medidas");
		editText_qtd = (EditText) dialog_qtd.findViewById(R.id.editText_qtd);
		editText_qtd.addTextChangedListener(this);
		b_qtd_cancel = (Button) dialog_qtd.findViewById(R.id.b_cancel);
		b_qtd_ok = (Button) dialog_qtd.findViewById(R.id.b_ok);
		b_qtd_cancel.setOnClickListener(this);
		b_qtd_ok.setOnClickListener(this);
		listView_add.setOnItemClickListener(this);
		listView_tabela.setOnItemClickListener(this);
		pesquisa = (EditText) findViewById(R.id.filtro_tabela_add);
		pesquisa.addTextChangedListener(this);
		obs = (EditText) findViewById(R.id.ref_obs);
		textView_carb_total.setText("0g");
		textView_peso_total.setText("0g");
//		radioGroup1 = (RadioGroup) findViewById(R.id.ref_radioGroup1);
//		radioGroup1.setOnCheckedChangeListener(this);
//		radioGroup2 = (RadioGroup) findViewById(R.id.ref_radioGroup2);
//		radioGroup2.setOnCheckedChangeListener(this);
//		cafe = (RadioButton) findViewById(R.id.ref_cafe);
//		almoco = (RadioButton) findViewById(R.id.ref_almoco);
//		jantar = (RadioButton) findViewById(R.id.ref_janta);
//		lanche = (RadioButton) findViewById(R.id.ref_lanche);
		//checkBox = (CheckBox) findViewById(R.id.checkBox_sugestao2);
		checkBoxNovo = (CheckBox) findViewById(R.id.checkBox_sugestao2);
//		cafe.setText(RefeicaoTipos.getNome(this, RefeicaoTipos.CAFE.getCod()));
//		almoco.setText(RefeicaoTipos.getNome(this, RefeicaoTipos.ALMOCO.getCod()));
//		jantar.setText(RefeicaoTipos.getNome(this, RefeicaoTipos.JANTA.getCod()));
//		lanche.setText(RefeicaoTipos.getNome(this, RefeicaoTipos.LANCHE.getCod()));
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		geral = (RadioButton) findViewById(R.id.tabela_normal);
		favorito = (RadioButton) findViewById(R.id.tabela_favorito);
		ArrayAdapter<CharSequence> adapter_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_refeicao, R.layout.spinner_item);
		tipo_refeicao = (Spinner) findViewById(R.id.tipo_refeicao_spinner);
		tipo_refeicao.setAdapter(adapter_tipo);
	}

	public void sugestaoCheckBox(View view) {
		try {
			glicemia = new Glicemia();
			if (checkBoxNovo.isChecked() == true) {
				GlicemiaDao glicemiaDao = new GlicemiaDao(this);
				glicemia = glicemiaDao.getUltima();
				if (glicemia == null) {
					msg = new Mensagem();
					msg.mensagemToast(this, "Informe a glicemia para utilizar esta opção.");
					checkBoxNovo.setChecked(false);
				} else {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
					dialogGlicemia = new Dialog(this, R.style.tema_dialogo);
					dialogGlicemia.setCancelable(false);
					dialogGlicemia.setTitle("última glicemia");
					dialogGlicemia.setContentView(R.layout.dialog_sugestao_insulina_glic);
					ultima_glicemia = (TextView) dialogGlicemia.findViewById(R.id.ultima_glicemia);
					ultima_glicemia_data = (TextView) dialogGlicemia.findViewById(R.id.ultima_glicecmia_data);
					sug_cancel = (Button) dialogGlicemia.findViewById(R.id.sug_b_cancel);
					sug_nova = (Button) dialogGlicemia.findViewById(R.id.sug_b_nova);
					sug_usar = (Button) dialogGlicemia.findViewById(R.id.sug_b_usar);
					sug_cancel.setOnClickListener(this);
					sug_nova.setOnClickListener(this);
					sug_usar.setOnClickListener(this);
					ultima_glicemia.setText(glicemia.getMedida() + "mg/dL");
					ultima_glicemia_data.setText(
							glicemia.getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
									+ ", " + format.format(glicemia.getData().getTime()));
					dialogGlicemia.show();
				}
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao sugerir insulina");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == b_qtd_ok) {
			if (editText_qtd.getText().toString().equals("") || editText_qtd.getText().toString().equals("0")) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Informe a quantidade de medidas.");
			} else {
				this.insereItem();
				textWatcher = TABELA; // volta o textwatcher para a pesquisa da
										// tabela de alimentos
			}
		} else if (v == b_qtd_cancel) {
			dialog_qtd.dismiss();
			textWatcher = TABELA; // volta o textwatcher para a pesquisa da
									// tabela de alimentos
		} else if (v == sug_cancel) {
			glicemia = null;
			checkBoxNovo.setChecked(false);
			dialogGlicemia.dismiss();
		} else if (v == sug_nova) {
			Intent i = new Intent(this, GlicemiaActivity.class);
			i.putExtra("extra", "sugestao");
			startActivityForResult(i, 0);
		} else if (v == sug_usar) {
			dialogGlicemia.dismiss();
			msg = new Mensagem();
			msg.mensagemToast(this,
					"Para a sugestão de insulina será usada a glicemia: " + glicemia.getMedida() + "md/dL");
			try {
				if (checkBoxNovo.isChecked() == true) {
					perfilDao = new PerfilDao(this);
					perfil = new Perfil();
					perfil = perfilDao.getPerfil();
					dialogInsulina = new Dialog(this, R.style.tema_dialogo);
					dialogInsulina.setCancelable(false);
					dialogInsulina.setTitle("Sugestão de insulina");
					dialogInsulina.setContentView(R.layout.dialog_sugestao_insulina);
					// EDITEXT E TEXT do dialog
					info1 = (EditText) dialogInsulina.findViewById(R.id.sug_insulina_info1); // glicemia
					sug_insulina = (EditText) dialogInsulina.findViewById(R.id.sug_insulina_info_qtd); // sugestao
																										// de
																										// insulina
					info2 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info2);
					info3 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info3);
					info4 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info4);
					info5 = (TextView) dialogInsulina.findViewById(R.id.sug_insulina_info5);
					// botoes do dialog
					recalcular = (Button) dialogInsulina.findViewById(R.id.ins_b_recalcular);
					recalcular.setOnClickListener(this);
					ins_cancel = (Button) dialogInsulina.findViewById(R.id.ins_b_cancel);
					ins_cancel.setOnClickListener(this);
					ins_insere = (Button) dialogInsulina.findViewById(R.id.ins_b_insere);
					ins_insere.setOnClickListener(this);
					ins_lembrete = (Button) dialogInsulina.findViewById(R.id.ins_b_lembrar);
					ins_lembrete.setOnClickListener(this);
					// carrega variaveis
					info1.setText("100");
					info2.setText(glicemia.getMedida() + " mg/dL");
					if (refeicao != null) {
						info3.setText(refeicao.getCarboidrato() + " g");
					} else {
						info3.setText("0 g");
					}
					info4.setText(Formatos.formataDoubleCasaDec(perfil.getFatorGlicemia()));
					info5.setText(Formatos.formataDoubleCasaDec(perfil.getFatorCarboidrato()));
					this.calcular();
					dialogInsulina.show();
				}
			} catch (Exception e) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Erro ao sugerir insulina");
			}
		} else if (v == ins_cancel) {
			dialogInsulina.dismiss();
			glicemia = null;
			checkBoxNovo.setChecked(false);
			msg = new Mensagem();
			msg.mensagemToast(this, "A sugestão de insulina foi desativada.");
		} else if (v == ins_insere) {
			String y = sug_insulina.getText().toString();
			double x = Double.parseDouble(y);
			Intent i = new Intent(this, InsulinaActivity.class);
			i.putExtra("insere", x);
			startActivityForResult(i, 1);
		} else if (v == ins_lembrete) {
			DialogLembretes lembretes = new DialogLembretes(this);
			String y = sug_insulina.getText().toString();
			double x = Double.parseDouble(y);
			lembretes.lembrar(x);
		} else if (v == recalcular) {
			try {
				this.calcular();
			} catch (Exception e) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Erro ao recalcular.");
				dialogInsulina.dismiss();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (layout_tabela.getVisibility() == View.VISIBLE) {
			this.alternarVisibilidade();
		} else if (layout_data_hora.getVisibility() == View.VISIBLE) {
			this.voltarDataHora(null);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		switch (textWatcher) {
		case DIALOG_QTD:
			String v = editText_qtd.getText().toString();
			if (v.equals("")) {
				v = "1";
			}
			v.replace(",", ".");
			double x = Double.parseDouble(v) * item.getAlimento().getCarboidrato();
			double z = Double.parseDouble(v) * item.getAlimento().getPeso();
			textView_carb_item.setText(Formatos.formataDouble(x) + "g");
			textView_peso_item.setText(Formatos.formataDouble(z) + "g");
			break;
		case TABELA:
			adapter.getFilter().filter(s.toString());
			break;
		}
	}

	public void inicializarTelaDataHora() {
		Calendar c = Calendar.getInstance();
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		timePicker = (TimePicker) findViewById(R.id.timePicker_refeicao);
		datePicker = (DatePicker) findViewById(R.id.datePicker_refeicao);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		datePicker.init(year, month, dayOfMonth, this);
		timePicker.setOnTimeChangedListener(this);
		dia_selecionado = (TextView) findViewById(R.id.tipo_glicemia);
		hora_selecionada = (TextView) findViewById(R.id.hora_selecionada);
		dia_selecionado.setText("Dia - " + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
				+ ", " + dayOfMonth + " de " + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
				+ " de " + year + "");
		hora_selecionada.setText("Hora - " + hourOfDay + " horas e " + minute + " minutos");
	}

	public Calendar setData(TimePicker timePicker, DatePicker datePicker) { // Recebe
																			// os
																			// datapiker
																			// e
																			// o
																			// timepicker
																			// e
																			// devolve
																			// em
																			// milisegundos.
		Calendar x = Calendar.getInstance();
		try {
			int ano = datePicker.getYear();
			int mes = datePicker.getMonth();
			int dia = datePicker.getDayOfMonth();
			int hora = timePicker.getCurrentHour();
			int min = timePicker.getCurrentMinute();
			x.set(ano, mes, dia, hora, min);
		} catch (Exception e) {
			Log.e("GLICEMIA ACTIVITY", e.getMessage());
		}
		return x;
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		hora_selecionada.setText("Hora - " + hourOfDay + " horas e " + minute + " minutos");
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = this.setData(timePicker, datePicker);
		dia_selecionado.setText("Dia - " + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
				+ ", " + dayOfMonth + " de " + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
				+ " de " + year + "");
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		alimentos = new ArrayList<Alimento>();
		switch (checkedId) {
		case R.id.tabela_normal:
			alerta = ProgressDialog.show(this, "Atualizando", "A lista está sendo atualizada, aguarde..",false,true);
			ThreadCarregarLista(false);
			Toast.makeText(getApplicationContext(), "Teste", Toast.LENGTH_SHORT);
			break;
		case R.id.tabela_favorito:
			alerta = ProgressDialog.show(this, "Atualizando", "A lista está sendo atualizada, aguarde..",false,true);
			ThreadCarregarLista(true);
			break;
		}		
	}
	


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			dialogGlicemia.dismiss();
			this.sugestaoCheckBox(null);
		} else if (requestCode == 1) {
			if (resultCode == 1) {
				dialogInsulina.dismiss();
				msg = new Mensagem();
				msg.mensagemToast(this, "Insulina inserida.");
			} else if (resultCode == 2) {
				msg = new Mensagem();
				msg.mensagemToast(this, "A insulina não foi inserida.");
			}
		}
	}

}
