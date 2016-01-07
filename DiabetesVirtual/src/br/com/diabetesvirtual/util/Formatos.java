package br.com.diabetesvirtual.util;

import java.text.DecimalFormat;
import java.util.Locale;

public class Formatos {

	 public static String formataDouble(double d){ //formata o numero com virgula para menos casas decimais
	       int a;
		   DecimalFormat df = new DecimalFormat();
		   df.setMaximumFractionDigits(2);
	       if(d == (int) d){
	           a = (int) d;
	    	   return String.format(Locale.getDefault(), "%d", a);
		   } else {
			   String x = df.format(d);
	       	   x.replace(",", ".");	
	           return x;
		   }
	   }
	 
	 public static String formataDoubleCasaDec(double d){ //formata o numero com virgula para menos casas decimais
		   DecimalFormat df = new DecimalFormat();
		   df.setMaximumFractionDigits(2);
	       String s = df.format(d);	       
		   return s.replace(",", ".");
	  }
	 
	 public static double formataUmaCasa(double d){ //formata o numero com virgula para menos casas decimais
		   DecimalFormat df = new DecimalFormat();
		   df.setMaximumFractionDigits(1);
		   String s = df.format(d);	 
		   double x = Double.parseDouble(s.replaceAll(",", "."));	       
		   return x;
	  }
	
	 public static String formataData(int year, int month, int day){ //formata a data
	       return String.format(Locale.getDefault(), "%02d%02d%02d",day, month, year);
	   }
}
