package br.com.diabetesvirtual.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import br.com.diabetesvirtual.util.ConnectDatabase;

public class CSVActivity extends Activity {
    	public void InsulinasCSV() {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.i("erro", null);
		} else {
			// We use the Download directory for saving our .csv file.
			String pathCSV = Environment.getExternalStorageDirectory()+"/Doce Desafio/Backup/";
			File exportDir = new File(pathCSV);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File file;
			PrintWriter printWriter = null;
			try {
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				file = new File(exportDir, "InsulinasCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				Cursor curCSV = db.rawQuery("select * from Insulina", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("insulinas");
				printWriter.println("obs,tipo,data,quantidade");
				while (curCSV.moveToNext()) {
					String obs = curCSV.getString(curCSV.getColumnIndex("obs"));
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					Long data = curCSV.getLong(curCSV.getColumnIndex("data"));
					Long quantidade = curCSV.getLong(curCSV.getColumnIndex("quantidade"));

					String record = obs + "," + tipo + "," + data + "," + quantidade;
					printWriter.println(record); 								
				}
				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
			} finally {
				if (printWriter != null)
					printWriter.close();
			}
		}
	}
	
	public void RefeicoesCSV() {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.i("erro", null);
		} else {
			// We use the Download directory for saving our .csv file.
			File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File file;
			PrintWriter printWriter = null;
			try {
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				file = new File(exportDir, "RefeicoesCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				Cursor curCSV = db.rawQuery("select * from Refeicao", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("refeicoes");
				printWriter.println("tipo,data,obs,peso, carboidratos");
				while (curCSV.moveToNext()) {
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					Long data = curCSV.getLong(curCSV.getColumnIndex("data"));
					String obs = curCSV.getString(curCSV.getColumnIndex("obs"));
					Long peso = curCSV.getLong(curCSV.getColumnIndex("peso"));
					Long carboidrato = curCSV.getLong(curCSV.getColumnIndex("carboidrato"));

					String record = tipo + "," + data + "," + obs + "," + peso + "," + carboidrato;
					printWriter.println(record); 								
				}
				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
			} finally {
				if (printWriter != null)
					printWriter.close();
			}
		}
	}
	
	public void GlicemiasCSV() {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.i("erro", null);
		} else {
			// We use the Download directory for saving our .csv file.
			File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File file;
			PrintWriter printWriter = null;
			try {
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				file = new File(exportDir, "GlicemiasCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				Cursor curCSV = db.rawQuery("select * from Glicemia", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("glicemias");
				printWriter.println("tipo,data,obs,medida");
				while (curCSV.moveToNext()) {
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					Long data = curCSV.getLong(curCSV.getColumnIndex("data"));
					String obs = curCSV.getString(curCSV.getColumnIndex("obs"));
					Long medida = curCSV.getLong(curCSV.getColumnIndex("medida"));

					String record = tipo + "," + data + "," + obs + "," + medida;
					printWriter.println(record); 								
				}
				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
			} finally {
				if (printWriter != null)
					printWriter.close();
			}
		}
	}
	
	public void ExerciciosCSV() {
		/**
		 * First of all we check if the external storage of the device is
		 * available for writing. Remember that the external storage is not
		 * necessarily the sd card. Very often it is the device storage.
		 */
		
		
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			
		} else {
			// We use the Download directory for saving our .csv file.
			File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			
			File file;
			PrintWriter printWriter = null;
			try {
				
				Context cxt = getApplicationContext();
				ConnectDatabase dbcOurDatabaseConnector = new ConnectDatabase(cxt);
				SQLiteDatabase db = dbcOurDatabaseConnector.getDatabase();
				
				
				file = new File(exportDir, "ExerciciosCSV.csv");
				file.createNewFile();
				printWriter = new PrintWriter(new FileWriter(file));

				/**
				 * This is our database connector class that reads the data from
				 * the database. The code of this class is omitted for brevity.
				 */
				//String path = context.getFilesDir().getParentFile().getPath() +"/assets/"+ "diabetes.db";
			    
				
				
				//dbcOurDatabaseConnector.getWritableDatabase();
				//DBCOurDatabaseConnector dbcOurDatabaseConnector = new DBCOurDatabaseConnector(myContext);
				//dbcOurDatabaseConnector.openForReading(); // open the database
															// for reading

				/**
				 * Let's read the first table of the database. getFirstTable()
				 * is a method in our DBCOurDatabaseConnector class which
				 * retrieves a Cursor containing all records of the table (all
				 * fields). The code of this class is omitted for brevity.
				 */
				
				Cursor curCSV = db.rawQuery("select * from Exercicios", null);
				
				// Write the name of the table and the name of the columns
				// (comma separated values) in the .csv file.
				printWriter.println("exercicio");
				printWriter.println("descricao,tipo,modalidade,intensidade");
				while (curCSV.moveToNext()) {
					String descricao = curCSV.getString(curCSV.getColumnIndex("descricao"));
					String tipo = curCSV.getString(curCSV.getColumnIndex("tipo"));
					String modalidade = curCSV.getString(curCSV.getColumnIndex("modalidade"));
					String intensidade = curCSV.getString(curCSV.getColumnIndex("intensidade"));

					/**
					 * Create the line to write in the .csv file. We need a
					 * String where values are comma separated. The field date
					 * (Long) is formatted in a readable text. The amount field
					 * is converted into String.
					 */
					String record = descricao + "," + tipo + "," + modalidade + "," + intensidade;
					printWriter.println(record); // write the record in the .csv
													// file
				}

				curCSV.close();
				dbcOurDatabaseConnector.close();
			}

			catch (Exception exc) {
				// if there are any exceptions, return false
				
			} finally {
				if (printWriter != null)
					printWriter.close();
			}

			// If there are no errors, return true.
	
		}
	}
	
	public void exportaCSV() {
		GlicemiasCSV();
		InsulinasCSV();
		RefeicoesCSV();
		ExerciciosCSV();
		
	}
}