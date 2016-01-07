package br.com.diabetesvirtual.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.diabetesvirtual.model.Login;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.Mensagem;

public class LoginDao {
	private static final String TABELA = "login";
	protected SQLiteDatabase db;
	protected ConnectDatabase helper;
	Mensagem msg;
	Context context;
	
	public LoginDao(Context ctx) {
		this.context = ctx;
		helper = new ConnectDatabase(context);
		db = helper.getDatabase();
	}
	
	public long inserir(Login login, boolean forceId) throws Exception{
		long x=-1;
		if (login.getId()!=null && !forceId) {
			this.atualizar(login);
		}else {
			ContentValues valores = new ContentValues();
			factoryContentValues(login,  valores);
		    x = db.insertOrThrow(TABELA, null, valores);				
		}			
		return x;
	}
	
	public void atualizar(Login login) throws Exception{
		ContentValues valores = new ContentValues();
		factoryContentValues(login,  valores);
	    db.update(TABELA, valores, "id = "+login.getId(), null);
	    db.close();
	}
	
	public void deletar(Login exe) throws Exception{
		db.delete(TABELA, "id = "+exe.getId(), null);
		db.close();
	}
	
	public Login getByID(int id) throws Exception{
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA+" WHERE id = ? ORDER BY data DESC", new String[] {String.valueOf(id)} );
		if (c.getCount()>0) {
			c.moveToFirst();	
			c.close();
			db.close();		
			return factoryObject(c);
		} 
		return null;
	}

	public List<Login> getAll() throws Exception{
		List<Login> lista = new ArrayList<Login>();
		Cursor c = db.rawQuery("SELECT * FROM "+TABELA, null);
		if (c.getCount()>0) {
			c.moveToFirst();
			do  {		
				lista.add(factoryObject(c));	
			} while (c.moveToNext());
			c.close();
			db.close();		
			return lista;
		} 
		return null;
	}
	
	public void factoryContentValues(Login login, ContentValues contentValues){
		contentValues.put("id", login.getId());
		contentValues.put("nome", login.getNome());
		contentValues.put("email", login.getEmail());
		contentValues.put("senha", login.getSenha());
		contentValues.put("nivelacesso", login.getNivelAcesso());
		contentValues.put("login", login.getLogin());
	}
	

	private Login factoryObject(Cursor c) {
		Login login = new Login();
		login.setId(c.getInt(c.getColumnIndex("id")));
		login.setNome(c.getString(c.getColumnIndex("nome")));
		login.setEmail(c.getString(c.getColumnIndex("email")));	
		login.setSenha(c.getString(c.getColumnIndex("senha")));	
		login.setNivelAcesso(c.getString(c.getColumnIndex("nivelacesso")));
		login.setLogin(c.getString(c.getColumnIndex("login")));
		
		return login;
	}
	
	
}
