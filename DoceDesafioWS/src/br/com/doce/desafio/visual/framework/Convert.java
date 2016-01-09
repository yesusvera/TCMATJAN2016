package br.com.doce.desafio.visual.framework;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Convert {

	public static String getDtFormatada(Timestamp ts) {
		if (ts == null) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			Date dt = new Date(ts.getTime());
			return sdf.format(dt);
		}

	}
}
