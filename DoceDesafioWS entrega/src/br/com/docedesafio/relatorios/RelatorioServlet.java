package br.com.docedesafio.relatorios;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/exercicios")
public class RelatorioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String nome = request.getServletContext().getRealPath("/WEB-INF/reports/exercicios.jasper");
			Map<String, Object> parametros = new HashMap<String, Object>();
			Connection connection = new ConnectionFactory().getConnection();
			
			GeradorRelatorio gerador = new GeradorRelatorio(nome, parametros, connection);
			gerador.geraPDFParaOutputStream(response.getOutputStream());
			connection.close();
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

}
