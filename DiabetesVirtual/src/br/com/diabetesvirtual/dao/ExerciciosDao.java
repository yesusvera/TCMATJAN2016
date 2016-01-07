package br.com.diabetesvirtual.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.Mensagem;

public class ExerciciosDao {
	private static final String TABELA = "Exercicios";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	Mensagem msg;
	Context context;
	
	public ExerciciosDao(Context ctx) {
		this.context = ctx;
		helper = new ConnectDatabase(context);
		db = helper.getDatabase();
	}
	
	public long inserir(Exercicios exercicio, boolean forceInsert) throws Exception{
		long x=-1;
		if (exercicio.getId()>0 && !forceInsert) {
			this.atualizar(exercicio);
		}else {
			ContentValues valores = new ContentValues();
		    valores.put("descricao", exercicio.getDescricao());
		    valores.put("modalidade", exercicio.getModalidade());
		    valores.put("intensidade", exercicio.getIntensidade());
		    //valores.put("tipo", exercicio.getTipo().getCod());
		    valores.put("tipo", exercicio.getTipo());
		    valores.put("duracao", exercicio.getDuracao());
		    valores.put("data", exercicio.getData().getTimeInMillis());
		    x = db.insertOrThrow(TABELA, null, valores);		
		    exercicio.setId((int)x);
		}			
		return x;
	}
	
	public void atualizar(Exercicios exercicio) throws Exception{
		ContentValues valores = new ContentValues();
	    valores.put("descricao", exercicio.getDescricao());
	    valores.put("modalidade", exercicio.getModalidade());
//	    valores.put("intensidade", exercicio.getIntensidade().getCod());
	    valores.put("intensidade", exercicio.getIntensidade());
	    //valores.put("tipo", exercicio.getTipo().getCod());
	    valores.put("tipo", exercicio.getTipo());
	    valores.put("duracao", exercicio.getDuracao());
	    valores.put("data", exercicio.getData().getTimeInMillis());
	    db.update(TABELA, valores, "id = "+exercicio.getId(), null);
	    db.close();
	}
	
	public void deletar(Exercicios exe) throws Exception{
		db.delete(TABELA, "id = "+exe.getId(), null);
		db.close();
	}
	
	public Exercicios getByID(int id) throws Exception{
		Exercicios exe = new Exercicios();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE id = ? ORDER BY data DESC", new String[] {String.valueOf(id)} );
		if (c.getCount()>0) {
			c.moveToFirst();	
			exe.setId(c.getInt(c.getColumnIndex("id")));
			exe.setDescricao(c.getString(c.getColumnIndex("descricao")));
			exe.setModalidade(c.getString(c.getColumnIndex("modalidade")));	
//			exe.setIntensidade(ExerciciosIntensidade.getIntensidade(c.getInt(c.getColumnIndex("intensidade"))));
			exe.setIntensidade(c.getString(c.getColumnIndex("intensidade")));	
			//exe.setTipo(ExerciciosTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
			exe.setTipo(c.getString(c.getColumnIndex("tipo")));
			Long a = c.getLong(c.getColumnIndex("data"));
			exe.getData().setTimeInMillis(a);
			exe.setDuracao(c.getInt(c.getColumnIndex("duracao")));
			c.close();
			db.close();		
			return exe;
		} 
		return null;
	}
	
	public List<Exercicios> getAll() throws Exception{
		List<Exercicios> lista = new ArrayList<Exercicios>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" ORDER BY data DESC", null);
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Exercicios exe = new Exercicios();
				exe.setId(c.getInt(c.getColumnIndex("id")));
				exe.setDescricao(c.getString(c.getColumnIndex("descricao")));	
				exe.setModalidade(c.getString(c.getColumnIndex("modalidade")));	
//				exe.setIntensidade(ExerciciosIntensidade.getIntensidade(c.getInt(c.getColumnIndex("intensidade"))));
				exe.setIntensidade(c.getString(c.getColumnIndex("intensidade")));
				//exe.setTipo(ExerciciosTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				exe.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				exe.getData().setTimeInMillis(a);
				exe.setDuracao(c.getInt(c.getColumnIndex("duracao")));
				lista.add(exe);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}
	
	public List<Exercicios> listarPorTipo(String tipo) throws Exception{
		List<Exercicios> lista = new ArrayList<Exercicios>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE tipo = ? ORDER BY data DESC", new String [] {String.valueOf(tipo)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Exercicios exe = new Exercicios();
				exe.setId(c.getInt(c.getColumnIndex("id")));
				exe.setDescricao(c.getString(c.getColumnIndex("descricao")));
				exe.setModalidade(c.getString(c.getColumnIndex("modalidade")));	
//				exe.setIntensidade(ExerciciosIntensidade.getIntensidade(c.getInt(c.getColumnIndex("intensidade"))));
				exe.setIntensidade(c.getString(c.getColumnIndex("intensidade")));	
				//exe.setTipo(ExerciciosTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				exe.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				exe.getData().setTimeInMillis(a);
				exe.setDuracao(c.getInt(c.getColumnIndex("duracao")));
				lista.add(exe);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}
	
	public List<Exercicios> listarPorIntensidade(int intensidade) throws Exception{
		List<Exercicios> lista = new ArrayList<Exercicios>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE tipo = ? ORDER BY data DESC", new String [] {String.valueOf(intensidade)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Exercicios exe = new Exercicios();
				exe.setId(c.getInt(c.getColumnIndex("id")));
				exe.setDescricao(c.getString(c.getColumnIndex("descricao")));
				exe.setModalidade(c.getString(c.getColumnIndex("modalidade")));	
//				exe.setIntensidade(ExerciciosIntensidade.getIntensidade(c.getInt(c.getColumnIndex("intensidade"))));
				exe.setIntensidade(c.getString(c.getColumnIndex("intensidade")));
				//exe.setTipo(ExerciciosTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				exe.setTipo(c.getString(c.getColumnIndex("tipo")));
				Long a = c.getLong(c.getColumnIndex("data"));
				exe.getData().setTimeInMillis(a);
				exe.setDuracao(c.getInt(c.getColumnIndex("duracao")));
				lista.add(exe);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}
	
}
