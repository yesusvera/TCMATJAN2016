package br.com.doce.desafio.visual;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class FactoryMessage {

	public static void createErrorMessage(HttpServletResponse response, String msg){
		String errorMessage ="<div class=\"div_erro\">"
				+ "<span class=\"erro\">"+msg+"</span>"
						+ "<span class=\"fechar\">X</span></div>";
		try {
			response.getWriter().print(errorMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
