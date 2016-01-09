package br.com.docedesafio.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.doce.desafio.visual.framework.Integration;
import br.com.docedesafio.dao.AlimentoDAO;
import br.com.docedesafio.model.Alimento;
import br.com.docedesafio.services.model.Alimentos;
import br.com.docedesafio.services.model.Mensagem;

/**
 * Servlet implementation class AlimentosServlet
 */
@WebServlet("/service/alimentos/*")
public class AlimentosServlet extends BaseServicesRestServlet {
	private static final long serialVersionUID = 1L;


	@Override
	protected void listarTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Alimentos alimentos = new Alimentos();
		alimentos.setLista(new AlimentoDAO().listAll());
		excreveXML(request, response, alimentos);
	}

	
	@Override
	protected void listarPorLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			Alimentos alimentos = new Alimentos();
			int idLogin = Integer.valueOf(request.getParameter("id"));
			alimentos.setLista(new AlimentoDAO().listByLogin(idLogin));
			excreveXML(request, response, alimentos);
		}catch(Exception e){
			Mensagem mensagem = new Mensagem();
			mensagem.setErro(true);
			mensagem.setMensagem(e.toString());
			excreveXML(request, response, mensagem);
		}
	}

	@Override
	protected void inserir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existParam(request, "idLogin") &&
				Integration.existParam(request, "nome") &&
				Integration.existParam(request, "medida") &&
				Integration.existParam(request, "peso") &&
				Integration.existParam(request, "carboidratos") &&
				Integration.existParam(request, "codigo")){
			
			Alimento alimentoSave = new Alimento();
			alimentoSave.setIdLogin(Integer.valueOf(request.getParameter("idLogin")));
			alimentoSave.setNome(request.getParameter("nome"));
			alimentoSave.setMedida(request.getParameter("medida"));
			try{ 
				alimentoSave.setPeso(Integer.valueOf(request.getParameter("peso")));
			}catch(NumberFormatException nfe){}
			
			try{
				alimentoSave.setCarboidratos(Integer.valueOf(request.getParameter("carboidratos")));
			}catch(NumberFormatException nfe){}
			
			try{
			alimentoSave.setCodigo(Integer.valueOf(request.getParameter("codigo")));
			}catch(NumberFormatException nfe){}
			
			new AlimentoDAO().save(alimentoSave);
			
			mensagem.setErro(false);
			mensagem.setMensagem("Alimento salvo com sucesso.");
		}else{
			mensagem.setErro(true);
			mensagem.setMensagem("Você precisa informar idLogin, nome, medida, peso, carboidratos e código.");
		}
		
		excreveXML(request, response, mensagem);
	}

	@Override
	protected String getPrefixNameService() {
		return "/service/alimentos/";
	}


	@Override
	protected void excluir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existParam(request, "idLogin") &&
				Integration.existParam(request, "codigo")){
			
			int idLogin = Integer.valueOf(request.getParameter("idLogin"));
			int codigo = Integer.valueOf(request.getParameter("codigo"));
			
			new AlimentoDAO().deleteByIdLoginCodigo(idLogin, codigo);
			
			mensagem.setErro(false);
			mensagem.setMensagem("Alimento excluído com sucesso.");
		}else{
			mensagem.setErro(true);
			mensagem.setMensagem("Você precisa informar idLogin e código.");
		}
		excreveXML(request, response, mensagem);
	}
}
