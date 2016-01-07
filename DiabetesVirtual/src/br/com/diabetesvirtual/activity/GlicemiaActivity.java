package br.com.diabetesvirtual.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.GlicemiaDao;
import br.com.diabetesvirtual.listactivity.GlicemiaListActivity;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.Mensagem;



@SuppressLint("SimpleDateFormat") //O calendar.displayName só funciona a partir da API 9 (2.3)..
public class GlicemiaActivity extends Activity implements TextWatcher, OnDateChangedListener, OnTimeChangedListener{	
	GlicemiaDao glicemiaDao;
	Glicemia glicemia;
	TimePicker timePicker;
	DatePicker datePicker;	
	EditText medida;
	EditText tipo;
	EditText obs;
	TextView dia_selecionado;
	TextView hora_selecionada;
	Mensagem msg;	
	Boolean sugestao = false;	
	Boolean editar = false;
	RadioButton nenhum, jejum, pre_prandial, pos_prandial;
	Spinner tipo_spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.glicemia_inserir);
		Calendar calendar = Calendar.getInstance();
		dia_selecionado = (TextView) findViewById(R.id.dia_selecionado);
		hora_selecionada = (TextView) findViewById(R.id.hora_selecionada);
		timePicker = (TimePicker) findViewById(R.id.time_picker);
		timePicker.setIs24HourView(true); //mostra no formaro sem AM PM
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		timePicker.setOnTimeChangedListener(this); //atualiza a hora selecionada para mostrar na tela
		datePicker = (DatePicker) findViewById(R.id.data_picker);  //**** CAso apresente erro, setar a data manualmente!
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
		medida = (EditText) findViewById(R.id.glicemia);
		medida.addTextChangedListener(this); //aumenta a letra ao digitar
		obs = (EditText) findViewById(R.id.obs);
