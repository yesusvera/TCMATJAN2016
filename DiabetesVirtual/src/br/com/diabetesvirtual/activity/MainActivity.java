package br.com.diabetesvirtual.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.dao.MetasDao;
import br.com.diabetesvirtual.dao.RefeicaoDao;
import br.com.diabetesvirtual.listactivity.ActivityBase;
import br.com.diabetesvirtual.listactivity.TabelaListActivity;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.InsulinaTipos;
import br.com.diabetesvirtual.model.Metas;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;


public class MainActivity extends ActivityBase implements OnCheckedChangeListener{	
	TextView meta_glic;
	TextView meta_insulina;
	TextView meta_carb;
	double percentual_glic = 0;
	double percentual_insulina = 0;
	double percentual_carb = 0;
	GlicemiaDao glicemiaDao;
	InsulinaDao insulinaDao;
	RefeicaoDao refeicaoDao;
	MetasDao metasDao;
	List<Glicemia> lista_glic;
	List<Insulina> lista_insulina;
	List<Insulina> lista_insulina_geral;
	List<Glicemia> lista_glicemia_geral;
	List<Refeicao> lista_carb;
	Metas metas;
	TextView glic_sucesso;
	TextView insulina_sucesso;
	TextView ref_sucesso;
	TextView ultima_glic;
	TextView ultima_ref;
	TextView ultima_insulina;
	TextView data_glic;
	TextView data_ref;
	TextView data_insulina;
	TextView tipo_ultima_insulina;
	TextView tipo_ultima_glicemia;
	TextView tipo_ultima_refeicao;
	RadioGroup radioGroup;
	RadioButton geral;
	RadioButton mensal;
	RadioButton semanal;
	RadioButton diaria;
	Handler handler = new Handler();
	Calendar fim = Calendar.getInstance();	
	SimpleDateFormat format_data = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
	Mensagem msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		fim.set(Calendar.HOUR_OF_DAY, 23);
		fim.set(Calendar.MINUTE, 59);
		meta_glic = (TextView) findViewById(R.id.meta_glicemica);
		meta_insulina = (TextView) findViewById(R.id.meta_insulina);
		meta_carb = (TextView) findViewById(R.id.meta_carb);
		ultima_glic = (TextView) findViewById(R.id.ultima_glic);
		ultima_insulina = (TextView) findViewById(R.id.ultima_insulina);
		ultima_ref = (TextView) findViewById(R.id.ultima_ref);
		data_glic = (TextView) findViewById(R.id.data_ultima_glic);
		data_insulina = (TextView) findViewById(R.id.data_ultima_insulina);
		data_ref = (TextView) findViewById(R.id.data_ultima_ref);		
		glic_sucesso = (TextView) findViewById(R.id.glic_sucesso);
		ref_sucesso = (TextView) findViewById(R.id.ref_sucesso);
		tipo_ultima_insulina = (TextView) findViewById(R.id.tipo_ultima_insulina);
		tipo_ultima_glicemia = (TextView) findViewById(R.id.tipo_ultima_glicemia);
		tipo_ultima_refeicao = (TextView) findViewById(R.id.tipo_ultima_refeicao);
		insulina_sucesso = (TextView) findViewById(R.id.insulina_sucesso);		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup_meta);
		geral = (RadioButton) findViewById(R.id.meta_geral);
		mensal = (RadioButton) findViewById(R.id.meta_mensal);
		semanal = (RadioButton) findViewById(R.id.meta_semanal);
		diaria = (RadioButton) findViewById(R.id.meta_diaria);
		radioGroup.setOnCheckedChangeListener(this);
		geral.setText(getResources().getString(R.string.metaGeral));
		mensal.setText(getResources().getString(R.string.metaMensal));
		semanal.setText(getResources().getString(R.string.metaSemanal));
		diaria.setText(getResources().getString(R.string.metaDiaria));
	} 
	
	public boolean verificaMetas() {
		try {
			metasDao = new MetasDao(this);
			metas = metasDao.getMetas();
			if (metas.getId()==0) {
				return false;		
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao recuperar metas.");
		}

		return true;
	}
		
	public void carregarLoad(int ultimo) {
		String load = "Carregando..";		
		if (ultimo==1) {
			ultima_glic.setText(load);
			ultima_ref.setText(load);
			ultima_insulina.setText(load);
			data_glic.setText(load);
			data_ref.setText(load);
			data_insulina.setText(load);
			tipo_ultima_insulina.setText("");
			//styles
			ultima_glic.setTextAppearance(this, R.style.fonte_metas_load);
			ultima_insulina.setTextAppearance(this, R.style.fonte_metas_load);
			ultima_ref.setTextAppearance(this, R.style.fonte_metas_load);
		}
		glic_sucesso.setText(load);
		insulina_sucesso.setText(load);
		ref_sucesso.setText(load);
		meta_glic.setText(load);
		meta_insulina.setText(load);
		meta_carb.setText(load);
		//styles
		meta_glic.setTextAppearance(this, R.style.fonte_metas_load);
		meta_insulina.setTextAppearance(this, R.style.fonte_metas_load);
		meta_carb.setTextAppearance(this, R.style.fonte_metas_load);
	}
	
	public void carregarStyle() {
		ultima_glic.setTextAppearance(this, R.style.fonte_metas_dados);
		ultima_insulina.setTextAppearance(this, R.style.fonte_metas_dados);
		ultima_ref.setTextAppearance(this, R.style.fonte_metas_dados);
		meta_glic.setTextAppearance(this, R.style.fonte_metas_dados);
		meta_insulina.setTextAppearance(this, R.style.fonte_metas_dados);
		meta_carb.setTextAppearance(this, R.style.fonte_metas_dados);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (!this.verificaMetas()) {
			Intent i = new Intent(MainActivity.this, PerfilActivity.class);
			startActivity(i);
		} else {
			if (geral.isChecked() == true) {
				onCheckedChanged(radioGroup, geral.getId());
			} else {
				geral.setChecked(true);
			}
		}
	}

	public void setarCores() {
		this.carregarStyle();
		if (percentual_glic<35) {
			meta_glic.setTextColor(getResources().getColor(R.color.ruim));
		} else if (percentual_glic < 75) {
			meta_glic.setTextColor(getResources().getColor(R.color.medio));
		} else {
			meta_glic.setTextColor(getResources().getColor(R.color.bom));
		}
		
		if (percentual_carb<35) {
			meta_carb.setTextColor(getResources().getColor(R.color.ruim));
		} else if (percentual_carb < 75) {
			meta_carb.setTextColor(getResources().getColor(R.color.medio));
		} else {
			meta_carb.setTextColor(getResources().getColor(R.color.bom));
		}
		
		if (percentual_insulina<35) {
			meta_insulina.setTextColor(getResources().getColor(R.color.ruim));
		} else if (percentual_insulina < 75) {
			meta_insulina.setTextColor(getResources().getColor(R.color.medio));
		} else {
			meta_insulina.setTextColor(getResources().getColor(R.color.bom));
		}
		meta_carb.setText(Formatos.formataDouble(percentual_carb)+"%");
		meta_glic.setText(Formatos.formataDouble(percentual_glic)+"%");
		meta_insulina.setText(Formatos.formataDouble(percentual_insulina)+"%");
	}
	
	public void carregarUltimosDados() {
		String semana;
		String nenhum_dado = "Sem dados";
		if (lista_carb!=null) {
			ultima_ref.setText(Formatos.formataDouble(lista_carb.get(0).getCarboidrato())+"g CHO");
			semana = lista_carb.get(0).getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
			data_ref.setText(semana+", "+format_data.format(lista_carb.get(0).getData().getTime()));
			tipo_ultima_refeicao.setText("("+lista_carb.get(0).getTipo()+")");
		} else {
			ultima_ref.setText(nenhum_dado);
			data_ref.setText(nenhum_dado);
		}
		if (lista_glic!=null) {
			ultima_glic.setText(lista_glic.get(0).getMedida()+"mg/dL");
			semana = lista_glic.get(0).getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
			data_glic.setText(semana+", "+format_data.format(lista_glic.get(0).getData().getTime()));
			tipo_ultima_glicemia.setText("("+lista_glic.get(0).getTipo()+")");
		} else {
			ultima_glic.setText(nenhum_dado);
			data_glic.setText(nenhum_dado);
		}
		if (lista_insulina_geral!=null) {
			ultima_insulina.setText(Formatos.formataUmaCasa(lista_insulina_geral.get(0).getQtd())+"UI");
			semana = lista_insulina_geral.get(0).getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
			data_insulina.setText(semana+", "+format_data.format(lista_insulina_geral.get(0).getData().getTime()));
			tipo_ultima_insulina.setText("("+lista_insulina_geral.get(0).getTipo()+")");
		} else {
			ultima_insulina.setText(nenhum_dado);
			data_insulina.setText(nenhum_dado);
		}		
	}
	
	public void carregarListas(final long inicio, final long fim) { //carrega as listas dos objetos
		refeicaoDao = new RefeicaoDao(this);
		glicemiaDao = new GlicemiaDao(this);
		insulinaDao = new InsulinaDao(this);
		new Thread() {
			@Override
			public void run() {
				int geral=0;
				try {
					if (inicio==0 && fim == 0) {
						lista_carb = refeicaoDao.getAll();
						lista_glic = glicemiaDao.getAll();
						lista_insulina = insulinaDao.listarPorTipo(InsulinaTipos.RAPIDA);
						insulinaDao = new InsulinaDao(MainActivity.this);
						lista_insulina_geral = insulinaDao.getAll();
						geral=1;
					} else {
						lista_carb = refeicaoDao.bucarPorIntervaloData(inicio, fim);
						lista_glic = glicemiaDao.bucarPorIntervaloData(inicio, fim);
						lista_insulina = insulinaDao.bucarPorIntervaloDataTipo(inicio, fim, InsulinaTipos.RAPIDA);
					}
					sleep(1000);
					getPercentuais(geral); //para buscar os ultimos dados apenas qnd checar o geral
				} catch (Exception e) {
					erro();
				} 				
			}
		}.start();	
		
	}
	
	public void erro() { //carrega os percentuais de exitos das metas
		handler.post(new Runnable() {
			@Override
			public void run() {
				msg = new Mensagem();
				msg.mensagemToast(MainActivity.this, "Erro ao carregar dados.");
			}
		});
		
	}
	
	public void getPercentuais(final int geral) { //carrega os percentuais de exitos das metas
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (lista_carb!=null) {
					double x=0;
					double y = lista_carb.size();
					for (Refeicao ref : lista_carb) {
						if (ref.getCarboidrato() < metas.getC_final() && ref.getCarboidrato() > metas.getC_inicial()) {
							x++;
						}
					}
					percentual_carb = x/y*100;
					ref_sucesso.setText("Sucessos: "+(int)x+" em "+lista_carb.size());			
				} else {
					percentual_carb = 0;
					ref_sucesso.setText("Sucessos: 0 em 0");
				}
				if (lista_glic!=null) {
					double x = 0;
					double y = lista_glic.size();
					for (Glicemia glic : lista_glic) {
						if (glic.getMedida() < metas.getG_final() && glic.getMedida() > metas.getG_inicial()) {
							x++;
						}
					}
					percentual_glic = x/y*100;			
					glic_sucesso.setText("Sucessos: "+(int)x+" em "+lista_glic.size());
				} else {
					percentual_glic = 0;
					glic_sucesso.setText("Sucessos: 0 em 0");
				}
				if (lista_insulina!=null) {
					double x = 0;
					double y = (double) lista_insulina.size();
					for (Insulina insulina : lista_insulina) {
						if (insulina.getQtd() < metas.getI_final() && insulina.getQtd() > metas.getI_inicial()) {
							x++;
						}
					}		
					percentual_insulina = x/y*100;
					insulina_sucesso.setText("Sucessos: "+(int)x+" em "+lista_insulina.size());
				}else {
					percentual_insulina = 0;
					insulina_sucesso.setText("Sucessos: 0 em 0");
				}
				if (geral==1) {
					carregarUltimosDados();
				}
				setarCores();							
			}
		});		
	}
	
	public void mostrarDialog(View view) {
		Dialog dialogo = new Dialog(this, R.style.tema_dialogo);
		dialogo.setTitle("Titulo do dialogo");
		dialogo.setContentView(R.layout.dialogo_teste);
		dialogo.show();
	}
	
	public void telaGlicemia(View view) {
		Intent intent = new Intent(MainActivity.this, GlicemiaActivity.class);
		startActivity(intent);
	}
	
	public void telaRelatorio(View view) {
		Intent intent = new Intent(MainActivity.this, RelatorioActivity.class);
		startActivity(intent);
	}
	
	public void telaRefeicao(View view) {
		Intent intent = new Intent(MainActivity.this, RefeicaoActivity.class);
		startActivity(intent);
	}
	
	public void telaInsulina(View view) {
		Intent intent = new Intent(this, InsulinaActivity.class);
		startActivity(intent);
	}
	
	public void tabela(View view) {
		Intent intent = new Intent(MainActivity.this, TabelaListActivity.class);
		startActivity(intent);
	}
	
	public void telaPerfil(View view) {
		Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
		startActivity(intent);
	}
	
	public void telaExercicios(View view) {
		Intent intent = new Intent(MainActivity.this, ExerciciosActivity.class);
		startActivity(intent);
	}
	
	public void telaLembrete(View view) {
		Intent intent = new Intent(MainActivity.this, AlertaActivity.class);
		startActivity(intent);
	}

	public void InsulinasCSV() {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.i("erro", null);
		} else {
			String pathCSV = Environment.getExternalStorageDirectory()+"/Doce Desafio/CSV/";
			File exportDir = new File(pathCSV);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File file;
			PrintWriter printWriter = null;
			try {
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				file = new File(exportDir, "InsulinasCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				Cursor curCSV = db.rawQuery("select * from Insulina", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("insulinas");
				printWriter.println("obs,tipo,data,quantidade");
				while (curCSV.moveToNext()) {
					String obs = curCSV.getString(curCSV.getColumnIndex("obs"));
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					Long data = curCSV.getLong(curCSV.getColumnIndex("data"));
					Long quantidade = curCSV.getLong(curCSV.getColumnIndex("quantidade"));

					String record = obs + "," + tipo + "," + data + "," + quantidade;
					printWriter.println(record); 								
				}
				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
			} finally {
				if (printWriter != null)
					printWriter.close();
			}
		}
	}
	
	public void RefeicoesCSV() {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.i("erro", null);
		} else {
			String pathCSV = Environment.getExternalStorageDirectory()+"/Doce Desafio/CSV/";
			File exportDir = new File(pathCSV);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File file;
			PrintWriter printWriter = null;
			try {
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				file = new File(exportDir, "RefeicoesCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				Cursor curCSV = db.rawQuery("select * from Refeicao", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("refeicoes");
				printWriter.println("tipo,data,obs,peso, carboidratos");
				while (curCSV.moveToNext()) {
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					Long data = curCSV.getLong(curCSV.getColumnIndex("data"));
					String obs = curCSV.getString(curCSV.getColumnIndex("obs"));
					Long peso = curCSV.getLong(curCSV.getColumnIndex("peso"));
					Long carboidrato = curCSV.getLong(curCSV.getColumnIndex("carboidrato"));

					String record = tipo + "," + data + "," + obs + "," + peso + "," + carboidrato;
					printWriter.println(record); 								
				}
				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
			} finally {
				if (printWriter != null)
					printWriter.close();
			}
		}
	}
	
	public void GlicemiasCSV() {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.i("erro", null);
		} else {
			String pathCSV = Environment.getExternalStorageDirectory()+"/Doce Desafio/CSV/";
			File exportDir = new File(pathCSV);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File file;
			PrintWriter printWriter = null;
			try {
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				file = new File(exportDir, "GlicemiasCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				Cursor curCSV = db.rawQuery("select * from Glicemia", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("glicemias");
				printWriter.println("tipo,data,obs,medida");
				while (curCSV.moveToNext()) {
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					Long data = curCSV.getLong(curCSV.getColumnIndex("data"));
					String obs = curCSV.getString(curCSV.getColumnIndex("obs"));
					Long medida = curCSV.getLong(curCSV.getColumnIndex("medida"));

					String record = tipo + "," + data + "," + obs + "," + medida;
					printWriter.println(record); 								
				}
				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
			} finally {
				if (printWriter != null)
					printWriter.close();
			}
		}
	}
	
	public void ExerciciosCSV() {
		/**
		 * First of all we check if the external storage of the device is
		 * available for writing. Remember that the external storage is not
		 * necessarily the sd card. Very often it is the device storage.
		 */
		
		
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			
		} else {
			String pathCSV = Environment.getExternalStorageDirectory()+"/Doce Desafio/CSV/";
			File exportDir = new File(pathCSV);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			
			File file;
			PrintWriter printWriter = null;
			try {
				
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				
				file = new File(exportDir, "ExerciciosCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				/**
				 * This is our database connector class that reads the data from
				 * the database. The code of this class is omitted for brevity.
				 */
				//String path = context.getFilesDir().getParentFile().getPath() +"/assets/"+ "diabetes.db";
			    
				
				
				//dbcOurDatabaseConnector.getWritableDatabase();
				//DBCOurDatabaseConnector dbcOurDatabaseConnector = new DBCOurDatabaseConnector(myContext);
				//dbcOurDatabaseConnector.openForReading(); // open the database
															// for reading

				/**
				 * Let's read the first table of the database. getFirstTable()
				 * is a method in our DBCOurDatabaseConnector class which
				 * retrieves a Cursor containing all records of the table (all
				 * fields). The code of this class is omitted for brevity.
				 */
				
				Cursor curCSV = db.rawQuery("select * from Exercicios", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("exercicio");
				printWriter.println("descricao,tipo,modalidade,intensidade");
				while (curCSV.moveToNext()) {
					String descricao = curCSV.getString(curCSV.getColumnIndex("descricao"));
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					String modalidade = curCSV.getString(curCSV.getColumnIndex("modalidade"));
					String intensidade = curCSV.getString(curCSV.getColumnIndex("intensidade"));

					/**
					 * Create the line to write in the .csv file. We need a
					 * String where values are comma separated. The field date
					 * (Long) is formatted in a readable text. The amount field
					 * is converted into String.
					 */
					String record = descricao + "," + tipo + "," + modalidade + "," + intensidade;
					printWriter.println(record); // write the record in the .csv
													// file
				}

				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
				
			} finally {
				if (printWriter != null)
					printWriter.close();
			}

			// If there are no errors, return true.
	
		}
	}
	
	public void exportaCSV() {
		GlicemiasCSV();
		InsulinasCSV();
		RefeicoesCSV();
		ExerciciosCSV();
		
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Calendar inicio = Calendar.getInstance();
		switch (checkedId) {
		case R.id.meta_geral:
			this.carregarLoad(1);
			this.carregarListas(0, 0);	
			break;
		case R.id.meta_mensal:
			inicio = Calendar.getInstance();
			inicio.set(Calendar.HOUR_OF_DAY, 0);
			inicio.set(Calendar.MINUTE, 0);
			inicio.add(Calendar.DAY_OF_YEAR, -30);
			this.carregarLoad(0);
			this.carregarListas(inicio.getTimeInMillis(), fim.getTimeInMillis());
			break;
		case R.id.meta_semanal:
			inicio = Calendar.getInstance();
			inicio.set(Calendar.HOUR_OF_DAY, 0);
			inicio.set(Calendar.MINUTE, 0);
			inicio.add(Calendar.DAY_OF_YEAR, -7);
			this.carregarLoad(0);
			this.carregarListas(inicio.getTimeInMillis(), fim.getTimeInMillis());
			break;
		case R.id.meta_diaria:
			inicio = Calendar.getInstance();
			inicio.set(Calendar.HOUR_OF_DAY, 0);
			inicio.set(Calendar.MINUTE, 0);
			this.carregarLoad(0);
			this.carregarListas(inicio.getTimeInMillis(), fim.getTimeInMillis());
			break;
		}		
	}
	
	// metodos dos relatorios do BACKUP
	private void backup() {
		ConnectDatabase connectDatabase = new ConnectDatabase(this);
		connectDatabase.backUp();
	}
	
	private void restaurar() {
		ConnectDatabase connectDatabase = new ConnectDatabase(this);
		connectDatabase.restore();
	}
	
	@Override
	public void onBackPressed() {
		
		
		AlertDialog.Builder msg = new AlertDialog.Builder(this);
		msg.setTitle("Encerrar APP");
		msg.setMessage(getResources().getString(R.string.encerrar_app));
		msg.setNeutralButton("Não", null);
		msg.setNegativeButton("Sim", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.super.onBackPressed();
				finish();
			}
		});
		msg.show();	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, getResources().getString(R.string.b_backup));
		menu.add(0, 1, 1, getResources().getString(R.string.b_restaurar));
		menu.add(0, 2, 2, getResources().getString(R.string.b_csv));
		menu.add(0, 3, 3, getResources().getString(R.string.b_sync));
		menu.add(0, 4, 4, getResources().getString(R.string.b_logout));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				AlertDialog.Builder msg1 = new AlertDialog.Builder(this);
				msg1.setTitle("Back-up");
				msg1.setMessage(getResources().getString(R.string.backup_info));
				msg1.setNeutralButton("Cancelar", null);
				msg1.setNegativeButton("OK", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						backup();
					}
				});
				msg1.show();	
				break;
			case 1:	
				AlertDialog.Builder msg2 = new AlertDialog.Builder(this);
				msg2.setTitle("Restaurar Back-up");
				msg2.setMessage(getResources().getString(R.string.restaurar_info));
				msg2.setNeutralButton("Não", null);
				msg2.setNegativeButton("Sim", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						restaurar();
						onResume();
					}
				});
				msg2.show();	
				break;
			case 2:	
				AlertDialog.Builder msg3 = new AlertDialog.Builder(this);
				msg3.setTitle("Gerar CSV");
				msg3.setMessage(getResources().getString(R.string.csv_info));
				msg3.setNeutralButton("Não", null);
				msg3.setNegativeButton("Sim", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						exportaCSV();
						Toast.makeText(getApplicationContext(), "CSV gerado com sucesso!", Toast.LENGTH_SHORT).show();
					}
				});
				msg3.show();	
				break;
			case 3:	
				syncRegistrosOffline(true);	
				break;

			case 4:	
				AlertDialog.Builder msg4 = new AlertDialog.Builder(this);
				msg4.setTitle("SAIR");
				msg4.setMessage(getResources().getString(R.string.pergunta_logout));
				msg4.setNeutralButton("Não", null);
				msg4.setNegativeButton("Sim", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//ZERO A BASE
						ConnectDatabase connectDatabase = new ConnectDatabase(MainActivity.this);
						connectDatabase.zerarBase();
						
//						SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
//						Editor editor = preferencias.edit();
//						editor.putBoolean(MANTER_CONECTADO, false);
////						editor.putBoolean(MANTER_CONECTADO, manterConectado.isChecked());
//						editor.commit();
						
						finish();
					}
				});
				msg4.show();	
				break;
		}
		return true;
	}

	
	
	
	
	

}
