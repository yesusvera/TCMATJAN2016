package br.com.diabetesvirtual.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.MetasDao;
import br.com.diabetesvirtual.model.Metas;
import br.com.diabetesvirtual.util.Mensagem;

public class MetasActivity extends Activity {

	EditText metas_g_inicial;
	EditText metas_g_final;
	EditText metas_i_inicial;
	EditText metas_i_final;
	EditText metas_c_inicial;
	EditText metas_c_final;
	Metas meta;
	MetasDao metasDao;
	Mensagem msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.metas_inserir);
		metas_g_inicial = (EditText) findViewById(R.id.metas_g_inicial);
		metas_g_final = (EditText) findViewById(R.id.metas_g_final);
		metas_i_inicial = (EditText) findViewById(R.id.metas_i_inicial);
		metas_i_final = (EditText) findViewById(R.id.metas_i_final);
		metas_c_inicial = (EditText) findViewById(R.id.metas_c_inicial);
		metas_c_final = (EditText) findViewById(R.id.metas_c_final);	
		this.carregarVariaveis();
	}
	
	public void carregarVariaveis() {
		meta = Metas.getMetas(meta);
		metasDao = new MetasDao(this);
		try {
			meta = metasDao.getMetas();
			if (meta.getId() != 0) {
				metas_g_inicial.setText(meta.getG_inicial()+"");
				metas_g_final.setText(meta.getG_final()+"");
				metas_i_inicial.setText(meta.getI_inicial()+"");
				metas_i_final.setText(meta.getI_final()+"");
				metas_c_inicial.setText(meta.getC_inicial()+"");
				metas_c_final.setText(meta.getC_final()+"");
			}
		} catch (Exception e) {
			 msg = new Mensagem();
			 msg.mensagemToast(this, "Erro ai recuperar dados");
		}
	}
	
	public void salvar(View view) {
		meta = Metas.getMetas(meta);
		if (this.validaCampos()) {
			meta.setG_inicial(Integer.parseInt(metas_g_inicial.getText().toString()));
			meta.setG_final(Integer.parseInt(metas_g_final.getText().toString()));
			meta.setI_inicial(Integer.parseInt(metas_i_inicial.getText().toString()));
			meta.setI_final(Integer.parseInt(metas_i_final.getText().toString()));
			meta.setC_inicial(Integer.parseInt(metas_c_inicial.getText().toString()));
			meta.setC_final(Integer.parseInt(metas_c_final.getText().toString()));
			try {
				metasDao = new MetasDao(this);
				metasDao.salvaOuAtualiza(meta);
				this.fechar();
			} catch (Exception e) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Erro ao salvar dados.");
			}

		} else {
			msg = new Mensagem();
			msg.mensagemToast(this, "Informe todos os campos.");
		}
	}
	
	public void fechar() {
		finish();
		return;
	}
	
	public Boolean validaCampos() {
		if (metas_g_inicial.getText().toString().equals("") || metas_g_final.getText().toString().equals("")
				|| metas_i_inicial.getText().toString().equals("") || metas_i_final.getText().toString().equals("")
						|| metas_c_inicial.getText().toString().equals("") || metas_c_final.getText().toString().equals("")) {
			return false;
			
		} else {
			return true;
		}
	}	
	
	public void voltar(View view) {
		Intent i = new Intent(this, PerfilActivity.class);
		startActivity(i);
		finish();
		return;
	}
}
