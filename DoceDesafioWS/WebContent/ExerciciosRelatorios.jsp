<%@ page language="java" contentType="application/pdf; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="net.sf.jasperreports.engine.*"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileNotFoundException"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.SQLException"%>


<%@page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@page import="net.sf.jasperreports.engine.util.JRLoader"%>
<%@page import="net.sf.jasperreports.engine.JasperReport"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.File"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@page import="net.sf.jasperreports.view.JasperViewer"%>
<%@page import="java.sql.ResultSet"%>

<!DOCTYPE html>


<html>
<head>
<title>JSP Page</title>
</head>
<body>
	<h1>Testando.</h1>

	<%
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/doce_desafio", "postgres",
				"admin");

		String relatorio = null;

		relatorio = "/WEB-INF/relatorios/RelatorioExercicios.jasper";

		File reportFile = new File(application.getRealPath(relatorio));

		/* Map parameters = new HashMap();

		parameters.put("descricao", "preco"); */

		byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), null, con);

		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);

		ouputStream.flush();
		ouputStream.close();
	%>

</body>
</html>
