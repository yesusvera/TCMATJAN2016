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

public class HistoricoListActivity extends ActivityBase {
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
	private ProgressDialog alerta;	
	ListView listView;
	
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historico_relatorio_geral);
		listView = (ListView) findViewById(R.id.historico_lista);
		//listView.setOnItemClickListener(this);
		glicemiaDao =  new GlicemiaDao(this);
		insulinaDao = new InsulinaDao(this);
		refeicaoDao = new RefeicaoDao(this);
		exerciciosDao = new ExerciciosDao(this);
		lista_historico_ord = new ArrayList<Historico>();
		lista_historico1 = new ArrayList<Historico>();
		lista_historico2 = new ArrayList<Historico>();
		alerta = ProgressDialog.show(this, "Carregando..", "Carregando lista, aguarde..",false,true);
		this.ThreadCarregarLista();
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
					lista_exercicios = exerciciosDao.getAll();
					lista_glicemia = glicemiaDao.getAll();
					lista_insulina = insulinaDao.getAll();
					lista_refeicao = refeicaoDao.getAll();
//WHILE QUE ORDENA INSULINA E GLICEMIA					
					while (lista_glicemia!=null && g < lista_glicemia.size() && lista_insulina!=null && i<lista_insulina.size()) {
						if (lista_glicemia.get(g).getData().getTimeInMillis() > lista_insulina.get(i).getData().getTimeInMillis()) {
							glicemia = new Glicemia();
							historico = new Historico();
							glicemia = lista_glicemia.get(g);
							historico.setId(glicemia.getId());
							historico.setDado(glicemia.getMedida()+" mg/dL");
							historico.setData(glicemia.getData().getTimeInMillis());
							historico.setObs("Observação: "+"\n"+glicemia.getObs());
							historico.setTipo("GLICEMIA");
							lista_historico1.add(historico);
							g++;
						} else {
							insulina = new Insulina();
							historico = new Historico();
							insulina = lista_insulina.get(i);
							historico.setId(insulina.getId());
							historico.setDado(Formatos.formataUmaCasa(insulina.getQtd())+" UI");
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
							historico.setDado(Formatos.formataUmaCasa(insulina.getQtd())+" UI");
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
									//+"\n"+"Intensidade: "+ExerciciosIntensidade.getNome(HistoricoListActivity.this , exercicio.getIntensidade().getCod()));
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

							//historico.setObs("Tipo: "+RefeicaoTipos.getNome(HistoricoListActivity.this, refeicao.getTipo().getCod())
							historico.setObs("Tipo: "+refeicao.getTipo()
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
							//historico.setObs("Tipo: "+RefeicaoTipos.getNome(HistoricoListActivity.this, refeicao.getTipo().getCod())
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
									//+"\n"+"Intensidade: "+ExerciciosIntensidade.getNome(HistoricoListActivity.this , exercicio.getIntensidade().getCod()));
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
				try{
					if (lista_historico_ord==null || lista_historico_ord.size()==0) {
						alerta.dismiss();
						msg = new Mensagem();
						msg.mensagemToast(HistoricoListActivity.this, "Nenhum dado encontrado.");
					} else {
						historicoAdapter = new HistoricoAdapter(HistoricoListActivity.this, R.layout.historico_item_da_lista, lista_historico_ord);
						listView.setAdapter(historicoAdapter);					
						alerta.dismiss();
					}
				} catch (Exception e) {
					alerta.dismiss();
					msg = new Mensagem();
					msg.mensagemToast(HistoricoListActivity.this, "ERRO AO GERAR HISTÓRICO.");
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
					msg.mensagemToast(HistoricoListActivity.this, "ERRO AO GERAR HISTÓRICO.");
				} catch (Exception e) {
					//tratar o erro
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
				HistoricoPdf pdf = new HistoricoPdf(this,"Relatório Histórico Geral",lista_historico_ord);
				pdf.gerarPdf();		
				break;
		default:
			break;
		}
		return true;
	}

	/*@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			LinearLayout tabela = (TableLayout) arg1.findViewById(R.id.historico_cor_item);	
			tabela.setBackgroundResource(R.color.laranja);
			historico = new Historico();
			historico = lista_historico_ord.get(arg2);
			if (historico.getTipo().equals("ATIV. FÍSICA")) {
				exerciciosDao = new ExerciciosDao(this);
				exercicio =  exerciciosDao.getByID(historico.getId());				
				DialogDetalhes dialog = new DialogDetalhes(this);
				dialog.DialogExercicios(exercicio);
			}
 		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "ERRO ao listar detalhes.");
		}
		
	}*/
	

}
