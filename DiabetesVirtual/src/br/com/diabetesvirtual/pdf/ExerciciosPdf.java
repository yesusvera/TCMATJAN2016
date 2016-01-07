package br.com.diabetesvirtual.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import br.com.diabetesvirtual.dao.PerfilDao;
import br.com.diabetesvirtual.model.Exercicios;
import br.com.diabetesvirtual.model.Perfil;
import br.com.diabetesvirtual.util.Mensagem;

public class ExerciciosPdf {
	List<Exercicios> lista;
	ProgressDialog progressBar;
	private Handler progressBarHandler = new Handler();
	private Handler handler = new Handler();
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat format_hora = new SimpleDateFormat("HH:mm",Locale.getDefault());
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
	private static Font font_titulo = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD, BaseColor.BLACK);
	final String path = Environment.getExternalStorageDirectory().getPath();
	final String pasta = "Doce Desafio";
	final String pasta_criada = path+"/"+pasta;
	Context context;
	final String nome;
	Perfil perfil;
	PerfilDao perfilDao;
	
	public ExerciciosPdf(Context ctx,String name,List<Exercicios> a) {
		lista = a;
		context = ctx;
		nome = name;
	}
	
	public void gerarPdf() {
		try {
			perfilDao = new PerfilDao(context);
			perfil = new Perfil();
			perfil = perfilDao.getPerfil();
			progressBar = new ProgressDialog(context);
			File file = new File(path,pasta);
			file.mkdirs();
			progressBar.setMax(lista.size()+1);
			progressBar.setMessage("Gerando PDF, pode demorar alguns segundos...");
			progressBar.setProgress(0);
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgressNumberFormat(null);
			progressBar.setCancelable(false);
			progressBar.show();	
			this.threadPdf();
		} catch (Exception e) {
			Log.e("Erro PDF", "Erro ao gerar");
		}
	}
	
	public void threadPdf() {
		new Thread() {
			@Override
			public void run() {
				try {
					Document document=new Document () {};
			        PdfWriter.getInstance(document,new FileOutputStream(pasta_criada+"/"+nome+".pdf"));
			        document.setMargins(5, 5, 5, 5);
			        document.open();	
			        Paragraph titulo = new Paragraph();
			        titulo = new Paragraph(nome,font_titulo);
			        titulo.add(new Paragraph(" "));
			        titulo.setAlignment(Element.ALIGN_CENTER);
			        document.add(titulo);
			        PdfPTable tabela = new PdfPTable(6); //numero de colunas	
			        tabela.setWidthPercentage(100);
			        tabela.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			        tabela.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			        tabela.getDefaultCell().setPadding(4);
			       
			        PdfPCell c = new PdfPCell(new Phrase("DATA",font));
			        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
			        c.setHorizontalAlignment(Element.ALIGN_CENTER);
			        c.setPadding(3);
			        tabela.addCell(c);
			        
			        c = new PdfPCell(new Phrase("HORA",font));
			        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
			        c.setHorizontalAlignment(Element.ALIGN_CENTER);
			        c.setPadding(3);
			        tabela.addCell(c);
			       
			        c = new PdfPCell(new Phrase("DESCRIÇÃO",font));
			        c.setHorizontalAlignment(Element.ALIGN_CENTER);
			        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
			        c.setPadding(3);
			        tabela.addCell(c);
			        
			        c = new PdfPCell(new Phrase("DURAÇÃO",font));
			        c.setHorizontalAlignment(Element.ALIGN_CENTER);
			        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
			        c.setPadding(3);
			        tabela.addCell(c);
			        
			        c = new PdfPCell(new Phrase("TIPO",font));
			        c.setHorizontalAlignment(Element.ALIGN_CENTER);
			        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
			        c.setPadding(3);
			        tabela.addCell(c);
			        
			        c = new PdfPCell(new Phrase("INTENSIDADE",font));
			        c.setHorizontalAlignment(Element.ALIGN_CENTER);
			        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
			        c.setPadding(3);
			        tabela.addCell(c);
			        
			        titulo = new Paragraph("Nome: "+perfil.getNome()+"\n"+"Peso: "+perfil.getPeso()+" kg"+"\n"+
			        		"Altura: "+perfil.getAltura()+" cm"+"\n"+"Idade: "+Perfil.getIdade(perfil.getData_nasc())+
			        		"\n"+"Fator de sensibilidade glicêmica: "+perfil.getFatorGlicemia()+
			        		"\n"+"Fator de sensibilidade ao carboidrato: "+perfil.getFatorCarboidrato());
			        titulo.setAlignment(Element.ALIGN_LEFT);
		        	titulo.add(new Paragraph(" "));
			        document.add(titulo);
			        
			        for (int i = 0; i < lista.size(); i++) {
						tabela.addCell(format_dia.format(lista.get(i).getData().getTime()));
						tabela.addCell(format_hora.format(lista.get(i).getData().getTime()));	
						tabela.addCell(lista.get(i).getDescricao());
						tabela.addCell(lista.get(i).getDuracao()+"min");
						tabela.addCell(lista.get(i).getTipo());
						//tabela.addCell(ExerciciosIntensidade.getNome(context, lista.get(i).getIntensidade().getCod()));
						tabela.addCell(lista.get(i).getIntensidade());
						final int x = i;
						progressBarHandler.post(new Runnable() {
							public void run() {
							  progressBar.setProgress(x);
							}
						});
					}			        
			        document.add(tabela);
			        document.close(); 
			        sucesso();
				} catch (Exception e) {
					Mensagem msg = new Mensagem();
			        msg.mensagemToast(context, "ERRO(1) AO GERAR PDF.");
				}
			}
		}.start();
	};
	
	public void sucesso() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				try{
					progressBar.dismiss();	
					Mensagem msg = new Mensagem();
			        msg.mensagemToast(context, "PDF criado com sucesso em "+pasta_criada+"/"+nome+".pdf");
				} catch (Exception e) {
					Mensagem msg = new Mensagem();
			        msg.mensagemToast(context, "ERRO(2) AO GERAR PDF.");
				}							
			}
		});
	}
}
