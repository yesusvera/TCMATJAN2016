package br.com.diabetesvirtual.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.activity.InsulinaActivity;
import br.com.diabetesvirtual.util.Mensagem;

public class TimerReceiver extends BroadcastReceiver{
	private NotificationManager notificationManager;
	private PendingIntent piIntent;
    private int NOTIFICATION = 1111;
    Mensagem msg;
    double insulina=-1;
    double basal=-1;
    int id=-1;
    String basal_obs="";
	
	@Override
	public void onReceive(Context context, Intent intent){
		try {
			Bundle b = intent.getExtras();
			insulina = b.getDouble("lembrete_insulina");
			basal = b.getDouble("lembrete_basal");
			if (insulina>0) {
				Intent i_insulina = new Intent(context, InsulinaActivity.class);
				i_insulina.putExtra("lembrete_insulina", insulina);
				piIntent = PendingIntent.getActivity(context, 0,i_insulina,0);				
				NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			    builder.setSmallIcon(R.drawable.ic_launcher2);
			    builder.setContentTitle("Aplicar insulina");
			    builder.setContentText("Clique para informar a insulina");
			    builder.setContentIntent(piIntent);
			    builder.setDefaults(Notification.DEFAULT_ALL);
			    builder.setLights(0xff00ff00, 400, 400);
			    Notification notification = builder.build();
			    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			    notification.flags |= Notification.FLAG_SHOW_LIGHTS; 
			    notification.flags |= Notification.FLAG_AUTO_CANCEL;
			    notificationManager.notify(NOTIFICATION, notification);	
			}else if (basal>0) {
				id = b.getInt("lembrete_basal_id");
				basal_obs = b.getString("lembrete_basal_obs");
				Intent i_insulina = new Intent(context, InsulinaActivity.class);
				if (id==1) {
					i_insulina.putExtra("lembrete_basal", basal);
					i_insulina.putExtra("lembrete_basal_obs", basal_obs);
					i_insulina.putExtra("lembrete_basal_id", id);
				}else if (id==2) {
					i_insulina.putExtra("lembrete_basal2", basal);
					i_insulina.putExtra("lembrete_basal_obs2", basal_obs);
					i_insulina.putExtra("lembrete_basal_id2", id);
				}				
				piIntent = PendingIntent.getActivity(context, id,i_insulina,0);				
				NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			    builder.setSmallIcon(R.drawable.ic_launcher2);
			    builder.setContentTitle("Aplicar insulina Basal");
			    builder.setContentText(basal_obs);
			    builder.setContentIntent(piIntent);
			    builder.setDefaults(Notification.DEFAULT_ALL);
			    builder.setLights(0xff00ff00, 400, 400);
			    Notification notification = builder.build();
			    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			    notification.flags |= Notification.FLAG_SHOW_LIGHTS; 
			    notification.flags |= Notification.FLAG_AUTO_CANCEL;
			    notificationManager.notify(NOTIFICATION, notification);	
			}	
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(context, "Erro ao disparar lembrete.");
		}		
	}

}
