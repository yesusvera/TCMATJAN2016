package br.com.doce.desafio.visual.framework;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Display {

	public static String showDateFormat(Date date){
		if(date==null) return "";
		java.util.Date dtTmp = new java.util.Date(date.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(dtTmp);
	}
	
}