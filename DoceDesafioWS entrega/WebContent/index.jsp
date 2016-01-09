<%@page import="br.com.docedesafio.model.Usuario"%>
<html>
<head>
<jsp:include page="./include/validacao_sessao.jsp" />
<jsp:include page="./include/docedesafio_style.jsp" />
<title>Doce desafio</title>
</head>
<body>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />
	
	<div class="boxConteudoDoceDesafio">
		<br/><br/>
		<%Usuario usuario = (Usuario) session.getAttribute("loggedInUser"); %>
		<center>
			Seja bem vindo, <b><%=usuario.getNome()%></b>!
			<br/>
			Escolha uma das opções ao lado.
			<br/>
		</center>
		<br/><br/>
		<!-- <img height="20%" width="20%" src="img/ic_launcher-web.png"/> -->
	</div>
</body>
</html>