package br.com.diabetesvirtual.dao;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.diabetesvirtual.model.Perfil;
import br.com.diabetesvirtual.model.PerfilCategoria;
import br.com.diabetesvirtual.model.Sexo;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.Mensagem;

public class PerfilDao {
	private static final String TABELA = "Perfil";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	protected Mensagem msg;
	protected Context context;
	
	public PerfilDao(Context ctx){
		helper = new ConnectDatabase(ctx);
		db = helper.getDatabase();	
		context = ctx;
	}
	
	public long salvaOuAtualiza(Perfil p) throws Exception {
		long x=0;
		if (p.getId() != 0) {
		   this.atualizar(p);
	    } else {
			ContentValues valores = new ContentValues();
			valores.put("nome", p.getNome());
			valores.put("peso", p.getPeso());
			valores.put("data_nasc", p.getData_nasc().getTimeInMillis());
			valores.put("obs", p.getObs());
			valores.put("altura", p.getAltura());
			valores.put("sexo", p.getSexo().getCod());
			valores.put("fator_glicemia", p.getFatorGlicemia());
			valores.put("fator_carboidrato", p.getFatorCarboidrato());
			valores.put("categoria", p.getCategoria().getValor());
			x = db.insertOrThrow(TABELA, null, valores);
			db.close();	   
	   }
	   return x;
	}
	
	public void atualizar(Perfil p) throws Exception{
		ContentValues valores = new ContentValues();
		valores.put("nome", p.getNome());
		valores.put("peso", p.getPeso());
		valores.put("data_nasc", p.getData_nasc().getTimeInMillis());
		valores.put("obs", p.getObs());
		valores.put("altura", p.getAltura());
		valores.put("sexo", p.getSexo().getCod());
		valores.put("fator_glicemia", p.getFatorGlicemia());
	    valores.put("fator_carboidrato", p.getFatorCarboidrato());
	    valores.put("categoria", p.getCategoria().getValor());
		db.update(TABELA, valores, "id="+p.getId(), null);
		db.close();
	}
	
	public Perfil getPerfil() throws Exception{
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA, null);
		Perfil p = new Perfil();
		if (c.getCount()>0) {
			c.moveToFirst();
			Calendar calendar= Calendar.getInstance();
			p.setId(c.getInt(c.getColumnIndex("id")));
			p.setNome(c.getString(c.getColumnIndex("nome")));
			p.setPeso((c.getDouble(c.getColumnIndex("peso"))));
			p.setObs(c.getString(c.getColumnIndex("obs")));
			calendar.setTimeInMillis(c.getLong(c.getColumnIndex("data_nasc")));
			p.setData_nasc(calendar);
			p.setAltura(c.getDouble(c.getColumnIndex("altura")));
			p.setSexo(Sexo.getSexo(c.getInt(c.getColumnIndex("sexo"))));
			p.setFatorGlicemia(c.getDouble(c.getColumnIndex("fator_glicemia")));
			p.setFatorCarboidrato(c.getDouble(c.getColumnIndex("fator_carboidrato")));
			double x = c.getDouble(c.getColumnIndex("categoria"));
			p.setCategoria(PerfilCategoria.getTipo(x));
		}
		c.close();
		db.close();
		return p;
	}
	
	
}
