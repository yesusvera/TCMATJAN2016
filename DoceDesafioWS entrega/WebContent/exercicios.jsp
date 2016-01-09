<%@page import="br.com.docedesafio.model.Exercicio"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="br.com.docedesafio.dao.ExercicioDAO"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="br.com.docedesafio.connection.StatementFactory"%>
<%@page import="br.com.docedesafio.model.Usuario"%>
<html>
<head>
<jsp:include page="./include/validacao_sessao.jsp" />
<title>Exercícios</title>

<jsp:include page="./include/docedesafio_style.jsp" />
</head>

<body>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />

	<div class="boxConteudoDoceDesafio">
		<div class="tituloBoxConteudo">REGISTROS DE EXERCÍCIOS</div>
		
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
					<td><input type="text" name="tipoExercicioFiltro" /></td>
				</tr>
				<tr>
					<td>Modalidade:</td>
					<td><input type="text" name="modalidadeExercicioFiltro" /></td>
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
				<th>Descrição <img src="img/ordenacao.png" width="8px"/></th>
				<th>Tipo <img src="img/ordenacao.png" width="8px"/></th>
				<th>Modalidade <img src="img/ordenacao.png" width="8px"/></th>
				<th>Intensidade <img src="img/ordenacao.png" width="8px"/></th>
				<th>Duração <img src="img/ordenacao.png" width="8px"/></th>
				<th>Data <img src="img/ordenacao.png" width="8px"/></th>
				<!-- <th>Excluir</th> -->
			</tr>
			</thead>
			<%		ExercicioDAO exercicioDAO = new ExercicioDAO();
					List<Exercicio> lista = new ArrayList<Exercicio>();
			
					String nomeUsuarioFiltro = request.getParameter("nomeUsuarioFiltro");
					String tipoExercicioFiltro = request.getParameter("tipoExercicioFiltro");
					String modalidadeExercicioFiltro = request.getParameter("modalidadeExercicioFiltro");
					
					if (!usuario.getNivelAcesso().equals("1")) {
						lista = exercicioDAO.listByFilters(nomeUsuarioFiltro, tipoExercicioFiltro, modalidadeExercicioFiltro, usuario.getId());
					} else {
						lista = exercicioDAO.listByFilters(nomeUsuarioFiltro, tipoExercicioFiltro, modalidadeExercicioFiltro, null);
					}

					for(Exercicio exercicio: lista) {
			%>
			<tr>
				<%
					if (usuario.getNivelAcesso().equals("1")) {
				%>
				<td><%=exercicio.getNomeUsuario()%></td>
				<%
					}
				%>
				<td><%=exercicio.getDescricao()%></td>
				<td><%=exercicio.getTipo()%></td>
				<td><%=exercicio.getModalidade()%></td>
				<td><%=exercicio.getIntensidade()%></td>
				<td><%=exercicio.getDuracao()%></td>
				<td><%=exercicio.getData()%></td>
				<%-- <td><a
					href="exercicios.jsp?acao=excluir&amp;codigo=<%=exercicio.getId()%>">
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
		
		<input type="button" name="imprimir" id="" value="Imprimir" />
	</div>
</body>
</html>