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
		<h1>Pogled 5.6 - Letovi početna</h1>
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
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/letovi/interval">
		Pregled 5.6.1 - Pregled spremljenih letova s određenog aerodroma u zadanom intervalu, uz straničenje.</a>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/letovi/dan">
		Pregled 5.6.2 - Pregled spremljenih letova s određenog aerodroma na zadani datum, uz straničenje.</a>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/letovi/OpenSky">
		Pregled 5.6.3 - Pregled letova s određenog aerodroma na zadani datum. Unose se icao, datum.</a>
	<br>
	<br>

</body>
</html>