package br.com.diabetesvirtual.listactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.adapter.HistoricoAdapter;
import br.com.diabetesvirtual.dao.ExerciciosDao;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.dao.RefeicaoDao;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.model.Historico;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.pdf.HistoricoPdf;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;

public class HistoricoTipoListActivity extends ActivityBase{
	Mensagem msg;
	Handler handler = new Handler();
	public static List<Glicemia> lista_glicemia;
	public static List<Insulina> lista_insulina;
	public static List<Refeicao> lista_refeicao;
	public static List<Exercicios> lista_exercicios;
	 
	Historico historico;
	HistoricoAdapter historicoAdapter;
	List<Historico> lista_historico_ord;
	List<Historico> lista_historico1;
	List<Historico> lista_historico2;
	
	GlicemiaDao glicemiaDao;
	InsulinaDao insulinaDao;
	RefeicaoDao refeicaoDao;
	ExerciciosDao exerciciosDao;
	Glicemia glicemia;
	Insulina insulina;
	Refeicao refeicao;
	Exercicios exercicio;
	ProgressDialog alerta;	
	
	ListView listView;
	CheckBox glic;
	CheckBox ref;
	CheckBox exe;
	CheckBox insul;
	
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historico_relatorio_tipo);
		listView = (ListView) findViewById(R.id.historico_lista_tipo);
		glic = (CheckBox) findViewById(R.id.hist_glic);
		ref = (CheckBox) findViewById(R.id.hist_ref);
		exe = (CheckBox) findViewById(R.id.hist_exe);
		insul = (CheckBox) findViewById(R.id.hist_insulina);
		glicemiaDao =  new GlicemiaDao(this);
		insulinaDao = new InsulinaDao(this);
		refeicaoDao = new RefeicaoDao(this);
		exerciciosDao = new ExerciciosDao(this);	
	}
	
	public void voltar(View view) {
		finish();
		return;
	}
	
	public void gerar(View view) {
		alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
		this.filtrar();
	}
	
	public void ThreadCarregarLista() {
		new Thread() {
			@Override
			public void run() {
				int g=0;
				int i=0;	
				int e=0;
				int r=0;
				int h1=0;
				int h2=0;
				try {
//WHILE QUE ORDENA INSULINA E GLICEMIA					
					while (lista_glicemia!=null && g < lista_glicemia.size() && lista_insulina!=null && i<lista_insulina.size()) {
						if (lista_glicemia.get(g).getData().getTimeInMillis() > lista_insulina.get(i).getData().getTimeInMillis()) {
							glicemia = new Glicemia();
							historico = new Historico();
							glicemia = lista_glicemia.get(g);
							historico.setId(glicemia.getId());
							historico.setDado(glicemia.getMedida()+" mg/dL");
							historico.setData(glicemia.getData().getTimeInMillis());
							historico.setObs("Observação: "+glicemia.getObs()+"\n"+"Tipo: "+glicemia.getTipo());
							historico.setTipo("GLICEMIA");
							lista_historico1.add(historico);
							g++;
						} else {
							insulina = new Insulina();
							historico = new Historico();
							insulina = lista_insulina.get(i);
							historico.setId(insulina.getId());
							historico.setDado(insulina.getQtd()+" UI");
							historico.setData(insulina.getData().getTimeInMillis());
							historico.setObs("Tipo: "+insulina.getTipo()+"\n"+"Observação: "+insulina.getObs());
							historico.setTipo("INSULINA");
							lista_historico1.add(historico);
							i++;
						}						
					}
					if ((lista_glicemia==null || g==lista_glicemia.size()) && lista_insulina!=null) {
						for (int j = i; j < lista_insulina.size(); j++) {
							insulina = new Insulina();
							historico = new Historico();
							insulina = lista_insulina.get(j);
							historico.setId(insulina.getId());
							historico.setDado(insulina.getQtd()+" UI");
							historico.setData(insulina.getData().getTimeInMillis());
							historico.setObs("Tipo: "+insulina.getTipo()+"\n"+"Observação: "+insulina.getObs());
							historico.setTipo("INSULINA");
							lista_historico1.add(historico);
						}
					 } else if ((lista_insulina ==null || i==lista_insulina.size()) && lista_glicemia!=null) {
						for (int j = g; j < lista_glicemia.size(); j++) {
							glicemia = new Glicemia();
							historico = new Historico();
							glicemia = lista_glicemia.get(j);
							historico.setId(glicemia.getId());
							historico.setDado(glicemia.getMedida()+" mg/dL");
							historico.setData(glicemia.getData().getTimeInMillis());
							historico.setObs("Observação: "+glicemia.getObs()+"\n"+"Tipo: "+glicemia.getTipo());
							historico.setTipo("GLICEMIA");
							lista_historico1.add(historico);
						}
					}
//WHILE QUE ORDENA EXERCICIO E REFEICAO		
					while (lista_exercicios!=null && e < lista_exercicios.size() && lista_refeicao!=null && r<lista_refeicao.size()) {
						if (lista_exercicios.get(e).getData().getTimeInMillis() > lista_refeicao.get(r).getData().getTimeInMillis()) {
							exercicio = new Exercicios();
							historico = new Historico();
							exercicio = lista_exercicios.get(e);
							historico.setId(exercicio.getId());
							historico.setDado(exercicio.getDescricao());
							historico.setData(exercicio.getData().getTimeInMillis());
							historico.setObs("Duração: "+exercicio.getDuracao()+" min"
									+"\n"+"Tipo: "+exercicio.getTipo()
									+"\n"+"Modalidade: "+exercicio.getModalidade()
									+"\n"+"Intensidade: "+exercicio.getIntensidade());
									//+"\n"+"Intensidade: "+ExerciciosIntensidade.getNome(HistoricoTipoListActivity.this , exercicio.getIntensidade().getCod()));
							historico.setTipo("ATIV. FÍSICA");
							lista_historico2.add(historico);
							e++;
						} else {
							refeicao = new Refeicao();
							historico = new Historico();
							refeicao = lista_refeicao.get(r);
							historico.setId(refeicao.getId());
							historico.setDado(Formatos.formataDouble(refeicao.getCarboidrato())+"g (CHO)");
							historico.setData(refeicao.getData().getTimeInMillis());
							historico.setObs("Tipo" + refeicao.getTipo()
							//historico.setObs("Tipo: "+RefeicaoTipos.getNome(HistoricoTipoListActivity.this, refeicao.getTipo().getCod())
									+"\n"+"Peso: "+Formatos.formataDouble(refeicao.getPeso())+"g"+"\n"+"Observação: "+refeicao.getObs());
							historico.setTipo("REFEIÇÃO");
							lista_historico2.add(historico);
							r++;
						}						
					}
					if ((lista_exercicios==null || e==lista_exercicios.size()) && lista_refeicao!=null) {
						for (int j = r; j < lista_refeicao.size(); j++) {
							refeicao = new Refeicao();
							historico = new Historico();
							refeicao = lista_refeicao.get(j);
							historico.setId(refeicao.getId());
							historico.setDado(Formatos.formataDouble(refeicao.getCarboidrato())+"g (CHO)");
							historico.setData(refeicao.getData().getTimeInMillis());
							historico.setObs("Tipo" + refeicao.getTipo()
							//historico.setObs("Tipo: "+RefeicaoTipos.getNome(HistoricoTipoListActivity.this, refeicao.getTipo().getCod())
									+"\n"+"Peso: "+Formatos.formataDouble(refeicao.getPeso())+"g"+"\n"+"Observação: "+refeicao.getObs());
							historico.setTipo("REFEIÇÃO");
							lista_historico2.add(historico);
						}
					 } else if ((lista_refeicao==null || r==lista_refeicao.size()) && lista_exercicios!=null) {
						for (int j = e; j < lista_exercicios.size(); j++) {
							exercicio = new Exercicios();
							historico = new Historico();
							exercicio = lista_exercicios.get(j);
							historico.setId(exercicio.getId());
							historico.setDado(exercicio.getDescricao());
							historico.setData(exercicio.getData().getTimeInMillis());
							historico.setObs("Duração: "+exercicio.getDuracao()+" min"
									+"\n"+"Tipo: "+exercicio.getTipo()
									+"\n"+"Modalidade: "+exercicio.getModalidade()
									+"\n"+"Intensidade: "+exercicio.getIntensidade());
									//+"\n"+"Intensidade: "+ExerciciosIntensidade.getNome(HistoricoTipoListActivity.this , exercicio.getIntensidade().getCod()));
							historico.setTipo("ATIV. FÍSICA");
							lista_historico2.add(historico);
						}
					}
//WHILE QUE ORDENA OS DOIS HISTORICOS
					while (h1 < lista_historico1.size() && h2 < lista_historico2.size()) {
						if (lista_historico1.get(h1).getData() > lista_historico2.get(h2).getData()) {
							historico = new Historico();
							historico = lista_historico1.get(h1);
							lista_historico_ord.add(historico);
							h1++;
						} else {
							historico = new Historico();
							historico = lista_historico2.get(h2);
							lista_historico_ord.add(historico);
							h2++;
						}						
					}
					if (h1 == lista_historico1.size() && lista_historico2.size() > 0) {
						for (int j = h2; j < lista_historico2.size(); j++) {
							lista_historico_ord.add(lista_historico2.get(j));
						}
					 } else if (h2 == lista_historico2.size() && lista_historico1.size()  >0) {
						for (int j = h1; j < lista_historico1.size(); j++) {
							lista_historico_ord.add(lista_historico1.get(j));
						}
					}
					atualizaTela();
				} catch (Exception x) {
					erro();
				}
			}
		}.start();
	};
	
	public void atualizaTela() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				historicoAdapter = new HistoricoAdapter(HistoricoTipoListActivity.this, R.layout.historico_item_da_lista, lista_historico_ord);
				try{
					if (lista_historico_ord==null || lista_historico_ord.size()==0) {
						msg = new Mensagem();
						msg.mensagemToast(HistoricoTipoListActivity.this, "Nenhum dado encontrado.");
					} 
					listView.setAdapter(historicoAdapter);	
					alerta.dismiss();
				} catch (Exception e) {
					alerta.dismiss();
					msg = new Mensagem();
					msg.mensagemToast(HistoricoTipoListActivity.this, "ERRO AO GERAR HISTÓRICO.");
				}							
			}
		});
	}
	
	public void erro() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				try{
					alerta.dismiss();
					msg = new Mensagem();
					msg.mensagemToast(HistoricoTipoListActivity.this, "ERRO AO GERAR HISTÓRICO.");
				} catch (Exception e) {
					//tratar o erro
				}							
			}
		});
	}

	public void filtrar() {
		lista_glicemia=null;
		lista_exercicios=null;
		lista_insulina=null;
		lista_refeicao=null;
		lista_historico1 = new ArrayList<Historico>();
		lista_historico2 = new ArrayList<Historico>();;
		lista_historico_ord= new ArrayList<Historico>();
		try {		
			if (glic.isChecked()) {
				glicemiaDao = new GlicemiaDao(this);
				lista_glicemia = glicemiaDao.getAll();
			}
			if (exe.isChecked()) {
				exerciciosDao = new ExerciciosDao(this);
				lista_exercicios = exerciciosDao.getAll();
			}
			if (ref.isChecked()) {
				refeicaoDao = new RefeicaoDao(this);
				lista_refeicao = refeicaoDao.getAll();
			}
			if (insul.isChecked()) {
				insulinaDao = new InsulinaDao(this);
				lista_insulina = insulinaDao.getAll();
			}	
			this.ThreadCarregarLista();
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao carregar dados");
		}	
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
				HistoricoPdf pdf = new HistoricoPdf(this,"Relatório Histórico Avançado",lista_historico_ord);
				pdf.gerarPdf();		
				break;
		default:
			break;
		}
		return true;
	}
}
