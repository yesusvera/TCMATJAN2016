package br.com.docedesafio.pdfs;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

public class BaseRel {

	protected static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	protected static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	protected static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	protected static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

	protected static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}
