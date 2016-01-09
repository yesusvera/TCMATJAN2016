<%@page import="br.com.docedesafio.dao.AlimentoDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.docedesafio.model.Alimento"%>
<%@page import="java.util.List"%>
<%@page import="br.com.docedesafio.connection.StatementFactory"%>
<%@page import="br.com.docedesafio.model.Usuario"%>
<%@ page language="java"
	import="java.sql.*"%>
<html>
<head>
<jsp:include page="./include/validacao_sessao.jsp" />
<title>Alimentos</title>
<jsp:include page="./include/docedesafio_style.jsp" />
</head>
<body>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />

	<div class="boxConteudoDoceDesafio">
		<div class="tituloBoxConteudo">REGISTROS DE ALIMENTOS</div>
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
					<td>Nome Alimento:</td>
					<td><input type="text" name="nomeAlimentoFiltro" /></td>
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
					<%
						if (usuario.getNivelAcesso().equals("1")) {
					%>
					<th>Usuário <img src="img/ordenacao.png" width="8px"/></th>
					<%
						}
					%>
					<th>Nome <img src="img/ordenacao.png" width="8px"/></th>
					<th>Medida <img src="img/ordenacao.png" width="8px"/></th>
					<th>Peso <img src="img/ordenacao.png" width="8px"/></th>
					<th>Carboidratos <img src="img/ordenacao.png" width="8px"/></th>
					<!-- <th>Excluir</th> -->
				</tr>
			</thead>
			<%
					AlimentoDAO alimentoDAO = new AlimentoDAO();
					if (request.getParameter("acao") != null) {
						alimentoDAO.delete(Integer.valueOf(request.getParameter("codigo")));
					 }
					
					List<Alimento> lista = new ArrayList<Alimento>();
					
					String nomeUsuarioFiltro = request.getParameter("nomeUsuarioFiltro");
					String nomeAlimentoFiltro = request.getParameter("nomeAlimentoFiltro");

					if (!usuario.getNivelAcesso().equals("1")) {
						lista = alimentoDAO.listByFilters(nomeUsuarioFiltro, nomeAlimentoFiltro, usuario.getId());
					} else {
						lista = alimentoDAO.listByFilters(nomeUsuarioFiltro, nomeAlimentoFiltro, null);
					}

					for(Alimento alimento: lista){ %>
						<tr>
							<% if (usuario.getNivelAcesso().equals("1")) { %>
							<td><%=alimento.getNomeUsuario()%></td>
							<% } %>
							<td><%=alimento.getNome()%></td>
							<td><%=alimento.getMedida()%></td>
							<td><%=alimento.getPeso()%></td>
							<td><%=alimento.getCarboidratos()%></td>
							<%-- <td>
							<a
								href="alimentos.jsp?acao=excluir&amp;codigo=<%=alimento.getId()%>">
									<img height="20px" width="20px" src="img/delete_icon.png"></img>
							</a>
							</td> --%> 
						</tr>
					<%	} %>
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

		<input type="button" name="imprimir" id="" value="Imprimir" />
	</div>

</body>
</html>