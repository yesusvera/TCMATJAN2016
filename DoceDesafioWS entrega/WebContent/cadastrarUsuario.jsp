<%@page import="br.com.doce.desafio.visual.framework.Display"%>
<%@page import="br.com.doce.desafio.visual.framework.Integration"%>
<%@page import="br.com.docedesafio.model.Perfil"%>
<%@page import="br.com.docedesafio.dao.PerfilDAO"%>
<%@page import="java.sql.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Date"%>
<%@page import="br.com.docedesafio.dao.AlimentoInitDAO"%>
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
		if (usuario == null || !usuario.getNivelAcesso().equals("1")) {
	%>
	<jsp:forward page="login.jsp" />
	<%
		}
	%>
	<jsp:include page="./include/cabecalho.jsp" />
	<jsp:include page="./include/menu.jsp" />

	<div class="boxConteudoDoceDesafio">
		<%
			LoginDAO loginDAO = new LoginDAO();	
			Usuario usuarioCadastro = new Usuario();
			if(Integration.existParam(request, "idUsuario")){
				usuarioCadastro.setId(Integer.valueOf(request.getParameter("idUsuario")));
				usuarioCadastro = loginDAO.finById(usuarioCadastro.getId());
				loginDAO.fillPerfil(usuarioCadastro);
			}
		%>
			<div class="tituloBoxConteudo"><%=usuarioCadastro.getId()!=null?"EDITAR":"CADASTRAR" %> USUÁRIO</div>
		<%
			
			String acao = request.getParameter("acao");

			if (acao != null && acao.equals("salvar")) {
				
				usuarioCadastro.setNome(request.getParameter("nome"));
				usuarioCadastro.setLogin(request.getParameter("login"));
				usuarioCadastro.setEmail(request.getParameter("email"));
				usuarioCadastro.setSenha(request.getParameter("senha"));
				usuarioCadastro.setNivelAcesso(request.getParameter("nivelAcesso"));
				
				if (usuarioCadastro.getNome() == null || usuarioCadastro.getNome().equals("")
						|| usuarioCadastro.getLogin() == null || usuarioCadastro.getLogin().equals("")
						|| usuarioCadastro.getEmail() == null || usuarioCadastro.getEmail().equals("")
						|| usuarioCadastro.getSenha() == null || usuarioCadastro.getSenha().equals("")) {
					FactoryMessage.createErrorMessage(response, "Por favor, preencha todos os campos");
				} else {
					Usuario usrTmp = loginDAO.findUsuarioByLogin(usuarioCadastro.getLogin());
					
					if(usrTmp!=null && usrTmp.getId()!=usuarioCadastro.getId()){
						FactoryMessage.createErrorMessage(response, "Este <b>login</b> já existe, por favor escolha outro.");
					}else{
						usrTmp = loginDAO.findUsuarioByEmail(usuarioCadastro.getEmail());
						if(usrTmp !=null && usrTmp.getId()!=usuarioCadastro.getId()){
							FactoryMessage.createErrorMessage(response, "Este <b>email</b> já existe, por favor escolha outro.");
							FactoryMessage.createErrorMessage(response,
									"Este <b>email</b> já existe, por favor escolha outro.");
						} else {
							if (Integration.existParam(request,"dataNascimento")) {
								long timeDate = new SimpleDateFormat("dd/MM/yyyy")
										.parse(request.getParameter("dataNascimento")).getTime();
								usuarioCadastro.getPerfil().setDataNascimento(new Date(timeDate));
							}
							if (Integration.existParam(request,"peso")) {
								usuarioCadastro.getPerfil().setPeso(Integer.valueOf(request.getParameter("peso")));
							}
							if (Integration.existParam(request,"altura")) {
								usuarioCadastro.getPerfil().setAltura(Integer.valueOf(request.getParameter("altura")));
							}
							if (Integration.existParam(request,"observacao")) {
								usuarioCadastro.getPerfil().setObservacao(request.getParameter("observacao"));
							}	
							if (Integration.existParam(request,"sexo")) {
								usuarioCadastro.getPerfil().setSexo(Byte.valueOf(request.getParameter("sexo")));
							}
							if (Integration.existParam(request,"fatorGlicemia")) {
								usuarioCadastro.getPerfil()
										.setFatorGlicemia(Integer.valueOf(request.getParameter("fatorGlicemia")));
							}
							if (Integration.existParam(request,"fatorCarboidrato")) {
								usuarioCadastro.getPerfil()
										.setFatorCarboidrato(Integer.valueOf(request.getParameter("fatorCarboidrato")));
							}

							Perfil perfil = usuarioCadastro.getPerfil();
							
							boolean primeiroCadastro = (usuarioCadastro.getId()==null);
							
							usuarioCadastro = loginDAO.save(usuarioCadastro);
							
							PerfilDAO perfilDAO = new PerfilDAO();
							perfil.setIdLogin(usuarioCadastro.getId());
							perfilDAO.save(perfil);
							
							if(primeiroCadastro){
								AlimentoInitDAO alimentoInitDAO = new AlimentoInitDAO();
								alimentoInitDAO.saveAlimentoFromInit(usuarioCadastro.getId());
							}
							
							FactoryMessage.createErrorMessage(response,
									"Usuário salvo com sucesso! <a href='usuarios.jsp'>Ver lista </a>");
						}
					}
				}
			}
		%>
		<form id="form" method="post">
			<input type="hidden" name="acao" value="salvar" />
			<input type="hidden" name="idUsuario" value="<%=usuarioCadastro.getId()!=null?usuarioCadastro.getId():""%>"/>
			<table align="center">
				<tr>
					<td>Nome usuário (*):</td>
					<td><input type="text" name="nome"
						value="<%=usuarioCadastro.getNome()%>" size="50" maxlength="50" /></td>
				</tr>
				<tr>
					<td>Peso:</td>
					<td><input type="text" name="peso"
						value="<%=usuarioCadastro.getPerfil().getPeso()!=null?usuarioCadastro.getPerfil().getPeso():""%>" class="sonums"
						size="3" maxlength="3" /> kg</td>
				</tr>
				<tr>
					<td>Altura:</td>
					<td><input type="text" name="altura"
						value="<%=usuarioCadastro.getPerfil().getAltura()!=null?usuarioCadastro.getPerfil().getAltura():""%>"
						class="sonums" size="3" maxlength="3" /> cm</td>
				</tr>
				<tr>
					<td>Sensibilidade Glicemia:</td>
					<td><input type="text" name="fatorGlicemia"
						value="<%=usuarioCadastro.getPerfil().getFatorGlicemia()!=null?usuarioCadastro.getPerfil().getFatorGlicemia():""%>"
						class="sonums" size="3" maxlength="3" /></td>
				</tr>
				<tr>
					<td>Sensibilidade Carboidrato:</td>
					<td><input type="text" name="fatorCarboidrato"
						value="<%=usuarioCadastro.getPerfil().getFatorCarboidrato()!=null?usuarioCadastro.getPerfil().getFatorCarboidrato():""%>"
						class="sonums" size="3" maxlength="3" /><td>
				</tr>
				<tr>
					<td>Data Nascimento:</td>
					<td><input type="text" name="dataNascimento" id="dataNascimento"
						value="<%=Display.showDateFormat(usuarioCadastro.getPerfil().getDataNascimento())%>"
						size="10" maxlength="10" /></td>
				</tr>
				<tr>
					<td>Observação:</td>
					<td><textarea rows="10" cols="20" name="observacao"><%=usuarioCadastro.getPerfil().getObservacao()%></textarea></td>
				</tr>
				<tr>
					<td>Sexo:</td>
					<td><select name="sexo">
							<option value="1"
								<%=usuarioCadastro.getPerfil().getSexo() == 1 ? "selected='selected'" : ""%>>Masculino</option>
							<option value="2"
								<%=usuarioCadastro.getPerfil().getSexo() == 2 ? "selected='selected'" : ""%>>Feminino</option>
					</select></td>
				</tr>
				<tr>
					<td>Login (*):</td>
					<td><input type="text" name="login"
						value="<%=usuarioCadastro.getLogin()%>" size="20" maxlength="20" /></td>
				</tr>
				<tr>
					<td>Senha (*):</td>
					<td><input type="password" name="senha"
						value="<%=usuarioCadastro.getSenha()%>" size="20" maxlength="20" /></td>
				</tr>
				<tr>
					<td>Email (*):</td>
					<td><input type="text" id="email" name="email"
						value="<%=usuarioCadastro.getEmail()%>" size="50" maxlength="50" /></td>
				</tr>
				<tr>
					<td>Nível Acesso:</td>
					<td><select name="nivelAcesso">
							<option value="1"
								<%=usuarioCadastro.getNivelAcesso().equals("1") ? "selected='selected'" : ""%>>Administrador</option>
							<option value="2"
								<%=usuarioCadastro.getNivelAcesso().equals("2") ? "selected='selected'" : ""%>>Usuário</option>
					</select></td>
				</tr>
			</table>
			<br /> <input type="submit" value="Salvar" />
			<script>
				$(function() {
					$("#dataNascimento").datepicker();
				});
			</script>
		</form>
	</div>
</body>
</html>