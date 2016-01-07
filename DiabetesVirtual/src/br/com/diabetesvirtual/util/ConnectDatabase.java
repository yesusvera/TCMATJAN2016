package br.com.diabetesvirtual.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class ConnectDatabase extends SQLiteOpenHelper{
	
	private Context context;
	private static String DBNAME ="diabetes.db";
	private static String DBNAME_BACKUP ="diabetes_backup.db";
	private static final int SCHEMA_VERSION = 1; //Para o Fabiano, usar a versao 2 ou reescrever o método ONDOWNGRADE
	SQLiteDatabase db_read = null;
	SQLiteDatabase liteDB = null;
	String backupPath = Environment.getExternalStorageDirectory()+"/Doce Desafio/Backup/";
	String origem = "/databases/"+ DBNAME;	
	Mensagem msg;
	
	  
	public ConnectDatabase(Context context) {
		super(context, DBNAME, null, SCHEMA_VERSION);
	    this.context = context;
	}
	
	public void backUp() {
		try {
			File file = new File(backupPath);
			if (!file.exists()) {
				   file.mkdirs();
			}
			File fileSource = new File(context.getFilesDir().getParentFile().getPath(),origem);
			File fileBackup = new File(backupPath,DBNAME_BACKUP); 
			if (fileBackup.exists()) {
				fileBackup.createNewFile();
			}
			FileChannel src = new FileInputStream(fileSource).getChannel();
	        FileChannel dst = new FileOutputStream(fileBackup).getChannel();	        
	        dst.transferFrom(src, 0, src.size());
	        src.close();
	        dst.close();	
	        Mensagem msg = new Mensagem();
			msg.mensagemToast(context, "Backup salvo em: "+backupPath+DBNAME_BACKUP);
		} catch (Exception e) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(context, "Erro ao gerar BACK UP.");
		}		
	}
	
	public void restore() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {
				File fileSource = new File(context.getFilesDir().getParentFile().getPath(),origem);
				if (fileSource.exists()) {
					fileSource.createNewFile();
				}
				File fileBackup = new File(backupPath,DBNAME_BACKUP);
				if (!fileBackup.exists()) {
					Mensagem msg = new Mensagem();
					msg.mensagemToast(context, "Nenhum arquivo de backup foi encontrado.");
				}else {
					FileChannel src = new FileOutputStream(fileSource).getChannel();
			        FileChannel dst = new FileInputStream(fileBackup).getChannel();
			        src.transferFrom(dst, 0, dst.size());
			        src.close();
			        dst.close();
			        Mensagem msg = new Mensagem();
					msg.mensagemToast(context, "Dados restaurados com sucesso.");
				}	
			} else {
				Mensagem msg = new Mensagem();
				msg.mensagemToast(context, "Cartão de memória em uso. Desconecte o dispositivo de qualquer outro aparelho.");
			}					    
		} catch (Exception e) {
			Mensagem msg = new Mensagem();
			msg.mensagemToast(context, "Erro ao gerar BACK UP.");
		}	
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion > oldVersion) {
			try {
				Mensagem msg = new Mensagem();
		    	msg.mensagemToast(context, "Atualizando banco de dados, aguarde..");
				//db.execSQL("ALTER TABLE Perfil ADD COLUMN nome TEXT");                    USAR DE EXMPLO
				//db.execSQL("ALTER TABLE Alimentos ADD COLUMN favorito INT DEFAULT 0");
			} catch (Exception e) {
				msg = new Mensagem();
		    	msg.mensagemToast(context, "Erro ao atualizar banco de dados.");
			}			
		}	
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//super.onDowngrade(db, oldVersion, newVersion);
	}

	private boolean checkDataBase() {		    
		SQLiteDatabase db = null;
		try {
			String path = context.getFilesDir().getParentFile().getPath() +"/databases/"+ DBNAME;
		    db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		    db.close();		    
		    Log.i("ConnectDatabase", "O banco existe.");
		    Log.i("ConnectDatabase", "Abrindo banco de dados...");
		    Log.i("ConnectDatabase", "Testando UPGRADE DO BANCO..");
		    Log.i("ConnectDatabase", "Fechando Banco de dados..");		    
		} catch (SQLiteException e) {
		     Log.e("ConnectDatabase", "Erro! O banco não existe."+e.toString());
		}
		return db != null;
	}
	
	private void createDataBase() throws Exception {
		boolean exists = checkDataBase();			    
		if(!exists) { //se o banco NAO existir
			zerarBase();      
		} else {
			db_read = this.getReadableDatabase();
			db_read.close();
		}
	}

	public void zerarBase() {
		db_read = this.getReadableDatabase();
		db_read.close();
		Log.i("ConnectDatabase", "Criando um banco vazio..");
		Log.i("ConnectDatabase", "Banco criado com sucesoo.. Fechando banco.");
		try {
			String path = Environment.getExternalStorageDirectory().getPath();
			String pastaRaiz = "Doce Desafio";
			File file = new File(path,pastaRaiz);
			file.mkdirs(); //cria a pasta raiz do app ao instalar
			file = new File(backupPath);
			file.mkdirs(); //cria a pasta backup do app ao instalar
		    copyDatabase();
		    Log.i("ConnectDatabase", "Copiando o banco da pasta ASSETS para o celular..");
		} catch (IOException e) {
			Log.e("ConnectDatabase", "Erro ao copiar o banco.." + e.toString());
		}
	}

	public SQLiteDatabase getDatabase() {	    
		try{
	        createDataBase();	
	        Log.i("ConnectDatabase", "Retornando banco para leitura."); 
	    }catch (Exception e) {
	    	Mensagem msg = new Mensagem();
	    	msg.mensagemToast(context, "Erro ao criar banco de dados.");
	    }	
		
		String path = context.getFilesDir().getParentFile().getPath() +"/databases/"+ DBNAME;
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	private void copyDatabase() throws IOException {			    
		try {
			String dbPath = context.getFilesDir().getParentFile().getPath() +"/databases/"+ DBNAME;	    
			OutputStream dbStream = new FileOutputStream(dbPath);
			InputStream dbInputStream =  context.getAssets().open(DBNAME);			    
			byte[] buffer = new byte[1024];
			int length;
			while((length = dbInputStream.read(buffer)) > 0) {
				dbStream.write(buffer, 0, length);
			}			    
			dbInputStream.close();			    
			dbStream.flush();
			dbStream.close();
		} catch (Exception e) {
			msg = new Mensagem();
	    	msg.mensagemToast(context, "Erro copiando tabela.");
		}
			    
	}

	public Cursor getFirstTable() {
		SQLiteDatabase db = getDatabase();
		return db.rawQuery("select * from exercicio", null);
	}	
}