//		nenhum = (RadioButton) findViewById(R.id.glicemia_nenhum);
//		jejum = (RadioButton) findViewById(R.id.glicemia_jejum);
//		pre_prandial = (RadioButton) findViewById(R.id.glicemia_pre_prandial);
//		pos_prandial = (RadioButton) findViewById(R.id.glicemia_pos_prandial);
//		nenhum.setText(GlicemiaTipos.NENHUM.getNome());
//		jejum.setText(GlicemiaTipos.JEJUM.getNome());
//		pre_prandial.setText(GlicemiaTipos.PRE_PRANDIAL.getNome());
//		pos_prandial.setText(GlicemiaTipos.POS_PRANDIAL.getNome());
		ArrayAdapter<CharSequence> adapter_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_glicemia, R.layout.spinner_item);
		tipo_spinner = (Spinner) findViewById(R.id.tipo_glicemia_spinner);
		tipo_spinner.setAdapter(adapter_tipo);
		this.getExtra();
		this.InicializarVariaveis();	
		//this.gerarDados();
	}

	public void getExtra() {
		Intent i = getIntent();
		String x = i.getStringExtra("extra");
		if (x!=null && x.equals("sugestao")) {
			this.sugestao = true; //informa que é para retornar para a refeicao
		} else if (x!=null && x.equals("editar")) {
			Glicemia g = GlicemiaListActivity.glicemia;
			this.glicemia = g;
			obs.setText(g.getObs());
			medida.setText(g.getMedida()+"");
			timePicker.setCurrentHour(g.getData().get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
			timePicker.setCurrentMinute(g.getData().get(Calendar.MINUTE));
			datePicker.init(g.getData().get(Calendar.YEAR), g.getData().get(Calendar.MONTH), g.getData().get(Calendar.DAY_OF_MONTH), this);
			editar = true;
		}
	}
	
	public void gerarDados() {
		try {
			glicemiaDao = new GlicemiaDao(this);
			Calendar c = Calendar.getInstance();
			c.set(2013,0, 1);
			Random r;
			r = new Random();
			for (int i = 0; i < 60; i++) {
				c.add(Calendar.DAY_OF_MONTH, 1);
				for (int j = 0; j < 4; j++) {
					c.set(Calendar.HOUR_OF_DAY, r.nextInt(24));
					c.set(Calendar.MINUTE, r.nextInt(60));
					glicemia = new Glicemia();
					glicemia.setMedida(r.nextInt(200)+40);
					glicemia.setObs("Teste");
					glicemia.setData(c);						
					glicemiaDao.inserir(glicemia, false);
				}				
			}
			msg = new Mensagem();
			msg.mensagemToast(this, "Dados gerados com sucesso!");
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao gerar dados - "+ e.getMessage());
		}
	}
	
	public void finalizar(View view) {
		try {
			if (medida.getText().toString().equals("")) { 			//verifica se o campo medida esta vazio
				Mensagem msg = new Mensagem();
				msg.mensagemToast(this, "Informe a glicemia.");
			} else {
				glicemia = Glicemia.getGlicemia(glicemia);		//popula o objeto glicemia com os dados da tela ou
//				if (nenhum.isChecked()) {
//					glicemia.setTipo(GlicemiaTipos.NENHUM);
//				} else if(jejum.isChecked()) {
//					glicemia.setTipo(GlicemiaTipos.JEJUM);				
//				} else if(pre_prandial.isChecked()) {
//					glicemia.setTipo(GlicemiaTipos.PRE_PRANDIAL);				
//				} else if(pos_prandial.isChecked()) {
//					glicemia.setTipo(GlicemiaTipos.POS_PRANDIAL);				
//				}
				glicemia.setTipo(tipo_spinner.getSelectedItem().toString()); 
				glicemia.setData(setData(timePicker, datePicker)); 			//chama o metodo q transforma os picker em mili
				glicemia.setMedida(Integer.parseInt(medida.getText().toString()));
				glicemia.setObs(obs.getText().toString());
				glicemiaDao = new GlicemiaDao(this);
				glicemiaDao.inserir(glicemia, false);			
				
				new SyncRESTBo().insertGlicemiaREST(glicemia, this, null);
				
				if (sugestao == true || editar == true) {
					setResult(0);
					finish();
					return;
				} 
				//mensagem de CONFIRMACAO
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				msg = new Mensagem();
				msg.mensagemToast(this, "Salvo em: "+ format.format(glicemia.getData().getTime()));	
				finish(); //Finaliza a ACTIVITY	
			}
		} catch (Exception e) {
			Log.e("GLICEMIA ACTIVITY", e.getMessage());
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
			Log.e("GLICEMIA ACTIVITY", e.getMessage());			
		}
		return x; 
	}

	@Override
	public void afterTextChanged(Editable s) {		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {	
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) { //aumentar a letra ao digitar o numero
		if (s.length()==0) {
			medida.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		}	else {
			medida.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = this.setData(timePicker, datePicker);
		dia_selecionado.setText("Dia - "+c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+dayOfMonth+" de "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" de "+year+"");	
	}

	@Override 
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		hora_selecionada.setText("Hora - "+hourOfDay+ " horas e "+minute+" minutos" );	
	}

	public void InicializarVariaveis() { //Inicializa as variavies de dia e hora selecionados da tela para a data atual
		try {
			Calendar c = this.setData(timePicker, datePicker);
			dia_selecionado.setText("Dia - "+c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+c.get(Calendar.DAY_OF_MONTH)+" de "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" de "+c.get(Calendar.YEAR)+"");
			hora_selecionada.setText("Hora - "+c.get(Calendar.HOUR_OF_DAY)+ " horas e "+c.get(Calendar.MINUTE)+" minutos" );	
		} catch (Exception e) {
			Log.e("ERRO", "Erro ao inicializar variaveis");
		}
	}

	public void voltar(View view) { //botao voltar
		finish();
	}
	
}
