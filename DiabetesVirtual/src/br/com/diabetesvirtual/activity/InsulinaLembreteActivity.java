package br.com.diabetesvirtual.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.LembreteDao;
import br.com.diabetesvirtual.model.Lembrete;
import br.com.diabetesvirtual.service.TimerReceiver;
import br.com.diabetesvirtual.util.Mensagem;

public class InsulinaLembreteActivity extends Activity implements TextWatcher{

	TimePicker timePicker;
	TimePicker timePicker2;
	Lembrete lembrete;
	Lembrete lembrete2;
	LembreteDao lembreteDao;
	CheckBox ativar;
	EditText qtd;
	EditText obs;
	CheckBox ativar2;
	EditText qtd2;
	EditText obs2;
	int hora;
	int min;
	Mensagem msg;
	Intent myIntent;
	PendingIntent pIntent;
	Calendar c = Calendar.getInstance();
	AlarmManager alarmManager;
	final Calendar calendar = Calendar.getInstance();
	List<Lembrete> lista;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); 	
		setContentView(R.layout.lembrete_insulina);
		timePicker = (TimePicker) findViewById(R.id.timePicker_insulina);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		ativar = (CheckBox) findViewById(R.id.checkAtivo);
		qtd = (EditText) findViewById(R.id.insulina_lembrete);
		qtd.addTextChangedListener(this);
		obs = (EditText) findViewById(R.id.obs_lembrete);	
