<%
	HttpSession sessao = request.getSession(false);
	if (sessao == null || sessao.getAttribute("loggedInUser") == null) {
%>
	<jsp:forward page="../login.jsp"/>
<%
	}
%>