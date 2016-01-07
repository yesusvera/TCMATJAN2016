package br.com.docedesafio.services;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.doce.desafio.visual.framework.Integration;
import br.com.docedesafio.dao.InsulinaDAO;
import br.com.docedesafio.model.Insulina;
import br.com.docedesafio.services.model.Insulinas;
import br.com.docedesafio.services.model.Mensagem;

/**
 * Servlet implementation class InsulinasServlet
 */
@WebServlet("/service/insulinas/*")
public class InsulinasServlet extends BaseServicesRestServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getPrefixNameService() {
		return "/service/insulinas/";
	}

	@Override
	protected void listarPorLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Insulinas insulinas = new Insulinas();
		insulinas.setLista(new InsulinaDAO().listAll());
		excreveXML(request, response, insulinas);
		
	}

	@Override
	protected void inserir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existAllParams(request, "idLogin", "tipo", "quantidade", "data", "codigo")){
			Insulina ex = new Insulina();

			ex.setIdLogin(Integer.valueOf(request.getParameter("idLogin")));
			ex.setObservacao(request.getParameter("obs"));
			ex.setTipo(request.getParameter("tipo"));
			ex.setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
			try {
				Long timeInMillis = Long.valueOf(request.getParameter("data"));
				ex.setData(new Timestamp(timeInMillis));
			} catch (Exception e) {}
			
			ex.setCodigo(Integer.valueOf(request.getParameter("codigo")));

			new InsulinaDAO().save(ex);

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
		try {
			Insulinas insulinas = new Insulinas();
			int idLogin = Integer.valueOf(request.getParameter("id"));
			insulinas.setLista(new InsulinaDAO().listByLogin(idLogin));
			excreveXML(request, response, insulinas);
		} catch (Exception e) {
			Mensagem mensagem = new Mensagem();
			mensagem.setErro(true);
			mensagem.setMensagem(e.toString());
			excreveXML(request, response, mensagem);
		}
	}

	@Override
	protected void excluir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existAllParams(request, "idLogin", "codigo")){
	
			int idLogin = Integer.valueOf(request.getParameter("idLogin"));
			int codigo = Integer.valueOf(request.getParameter("codigo"));
			
			new InsulinaDAO().deleteByIdLoginCodigo(idLogin, codigo);
			
			mensagem.setErro(false);
			mensagem.setMensagem("Excluído com sucesso.");
		}else{
			mensagem.setErro(true);
			mensagem.setMensagem("Você precisa informar idLogin e código.");
		}
		excreveXML(request, response, mensagem);
		
	}
}
