package br.com.docedesafio.services;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.doce.desafio.visual.framework.Integration;
import br.com.docedesafio.dao.RefeicaoDAO;
import br.com.docedesafio.model.Refeicao;
import br.com.docedesafio.services.model.Mensagem;
import br.com.docedesafio.services.model.Refeicoes;

/**
 * Servlet implementation class RefeicoesServlet
 */
@WebServlet("/service/refeicoes/*")
public class RefeicoesServlet extends BaseServicesRestServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getPrefixNameService() {
		return "/service/refeicoes/";
	}

	@Override
	protected void listarPorLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			Refeicoes refeicoes = new Refeicoes();
			int idLogin = Integer.valueOf(request.getParameter("id"));
			refeicoes.setLista(new RefeicaoDAO().listByLogin(idLogin));
			excreveXML(request, response, refeicoes);
		}catch(Exception e){
			Mensagem mensagem = new Mensagem();
			mensagem.setErro(true);
			mensagem.setMensagem(e.toString());
			excreveXML(request, response, mensagem);
			return;
		}
	}

	@Override
	protected void inserir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existAllParams(request, "idLogin","carboidrato", "peso", "tipo","data","codigo")){
			Refeicao refeicao = new Refeicao();

			refeicao.setIdLogin(Integer.valueOf(request.getParameter("idLogin")));
			refeicao.setCarboidrato(Integer.valueOf(request.getParameter("carboidrato")));
			refeicao.setPeso(Integer.valueOf(request.getParameter("peso")));
			refeicao.setObservacao(request.getParameter("obs"));
			refeicao.setTipo(request.getParameter("tipo"));
			try {
				Long timeInMillis = Long.valueOf(request.getParameter("data"));
				refeicao.setData(new Timestamp(timeInMillis));
			} catch (Exception e) {}
			
			refeicao.setCodigo(Integer.valueOf(request.getParameter("codigo")));

			new RefeicaoDAO().save(refeicao);

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
		Refeicoes refeicoes = new Refeicoes();
		refeicoes.setLista(new RefeicaoDAO().listAll());
		excreveXML(request, response, refeicoes);
	}

	@Override
	protected void excluir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Mensagem mensagem = new Mensagem();

		if(Integration.existAllParams(request, "idLogin", "codigo")){
	
			int idLogin = Integer.valueOf(request.getParameter("idLogin"));
			int codigo = Integer.valueOf(request.getParameter("codigo"));
			
			new RefeicaoDAO().deleteByIdLoginCodigo(idLogin, codigo);
			
			mensagem.setErro(false);
			mensagem.setMensagem("Excluído com sucesso.");
		}else{
			mensagem.setErro(true);
			mensagem.setMensagem("Você precisa informar idLogin e código.");
		}
		excreveXML(request, response, mensagem);
		
	}
}