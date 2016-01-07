<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="br.com.docedesafio.dao.LoginDAO"%>
<%@page import="br.com.docedesafio.model.Usuario"%>
<%@page import="br.com.docedesafio.connection.StatementFactory"%>
<html>
<head>
<jsp:include page="./include/validacao_sessao.jsp" />
<title>Doce Desafio - Insulina</title>
<jsp:include page="./include/docedesafio_style.jsp" />
<script>
	var id=0;
	function excluirUsuario(_id){
		id = _id;
		$( "#dialog-confirm" ).dialog( "open" );
	}
	
	$(function() {
	$( "#dialog-confirm" ).dialog({
		  autoOpen: false,
	      resizable: false,
	      height:250,
	      modal: true,
	      buttons: {
	        "Excluir": function() {
	          document.location = "?acao=excluir&id="+id;
	        },
	        "Cancelar": function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	
	  });
</script>
</head>
<body>
	<div id="dialog-confirm" title="Exclusão de usuário?">
		<p>
			<span class="ui-icon ui-icon-alert"
				style="float: left; margin: 0 7px 20px 0;"></span>
			Deseja realmente excluir este usuário?
		</p>
	</div>

	<% Usuario usuario = (Usuario) session.getAttribute("loggedInUser"); 
				if (!usuario.getNivelAcesso().equals("1")) {
	%>
				<jsp:forward page="login.jsp"/>
	<% 
				
				} %>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />

	<div class="boxConteudoDoceDesafio">

		<div class="tituloBoxConteudo">Lista de Usuários</div>
		
		<form method="post">
			<table align="center">
				<tr>
					<td>Nome usuário:</td>
					<td><input type="text" name="nomeUsuarioFiltro" /></td>
				</tr>
				<tr>
					<td>Login:</td>
					<td><input type="text" name="loginUsuarioFiltro" /></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td><input type="text" name="emailUsuarioFiltro" /></td>
				</tr>
				<tr>
					<td>Nível Acesso:</td>
					<td><input type="text" name="nivelAcessoUsuarioFiltro" /></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
						<input type="submit" value="Pesquisar" />
						<input type="button" value="Novo Usuário" onclick="document.location='cadastrarUsuario.jsp'"/>
					</td>
				</tr>
			</table>
		</form>
		
		<hr/>
		
		<table id="tblPrincipal" border="1">
			<thead>
				<tr>
					<th>Nome Usuário<img src="img/ordenacao.png" width="8px" /></th>
					<th>Login<img src="img/ordenacao.png" width="8px" /></th>
					<th>Email<img src="img/ordenacao.png" width="8px" /></th>
					<th>Nível Acesso<img src="img/ordenacao.png" width="8px" /></th>
					<th>Acões</th>
				</tr>
			</thead>
			<%      LoginDAO loginDAO = new LoginDAO();
			
					String acao = request.getParameter("acao");
					
					if(acao!=null && acao.equals("excluir")){
						int id = Integer.valueOf(request.getParameter("id"));
						loginDAO.delete(id);
					}
			
					List<Usuario> listaUsuarios = new ArrayList<Usuario>();
					
					Usuario userFiltro = new Usuario();
					userFiltro.setNome(request.getParameter("nomeUsuarioFiltro"));
					userFiltro.setLogin(request.getParameter("loginUsuarioFiltro"));
					userFiltro.setEmail(request.getParameter("emailUsuarioFiltro"));
					userFiltro.setNivelAcesso(request.getParameter("nivelAcessoUsuarioFiltro"));
					
					listaUsuarios = loginDAO.listAll(userFiltro);
					
					for(Usuario usr: listaUsuarios) {
			%>
			<tr>
				<td><%=usr.getNome()%></td>
				<td><%=usr.getLogin()%></td>
				<td><%=usr.getEmail()%></td>
				<td><%=usr.getNivelAcesso().equals("1") ? "Administrador" : "Usuário"%></td>
				<td>
					<a href="cadastrarUsuario.jsp?idUsuario=<%=usr.getId()%>"><img src="img/edit_icon.png"/></a>
					<img src="img/delete_icon.png" onclick="excluirUsuario(<%=usr.getId()%>)"/>
				</td>
			</tr>
			<%
				}
			%>
		
		</table>
		<span class="msgRegistros">
			<i>
				<b><%=listaUsuarios.size()%></b>
				registro(s) encontrados.
			</i> 
		</span>

		<script>
			$(document).ready(function() {
				$("#tblPrincipal").tablesorter();
			});
		</script>
	</div>
</body>
</html>