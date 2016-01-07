<%@page import="br.com.doce.desafio.visual.FactoryMessage"%>
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
</head>
<body>
	<%
		Usuario usuario = (Usuario) session.getAttribute("loggedInUser");
		if (usuario==null || !usuario.getNivelAcesso().equals("1")) {
	%>
	<jsp:forward page="login.jsp" />
	<%
		}
	%>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />
	

	<div class="boxConteudoDoceDesafio">
		<div class="tituloBoxConteudo">Editar Usuário </div>
		
		<%
		int id = Integer.valueOf(request.getParameter("id"));
		
		LoginDAO loginDAO = new LoginDAO();
		
		Usuario usuarioCadastro = loginDAO.finById(id);
		
		String acao = request.getParameter("acao");
			
		if(acao!=null && acao.equals("salvar")){
			
			usuarioCadastro.setNome(request.getParameter("nome"));
			usuarioCadastro.setLogin(request.getParameter("login"));
			usuarioCadastro.setEmail(request.getParameter("email"));
			usuarioCadastro.setSenha(request.getParameter("senha"));
			usuarioCadastro.setNivelAcesso(request.getParameter("nivelAcesso"));
			
			if(usuarioCadastro.getNome()==null || usuarioCadastro.getNome().equals("") ||
					usuarioCadastro.getLogin()==null || usuarioCadastro.getLogin().equals("") ||
					usuarioCadastro.getEmail()==null || usuarioCadastro.getEmail().equals("") ||
					usuarioCadastro.getSenha()==null || usuarioCadastro.getSenha().equals("") ){
				FactoryMessage.createErrorMessage(response, "Por favor, preencha todos os campos");
			}else{
				Usuario usrTmp = loginDAO.findUsuarioByLogin(usuarioCadastro.getLogin());
				
				if(usrTmp!=null && usrTmp.getId()!=usuarioCadastro.getId()){
					FactoryMessage.createErrorMessage(response, "Este <b>login</b> já existe, por favor escolha outro.");
				}else{
					usrTmp = loginDAO.findUsuarioByEmail(usuarioCadastro.getEmail());
					if(usrTmp !=null && usrTmp.getId()!=usuarioCadastro.getId()){
						FactoryMessage.createErrorMessage(response, "Este <b>email</b> já existe, por favor escolha outro.");
					}else{
						loginDAO.update(usuarioCadastro);
						FactoryMessage.createErrorMessage(response, "Usuário alterado com sucesso! <a href='usuarios.jsp'>Ver lista </a>");
					}
				}
			}
		}
	%>
		<form id="form" method="post">
			<input type="hidden" name="acao" value="salvar"/>
			<input type="hidden" name="id" value="<%=usuarioCadastro.getId()%>"/>
			<table align="center">
				<tr>
					<td>Nome usuário (*):</td>
					<td><input type="text" name="nome" value="<%=usuarioCadastro.getNome()%>" size="50" maxlength="50" /></td>
				</tr>
				<tr>
					<td>Login (*):</td>
					<td><input type="text" name="login" value="<%=usuarioCadastro.getLogin()%>"  size="20" maxlength="20" /></td>
				</tr>
				<tr>
					<td>Senha (*):</td>
					<td><input type="password" name="senha" value="<%=usuarioCadastro.getSenha()%>"  size="20" maxlength="20" /></td>
				</tr>
				<tr>
					<td>Email (*):</td>
					<td><input type="text" id="email" name="email" value="<%=usuarioCadastro.getEmail()%>"  size="50" maxlength="50" /></td>
				</tr>
				<tr>
					<td>Nível Acesso:</td>
					<td>
						<select name="nivelAcesso">
							<option value="1" <%=usuarioCadastro.getNivelAcesso().equals("1")?"selected='selected'":"" %>>Administrador</option>
							<option value="2" <%=usuarioCadastro.getNivelAcesso().equals("2")?"selected='selected'":"" %>>Usuário</option>
						</select>
					</td>
				</tr>
			</table>
			<br/>
			<input type="submit" value="Alterar" />
		</form> 
	</div>
</body>
</html>