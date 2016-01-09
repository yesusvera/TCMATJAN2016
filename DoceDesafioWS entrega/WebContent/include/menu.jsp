<%@page import="br.com.docedesafio.model.Usuario"%>
<div style="position: absolute;float: left;">
	<br/>
	<ul id="menu">
		<li class="ui-widget-header">Menu de Opções</li>
		<li><a href="index.jsp">Home</a></li>
		<li><a href="alimentos.jsp">Alimentos</a></li>
		<li><a href="exercicios.jsp">Exercícios</a></li>
		<li><a href="glicemias.jsp">Glicemia</a></li>
		<li><a href="insulinas.jsp">Insulina</a></li>
		<li><a href="refeicoes.jsp">Refeições</a></li>
		<%
			Usuario usuario = (Usuario) session.getAttribute("loggedInUser");

			if (usuario !=null && usuario.getNivelAcesso().equals("1")) {
		%>
		<li class="ui-widget-header">Administrativo</li>
		<li><a href="cadastrarUsuario.jsp">Cadastrar Usuário</a></li>
		<li><a href="usuarios.jsp">Listar Usuários</a></li>
		<%
			}
		%>
	</ul>
</div>