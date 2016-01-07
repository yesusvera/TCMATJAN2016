package br.com.diabetesvirtual.activity;

import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.bo.SyncInit;
import br.com.diabetesvirtual.dao.LoginDao;
import br.com.diabetesvirtual.listactivity.ActivityBase;
import br.com.diabetesvirtual.model.Login;
import br.com.diabetesvirtual.prop.ConstantesREST;
import br.com.diabetesvirtual.rest.ConvertObject;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.ConnectionNetwork;
import br.com.docedesafio.model.Usuario;
import br.com.docedesafio.services.model.Authenticate;
import cz.msebera.android.httpclient.Header;


public class LoginActivity extends ActivityBase {

	private EditText usuario;
	private EditText senha;
	private CheckBox manterConectado;
	public static Authenticate usuarioLogado = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		usuario = (EditText) findViewById(R.id.usuario);
		senha = (EditText) findViewById(R.id.senha);
		manterConectado = (CheckBox) findViewById(R.id.manterConectado);
		
//		SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
//		boolean conectado = preferencias.getBoolean(MANTER_CONECTADO, false);
		
		LoginDao loginDao = new LoginDao(LoginActivity.this);
		
		try {
			List<Login> lista = loginDao.getAll();

			if (lista != null && lista.size()>0 ) {
				
				Login loginAtual = lista.get(0);
				
				Usuario usr = new Usuario();
				usr.setId(loginAtual.getId());
				
				usuarioLogado = new Authenticate();
				usuarioLogado.setUser(usr);
				
				startActivity(new Intent(this, MainActivity.class));
				finish();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void entrarOnClick(View v) {
		final String user = usuario.getText().toString();
		final String password = senha.getText().toString();
		
		if(user==null || password==null || user.trim().isEmpty() || password.trim().isEmpty()){
			String mensagemErro = "É necessário informar o usuário e senha.";
			Toast toast = Toast.makeText(LoginActivity.this, mensagemErro, Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		
		
		if(!ConnectionNetwork.verifiedInternetConnection(this)){
			String mensagemErro = "Falha ao conectar no servidor.";
			Toast toast = Toast.makeText(LoginActivity.this, mensagemErro, Toast.LENGTH_SHORT);
			toast.show();
			
			return;
		}
		
		String strURL= ConstantesREST.getURLService(ConstantesREST.AUTHENTICATION_SERVICE) +
									"?user="+user+"&password="+password;

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(strURL, new AsyncHttpResponseHandler() {
			
			ProgressDialog dialog = null;

		    @Override
		    public void onStart() {
		    	dialog = ProgressDialog.show(LoginActivity.this, "", 
	                    "Realizando a autenticação, aguarde por favor.", true);
		    }

		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		    	String xmlRetorno = new String(response).toString();
				XStream xStream = new XStream(new DomDriver());
				Log.i("xmlRetornoLogin", xmlRetorno);
				
				Authenticate auth = (Authenticate) xStream.fromXML(xmlRetorno);
		    	
				if(!auth.getMessage().isErro()){
					
//					dialog.dismiss();
					
					usuarioLogado = auth;
					
					LoginDao loginDao = new LoginDao(LoginActivity.this);
					
					boolean usuarioExistente = false;
					
					try {
						List<Login> lista = loginDao.getAll();
						if(lista != null && lista.size()>0){
							for(Login lg: lista){
								if(auth.getUser().getLogin().equals(lg.getLogin()) &&
									auth.getUser().getSenha().equals(lg.getSenha())){
									usuarioExistente = true;
								}
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					
					
					if(!usuarioExistente){
						try {
							//ZERO A BASE
							ConnectDatabase connectDatabase = new ConnectDatabase(LoginActivity.this);
							connectDatabase.zerarBase();
							
							Login loginInsert = ConvertObject.convertFromUsuario(auth.getUser());
//							loginInsert.setId(null);
							loginDao.inserir(loginInsert, true);
							
//							dialog = ProgressDialog.show(LoginActivity.this, "", 
//				                    "Realizando sincronização dos registros da web.", true);
							new SyncInit().synchronize(auth, LoginActivity.this);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
//					SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
//					Editor editor = preferencias.edit();
//					editor.putBoolean(MANTER_CONECTADO, true);
//					editor.putBoolean(MANTER_CONECTADO, manterConectado.isChecked());
//					editor.commit();

					dialog.dismiss();
					
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					
					finish();
				}else{
					String mensagemErro = getString(R.string.erro_autenticacao);
					Toast toast = Toast.makeText(LoginActivity.this, mensagemErro, Toast.LENGTH_SHORT);
					toast.show();
				}
				
		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    	String mensagemErro = "Falha ao conectar no servidor.";
				Toast toast = Toast.makeText(LoginActivity.this, mensagemErro, Toast.LENGTH_SHORT);
				toast.show();
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
	}
}