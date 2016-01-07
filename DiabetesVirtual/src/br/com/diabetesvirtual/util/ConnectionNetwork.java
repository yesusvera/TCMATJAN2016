package br.com.diabetesvirtual.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionNetwork {

	public static boolean verifiedInternetConnection(Activity context){
		 boolean conectado;
			ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    if (conectivtyManager.getActiveNetworkInfo() != null
		            && conectivtyManager.getActiveNetworkInfo().isAvailable()
		            && conectivtyManager.getActiveNetworkInfo().isConnected()) {
		    	conectado = true;
		    } else {
		        conectado = false;
		    }
		    return conectado;
	}
}