package br.com.diabetesvirtual.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.diabetesvirtual.model.Metas;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.Mensagem;

public class MetasDao {

	private static final String TABELA = "Metas";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	private Context context;
	private Mensagem msg;
	
	public MetasDao(Context ctx){
		helper = new ConnectDatabase(ctx);
		db = helper.getDatabase();
		this.context = ctx;
	}
	
	public Metas getMetas() throws Exception{
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA, null);
		Metas m = new Metas();
		if (c.getCount()>0) {
			c.moveToFirst();
			m.setId(c.getInt(c.getColumnIndex("id")));
			m.setG_inicial((c.getInt(c.getColumnIndex(Metas.COLUNA_G_INICIAL))));
			m.setG_final(c.getInt(c.getColumnIndex(Metas.COLUNA_G_FINAL)));
			m.setI_inicial((c.getInt(c.getColumnIndex(Metas.COLUNA_I_INICIAL))));
			m.setI_final(c.getInt(c.getColumnIndex(Metas.COLUNA_I_FINAL)));
			m.setC_inicial((c.getInt(c.getColumnIndex(Metas.COLUNA_C_INICIAL))));
			m.setC_final((c.getInt(c.getColumnIndex(Metas.COLUNA_C_FINAL))));
		}
		c.close();
		db.close();
		return m;
	}
	
	public long salvaOuAtualiza(Metas meta) throws Exception {
		long x=0;
		if (meta.getId() != 0) {
		   this.atualizar(meta);
	    } else {
		   ContentValues valores = new ContentValues();
		   valores.put(Metas.COLUNA_G_INICIAL, meta.getG_inicial());
		   valores.put(Metas.COLUNA_G_FINAL, meta.getG_final());
		   valores.put(Metas.COLUNA_I_INICIAL, meta.getI_inicial());
		   valores.put(Metas.COLUNA_I_FINAL, meta.getI_final());
		   valores.put(Metas.COLUNA_C_INICIAL, meta.getC_inicial());
		   valores.put(Metas.COLUNA_C_FINAL, meta.getC_final());
		   x = db.insertOrThrow(TABELA, null, valores);	
		   db.close();		   
		   msg = new Mensagem();
		   msg.mensagemToast(context, "Dados inseridos com sucesso.");
	   }
	   return x;
	}
	
	public void atualizar(Metas meta) throws Exception{
		ContentValues valores = new ContentValues();
		valores.put(Metas.COLUNA_G_INICIAL, meta.getG_inicial());
		valores.put(Metas.COLUNA_G_FINAL, meta.getG_final());
		valores.put(Metas.COLUNA_I_INICIAL, meta.getI_inicial());
		valores.put(Metas.COLUNA_I_FINAL, meta.getI_final());
		valores.put(Metas.COLUNA_C_INICIAL, meta.getC_inicial());
		valores.put(Metas.COLUNA_C_FINAL, meta.getC_final());
		db.update(TABELA, valores, "id="+meta.getId(), null);
		db.close();
		msg = new Mensagem();
		msg.mensagemToast(context, "Dados atualizados com sucesso.");
	}
	
}
