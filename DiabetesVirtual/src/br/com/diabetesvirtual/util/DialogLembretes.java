package br.com.diabetesvirtual.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.activity.RefeicaoActivity;
import br.com.diabetesvirtual.service.TimerReceiver;

public class DialogLembretes implements OnClickListener, OnCheckedChangeListener{
	Calendar c = Calendar.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault());
	AlarmManager alarmManager;
	Intent myIntent;
	PendingIntent piIntent;
	Context context;
	Dialog dialog;
	Mensagem msg;
	Button cancel;
	Button ok;
	double insulina;
	RadioGroup radioGroup;
	int minutos = 2;
	
	public DialogLembretes(Context ctx) {
		this.context = ctx;
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 	
	}
	
	public void lembrar(double insulina) {
		try {
			this.insulina = insulina;
			dialog = new Dialog(context, R.style.tema_dialogo);
			dialog.setTitle("Lembrar em");
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_lembrete);
			cancel = (Button) dialog.findViewById(R.id.ins_lem_cancel);
			cancel.setOnClickListener(this);
			ok = (Button) dialog.findViewById(R.id.ins_lem_ok);
			ok.setOnClickListener(this);
			radioGroup = (RadioGroup) dialog.findViewById(R.id.lem_radioGroup);
			radioGroup.setOnCheckedChangeListener(this);
			dialog.show();			
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(context, "Erro ao salvar lembrete.");
		}
	}
	

	@Override
	public void onClick(View v) {
		if (v == cancel) {
			dialog.dismiss();
		} else {
			this.criarLembrete();
			RefeicaoActivity.dialogInsulina.dismiss();
			dialog.dismiss();
		}		
	}

	
	private void criarLembrete() {
		try {
			myIntent = new Intent(context, TimerReceiver.class);
			myIntent.putExtra("lembrete_insulina", insulina);
			piIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
//seta o alarme			
			c.setTimeInMillis(System.currentTimeMillis());
			c.add(Calendar.MINUTE, minutos);
			alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), piIntent);
//Mensagem de sucesso
			msg = new Mensagem();
			msg.mensagemToast(context, "Lembrete setado para: "+format.format(c.getTime()));
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(context, "ERRO AO SETAR LEMBRETE.");
		}		
	}

	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.lem_2min:
			minutos = 2;
			break;
		case R.id.lem_5min:
			minutos = 5;
			break;	
		case R.id.lem_10min:
			minutos = 10;
			break;
		case R.id.lem_15min:
			minutos = 15;
			break;	
		case R.id.lem_30min:
			minutos = 30;
			break;	
		case R.id.lem_45min:
			minutos = 45;
			break;
		case R.id.lem_1hora:
			minutos = 60;
			break;	
		
		}
		
	}
}
