package br.com.docedesafio.services;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.doce.desafio.visual.framework.Integration;
import br.com.docedesafio.dao.GlicemiaDAO;
import br.com.docedesafio.model.Glicemia;
import br.com.docedesafio.services.model.Glicemias;
import br.com.docedesafio.services.model.Mensagem;

/**
 * Servlet implementation class GlicemiasServlet
 */
@WebServlet("/service/glicemias/*")
public class GlicemiasServlet extends BaseServicesRestServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getPrefixNameService() {
		return "/service/glicemias/";
	}

	@Override
	protected void listarPorLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Glicemias glicemias = new Glicemias();
			int idLogin = Integer.valueOf(request.getParameter("id"));
			glicemias.setLista(new GlicemiaDAO().listByLogin(idLogin));
			excreveXML(request, response, glicemias);
		} catch (Exception e) {
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

		if(Integration.existAllParams(request, "idLogin", "tipo", "medida","data","codigo")){
			Glicemia obj = new Glicemia();

			obj.setIdLogin(Integer.valueOf(request.getParameter("idLogin")));
			obj.setTipo(request.getParameter("tipo"));
			obj.setObservacao(request.getParameter("obs"));
			obj.setMedida(Integer.valueOf(request.getParameter("medida")));
			try {
				Long timeInMillis = Long.valueOf(request.getParameter("data"));
				obj.setData(new Timestamp(timeInMillis));
			} catch (Exception e) {}
			
			obj.setCodigo(Integer.valueOf(request.getParameter("codigo")));

			new GlicemiaDAO().save(obj);

			mensagem.setErro(false);
			mensagem.setMensagem("Salvo com sucesso.");
		} else {
			mensagem.setErro(true);
			mensagem.setMensagem("Faltam parâmetros.");
		}
		excreveXML(request, response, mensagem);
	}

	@Override
	protected void listarTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Glicemias glicemias = new Glicemias();
		glicemias.setLista(new GlicemiaDAO().listAll());
		excreveXML(request, response, glicemias);
	}

	@Override
	protected void excluir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existAllParams(request, "idLogin", "codigo")){
	
			int idLogin = Integer.valueOf(request.getParameter("idLogin"));
			int codigo = Integer.valueOf(request.getParameter("codigo"));
			
			new GlicemiaDAO().deleteByIdLoginCodigo(idLogin, codigo);
			
			mensagem.setErro(false);
			mensagem.setMensagem("Excluído com sucesso.");
		}else{
			mensagem.setErro(true);
			mensagem.setMensagem("Você precisa informar idLogin e código.");
		}
		excreveXML(request, response, mensagem);

		
	}

}
