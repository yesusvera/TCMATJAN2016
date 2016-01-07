package br.com.diabetesvirtual.listactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import br.com.diabetesvirtual.rest.SyncRESTBo;
import br.com.diabetesvirtual.util.ConnectionNetwork;

public class ActivityBase extends Activity{

//	public static final String MANTER_CONECTADO = "manter_conectado";
	
	@Override
	protected void onResume() {
		super.onResume();
		
		syncRegistrosOffline(false);
	}
	
	protected void syncRegistrosOffline(boolean showNaoPossui) {
		
		SyncRESTBo syncRESTBo =  new SyncRESTBo();
		int qtdRegistrosSincronizacao = syncRESTBo.qtdeRegistrosSincronizacao(this);
		
		if(!ConnectionNetwork.verifiedInternetConnection(this)){
			if(showNaoPossui){
				AlertDialog.Builder msg4 = new AlertDialog.Builder(this);
				msg4.setMessage("Você não possui registros para sincronização.");
				msg4.setNeutralButton("OK", null);
				msg4.show();
			}
			return;
		}
		
		//************* 
		
		
		AlertDialog.Builder msg4 = new AlertDialog.Builder(this);
		msg4.setTitle("Sincronização");
		
		
		if(qtdRegistrosSincronizacao > 0){
			msg4.setMessage("Você possui " + qtdRegistrosSincronizacao + " registros para sincronização, deseja sincronizar agora?");
			msg4.setNeutralButton("Não", null);
			msg4.setNegativeButton("Sim", new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i("SYNC", "****** SINCRONIZANDO APP *******");
					new SyncRESTBo().sincronizarRegistros(ActivityBase.this);
				}
			});
			msg4.show();
		}else{
			if(showNaoPossui){
				msg4.setMessage("Você não possui registros para sincronização.");
				msg4.setNeutralButton("OK", null);
				msg4.show();
			}
		}
	}
}
