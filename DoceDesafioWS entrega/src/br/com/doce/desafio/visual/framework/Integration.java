package br.com.doce.desafio.visual.framework;

import javax.servlet.http.HttpServletRequest;

public class Integration {

	public static boolean existParam(HttpServletRequest request, String param){
		String parameter = request.getParameter(param);
		if(parameter!=null && !parameter.trim().equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean existAllParams(HttpServletRequest request, String ... params){
		for(String p: params){
			if(!existParam(request, p)){
				return false;
			}
		}
		return true;
	}
}
