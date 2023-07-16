<%@page import="org.foi.nwtis.ilucic.aplikacija_4.ws.WsKorisnici.endpoint.Korisnici"%>
<%@page import="org.foi.nwtis.podaci.UdaljenostAerodromDrzava"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled korisnika</title>
<style>
header {
	background-color: lightblue;
	color: black;
	padding: 10px;
}
</style>
</head>
<body>

	<header>
		<h1>Pogled 5.2.3. - Pregled korisnika</h1>
	</header>
	<%
	String greska = (String) request.getAttribute("greska");
	if (greska != null) {
	%>
	<h2>${greska}</h2>
	<%
}
%>
	<br>
	<br>
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici">Početna
		stranica za korisnike</a>
	<br>
	<br>
	<form method="POST"
		action="${pageContext.request.contextPath}/mvc/korisnici/pregledKorisnika">
		<label>Ime:</label> <input type="text" id="traziImeKorisnika" name="traziImeKorisnika"><br>
		<label>Prezime:</label> <input type="text" id="traziPrezimeKorisnika" name="traziPrezimeKorisnika"><br>
		<button type="submit">Pronađi korisnike</button>
	</form>
	<br>
	<br>
	<table border=1>
		<tr>
			<th>Ime</th>
			<th>Prezime</th>
			<th>Korisničko ime</th>
			<th>Lozinka</th>
		</tr>
		<%
		List<Korisnici> listaKorisnika =
		    (List<Korisnici>) request.getAttribute("korisnici");
		if (listaKorisnika != null) {
		  for (Korisnici k : listaKorisnika) {
		%>
		<tr>
			<td><%=k.getIme()%></td>
			<td><%=k.getPrezime()%></td>
			<td><%=k.getKorisnickoIme()%></td>
			<td><%=k.getSifra()%></td>
		</tr>
		<%
		}
		}
		%>
	</table>

</body>
</html>