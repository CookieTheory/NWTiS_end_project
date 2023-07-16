<%@ page import="jakarta.servlet.ServletContext"%>
<%@ page import="org.foi.nwtis.Konfiguracija"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Početna</title>
<style>
header {
	background-color: lightblue;
	color: black;
	padding: 10px;
}
.gumbi {
	display: inline-block;
	margin: 10px;
	padding: 10px 20px;
	font-size: 16px;
	border: 1px solid #ccc;
	border-radius: 5px;
	background-color: #f0f0f0;
	cursor: pointer;
	text-decoration: none;
	color: inherit;
}
</style>
</head>
<body>

	<header>
		<h1>Pogled 5.2 - Korisnici početna</h1>
	</header>
	<br>
	<br>
	<%
	ServletContext context = request.getServletContext();
	Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
	%>
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
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/korisnici/registracija">
		Pregled 5.2.1 - Registracija korisnika.</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/korisnici/prijava">
		Pregled 5.2.2 - Prijava korisnika.</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/korisnici/pregledKorisnika">
		Pregled 5.2.3 - Pregled korisnika uz filtiranje po imenu i prezimenu.</a>
	<br>
	<br>

</body>
</html>