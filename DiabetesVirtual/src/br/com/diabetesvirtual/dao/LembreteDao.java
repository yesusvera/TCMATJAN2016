package br.com.diabetesvirtual.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.diabetesvirtual.model.Lembrete;
import br.com.diabetesvirtual.util.ConnectDatabase;

public class LembreteDao { 

	private static final String TABELA = "lembrete";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	
	public LembreteDao(Context ctx){
		helper = new ConnectDatabase(ctx);
		db = helper.getDatabase();	
	}
	
	
	public List<Lembrete> getLembrete() throws Exception{
		List<Lembrete> lista = new ArrayList<Lembrete>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA, null);
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Lembrete lem = new Lembrete();
				lem.setId(c.getInt(c.getColumnIndex("id")));
				Long a = c.getLong(c.getColumnIndex("hora"));
				lem.getHora().setTimeInMillis(a);
				lem.setDescricao(c.getString(c.getColumnIndex("descricao")));
				lem.setTipo(c.getInt(c.getColumnIndex("tipo")));				
				lem.setAtivo(c.getInt(c.getColumnIndex("ativo")));
				lista.add(lem);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return lista;
	}
	
	
	public long inserir(Lembrete g) throws Exception {
		long x = 0;
		if (g.getId()>0) {
			this.atualiza(g);
		} else {
			ContentValues valores = new ContentValues();
		    valores.put("hora", g.getHora().getTimeInMillis());
		    valores.put("descricao", g.getDescricao());
		    valores.put("tipo", g.getTipo());
		    valores.put("ativo", g.getAtivo());
		    x = db.insert(TABELA, null, valores);	
		}		
	    return  x;   
	}
	
	public void atualiza(Lembrete g) throws Exception{
		ContentValues valores = new ContentValues();
		valores.put("hora", g.getHora().getTimeInMillis());
		valores.put("descricao", g.getDescricao());
		valores.put("tipo", g.getTipo());
		valores.put("ativo", g.getAtivo());
	    db.update(TABELA, valores, "id = "+g.getId(), null);
	    db.close();
	}
	
	public void deletar(Lembrete g) throws Exception{
		db.delete(TABELA, "id = "+g.getId(), null);
		db.close();
	}
	
	
	
	
	
	
	

}