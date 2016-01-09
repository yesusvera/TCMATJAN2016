<%@page import="br.com.docedesafio.dao.LoginDAO"%>
<%@page import="br.com.docedesafio.model.Usuario"%>
<html>
<head>
<jsp:include page="./include/docedesafio_style.jsp" />
<title>Login</title>
</head>
<body>
	<%
		LoginDAO loginDAO = new LoginDAO();
	
		String login = request.getParameter("login");	
		String senha = request.getParameter("senha");
		
		if(login!=null){
			Usuario usuario = loginDAO.loginUsuario(login, senha);
			
			if(usuario!=null){
				session.setAttribute("loggedInUser", usuario);
	%>
				<jsp:forward page="index.jsp" />
	<%		} else {
				session.setAttribute("loggedInUser", null);
	%>
				<script>
					alert("Usuário ou senha inválidos!");
				</script>
	<%		}
		}
	%>
	<div id="header">
		<h1>DOCE DESAFIO</h1>
	</div>
	<br />
	<div style="margin-left: 200px; position: absolute; float: center;">
		<img src="img/ic_launcher-web.png"></img>
	</div>
	<div class="boxLogin">
		<form method="post" action="">
			<div align="center">
				<br />
				<h1>Acesso ao Sistema</h1>
				<span class="style3">Usuário:</span> 
				<input name="login" type="text" id="usuario" size="20" /> <br /> <br />
				
				<span class="style3">Senha:</span>
				<input name="senha" type="password" id="senha" size="20" /> <br /><br />
				
				<input type="reset" name="limpar" id="limpar" value="Limpar" /> <input
					type="submit" name="acesso" id="acesso" value="Acessar" />
			</div>
		</form>
	</div>
</body>
</html>