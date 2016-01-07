package br.com.docedesafio.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.docedesafio.dao.AlimentoDAO;
import br.com.docedesafio.dao.ExercicioDAO;
import br.com.docedesafio.dao.GlicemiaDAO;
import br.com.docedesafio.dao.InsulinaDAO;
import br.com.docedesafio.dao.LoginDAO;
import br.com.docedesafio.dao.RefeicaoDAO;
import br.com.docedesafio.model.Usuario;
import br.com.docedesafio.services.model.Authenticate;
import br.com.docedesafio.services.model.Mensagem;

/**
 * Servlet implementation class AlimentosServlet
 */
@WebServlet("/service/authentication/*")
public class LoginService extends BaseServicesRestServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Authenticate auth = new Authenticate();
		
		if(request.getRequestURI().lastIndexOf("/service/authentication/login")!=-1){
			try{
				String login = request.getParameter("user");
				String password = request.getParameter("password");
				
				LoginDAO loginDAO = new LoginDAO();
				Usuario usr = loginDAO.loginUsuario(login, password);
				
				auth.setUser(usr);
				Mensagem mensagem = new Mensagem();

				if(usr==null){
					mensagem.setErro(true);
					mensagem.setMensagem("Usuário ou senha inválido.");
				}else{
					
					usr.setListaAlimentos(new AlimentoDAO().listByLogin(usr.getId()));
					usr.setListaExercicios(new ExercicioDAO().listByLogin(usr.getId()));
					usr.setListaGlicemia(new GlicemiaDAO().listByLogin(usr.getId()));
					usr.setListaInsulina(new InsulinaDAO().listByLogin(usr.getId()));
					usr.setListaRefeicoes(new RefeicaoDAO().listByLogin(usr.getId()));
					
					mensagem.setErro(false);
					mensagem.setMensagem("Login OK.");
				}
				auth.setMessage(mensagem);
			}catch(Exception e){
				Mensagem mensagem = new Mensagem();
				mensagem.setErro(true);
				mensagem.setMensagem(e.toString());
				excreveXML(request, response, mensagem);
				return;
			}
		}
		
		excreveXML(request, response, auth);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected String getPrefixNameService() {
		return null;
	}

	@Override
	protected void listarPorLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void inserir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void listarTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void excluir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
