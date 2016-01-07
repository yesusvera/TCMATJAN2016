package br.com.diabetesvirtual.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.service.TimerReceiver;
import br.com.diabetesvirtual.util.Mensagem;

public class AlertaActivity extends Activity{
	Calendar c = Calendar.getInstance();
	AlarmManager alarmManager;
	Intent myIntent;
	PendingIntent piIntent;
	Mensagem msg;
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarme_inserir);
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); 			
	}
	
	public void setAlarmInsulina(double insulina) {
		try {
			myIntent = new Intent(this, TimerReceiver.class);
			myIntent.putExtra("lembrete_insulina", insulina);
			piIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
			//seta o alarme			
			c.setTimeInMillis(System.currentTimeMillis());
			c.add(Calendar.SECOND, 10);
			alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), piIntent);
		
			msg = new Mensagem();
			msg.mensagemToast(this, "Alarme setado para: "+format.format(c.getTime()));
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "ERRO AO SETAR ALARME.");
		}
		
	}
}
