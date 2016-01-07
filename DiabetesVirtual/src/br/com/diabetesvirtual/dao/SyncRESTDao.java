package br.com.diabetesvirtual.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.diabetesvirtual.model.SyncREST;
import br.com.diabetesvirtual.util.ConnectDatabase;

public class SyncRESTDao {

	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	private final String TABELA = "SyncREST";

	public SyncRESTDao(Context ctx){
		helper = new ConnectDatabase(ctx);
		db = helper.getDatabase();	
	}
	
	public void salvar(SyncREST syncRest) throws Exception{
		if (syncRest.getId()!=null) {
			this.atualizar(syncRest);
		} else {
			ContentValues values = new ContentValues();
			values.put("tipo_tabela", syncRest.getTipoTabela());
			values.put("codigo", syncRest.getCodigo());
			values.put("operacao", syncRest.getOperacao());
			syncRest.setId((int)db.insertOrThrow(TABELA, null, values));
		}		
	}
	
	private void atualizar (SyncREST syncRest) throws Exception{
		ContentValues values = new ContentValues();
		values.put("tipo_tabela", syncRest.getTipoTabela());
		values.put("codigo", syncRest.getCodigo());
		values.put("operacao", syncRest.getOperacao());
		db.update(TABELA, values, "id = "+syncRest.getId(), null);
	}
	
	public void deletar(int tipoTabela, int codigo) throws Exception{
		db.delete(TABELA, "tipoTabela ="+tipoTabela + " and codigo=" + codigo, null);
	}
	
	public void deletar(int id) throws Exception{
		db.delete(TABELA, "id ="+id, null);
	}
	
	public List<SyncREST> listar() {
		List<SyncREST> lista = new ArrayList<SyncREST>();
		Cursor c;
		try {
			c = db.rawQuery("SELECT * FROM " + TABELA, null);
			if(c.moveToFirst()){		
				do  {		
					SyncREST syncRest = new SyncREST();
					syncRest.setId(c.getInt(c.getColumnIndex("id")));
					syncRest.setTipoTabela(c.getInt(c.getColumnIndex("tipo_tabela")));
					syncRest.setCodigo(c.getInt(c.getColumnIndex("codigo")));
					syncRest.setOperacao(c.getInt(c.getColumnIndex("operacao")));
					lista.add(syncRest);	
				} while (c.moveToNext());
			}
			c.close();
			db.close();		
			
		} catch (Exception e) {
			Log.e("SyncRestDAO", "Erro ao buscar as sincronizações." + e.toString());
		}
		return lista;
	}
}