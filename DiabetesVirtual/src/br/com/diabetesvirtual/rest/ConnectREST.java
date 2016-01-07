package br.com.diabetesvirtual.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;
import br.com.diabetesvirtual.dao.SyncRESTDao;
import br.com.diabetesvirtual.model.SyncREST;
import br.com.diabetesvirtual.prop.ConstantesREST;
import br.com.diabetesvirtual.util.ConnectionNetwork;
import br.com.docedesafio.services.model.Mensagem;
import cz.msebera.android.httpclient.Header;

public class ConnectREST {

	private String constanteService;
	private final Activity context;
//	private boolean excluirRegistroSyncAposSucesso;
	private Integer idSyncREST;
	
	
	public ConnectREST(String CONSTANTE_SERVICE, Activity context){
		this.constanteService = CONSTANTE_SERVICE;
		this.context = context;
	}
	
	public void sincronizacaoDeOfflinePorID(Integer idSyncREST){
		this.idSyncREST = idSyncREST;
	}
	
	/**
	 * Método genérico para enviar parâmetros via get.
	 * @param getParams
	 */
	@SuppressWarnings("deprecation")
	public void asyncExecute(final HashMap<String, String> getParams) {
		String strURL = ConstantesREST.getURLService(constanteService);
		
		if(getParams!=null && getParams.size()>0){
			strURL += "?";
			
			Set<String> keys = getParams.keySet();
			
			boolean first = true;
			
			for(String key: keys){
				if(!first){
					strURL += "&";
				}
				try {
					strURL += key + "=" + URLEncoder.encode(getParams.get(key), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				first = false;
			}
		}

		Log.i("REST URL ", strURL);
		AsyncHttpClient client = new AsyncHttpClient();
		
		if(!ConnectionNetwork.verifiedInternetConnection(context)){
			
			
			//SALVAR NA TABELA SYNCREST
			if(getParams.get("codigo")!=null && getParams.get("idLogin")!=null){
				SyncRESTDao syncRESTDao = new SyncRESTDao(context);
				
				int tipoTabela = 0;
				int operacao = 0;

				if(constanteService.equals(ConstantesREST.INSERT_ALIMENTO_SERVICE)){ tipoTabela = 1; operacao = 1;}
				if(constanteService.equals(ConstantesREST.INSERT_EXERCICIO_SERVICE)){ tipoTabela = 2; operacao = 1;}
				if(constanteService.equals(ConstantesREST.INSERT_GLICEMIA_SERVICE)){ tipoTabela = 3; operacao = 1;}
				if(constanteService.equals(ConstantesREST.INSERT_INSULINA_SERVICE)){ tipoTabela = 4; operacao = 1;}
				if(constanteService.equals(ConstantesREST.INSERT_REFEICAO_SERVICE)){ tipoTabela = 5;operacao = 1; }

				if(constanteService.equals(ConstantesREST.DELETE_ALIMENTO_SERVICE)){  tipoTabela = 1; operacao = 2;}
				if(constanteService.equals(ConstantesREST.DELETE_EXERCICIO_SERVICE)){  tipoTabela = 2; operacao = 2;}
				if(constanteService.equals(ConstantesREST.DELETE_GLICEMIA_SERVICE)){  tipoTabela = 3; operacao = 2; }
				if(constanteService.equals(ConstantesREST.DELETE_INSULINA_SERVICE)){  tipoTabela = 4; operacao = 2;}
				if(constanteService.equals(ConstantesREST.DELETE_REFEICAO_SERVICE)){  tipoTabela = 5; operacao = 2; }
				
				try {
					SyncREST syncRest = new SyncREST();
					syncRest.setId(idSyncREST);
					syncRest.setCodigo(Integer.valueOf(getParams.get("codigo")));
					syncRest.setOperacao(operacao);
					syncRest.setTipoTabela(tipoTabela);
					syncRESTDao.salvar(syncRest);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			// called when response HTTP status is "4XX" (eg.
			// 401, 403, 404)
			String mensagemErro = "Salvo offline. Registro não sincronizado.";
			Toast toast = Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT);
			toast.show();
		}else{
				client.get(strURL, new AsyncHttpResponseHandler() {
					ProgressDialog dialog = null;
		
					@Override
					public void onStart() {
							dialog = ProgressDialog.show(context, "",
									"Sincronizando com servidor, aguarde.", true);
					}
		
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] response) {
						String xmlRetorno = new String(response).toString();
						XStream xStream = new XStream(new DomDriver());
						Log.i("xmlRetornoLogin", xmlRetorno);
						
						Mensagem mensagem = (Mensagem) xStream.fromXML(xmlRetorno);
						
		
						Toast toast = Toast.makeText(context, mensagem.getMensagem(), Toast.LENGTH_SHORT);
						toast.show();
						
						
						dialog.dismiss();
						
						if(!mensagem.isErro()){
							if(idSyncREST!=null){
								try {
									new SyncRESTDao(context).deletar(idSyncREST);
									Toast toast2 = Toast.makeText(context, "Sincronizado com sucesso!", Toast.LENGTH_SHORT);
									toast2.show();
									
									new SyncRESTBo().sincronizarRegistros(context);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
		
					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
						Log.i("Erro Sincronização", e.toString());
						Toast toast2 = Toast.makeText(context, "("+statusCode+")Erro ao sincronizar o registro, o servidor pode estar fora do ar ou você não possui conexão com a internet.", Toast.LENGTH_SHORT);
						toast2.show();
						dialog.dismiss();
					}
		
					@Override
					public void onRetry(int retryNo) {
					}
				});
		}
	}
}
