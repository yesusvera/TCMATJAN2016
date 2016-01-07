package br.com.diabetesvirtual.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.ExerciciosDao;
import br.com.diabetesvirtual.listactivity.ExerciciosListActivity;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.model.ExerciciosTipos;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.Mensagem;

public class ExerciciosActivity extends Activity implements OnDateChangedListener, OnTimeChangedListener, OnCheckedChangeListener, OnSeekBarChangeListener{
	private ExerciciosDao exerciciosDao;
	private Exercicios exercicio;
	public static List<Exercicios> lista;
	RadioGroup radioGroup_tipo;
	RadioGroup radioGroup_intensidade;
	RadioButton aero;
	RadioButton anaero;
//	RadioButton muito_leve;
//	RadioButton leve;
//	RadioButton moderada;
//	RadioButton intenso;
//	RadioButton muito_intenso;
	DatePicker datePicker;
	TimePicker timePicker;
	TextView dia_selecionado;
	TextView hora_selecionada;
	EditText desc;
	SeekBar seekBar;
	TextView text_duracao;
	Mensagem msg;
	final Calendar calendar = Calendar.getInstance();
	Boolean editar = false;
	Spinner modalidade;
	Spinner intensidade;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercicios_inserir);
		desc = (EditText) findViewById(R.id.exercicio_desc);
		seekBar = (SeekBar) findViewById(R.id.exe_seek_duracao);
		text_duracao = (TextView) findViewById(R.id.exe_duracao);
//		radioGroup_intensidade = (RadioGroup) findViewById(R.id.exe_radio_intensidade);
		radioGroup_tipo = (RadioGroup) findViewById(R.id.exe_radio_tipo);
		aero = (RadioButton) findViewById(R.id.exe_aero);
		anaero = (RadioButton) findViewById(R.id.exe_anaero);
//		muito_leve = (RadioButton) findViewById(R.id.exe_muito_leve);
//		leve = (RadioButton) findViewById(R.id.exe_leve);
//		moderada = (RadioButton) findViewById(R.id.exe_moderado);
//		intenso = (RadioButton) findViewById(R.id.exe_intenso);
//		muito_intenso = (RadioButton) findViewById(R.id.exe_muito_intenso);
		datePicker = (DatePicker) findViewById(R.id.exe_data_picker);
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker = (TimePicker) findViewById(R.id.exe_time_picker);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		timePicker.setOnTimeChangedListener(this);
		dia_selecionado = (TextView) findViewById(R.id.exe_dia_selecionado);
		hora_selecionada = (TextView) findViewById(R.id.exe_hora_selecionada);	
		radioGroup_tipo.setOnCheckedChangeListener(this);