//alarme 2		
		timePicker2 = (TimePicker) findViewById(R.id.timePicker_insulina2);
		timePicker2.setIs24HourView(true);
		timePicker2.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY)); //setando a hora ATUAL no formato 24h no timepick
		timePicker2.setCurrentMinute(calendar.get(Calendar.MINUTE));
		ativar2 = (CheckBox) findViewById(R.id.checkAtivo2);
		qtd2 = (EditText) findViewById(R.id.insulina_lembrete2);
		qtd2.addTextChangedListener(this);
		obs2 = (EditText) findViewById(R.id.obs_lembrete2);	
		this.inicializarVariaveis();
	}
	
	
	public void inicializarVariaveis() {
		try {
			lista = new ArrayList<Lembrete>();
			lembreteDao = new LembreteDao(this);
			lembrete = new Lembrete();
			lembrete2 = new Lembrete();
			lista = lembreteDao.getLembrete();
			if (lista.size()>0) { //lembrete 01
				lembrete = lista.get(0);
				timePicker.setCurrentHour(lembrete.getHora().get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(lembrete.getHora().get(Calendar.MINUTE));
				qtd.setText(lembrete.getTipo()+"");
				obs.setText(lembrete.getDescricao());
				if (lembrete.getAtivo()==1) {
					ativar.setChecked(true);
				} else {
					ativar.setChecked(false);
				}
			} if (lista.size()>1) { //lembrete 02
				lembrete2 = lista.get(1);
				timePicker2.setCurrentHour(lembrete2.getHora().get(Calendar.HOUR_OF_DAY));
				timePicker2.setCurrentMinute(lembrete2.getHora().get(Calendar.MINUTE));
				qtd2.setText(lembrete2.getTipo()+"");
				obs2.setText(lembrete2.getDescricao());
				if (lembrete2.getAtivo()==1) {
					ativar2.setChecked(true);
				} else {
					ativar2.setChecked(false);
				}
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao recuperar lembretes.");
		}
	}
	
	public void salvar(View view) {
		try {			
			lembreteDao = new LembreteDao(this);
			if (qtd.getText().toString().equals("") || obs.getText().toString().equals("")) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Preencha todos os campos do lembrete 1.");
			} else if (ativar.isChecked()==true) {
				lembrete = Lembrete.getLembrete(lembrete);
				lembrete.setAtivo(1);
				lembrete.setHora(this.getHora(timePicker));
				lembrete.setTipo(Double.parseDouble(qtd.getText().toString()));
				lembrete.setDescricao(obs.getText().toString());			
				int x = (int) lembreteDao.inserir(lembrete); //insere o lembrete e guarda o id, na primeira vez, pois naoe xiste no banco ainda
				if (lembrete.getId()==0) {
					lembrete.setId(x);
				}
				this.criarLembrete(lembrete);
			} else {
				lembrete.setAtivo(0);
				lembrete.setHora(this.getHora(timePicker));
				lembrete.setTipo(Double.parseDouble(qtd.getText().toString()));
				lembrete.setDescricao(obs.getText().toString());
				int x = (int) lembreteDao.inserir(lembrete);
				if (lembrete.getId()==0) {
					lembrete.setId(x);
				}
				cancelarLembrete(lembrete);
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao recuperar hora do lembrete 1.");
		}
	}
	
	public void salvar2(View view) {
		try {			
			lembreteDao = new LembreteDao(this);
			if (qtd2.getText().toString().equals("") || obs2.getText().toString().equals("")) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Preencha todos os campos do lembrete 2.");
			} else if (ativar2.isChecked()==true) {
				lembrete2 = Lembrete.getLembrete(lembrete2);
				lembrete2.setAtivo(1);
				lembrete2.setHora(this.getHora(timePicker2));
				lembrete2.setTipo(Double.parseDouble(qtd2.getText().toString()));
				lembrete2.setDescricao(obs2.getText().toString());			
				int x = (int) lembreteDao.inserir(lembrete2); //insere o lembrete e guarda o id, na primeira vez, pois naoe xiste no banco ainda
				if (lembrete2.getId()==0) {
					lembrete2.setId(x);
				}
				this.criarLembrete(lembrete2);
			} else {
				lembrete2.setAtivo(0);
				lembrete2.setHora(this.getHora(timePicker2));
				lembrete2.setTipo(Double.parseDouble(qtd2.getText().toString()));
				lembrete2.setDescricao(obs2.getText().toString());
				int x = (int) lembreteDao.inserir(lembrete2);
				if (lembrete2.getId()==0) {
					lembrete2.setId(x);
				}
				cancelarLembrete(lembrete2);
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao recuperar hora do lembrete 2.");
		}
	}
	
	private void criarLembrete(Lembrete lembrete) {
		try {
			myIntent = new Intent(this, TimerReceiver.class);
			myIntent.putExtra("lembrete_basal_id", lembrete.getId());
			myIntent.putExtra("lembrete_basal", lembrete.getTipo());
			myIntent.putExtra("lembrete_basal_obs", lembrete.getDescricao());
			pIntent = PendingIntent.getBroadcast(this, lembrete.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//seta o alarme			
			c = Calendar.getInstance();
			c.set(Calendar.MINUTE, min);
			c.set(Calendar.HOUR_OF_DAY, hora);
			if (c.getTimeInMillis()<=System.currentTimeMillis()) {
				c.add(Calendar.HOUR_OF_DAY, 24);
			}
			c.set(Calendar.SECOND, 0);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pIntent);
//Mensagem de sucesso
			msg = new Mensagem();
			msg.mensagemToast(this, "Lembrete diário ativado para "+hora+" horas "+min+" minutos.");
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "ERRO AO SETAR LEMBRETE.");
		}		
	}
	
	private void cancelarLembrete(Lembrete lembrete) {
		try {
			if (lembrete.getId()>0) {
				myIntent = new Intent(this, TimerReceiver.class);
				pIntent = PendingIntent.getBroadcast(this, lembrete.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//cancela o alarme			
				alarmManager.cancel(pIntent);
			}
			msg = new Mensagem();
			msg.mensagemToast(this, "Lembrete desativado.");
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "ERRO AO SETAR LEMBRETE.");
		}		
	}

	
	public Calendar getHora(TimePicker timePicker) { 
		Calendar c = Calendar.getInstance();
		try {
			hora = timePicker.getCurrentHour();
			min = timePicker.getCurrentMinute();
			c.set(Calendar.HOUR_OF_DAY, hora);
			c.set(Calendar.MINUTE, min);
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao recuperar hora.");
		}
		return c;
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
			if (qtd.getText().toString().equals("")) {
				qtd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			}
			if (qtd2.getText().toString().equals("")) {
				qtd2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			}	
		}else {
			if (qtd.getText().toString().length()>0) {
				qtd.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			}
			if (qtd2.getText().toString().length()>0) {
				qtd2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
			}
		}
	}
}
