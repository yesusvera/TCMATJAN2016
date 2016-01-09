<%@page import="br.com.docedesafio.model.Usuario"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Usuario usuario = (Usuario) session.getAttribute("loggedInUser");

	if(usuario!=null){
%>
<div style="width:100%;font-size:medium;background-color: #87AB8C;color: white;">
	&nbsp;
	Usuário: <%=usuario.getNome()%> | 
	Perfil: <%=usuario.getNivelAcesso() %>
	<span style="float:right;color:white;margin-right: 10px"><a href="./include/sair.jsp" style="color:white">Sair</a></span>
</div>

<% } %>