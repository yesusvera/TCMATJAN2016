package br.com.diabetesvirtual.dao;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.diabetesvirtual.model.Glicemia;
import br.com.diabetesvirtual.util.ConnectDatabase;


public class GlicemiaDao {
	
	private static final String TABELA = "Glicemia";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	
	public GlicemiaDao(Context ctx){
		helper = new ConnectDatabase(ctx);
		db = helper.getDatabase();	
	}
	
	public long inserir(Glicemia g,boolean forceInsert) throws Exception {
		long x = 0;
		if (g.getId()>0 && !forceInsert) {
			this.atualiza(g);
		} else {
			Log.i("Glicemia", "entrou glicemia dao");
			ContentValues valores = new ContentValues();
			valores.put("tipo", g.getTipo());
		    valores.put("data", g.getData().getTimeInMillis());
		    valores.put("obs", g.getObs());
		    valores.put("medida", g.getMedida());
		    
		    x = db.insert(TABELA, null, valores);	
		    g.setId((int)x);
		}		
		db.close();
	    return  x;   
	}
	
	public void atualiza(Glicemia g) throws Exception{
		ContentValues valores = new ContentValues();
		valores.put("tipo", g.getTipo());
	    valores.put("data", g.getData().getTimeInMillis());
	    valores.put("obs", g.getObs());
	    valores.put("medida", g.getMedida());
	    db.update(TABELA, valores, "id = "+g.getId(), null);
	    db.close();
	}
	
	public void deletar(Glicemia g) throws Exception{
		db.delete(TABELA, "id = "+g.getId(), null);
		db.close();
	}
	
	public Glicemia getUltima() throws Exception{ //retorna null se nao achar
		Glicemia glic = new Glicemia();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" ORDER BY data DESC", null);
		if (c.getCount()>0) {
			c.moveToFirst();			
			glic.setId(c.getInt(c.getColumnIndex("id")));
//			glic.setTipo(GlicemiaTipos.forGlicemia(c.getInt(c.getColumnIndex("tipo"))));
			glic.setTipo(c.getString(c.getColumnIndex("tipo")));
			Long a = c.getLong(c.getColumnIndex("data"));
			glic.getData().setTimeInMillis(a);
			glic.setMedida(c.getInt(c.getColumnIndex("medida")));
			glic.setObs(c.getString(c.getColumnIndex("obs")));				
			c.close();
			db.close();		
			return glic;
		} else {
			return null;
		}
		
	}
	
	public List<Glicemia> getAll() throws Exception{
		List<Glicemia> lista = new ArrayList<Glicemia>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" ORDER BY data DESC", null);
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Glicemia glic = new Glicemia();
				glic.setId(c.getInt(c.getColumnIndex("id")));
//				glic.setTipo(GlicemiaTipos.forGlicemia(c.getInt(c.getColumnIndex("tipo"))));
				glic.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				glic.getData().setTimeInMillis(a);
				glic.setMedida(c.getInt(c.getColumnIndex("medida")));
				glic.setObs(c.getString(c.getColumnIndex("obs")));				
				lista.add(glic);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}	
	
	public List<Glicemia> bucarPorIntervaloData(Long inicio, Long fim) throws Exception{
		List<Glicemia> lista = new ArrayList<Glicemia>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE data >= ? AND data <= ? ORDER BY data DESC", new String [] {String.valueOf(inicio), String.valueOf(fim)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Glicemia glic = new Glicemia();
				glic.setId(c.getInt(c.getColumnIndex("id")));
//				glic.setTipo(GlicemiaTipos.forGlicemia(c.getInt(c.getColumnIndex("tipo"))));
				glic.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				glic.getData().setTimeInMillis(a);
				glic.setMedida(c.getInt(c.getColumnIndex("medida")));
				glic.setObs(c.getString(c.getColumnIndex("obs")));				
				lista.add(glic);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		}
		return null;
	}
	
	public List<Glicemia> bucarPorIntervaloGlic(int inicio, int fim) throws Exception{
		List<Glicemia> lista = new ArrayList<Glicemia>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE medida >= ? AND medida <= ? ORDER BY data DESC", new String [] {String.valueOf(inicio), String.valueOf(fim)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Glicemia glic = new Glicemia();
				glic.setId(c.getInt(c.getColumnIndex("id")));
//				glic.setTipo(GlicemiaTipos.forGlicemia(c.getInt(c.getColumnIndex("tipo"))));
				glic.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				glic.getData().setTimeInMillis(a);
				glic.setMedida(c.getInt(c.getColumnIndex("medida")));
				glic.setObs(c.getString(c.getColumnIndex("obs")));				
				lista.add(glic);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		}
		return lista;
	}
	
	public List<Glicemia> bucarPorDataHora(Calendar inicio, Calendar fim) throws Exception{
		List<Glicemia> lista = new ArrayList<Glicemia>();
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
					Glicemia glic = new Glicemia();
					glic.setId(c.getInt(c.getColumnIndex("id")));
//					glic.setTipo(GlicemiaTipos.forGlicemia(c.getInt(c.getColumnIndex("tipo"))));
					glic.setTipo(c.getString(c.getColumnIndex("tipo")));
					Long a = c.getLong(c.getColumnIndex("data"));
					glic.getData().setTimeInMillis(a);
					glic.setMedida(c.getInt(c.getColumnIndex("medida")));
					glic.setObs(c.getString(c.getColumnIndex("obs")));				
					lista.add(glic);	
				} while (c.moveToNext());
			}	
			ini.add(Calendar.DAY_OF_MONTH, 1);
			x.add(Calendar.DAY_OF_MONTH, 1);
		}
		db.close();	
		return lista;
	}
	public List<Glicemia> listarPorTipo(String tipo) throws Exception{
		List<Glicemia> lista = new ArrayList<Glicemia>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE tipo = ? ORDER BY data DESC", new String[]{String.valueOf(tipo)});
		
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Glicemia i = new Glicemia();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setMedida(c.getInt(c.getColumnIndex("medida")));
				lista.add(i);	
				Log.i(tipo, i.getObs());
				Log.i(tipo, i.getTipo());
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}	

	public Glicemia findByID(long id) throws Exception{
		Glicemia glicemia = null;
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE id = ? ORDER BY data DESC", new String[]{String.valueOf(id)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				glicemia = new Glicemia();
				glicemia.setId(c.getInt(c.getColumnIndex("id")));
				glicemia.setObs(c.getString(c.getColumnIndex("obs")));	
				glicemia.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				glicemia.getData().setTimeInMillis(a);
				glicemia.setMedida(c.getInt(c.getColumnIndex("medida")));
				break;
			} while (c.moveToNext());
			c.close();
			db.close();		
		} 
		return glicemia;
	}	
	
	public List<Glicemia> listarPorTipoASC(String tipo) throws Exception{
		List<Glicemia> lista = new ArrayList<Glicemia>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE tipo = ? ORDER BY data", new String[]{String.valueOf(tipo)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Glicemia i = new Glicemia();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setMedida(c.getInt(c.getColumnIndex("quantidade")));
				lista.add(i);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}	
	
}
