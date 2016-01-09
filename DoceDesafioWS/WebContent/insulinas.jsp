<%@page import="br.com.doce.desafio.visual.framework.Convert"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="br.com.docedesafio.dao.InsulinaDAO"%>
<%@page import="br.com.docedesafio.model.Insulina"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="br.com.docedesafio.model.Usuario"%>
<%@page import="br.com.docedesafio.connection.StatementFactory"%>
<html>
<head>
<jsp:include page="./include/validacao_sessao.jsp" />
<title>Doce Desafio - Insulina</title>
<jsp:include page="./include/docedesafio_style.jsp" />
</head>
<body>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />

	<div class="boxConteudoDoceDesafio">

		<div class="tituloBoxConteudo">REGISTROS DE INSULINAS</div>
		
		<% Usuario usuario = (Usuario) session.getAttribute("loggedInUser"); %>
		<form method="post">
			<table align="center">
			<%
				if (usuario.getNivelAcesso().equals("1")) {
			%>
				<tr>
					<td>Nome usuário:</td>
					<td><input type="text" name="nomeUsuarioFiltro" /></td>
				</tr>
			<%
				}
			%>
				<tr>
					<td>Tipo:</td>
					<td><input type="text" name="tipoInsulinaFiltro" /></td>
				</tr>
				<tr>
					<td colspan="2"><center>
							<input type="submit" value="Pesquisar" />
						</center>
					</td>
				</tr>
			</table>
		</form>
		
		<hr/>

		<table id="tblPrincipal" align="center" border="1">
			<thead>
				<tr>
				<%
					if (usuario.getNivelAcesso().equals("1")) {
				%>
				<th>Usuário <img src="img/ordenacao.png" width="8px"/></th>
				<%
					}
				%>
				<th>Tipo <img src="img/ordenacao.png" width="8px"/></th>
				<th>Quantidade <img src="img/ordenacao.png" width="8px"/></th>
				<th>Observação <img src="img/ordenacao.png" width="8px"/></th>
				<th>Data <img src="img/ordenacao.png" width="8px"/></th>
				<!-- <th>Excluir</th> -->
			</tr>
			</thead>
			<%
				InsulinaDAO insulinaDAO = new InsulinaDAO();
			
				List<Insulina> lista = new ArrayList<Insulina>();

				String nomeUsuarioFiltro = request.getParameter("nomeUsuarioFiltro");
				String tipoInsulinaFiltro = request.getParameter("tipoInsulinaFiltro");
	
				if (!usuario.getNivelAcesso().equals("1")) {
					lista = insulinaDAO.listByFilters(nomeUsuarioFiltro, tipoInsulinaFiltro, usuario.getId());
				} else {
					lista = insulinaDAO.listByFilters(nomeUsuarioFiltro, tipoInsulinaFiltro, null);
				}
				
				for(Insulina insulina: lista) {
			%>
			<tr>
				<% if (usuario.getNivelAcesso().equals("1")) { %>
				<td><%=insulina.getNomeUsuario()%></td>
				<% } %>
				<td><%=insulina.getTipo()%></td>
				<td><%=insulina.getQuantidade()%></td>
				<td><%=insulina.getObservacao()%></td>
				<td><%=Convert.getDtFormatada(insulina.getData())%></td>
				<%-- <td><a
					href="insulinas.jsp?acao=excluir&amp;codigo=<%=insulina.getId()%>">
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