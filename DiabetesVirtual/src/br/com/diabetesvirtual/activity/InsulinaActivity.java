package br.com.diabetesvirtual.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.InsulinaDao;
import br.com.diabetesvirtual.listactivity.InsulinaListActivity;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.InsulinaTipos;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;


public class InsulinaActivity extends Activity implements TextWatcher, OnDateChangedListener, OnTimeChangedListener{
	RadioButton lenta;
	RadioButton rapida;
	Insulina insulina;
	List<Insulina> lista;
	TimePicker timePicker;
	DatePicker datePicker;
	TextView dia_selecionado;
	TextView hora_selecionada;
	EditText qtd;
	double insulinaLembrete=0;
	Boolean editar = false;
	boolean lembrete_basal=false;
	EditText obs;
	Mensagem msg;
	InsulinaDao insulinaDao;
	final Calendar calendar = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insulina_inserir);
		lenta = (RadioButton) findViewById(R.id.lenta);
		rapida = (RadioButton) findViewById(R.id.rapida);
		datePicker = (DatePicker) findViewById(R.id.data_picker);
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker = (TimePicker) findViewById(R.id.time_picker);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		timePicker.setOnTimeChangedListener(this);
		qtd = (EditText) findViewById(R.id.insulina);
		qtd.addTextChangedListener(this);
		obs = (EditText) findViewById(R.id.obs);
		dia_selecionado = (TextView) findViewById(R.id.tipo_glicemia);
		hora_selecionada = (TextView) findViewById(R.id.hora_selecionada);
		lenta.setText(InsulinaTipos.LENTA.getNome());
		rapida.setText(InsulinaTipos.RAPIDA.getNome());
		this.getEditar();
		this.InicializarVariaveis();
		this.getLembreteBasal();
		
		//this.gerarDados();
	}
	
	public void getEditar() {
		Intent i = getIntent();
		String x = i.getStringExtra("extra");
		if (x!=null && x.equals("editar")) {
			insulina = InsulinaListActivity.insulina;
			obs.setText(insulina.getObs());
			qtd.setText(insulina.getQtd()+"");
			timePicker.setCurrentHour(insulina.getData().get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
			timePicker.setCurrentMinute(insulina.getData().get(Calendar.MINUTE));
			datePicker.init(insulina.getData().get(Calendar.YEAR), insulina.getData().get(Calendar.MONTH), insulina.getData().get(Calendar.DAY_OF_MONTH), this);
			if (insulina.getTipo().equals("Basal")) {
				lenta.setChecked(true);
				rapida.setChecked(false);
			} else {
				lenta.setChecked(false);
				rapida.setChecked(true);				
			}
			editar = true;
		}
	}

	public Boolean getLembrete() {
		if (getIntent().getExtras() != null && getIntent().getExtras().getDouble("lembrete_insulina") > 0) {
			insulinaLembrete = getIntent().getExtras().getDouble("lembrete_insulina");
			return true;
		}
		return false;
	}
	
	public void getLembreteBasal() {
		String x="";
		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().getInt("lembrete_basal_id") == 1) {
				insulinaLembrete = getIntent().getExtras().getDouble("lembrete_basal");
				x = getIntent().getExtras().getString("lembrete_basal_obs");	
				qtd.setText(Formatos.formataUmaCasa(insulinaLembrete)+"");
				obs.setText(x);
				lenta.setChecked(true);
				rapida.setChecked(false);
			} else if (getIntent().getExtras().getInt("lembrete_basal_id2") == 2) {
				insulinaLembrete = getIntent().getExtras().getDouble("lembrete_basal2");
				x = getIntent().getExtras().getString("lembrete_basal_obs2");	
				qtd.setText(Formatos.formataUmaCasa(insulinaLembrete)+"");
				obs.setText(x);
				lenta.setChecked(true);
				rapida.setChecked(false);
			}		
		}
	}
	
	public Boolean getInsere() {
		if (getIntent().getExtras() != null && getIntent().getExtras().getDouble("insere") > 0) {
			insulinaLembrete = getIntent().getExtras().getDouble("insere");
			return true;
		}
		return false;
	}
	
	public void gerarDados() { //Gerar 3 meses de dados automaticamente
		insulinaDao = new InsulinaDao(this);
		try {
			Calendar c = Calendar.getInstance();
			c.set(2013,0, 1);
			Random r;
			r = new Random();
			for (int i = 0; i < 60; i++) {
				c.add(Calendar.DAY_OF_MONTH, 1);
				for (int j = 0; j < 6; j++) {
					c.set(Calendar.HOUR_OF_DAY, r.nextInt(24));
					c.set(Calendar.MINUTE, r.nextInt(60));
					insulina = new Insulina();
					insulina.setObs("blá blá blá");
					if (j==0) {
						insulina.setTipo("Basal");
						insulina.setQtd(r.nextInt(20));
						c.set(Calendar.HOUR_OF_DAY, 6);
						c.set(Calendar.MINUTE,0);
						insulina.setData(c);
					} else if (j==5) {
						insulina.setTipo("Basal");
						insulina.setQtd(r.nextInt(20));
						c.set(Calendar.HOUR_OF_DAY, 18);
						c.set(Calendar.MINUTE,0);
						insulina.setData(c);
					} else {
						insulina.setTipo("Ultra-rápida");
						insulina.setQtd(r.nextInt(10)+2);
						insulina.setData(c);
					}
					insulinaDao.inserir(insulina, false);
				}				
			}
			msg = new Mensagem();
			msg.mensagemToast(this, "Dados gerados com sucesso!");
		} catch (Exception e) {	
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao gerar dados - "+ e.getMessage());
		}
	}

	public void salvar(View view) {
		try {
			if (qtd.getText().toString().equals("")) { 			//verifica se o campo medida esta vazio
				msg = new Mensagem();
				msg.mensagemToast(this, "Informe a quantidade de insulina.");
			} else {
				insulinaDao = new InsulinaDao(this);
				insulina = Insulina.getInsulina(insulina);
				insulina.setData(setData(timePicker, datePicker));
				insulina.setObs(obs.getText().toString());
				insulina.setQtd(Double.parseDouble(qtd.getText().toString()));
				if (lenta.isChecked()) {
					insulina.setTipo("Basal");
				} else {
					insulina.setTipo("Ultra-rápida");				
				}
				insulinaDao.inserir(insulina, false);		
				
				new SyncRESTBo().insertInsulinaREST(insulina, this, null);
				
				if (this.getInsere() || editar==true) {
					setResult(1);
					finish();
					return;
				}
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
				msg = new Mensagem();
				msg.mensagemToast(this, "Salvo em: "+ format.format(insulina.getData().getTime()));	
				finish();
				return;
			}	
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao salvar!"+ e.getMessage());		
		}
	}
	
	public void lembrete_basal(View view) {
		Intent i = new Intent(this, InsulinaLembreteActivity.class);
		startActivity(i);
		finish();
		return;
	}
	
	public void InicializarVariaveis() { //Inicializa as variavies de dia e hora selecionados da tela para a data atual
		try {
			Calendar c = this.setData(timePicker, datePicker);
			dia_selecionado.setText("Dia - "+c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+c.get(Calendar.DAY_OF_MONTH)+" de "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" de "+c.get(Calendar.YEAR)+"");
			hora_selecionada.setText("Hora - "+c.get(Calendar.HOUR_OF_DAY)+ " horas e "+c.get(Calendar.MINUTE)+" minutos" );	
			if (getLembrete()) {
				qtd.setText(Formatos.formataUmaCasa(insulinaLembrete)+"");
				obs.setText("Lembrete de insulina.");
				lenta.setChecked(false);
				rapida.setChecked(true);
			} else if (getInsere()) {
				qtd.setText(Formatos.formataUmaCasa(insulinaLembrete)+"");
				lenta.setChecked(false);
				rapida.setChecked(true);
			} 
		} catch (Exception e) {
			Log.e("ERRO", "Erro ao inicializar variaveis");
		}
	}

	public Calendar setData(TimePicker timePicker, DatePicker datePicker) { //Recebe os datapiker e o timepicker e devolve em milisegundos.
		Calendar x = Calendar.getInstance();
		try {
			int ano = datePicker.getYear();
			int mes = datePicker.getMonth();
			int dia = datePicker.getDayOfMonth();
			int hora = timePicker.getCurrentHour();
			int min = timePicker.getCurrentMinute();			
			x.set(ano, mes, dia, hora, min);		
		} catch (Exception e) {
			Log.e("INSULINA ACTIVITY", e.getMessage());			
		}
		return x; 
	}
	
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		hora_selecionada.setText("Hora - "+hourOfDay+ " horas e "+minute+" minutos" );		
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
		Calendar c = this.setData(timePicker, datePicker);
		dia_selecionado.setText("Dia - "+c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+dayOfMonth+" de "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" de "+year+"");	
	}
	
	@Override
	public void afterTextChanged(Editable s) {		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {		
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length()==0) {
			qtd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		}	else {
			qtd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
		}	
	}

	@Override
	public void onBackPressed() {
		if (this.getInsere()) {
			setResult(2);
			finish();
			return;
		}
		super.onBackPressed();
	}
	
	
	
}
