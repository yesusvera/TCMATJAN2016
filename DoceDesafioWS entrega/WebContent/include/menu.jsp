<%@page import="br.com.docedesafio.model.Usuario"%>
<div style="position: absolute;float: left;">
	<br/>
	<ul id="menu">
		<li class="ui-widget-header">Menu de Op��es</li>
		<li><a href="index.jsp">Home</a></li>
		<li><a href="alimentos.jsp">Alimentos</a></li>
		<li><a href="exercicios.jsp">Exerc�cios</a></li>
		<li><a href="glicemias.jsp">Glicemia</a></li>
		<li><a href="insulinas.jsp">Insulina</a></li>
		<li><a href="refeicoes.jsp">Refei��es</a></li>
		<%
			Usuario usuario = (Usuario) session.getAttribute("loggedInUser");

			if (usuario !=null && usuario.getNivelAcesso().equals("1")) {
		%>
		<li class="ui-widget-header">Administrativo</li>
		<li><a href="cadastrarUsuario.jsp">Cadastrar Usu�rio</a></li>
		<li><a href="usuarios.jsp">Listar Usu�rios</a></li>
		<%
			}
		%>
	</ul>
</div>