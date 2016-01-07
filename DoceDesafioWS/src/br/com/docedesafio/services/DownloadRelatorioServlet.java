package br.com.docedesafio.services;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadRelatorioServlet
 */
@WebServlet("/DownloadRelatorioServlet")
public class DownloadRelatorioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DownloadRelatorioServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "arquivo.pdf");
		response.setHeader(headerKey, headerValue);
		
		// obtains response's output stream
		OutputStream outStream = response.getOutputStream();

		outStream.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}