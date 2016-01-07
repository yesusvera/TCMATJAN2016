package br.com.diabetesvirtual.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.diabetesvirtual.model.Insulina;
import br.com.diabetesvirtual.model.InsulinaTipos;
import br.com.diabetesvirtual.util.ConnectDatabase;

public class InsulinaDao {

	private static final String TABELA = "Insulina";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	
	public InsulinaDao(Context ctx) {
		helper = new ConnectDatabase(ctx);
		db = helper.getDatabase();	
	}
	
	public long inserir(Insulina i,boolean forceInsert) throws Exception {
	    long x = 0;
		if (i.getId()>0 && !forceInsert) {
			this.atualizar(i);
		} else {
			ContentValues valores = new ContentValues();
		    valores.put("data", i.getData().getTimeInMillis());
		    valores.put("obs", i.getObs());
		    valores.put("tipo", i.getTipo());
		    valores.put("quantidade", i.getQtd());
		    x = db.insert(TABELA, null, valores);	
		    i.setId((int)x);
		}		
	    return  x;   
	}
	
	public void deletar(Insulina insulina) {
		db.delete(TABELA, "id = "+insulina.getId(), null);
		db.close();
	}
	
	public void atualizar(Insulina i) throws Exception{
		ContentValues valores = new ContentValues();
	    valores.put("data", i.getData().getTimeInMillis());
	    valores.put("obs", i.getObs());
	    valores.put("tipo", i.getTipo());
	    valores.put("quantidade", i.getQtd());
	    db.update(TABELA, valores, "id = "+i.getId(), null);
	    db.close();
	}
	
	public List<Insulina> getAll() throws Exception{
		List<Insulina> lista = new ArrayList<Insulina>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" ORDER BY data DESC", null);
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Insulina i = new Insulina();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setQtd(c.getDouble(c.getColumnIndex("quantidade")));
				lista.add(i);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}	
	
	public Insulina findByID(long id) throws Exception{
		Insulina i = null;
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE id = ? ORDER BY data DESC", new String[]{String.valueOf(id)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				i = new Insulina();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setQtd(c.getDouble(c.getColumnIndex("quantidade")));
				break;
			} while (c.moveToNext());
			c.close();
			db.close();		
		} 
		return i;
	}	
	
	public List<Insulina> listarPorTipo(InsulinaTipos tipo) throws Exception{
		List<Insulina> lista = new ArrayList<Insulina>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE tipo = ? ORDER BY data DESC", new String[]{String.valueOf(tipo.getNome())});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Insulina i = new Insulina();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
//				i.setTipo(InsulinaTipos.forInsulina(c.getInt(c.getColumnIndex("tipo"))));
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setQtd(c.getDouble(c.getColumnIndex("quantidade")));
				lista.add(i);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}	
	
	public List<Insulina> listarPorTipoASC(InsulinaTipos tipo) throws Exception{
		List<Insulina> lista = new ArrayList<Insulina>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE tipo = ? ORDER BY data", new String[]{String.valueOf(tipo.getCod())});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Insulina i = new Insulina();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
//				i.setTipo(InsulinaTipos.forInsulina(c.getInt(c.getColumnIndex("tipo"))));
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setQtd(c.getDouble(c.getColumnIndex("quantidade")));
				lista.add(i);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}	
	
	public List<Insulina> bucarPorIntervaloData(Long inicio, Long fim) throws Exception{
		List<Insulina> lista = new ArrayList<Insulina>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE data >= ? AND data <= ? ORDER BY data DESC", new String [] {String.valueOf(inicio), String.valueOf(fim)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Insulina insulina = new Insulina();
				insulina.setId(c.getInt(c.getColumnIndex("id")));
				Long a = c.getLong(c.getColumnIndex("data"));
				insulina.getData().setTimeInMillis(a);
				insulina.setQtd(c.getDouble(c.getColumnIndex("quantidade")));
				insulina.setObs(c.getString(c.getColumnIndex("obs")));	
//				insulina.setTipo(InsulinaTipos.forInsulina(c.getInt(c.getColumnIndex("tipo"))));
				insulina.setTipo(c.getString(c.getColumnIndex("tipo")));
				lista.add(insulina);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		}
		return null;
	}
	
	public List<Insulina> bucarPorIntervaloDataTipo(Long inicio, Long fim, InsulinaTipos tipo) throws Exception{
		List<Insulina> lista = new ArrayList<Insulina>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE data >= ? AND data <= ? AND tipo = ? ORDER BY data DESC", new String [] {String.valueOf(inicio), String.valueOf(fim), String.valueOf(tipo.getCod())});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Insulina insulina = new Insulina();
				insulina.setId(c.getInt(c.getColumnIndex("id")));
				Long a = c.getLong(c.getColumnIndex("data"));
				insulina.getData().setTimeInMillis(a);
				insulina.setQtd(c.getDouble(c.getColumnIndex("quantidade")));
				insulina.setObs(c.getString(c.getColumnIndex("obs")));	
//				insulina.setTipo(InsulinaTipos.forInsulina(c.getInt(c.getColumnIndex("tipo"))));
				insulina.setTipo(c.getString(c.getColumnIndex("tipo")));
				lista.add(insulina);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		}
		return null;
	}
	
	public List<Insulina> bucarPorDataHora(Calendar inicio, Calendar fim) throws Exception{
		List<Insulina> lista = new ArrayList<Insulina>();
		Calendar ini = Calendar.getInstance();
		ini.set(inicio.get(Calendar.YEAR), inicio.get(Calendar.MONTH), inicio.get(Calendar.DAY_OF_MONTH), inicio.get(Calendar.HOUR_OF_DAY), inicio.get(Calendar.MINUTE));
		Calendar x = Calendar.getInstance();
		x.set(Calendar.HOUR_OF_DAY, fim.get(Calendar.HOUR_OF_DAY));
		x.set(Calendar.MINUTE, fim.get(Calendar.MINUTE));
		if (inicio.get(Calendar.HOUR_OF_DAY) > fim.get(Calendar.HOUR_OF_DAY)) {
			x.set(inicio.get(Calendar.YEAR), inicio.get(Calendar.MONTH), (inicio.get(Calendar.DAY_OF_MONTH)+1));
		} else {
			x.set(inicio.get(Calendar.YEAR), inicio.get(Calendar.MONTH), inicio.get(Calendar.DAY_OF_MONTH));
		}
		while (ini.getTimeInMillis() <= fim.getTimeInMillis()) {
			Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE data >= ? AND data <= ? ORDER BY data", new String [] {String.valueOf(ini.getTimeInMillis()), String.valueOf(x.getTimeInMillis())});
			if (c.getCount()>0) {
				c.moveToFirst();
				do  {		
					Insulina insulina = new Insulina();
					insulina.setId(c.getInt(c.getColumnIndex("id")));
					Long a = c.getLong(c.getColumnIndex("data"));
					insulina.getData().setTimeInMillis(a);
					insulina.setQtd(c.getDouble(c.getColumnIndex("quantidade")));
					insulina.setObs(c.getString(c.getColumnIndex("obs")));	
//					insulina.setTipo(InsulinaTipos.forInsulina(c.getInt(c.getColumnIndex("tipo"))));
					insulina.setTipo(c.getString(c.getColumnIndex("tipo")));
					lista.add(insulina);	
				} while (c.moveToNext());
			}	
			ini.add(Calendar.DAY_OF_MONTH, 1);
			x.add(Calendar.DAY_OF_MONTH, 1);
		}
		db.close();	
		return lista;
	}
	
	
	
}
