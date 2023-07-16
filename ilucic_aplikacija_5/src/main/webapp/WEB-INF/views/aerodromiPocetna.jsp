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
		<h1>Pogled 5.5 - Aerodromi početna</h1>
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
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi">
		Pregled 5.5.1 - pregled svih aerodroma uz filtriranje po nazivu aerodroma i državi, uz straničenje.</a>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icao">
		Pregled 5.5.2 - Pregled podataka izabranog aerodroma i njegovih meteo podataka.</a>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icaoOd/icaoDo">
		Pregled 5.5.4 - Udaljenost izmedu dva aerodroma unutar države i ukupna udaljenost.</a>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icaoOd/izracunaj/icaoDo">
		Pregled 5.5.5 - Udaljenost izmedu dva aerodroma koristeći aplikaciju 1.</a>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icaoOd/udaljenost1/icaoDo">
		Pregled 5.5.6 - Udaljenost izmedu početnog aerodroma i aerodroma unutar države odredišnog s manjom udaljenosti između početnog i odredišnog.</a>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icaoOd/udaljenost2">
		Pregled 5.5.7 - Pregled aerodroma i udaljenosti do polaznog aerodroma unutar zadane države koje su manje od zadane udaljenosti.</a>
	<br>
	<br>

	<h1></h1>

</body>
</html>