//		radioGroup_intensidade.setOnCheckedChangeListener(this);
		seekBar.setOnSeekBarChangeListener(this);
		ArrayAdapter<CharSequence> adapter_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_exercicio, R.layout.spinner_item);
		modalidade = (Spinner) findViewById(R.id.tipo_glicemia_relatorio_spinner);
		modalidade.setAdapter(adapter_tipo);
		ArrayAdapter<CharSequence> adapter_intensidade = ArrayAdapter.createFromResource(this, R.array.intensidade_exercicio, R.layout.spinner_item);
		intensidade = (Spinner) findViewById(R.id.intensidade_exercicio_spinner);
		intensidade.setAdapter(adapter_intensidade);
		
		this.InicializarVariaveis();
		this.editar();
		
	}
	
	public void editar() {
		Intent i = getIntent();
		String x = i.getStringExtra("extra");
		if (x!=null && x.equals("editar")) {
			exercicio = ExerciciosListActivity.exercicio;
			seekBar.setProgress(exercicio.getDuracao());
			timePicker.setCurrentHour(exercicio.getData().get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
			timePicker.setCurrentMinute(exercicio.getData().get(Calendar.MINUTE));
			datePicker.init(exercicio.getData().get(Calendar.YEAR), exercicio.getData().get(Calendar.MONTH), exercicio.getData().get(Calendar.DAY_OF_MONTH), this);
			desc.setText(exercicio.getDescricao());
			if (exercicio.getTipo().equals("aerobico")) {
				aero.setChecked(true);
			} else {
				anaero.setChecked(true);
			}
//			if (exercicio.getIntensidade()==ExerciciosIntensidade.MUITO_INTENSO) {
//				muito_intenso.setChecked(true);
//			} else if (exercicio.getIntensidade()==ExerciciosIntensidade.INTENSO) {
//				intenso.setChecked(true);
//			} else if (exercicio.getIntensidade()==ExerciciosIntensidade.MODERADO) {
//				moderada.setChecked(true);
//			} else if (exercicio.getIntensidade()==ExerciciosIntensidade.LEVE) {
//				leve.setChecked(true);
//			
//			} else {
//				muito_leve.setChecked(true);
//			}
			editar = true;
		}
	}
	
	public void salvar(View view) {
		try {
			exerciciosDao = new ExerciciosDao(this);
			if (desc.getText().toString().equals("")) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Informe a descrição do exercício.");
			} else {
				exercicio.setData(this.setData(timePicker, datePicker));
				exercicio.setDescricao(desc.getText().toString());
				exercicio.setModalidade(modalidade.getSelectedItem().toString());
				exercicio.setIntensidade(intensidade.getSelectedItem().toString());
				 
				exerciciosDao.inserir(exercicio, false);
				
				new SyncRESTBo().insertExercicioREST(exercicio, this, null);
				
				if (editar == true) {
					setResult(0);
					finish();
					return;
				} else {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
				    msg = new Mensagem();
					msg.mensagemToast(this, "Salvo em: "+ format.format(exercicio.getData().getTime()));
					finish();
					return;
				}				
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ERRO", "Erro ao salvar exercicio");
			e.printStackTrace();
		}
		
	}
	

	public void InicializarVariaveis() { //Inicializa as variavies 
		try {
			aero.setChecked(true);
//			leve.setChecked(true);
			exercicio = Exercicios.getExercicios(exercicio);
			exercicio.setDuracao(0);
			text_duracao.setText("Duração 0 minutos");
			aero.setText(ExerciciosTipos.getNome(this, ExerciciosTipos.AERO.getCod()));
			anaero.setText(ExerciciosTipos.getNome(this, ExerciciosTipos.ANAERO.getCod()));
			//muito_leve.setText(ExerciciosIntensidade.getNome(this, ExerciciosIntensidade.MUITO_LEVE.getCod()));
			//leve.setText(ExerciciosIntensidade.getNome(this, ExerciciosIntensidade.LEVE.getCod()));
			//moderada.setText(ExerciciosIntensidade.getNome(this, ExerciciosIntensidade.MODERADO.getCod()));
			//intenso.setText(ExerciciosIntensidade.getNome(this, ExerciciosIntensidade.INTENSO.getCod()));			
			//muito_intenso.setText(ExerciciosIntensidade.getNome(this, ExerciciosIntensidade.MUITO_INTENSO.getCod()));			
			
			
			Calendar c = this.setData(timePicker, datePicker);
			dia_selecionado.setText("Dia - "+c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+", "+c.get(Calendar.DAY_OF_MONTH)+" de "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" de "+c.get(Calendar.YEAR)+"");
			hora_selecionada.setText("Hora - "+c.get(Calendar.HOUR_OF_DAY)+ " horas e "+c.get(Calendar.MINUTE)+" minutos" );	
		} catch (Exception e) {
			Log.e("ERRO", "Erro ao inicializar variaveis");
		}
	}

//** ESSE MÉTODO PODE SER ISOLADO NA CLASSE FORMATOS, POIS VARIOS MODULOS ESTAO UTILIZANDO-O (REF, INSUL E GLIC) **//
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		exercicio = Exercicios.getExercicios(exercicio);
		if (group == radioGroup_tipo) {
			switch (checkedId) {
			case R.id.exe_aero:
				exercicio.setTipo("Aeróbico");
				break;
			case R.id.exe_anaero:
				exercicio.setTipo("Anaeróbico");
				break;
			} 
		}
//		 else {
//			switch (checkedId) {
//			case R.id.exe_muito_leve:
//				exercicio.setIntensidade(ExerciciosIntensidade.MUITO_LEVE);
//				break;
//			case R.id.exe_leve:
//				exercicio.setIntensidade(ExerciciosIntensidade.LEVE);
//				break;
//			case R.id.exe_moderado:
//				exercicio.setIntensidade(ExerciciosIntensidade.MODERADO);
//				break;
//			case R.id.exe_intenso:
//				exercicio.setIntensidade(ExerciciosIntensidade.INTENSO);
//				break;
//			case R.id.exe_muito_intenso:
//				exercicio.setIntensidade(ExerciciosIntensidade.MUITO_INTENSO);
//				break;
//			}
//		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		text_duracao.setText("Duração "+progress+" minutos");	
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int minutos = seekBar.getProgress();
		int hora = 0;
		while (minutos >= 60) {
			hora++;
			minutos = minutos - 60;
		}
		if (hora>0) {
			text_duracao.setText("Duração "+hora+" horas e "+ minutos+" minutos");	
		} else {
			text_duracao.setText("Duração "+minutos+" minutos");	
		}		
		exercicio = Exercicios.getExercicios(exercicio);
		exercicio.setDuracao(seekBar.getProgress());
	}	
	

	
	
}
