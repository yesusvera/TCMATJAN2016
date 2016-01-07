package br.com.diabetesvirtual.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.model.Refeicao;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.Mensagem;

public class RefeicaoDao {

	private static final String TABELA_REFEICAO = "Refeicao";
	private static final String TABELA_ITENS = "Itens_refeicao";
	private static final String TABELA_ALIMENTOS = "Alimentos";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	Mensagem msg;
	Context context;
	
	public RefeicaoDao(Context ctx){
		this.context = ctx;
		helper = new ConnectDatabase(context);
		db = helper.getDatabase();	
	}
	
	public List<ItensRefeicao> getAllItens() throws Exception{
		List<Refeicao> refeicoes = new ArrayList<Refeicao>();
		List<ItensRefeicao> itens = new ArrayList<ItensRefeicao>();		
		Cursor c;
		c = db.rawQuery("SELECT * FROM "+TABELA_REFEICAO+" ORDER BY data DESC", null);
//carrega as refeicoes		
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Refeicao i = new Refeicao();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setCarboidrato(c.getDouble(c.getColumnIndex("carboidrato")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setPeso(c.getDouble(c.getColumnIndex("peso")));
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
//				i.setTipo(RefeicaoTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				refeicoes.add(i);	
			} while (c.moveToNext());
//Carrega os itens	
			for (Refeicao ref: refeicoes) {
				c = db.rawQuery("SELECT * FROM "+TABELA_ITENS+" WHERE id_refeicao = "+ref.getId(), null);	
				ItensRefeicao item = new ItensRefeicao();
				if (c.getCount()>0) {
					c.moveToFirst();
					do  {		
						item = new ItensRefeicao();
						item.setRefeicao(ref);
						item.setId(c.getInt(c.getColumnIndex("id")));
						item.setQtd(c.getDouble(c.getColumnIndex("qtd")));	
						item.getAlimento().setId_alimentos(c.getInt(c.getColumnIndex("id_alimento")));
						itens.add(item);
					} while (c.moveToNext());
				}
			}
//Carrega os alimentos da tabela de itens
			for (ItensRefeicao item: itens) {
				c = db.rawQuery("SELECT * FROM "+TABELA_ALIMENTOS+" WHERE id_alimentos = "+item.getAlimento().getId_alimentos()+"", null);	
				if (c.getCount()>0) {
					c.moveToFirst();
					do  {		
						item.getAlimento().setCarboidrato(c.getInt(c.getColumnIndex("carboidrato")));
						item.getAlimento().setDescricao(c.getString(c.getColumnIndex("descricao")));
						item.getAlimento().setPeso(c.getInt(c.getColumnIndex("peso")));	
						item.getAlimento().setMedida(c.getString(c.getColumnIndex("medida")));	
					} while (c.moveToNext());
				}
			}	
			c.close();
			db.close();		
		} 
		return itens;
	}
	
	public void deletar(Refeicao r) throws Exception{
		int x=0;
		x = db.delete(TABELA_ITENS, "id_refeicao = "+r.getId(), null);
		if (x>0) {
			db.delete(TABELA_REFEICAO, "id = "+r.getId(), null);
		} else {
			msg = new Mensagem();
			msg.mensagemToast(context, "Impossivel excluir, nenhum item encontrado para essa refeição.");
		}
		db.close();
	}
	
	public long inserir(Refeicao r, List<ItensRefeicao> itens) {
	    long x=-1;
		try {
			if (r.getId()>0) {
				this.atualizar(r, itens);
			} else {
				ContentValues valores = new ContentValues();
			    valores.put("data", r.getData().getTimeInMillis());
			    valores.put("carboidrato", r.getCarboidrato());
			    valores.put("peso", r.getPeso());
			    valores.put("obs", r.getObs());
			    Log.i("testerefeicaodao", "antes tipo");
			    valores.put("tipo", r.getTipo());
			    Log.i("testerefeicaodao", "depois tipo");
//		    	valores.put("tipo", r.getTipo().getCod());
			    x = db.insertOrThrow(TABELA_REFEICAO, null, valores);	
			    r.setId((int)x);
			    for (ItensRefeicao item : itens) {
			    	valores = new ContentValues();
			    	valores.put("id_refeicao", (int) x);
			    	valores.put("id_alimento", item.getAlimento().getId_alimentos());
			    	valores.put("qtd",item.getQtd());
			    	db.insertOrThrow(TABELA_ITENS, null, valores);
				}
			    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
			    msg = new Mensagem();
				msg.mensagemToast(context, "Salvo em: "+ format.format(r.getData().getTime()));
			}	    	
		} catch (Exception e) {
			db.delete(TABELA_REFEICAO, "id = ?", new String [] {String.valueOf(x)});
			msg = new Mensagem();
			msg.mensagemToast(context, "Erro ao salvar dados.");
		}
		db.close();
	    return  x;   
	}
	
