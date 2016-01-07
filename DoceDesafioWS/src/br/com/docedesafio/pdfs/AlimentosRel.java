package br.com.docedesafio.pdfs;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.docedesafio.model.Alimento;

public class AlimentosRel extends BaseRel {
	private static String FILE = "/Users/yesus/Documents/doce desafio TCC/generate/AlimentosRel.pdf";

	private String[] headersName = 
			{"Usuário", "Código", "Nome", "Medida", "Peso", "Carboidratos"};
	
	private List<Alimento> listaAlimento;
	
	public ByteArrayOutputStream getByteArray(List<Alimento> listaAlimento) {
		
		this.listaAlimento = listaAlimento;
		
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, bas);
			document.open();
			addContent(document);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bas;
	}

	public static void main(String[] args) {
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			new AlimentosRel().addContent(document);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addContent(Document document) throws DocumentException {
		Anchor anchor = new Anchor("Relatório de Registro de Alimentos", catFont);
		anchor.setName("Relatório de Registro de Alimentos");
		Paragraph paragraph = new Paragraph(anchor);
		addEmptyLine(paragraph, 1);
		createTable(paragraph);
		document.add(paragraph);
	}

	private void createTable(Paragraph paragraph) throws BadElementException {
		PdfPTable table = new PdfPTable(headersName.length);

		// t.setBorderColor(BaseColor.GRAY);
		// t.setPadding(4);
		// t.setSpacing(4);
		// t.setBorderWidth(1);

		for (String strH : headersName) {
			PdfPCell cpdfpCell = new PdfPCell(new Phrase(strH));
			cpdfpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cpdfpCell);
		}
		
		table.setHeaderRows(1);

		for(Alimento alimento: listaAlimento){
			table.addCell(alimento.getNomeUsuario());
			table.addCell(String.valueOf(alimento.getId()));
			table.addCell(alimento.getNome());
			table.addCell(alimento.getMedida());
			table.addCell(String.valueOf(alimento.getPeso()));
			table.addCell(String.valueOf(alimento.getCarboidratos()));
		}
		
		paragraph.add(table);
	}
}
