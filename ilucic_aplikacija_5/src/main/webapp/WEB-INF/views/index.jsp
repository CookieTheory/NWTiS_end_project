<%@ page import="jakarta.servlet.ServletContext"%>
<%@ page import="org.foi.nwtis.Konfiguracija"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pogled 5.1 - Početna</title>
<style>
header {
	background-color: lightblue;
	color: black;
	padding: 10px;
}

a.gumbi {
	display: inline-block;
	background-color: blue;
	color: white;
	text-align: center;
	padding: 10px 20px;
	text-decoration: none;
	border-radius: 4px;
}

a.gumbi:hover {
	background-color: black;
}
</style>
</head>
<body>

	<header>
		<h1>Pogled 5.1 - početni izbornik</h1>
	</header>
	<%
	ServletContext context = request.getServletContext();
	Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
	%>
	<br>
	<br>
	<p>
		Ime autora:
		<%=konfig.dajPostavku("autor.ime")%><br> Prezime autora:
		<%=konfig.dajPostavku("autor.prezime")%><br> Predmet:
		<%=konfig.dajPostavku("autor.predmet")%><br> Godina:
		<%=konfig.dajPostavku("aplikacija.godina")%><br> Verzija:
		<%=konfig.dajPostavku("aplikacija.verzija")%><br>
	</p>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/korisnici">5.2 - Početni izbornik za korisnike</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/pregled53">5.3 - Aplikacija 1 terminal</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi">5.5 - Početni izbornik za aerodrome</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/letovi">5.6 - Početni izbornik za letove</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik">5.7 - Pregled dnevnika</a>
	<br>
	<br>
</body>
</html>