package br.com.docedesafio.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public abstract class BaseServicesRestServlet extends HttpServlet {

	private static final long serialVersionUID = -8578212434386639948L;
	
	protected JAXBContext context;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		System.out.println(request.getRequestURI());
		
		if(request.getRequestURI().lastIndexOf(getPrefixNameService()+"listarTodos")!=-1){
			listarTodos(request, response);
		}else if(request.getRequestURI().lastIndexOf(getPrefixNameService()+"listarPorLogin")!=-1){
			listarPorLogin(request, response);
		}else if(request.getRequestURI().lastIndexOf(getPrefixNameService()+"inserir")!=-1){
			inserir(request, response);
		}else if(request.getRequestURI().lastIndexOf(getPrefixNameService()+"excluir")!=-1){
			excluir(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	protected void excreveXML(HttpServletRequest req, HttpServletResponse resp, Object objectWriter) throws ServletException, IOException {
		try {
			resp.setContentType("application/xml;charset=UTF-8");
			PrintWriter out = resp.getWriter();

			XStream xStream = new XStream(new DomDriver());
			String xml = xStream.toXML(objectWriter);

			out.print(xml);
		} catch (Exception e) {
			resp.sendError(500, e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected abstract String getPrefixNameService();
	
	protected abstract void listarPorLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	protected abstract void inserir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
	
	protected abstract void excluir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	protected abstract void listarTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
}
