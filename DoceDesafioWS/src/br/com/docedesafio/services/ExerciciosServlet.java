package br.com.docedesafio.services;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.doce.desafio.visual.framework.Integration;
import br.com.docedesafio.dao.ExercicioDAO;
import br.com.docedesafio.model.Exercicio;
import br.com.docedesafio.services.model.Exercicios;
import br.com.docedesafio.services.model.Mensagem;

/**
 * Servlet implementation class ExerciciosServlet
 */
@WebServlet("/service/exercicios/*")
public class ExerciciosServlet extends BaseServicesRestServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getPrefixNameService() {
		return "/service/exercicios/";
	}

	@Override
	protected void listarPorLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Exercicios exercicios = new Exercicios();
			int idLogin = Integer.valueOf(request.getParameter("id"));
			exercicios.setLista(new ExercicioDAO().listByLogin(idLogin));

			excreveXML(request, response, exercicios);
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

		if(Integration.existAllParams(request, "idLogin", "tipo",
												"modalidade", "intensidade","duracao", "data", "codigo")){
			Exercicio ex = new Exercicio();

			ex.setIdLogin(Integer.valueOf(request.getParameter("idLogin")));
			ex.setDescricao(request.getParameter("descricao"));
			ex.setTipo(request.getParameter("tipo"));
			ex.setModalidade(request.getParameter("modalidade"));
			ex.setIntensidade(request.getParameter("intensidade"));
			ex.setDuracao(Integer.valueOf(request.getParameter("duracao")));
			try {
				Long timeInMillis = Long.valueOf(request.getParameter("data"));
				ex.setData(new Timestamp(timeInMillis));
			} catch (Exception e) {}
			
			ex.setCodigo(Integer.valueOf(request.getParameter("codigo")));

			new ExercicioDAO().save(ex);

			mensagem.setErro(false);
			mensagem.setMensagem("Exercício salvo com sucesso.");
		} else {
			mensagem.setErro(true);
			mensagem.setMensagem("Faltam parâmetros.");
		}
		excreveXML(request, response, mensagem);
	}

	@Override
	protected void listarTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Exercicios exercicios = new Exercicios();
		exercicios.setLista(new ExercicioDAO().listAll());

		excreveXML(request, response, exercicios);
	}

	@Override
	protected void excluir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existAllParams(request, "idLogin", "codigo")){
	
			int idLogin = Integer.valueOf(request.getParameter("idLogin"));
			int codigo = Integer.valueOf(request.getParameter("codigo"));
			
			new ExercicioDAO().deleteByIdLoginCodigo(idLogin, codigo);
			
			mensagem.setErro(false);
			mensagem.setMensagem("Exercício excluído com sucesso.");
		}else{
			mensagem.setErro(true);
			mensagem.setMensagem("Você precisa informar idLogin e código.");
		}
		excreveXML(request, response, mensagem);

	}
}
