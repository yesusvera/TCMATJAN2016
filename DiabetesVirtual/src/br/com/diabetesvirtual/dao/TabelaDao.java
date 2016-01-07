package br.com.diabetesvirtual.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.diabetesvirtual.model.Alimento;
import br.com.diabetesvirtual.util.ConnectDatabase;

public class TabelaDao {

	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	private final String TABELA = "Alimentos";

	public TabelaDao(Context ctx){
		helper = new ConnectDatabase(ctx);
		db = helper.getDatabase();	
	}
	
	public Alimento findByID(long id){
		List<Alimento> alimentos = new ArrayList<Alimento>();
		Cursor c;
		try {
			c = db.rawQuery("SELECT * FROM Alimentos WHERE id_alimentos = " + id, null);				
			c.moveToFirst();			
			do  {		
				Alimento alimento = new Alimento();
				alimento.setId_alimentos(c.getInt(c.getColumnIndex("id_alimentos")));
				alimento.setDescricao(c.getString(c.getColumnIndex("descricao")));
				alimento.setMedida(c.getString(c.getColumnIndex("medida")));
				alimento.setPeso(c.getInt(c.getColumnIndex("peso")));
				alimento.setCarboidrato(c.getInt(c.getColumnIndex("carboidrato")));
				alimento.setFavorito(c.getInt(c.getColumnIndex("favorito")));
				alimentos.add(alimento);	
			} while (c.moveToNext());
			c.close();
			db.close();			
		} catch (Exception e) {
			Log.e("ALimentoDao", "Erro ao buscar alimentos" + e.toString());
		}
		if(alimentos.size()>0){
			return alimentos.get(0);
		}else{
			return null;
		}
	}
	
	public void inserir(Alimento alimento ,boolean forceInsert) throws Exception{
		if (alimento.getId_alimentos()>0 && !forceInsert) {
			this.atualizar(alimento);
		} else {
			ContentValues values = new ContentValues();
			values.put("descricao", alimento.getDescricao());
			values.put("medida", alimento.getMedida());
			values.put("peso", alimento.getPeso());
			values.put("carboidrato", alimento.getCarboidrato());
			values.put("favorito", alimento.getFavorito());
			alimento.setId_alimentos((int)db.insertOrThrow(TABELA, null, values));
		}		
	}
	
	private void atualizar (Alimento alimento) throws Exception{
		ContentValues values = new ContentValues();
		values.put("descricao", alimento.getDescricao());
		values.put("medida", alimento.getMedida());
		values.put("peso", alimento.getPeso());
		values.put("carboidrato", alimento.getCarboidrato());
		values.put("favorito", alimento.getFavorito());
		db.update(TABELA, values, "id_alimentos = "+alimento.getId_alimentos(), null);
	}
	
	public Boolean deletar(Alimento x) throws Exception{
		Boolean existe = false;
		if (this.emUso(x.getId_alimentos())) {
			existe = true;
		} else {
			db.delete(TABELA, "id_alimentos = "+x.getId_alimentos(), null);
		}
		return existe;
	}
	
	public List<Alimento> listar(Boolean favorito) {
		List<Alimento> alimentos = new ArrayList<Alimento>();
		Cursor c;
		try {
			if (favorito) {
				c = db.rawQuery("SELECT * FROM Alimentos WHERE favorito = 1 ORDER BY descricao", null);				
			}else {
				c = db.rawQuery("SELECT * FROM Alimentos ORDER BY descricao", null);
			}			
			c.moveToFirst();			
			do  {		
				Alimento alimento = new Alimento();
				alimento.setId_alimentos(c.getInt(c.getColumnIndex("id_alimentos")));
				alimento.setDescricao(c.getString(c.getColumnIndex("descricao")));
				alimento.setMedida(c.getString(c.getColumnIndex("medida")));
				alimento.setPeso(c.getInt(c.getColumnIndex("peso")));
				alimento.setCarboidrato(c.getInt(c.getColumnIndex("carboidrato")));
				alimento.setFavorito(c.getInt(c.getColumnIndex("favorito")));
				alimentos.add(alimento);	
			} while (c.moveToNext());
			c.close();
			db.close();			
		} catch (Exception e) {
			Log.e("ALimentoDao", "Erro ao buscar alimentos" + e.toString());
		}
		return alimentos;
	}
	
	
	private Boolean emUso(int id) {
		Cursor c;
		Boolean x = false;
		try {
			c = db.rawQuery("SELECT * FROM itens_refeicao WHERE id_alimento = "+id+"", null);						
			if (c.getCount()>0) {
				x = true;
				db.close();
			} 
			c.close();				
		} catch (Exception e) {
			Log.e("ALimentoDao", "Erro ao executar operação nos registros.");
		}
		return x;
	}
	
	public List<Alimento> getAlimentosPorDescricao(String descricao) {
		List<Alimento> alimentos = new ArrayList<Alimento>();
		try {
			db = helper.getDatabase();
			Cursor c = db.rawQuery("SELECT * FROM Alimentos WHERE descricao = ?", new String[] {descricao});
			c.moveToFirst();			
			do  {		
				Alimento alimento = new Alimento();
				alimento.setId_alimentos(c.getInt(c.getColumnIndex("id_alimentos")));
				alimento.setDescricao(c.getString(c.getColumnIndex("descricao")));
				alimento.setMedida(c.getString(c.getColumnIndex("medida")));
				alimento.setPeso(c.getInt(c.getColumnIndex("peso")));
				alimento.setCarboidrato(c.getInt(c.getColumnIndex("carboidrato")));
				alimento.setFavorito(c.getInt(c.getColumnIndex("favorito")));
				alimentos.add(alimento);	
			} while (c.moveToNext());
			c.close();
			db.close();
		} catch (Exception e) {
			Log.e("ALimentoDao", "Erro ao buscar alimento" + e.toString());
		}
		return alimentos;
	}
	
	public List<String> getDescricao() {
		List<String> listaDescricao = new ArrayList<String>();
		try {
			db = helper.getDatabase();
			Cursor c = db.rawQuery("SELECT DISTINCT descricao FROM Alimentos ORDER BY descricao", null);
			c.moveToFirst();
			do  {	
				String descricao = new String();
				descricao = c.getString(c.getColumnIndex("descricao"));
				listaDescricao.add(descricao);				
			} while (c.moveToNext());
			c.close();
			db.close();			
		} catch (Exception e) {
			Log.e("ALimentoDao", "Erro ao buscar Classes" + e.toString());
		}
		return listaDescricao;
	}
	
}
