package br.com.diabetesvirtual.util;

import android.content.Context;
import android.widget.Toast;

public class Mensagem {

	public void mensagemToast(Context context, String mensagem) {
    	Toast toast = Toast.makeText(context, mensagem, Toast.LENGTH_LONG);
	    toast.show();
    }
	
}
