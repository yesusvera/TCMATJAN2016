<%@page import="java.util.ArrayList"%>
<%@page import="br.com.docedesafio.model.Glicemia"%>
<%@page import="java.util.List"%>
<%@page import="br.com.docedesafio.dao.GlicemiaDAO"%>
<%@page import="br.com.docedesafio.model.Usuario"%>
<%@page import="br.com.docedesafio.connection.StatementFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.sql.*" pageEncoding="UTF-8"%>
<html>
<head>
<jsp:include page="./include/validacao_sessao.jsp" />
<title>Glicemias</title>
<jsp:include page="./include/docedesafio_style.jsp" />
</head>
<body>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />

	<div class="boxConteudoDoceDesafio">
		<div class="tituloBoxConteudo">REGISTROS DE GLICEMIA</div>

		<% Usuario usuario = (Usuario) session.getAttribute("loggedInUser"); %>
		<form method="post">
			<table align="center">
				<% if (usuario.getNivelAcesso().equals("1")) { %>
				
				<tr>
					<td>Nome usuário:</td>
					<td><input type="text" name="nomeUsuarioFiltro" /></td>
				</tr>
				
				<% } %>
				<tr>
					<td>Tipo:</td>
					<td><input type="text" name="tipoGlicemiaFiltro" /></td>
				</tr>
				<tr>
					<td colspan="2"><center><input type="submit" value="Pesquisar" /></center></td>
				</tr>
			</table>
		</form>
		
		<hr/>

		<table id="tblPrincipal" align="center" border="1">
			<thead>
				<tr>
				<% if (usuario.getNivelAcesso().equals("1")) { %>
				<th>Usuário <img src="img/ordenacao.png" width="8px"/></th>
				<% } %>
				<th>Tipo <img src="img/ordenacao.png" width="8px"/></th>
				<th>Data <img src="img/ordenacao.png" width="8px"/></th>
				<th>Medida <img src="img/ordenacao.png" width="8px"/></th>
				<th>Observações <img src="img/ordenacao.png" width="8px"/></th>
				<!-- <th>Excluir</th> -->
				</tr>
			</thead>
			<%      GlicemiaDAO glicemiaDAO = new GlicemiaDAO();
					List<Glicemia> lista = new ArrayList<Glicemia>();

					String nomeUsuarioFiltro = request.getParameter("nomeUsuarioFiltro");
					String tipoGlicemiaFiltro = request.getParameter("tipoGlicemiaFiltro");

					if (!usuario.getNivelAcesso().equals("1")) {
						lista = glicemiaDAO.listByFilters(nomeUsuarioFiltro, tipoGlicemiaFiltro, usuario.getId());
					} else {
						lista = glicemiaDAO.listByFilters(nomeUsuarioFiltro, tipoGlicemiaFiltro, null);
					}
					for(Glicemia glicemia: lista) {
			%>
			<tr>
				<% if (usuario.getNivelAcesso().equals("1")) { %>
				<td><%=glicemia.getNomeUsuario()%></td>
				<% } %>
				<td><%=glicemia.getTipo()%></td>
				<td><%=glicemia.getData()%></td>
				<td><%=glicemia.getMedida()%></td>
				<td><%=glicemia.getObservacao()%></td>
				<%-- <td><a
					href="glicemias.jsp?acao=excluir&amp;codigo=<%=glicemia.getId()%>">
						<img height="20px" width="20px" src="img/delete_icon.png"></img>
				</a></td> --%>
			</tr>
			<% } %>
		</table>
		<span class="msgRegistros">
			<i>
				<b><%=lista.size()%></b>
				registro(s) encontrados.
			</i> 
		</span>
		<hr/>
		<script>
			$(document).ready(function() {
				$("#tblPrincipal").tablesorter();
			});
		</script>
	</div>
</body>
</html>