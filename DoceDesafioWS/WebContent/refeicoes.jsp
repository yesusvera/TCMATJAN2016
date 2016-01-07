<%@page import="java.util.ArrayList"%>
<%@page import="br.com.docedesafio.model.Refeicao"%>
<%@page import="java.util.List"%>
<%@page import="br.com.docedesafio.dao.RefeicaoDAO"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="br.com.docedesafio.model.Usuario"%>
<%@page import="br.com.docedesafio.connection.StatementFactory"%>
<html>
<head>
<jsp:include page="./include/validacao_sessao.jsp" />
<title>Refeições</title>
<jsp:include page="./include/docedesafio_style.jsp" />
</head>
<body>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />

	<div class="boxConteudoDoceDesafio">
		<div class="tituloBoxConteudo">REGISTROS DE REFEIÇÕES</div>

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
					<td><input type="text" name="tipoRefeicaoFiltro" /></td>
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
				<th>Observação <img src="img/ordenacao.png" width="8px"/></th>
				<th>Peso <img src="img/ordenacao.png" width="8px"/></th>
				<th>Carboidratos <img src="img/ordenacao.png" width="8px"/></th>
				<th>Data <img src="img/ordenacao.png" width="8px"/></th>
				<!-- <th>Excluir</th> -->
			</tr>
			</thead>

			<%
				RefeicaoDAO refeicaoDAO = new RefeicaoDAO();
				
				List<Refeicao> lista = new ArrayList<Refeicao>();

				String nomeUsuarioFiltro = request.getParameter("nomeUsuarioFiltro");
				String tipoRefeicaoFiltro = request.getParameter("tipoRefeicaoFiltro");
	
				if (!usuario.getNivelAcesso().equals("1")) {
					lista = refeicaoDAO.listByFilters(nomeUsuarioFiltro, tipoRefeicaoFiltro, usuario.getId());
				} else {
					lista = refeicaoDAO.listByFilters(nomeUsuarioFiltro, tipoRefeicaoFiltro, null);
				}
				
				for(Refeicao refeicao: lista) {
			%>
			<tr>
				<%
					if (usuario.getNivelAcesso().equals("1")) {
				%>
				<td><%=refeicao.getNomeUsuario()%></td>
				<%
					}
				%>
				<td><%=refeicao.getTipo()%></td>
				<td><%=refeicao.getObservacao()%></td>
				<td><%=refeicao.getPeso()%></td>
				<td><%=refeicao.getCarboidrato()%></td>
				<td><%=refeicao.getData()%></td>
				<%-- <td><a
					href="refeicoes.jsp?acao=excluir&amp;codigo=<%=refeicao.getId()%>">
						<img height="20px" width="20px" src="img/delete_icon.png"></img>
				</a></td> --%>
			</tr>
			<%
				}
			%>
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