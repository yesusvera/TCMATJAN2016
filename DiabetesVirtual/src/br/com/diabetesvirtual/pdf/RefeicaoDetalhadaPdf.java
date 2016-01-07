package br.com.diabetesvirtual.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import br.com.diabetesvirtual.model.ItensRefeicao;
import br.com.diabetesvirtual.model.Perfil;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;

public class RefeicaoDetalhadaPdf {
	List<ItensRefeicao> lista;
	ProgressDialog progressBar;
	private Handler progressBarHandler = new Handler();
	private Handler handler = new Handler();
	SimpleDateFormat format_dia = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault());
	private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
	private static Font font_titulo = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD, BaseColor.BLACK);
	final String path = Environment.getExternalStorageDirectory().getPath();
	final String pasta = "Doce Desafio";
	final String pasta_criada = path+"/"+pasta;
	Context context;
	final String nome;
	Perfil perfil;
	PerfilDao perfilDao;
	
	public RefeicaoDetalhadaPdf(Context ctx,String name,List<ItensRefeicao> a) {
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
			        document.setMargins(30, 30, 5, 5);
			        document.open();	
			        
			        Paragraph titulo = new Paragraph();
			        titulo = new Paragraph(nome,font_titulo);
			        titulo.add(new Paragraph(" "));
			        titulo.setAlignment(Element.ALIGN_CENTER);
			        document.add(titulo);
			        
			        titulo = new Paragraph("Nome: "+perfil.getNome()+"\n"+"Peso: "+perfil.getPeso()+" kg"+"\n"+
			        		"Altura: "+perfil.getAltura()+" cm"+"\n"+"Idade: "+Perfil.getIdade(perfil.getData_nasc())+
			        		"\n"+"Fator de sensibilidade glicêmica: "+perfil.getFatorGlicemia()+
			        		"\n"+"Fator de sensibilidade ao carboidrato: "+perfil.getFatorCarboidrato());
			        titulo.setAlignment(Element.ALIGN_LEFT);
		        	titulo.add(new Paragraph(" "));
			        document.add(titulo);
			        
			        PdfPTable tabela = new PdfPTable(1);
			        tabela.setWidthPercentage(100);
			        tabela.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			        tabela.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			        tabela.getDefaultCell().setPadding(5);
			        
			        int id = -1;
			        int i = 0;
			        for (ItensRefeicao item : lista) {
				        if (id != item.getRefeicao().getId()) {
				  
				        	//
				        	PdfPCell c = new PdfPCell(new Phrase(item.getRefeicao().getTipo()+
				        			" (" +Formatos.formataDouble(item.getRefeicao().getCarboidrato())+"g CHO"+")"+"\n"+"Em, "+item.getRefeicao().getData().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+
									", "+format_dia.format(item.getRefeicao().getData().getTime()),font));
					        c.setBackgroundColor(BaseColor.LIGHT_GRAY);
					        c.setHorizontalAlignment(Element.ALIGN_LEFT);
					        c.setPadding(4);
					        tabela.addCell(c);	
						}
				    	tabela.addCell(item.getAlimento().getDescricao()+"\n"+
				    					item.getAlimento().getMedida()+" ("+Formatos.formataDouble(item.getQtd())+")"+"\n"+
				    					"Carboidrato por medida: "+Formatos.formataDouble(item.getAlimento().getCarboidrato())+"g"
				    					+"\n"+"Total de carboidrato: "+Formatos.formataDouble(item.getQtd()*item.getAlimento().getCarboidrato())+"g");					        
					id = item.getRefeicao().getId();
				    i++;
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
					erro();
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
			        msg.mensagemToast(context, "ERRO AO GERAR PDF.");
				}							
			}
		});
	}
	
	public void erro() {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				Mensagem msg = new Mensagem();
			    msg.mensagemToast(context, "ERRO AO GERAR PDF.");							
			}
		});
	}
}