	public void atualizar(Refeicao r, List<ItensRefeicao> itens) throws Exception{
		ContentValues valores = new ContentValues(); 
		valores.put("data", r.getData().getTimeInMillis());
		valores.put("carboidrato", r.getCarboidrato());
		valores.put("peso", r.getPeso());
		valores.put("obs", r.getObs());
		Log.i("teste", "antes tipo");
		valores.put("tipo", r.getTipo());
		Log.i("teste", "depois tipo");
//	    valores.put("tipo", r.getTipo().getCod());
		db.update(TABELA_REFEICAO, valores, "id = "+r.getId(), null);
		db.delete(TABELA_ITENS, "id_refeicao = "+r.getId(), null);
		for (ItensRefeicao item : itens) {
	    	valores = new ContentValues();
	    	valores.put("id_refeicao", (int) r.getId());
	    	valores.put("id_alimento", item.getAlimento().getId_alimentos());
	    	valores.put("qtd",item.getQtd());
	    	db.insertOrThrow(TABELA_ITENS, null, valores);
		}	
		db.close();
	}
	
	public List<Refeicao> getAll() throws Exception{
		List<Refeicao> lista = new ArrayList<Refeicao>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA_REFEICAO+" ORDER BY data DESC", null);
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Refeicao i = new Refeicao();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setCarboidrato(c.getDouble(c.getColumnIndex("carboidrato")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setPeso(c.getDouble(c.getColumnIndex("peso")));
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
//				i.setTipo(RefeicaoTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				lista.add(i);	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}

	public Refeicao findByID(long id){
		Refeicao refeicao =  null;
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA_REFEICAO+" WHERE id = ?",  new String [] {String.valueOf(id)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do{		
				refeicao = new Refeicao();
				refeicao.setId(c.getInt(c.getColumnIndex("id")));
				refeicao.setObs(c.getString(c.getColumnIndex("obs")));	
				refeicao.setCarboidrato(c.getDouble(c.getColumnIndex("carboidrato")));
				Long a = c.getLong(c.getColumnIndex("data"));
				refeicao.getData().setTimeInMillis(a);
				refeicao.setPeso(c.getDouble(c.getColumnIndex("peso")));
				refeicao.setTipo(c.getString(c.getColumnIndex("tipo")));
//				i.setTipo(RefeicaoTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				break;	
			} while (c.moveToNext());
			c.close();
			db.close();		
		} 
		return refeicao;
	}
	
	public List<ItensRefeicao> getItens(int id) throws Exception{
		List<ItensRefeicao> lista = new ArrayList<ItensRefeicao>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA_ITENS+" WHERE id_refeicao = ?",  new String [] {String.valueOf(id)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				ItensRefeicao i = new ItensRefeicao();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setQtd(c.getDouble(c.getColumnIndex("qtd")));	
				i.getAlimento().setId_alimentos(c.getInt(c.getColumnIndex("id_alimento")));
				lista.add(i);	
			} while (c.moveToNext());	
		}
		for(int j=0; j<lista.size(); j++) {
			c = db.rawQuery("SELECT * FROM "+TABELA_ALIMENTOS+" WHERE id_alimentos = ?",  new String [] {String.valueOf(lista.get(j).getAlimento().getId_alimentos())});
			if (c.getCount()>0) {
				c.moveToFirst();
				lista.get(j).getAlimento().setCarboidrato(c.getInt(c.getColumnIndex("carboidrato")));
				lista.get(j).getAlimento().setDescricao(c.getString(c.getColumnIndex("descricao")));
				lista.get(j).getAlimento().setMedida(c.getString(c.getColumnIndex("medida")));
				lista.get(j).getAlimento().setPeso(c.getInt(c.getColumnIndex("peso")));
			} else {
				msg = new Mensagem();
				msg.mensagemToast(context, "Erro ao recupear dados.");
			}
		}
		c.close();
		db.close();	
		return lista;
	}	
	
	public Refeicao getById(int id){
		try {
			Cursor c = db.rawQuery("SELECT * FROM "+TABELA_REFEICAO+" WHERE id = "+id+"" , null);
			if (c.getCount()>0) {
				c.moveToFirst();	
				Refeicao i = new Refeicao();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setCarboidrato(c.getDouble(c.getColumnIndex("carboidrato")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
//				i.setTipo(RefeicaoTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				c.close();
				db.close();		
				return i;
			} 
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(context, "Erro ao recuperar refeição.");
		}
		return null;
	}
	
	public List<Refeicao> bucarPorIntervaloCarb(int inicio, int fim) throws Exception{
		List<Refeicao> lista = new ArrayList<Refeicao>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA_REFEICAO+" WHERE carboidrato >= ? AND carboidrato <= ? ORDER BY data DESC", new String [] {String.valueOf(inicio), String.valueOf(fim)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Refeicao i = new Refeicao();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setCarboidrato(c.getDouble(c.getColumnIndex("carboidrato")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setPeso(c.getDouble(c.getColumnIndex("peso")));
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
//				i.setTipo(RefeicaoTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				lista.add(i);
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		}
		return lista;
	}
	
	public List<Refeicao> bucarPorDataHora(Calendar inicio, Calendar fim) throws Exception{
		List<Refeicao> lista = new ArrayList<Refeicao>();
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
			Cursor c = db.rawQuery("SELECT * FROM "+TABELA_REFEICAO+" WHERE data >= ? AND data <= ? ORDER BY data", new String [] {String.valueOf(ini.getTimeInMillis()), String.valueOf(x.getTimeInMillis())});
			if (c.getCount()>0) {
				c.moveToFirst();
				do  {		
					Refeicao i = new Refeicao();
					i.setId(c.getInt(c.getColumnIndex("id")));
					i.setObs(c.getString(c.getColumnIndex("obs")));	
					i.setCarboidrato(c.getDouble(c.getColumnIndex("carboidrato")));
					Long a = c.getLong(c.getColumnIndex("data"));
					i.getData().setTimeInMillis(a);
					i.setPeso(c.getDouble(c.getColumnIndex("peso")));
					i.setTipo(c.getString(c.getColumnIndex("tipo")));
//					i.setTipo(RefeicaoTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
					lista.add(i);
				} while (c.moveToNext());
			}	
			ini.add(Calendar.DAY_OF_MONTH, 1);
			x.add(Calendar.DAY_OF_MONTH, 1);
		}
		db.close();	
		return lista;
	}
	
	public List<Refeicao> bucarPorIntervaloData(Long inicio, Long fim) throws Exception{
		List<Refeicao> lista = new ArrayList<Refeicao>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA_REFEICAO+" WHERE data >= ? AND data <= ? ORDER BY data DESC", new String [] {String.valueOf(inicio), String.valueOf(fim)});
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				Refeicao i = new Refeicao();
				i.setId(c.getInt(c.getColumnIndex("id")));
				i.setObs(c.getString(c.getColumnIndex("obs")));	
				i.setCarboidrato(c.getDouble(c.getColumnIndex("carboidrato")));
				Long a = c.getLong(c.getColumnIndex("data"));
				i.getData().setTimeInMillis(a);
				i.setPeso(c.getDouble(c.getColumnIndex("peso")));
				i.setTipo(c.getString(c.getColumnIndex("tipo")));
//				i.setTipo(RefeicaoTipos.getTipo(c.getInt(c.getColumnIndex("tipo"))));
				lista.add(i);
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		}
		return null;
	}
		
}
