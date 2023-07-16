<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registracija</title>
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
		<h1>Pogled 5.2.1. - Registracija</h1>
	</header>
	<%
	String greska = (String) request.getAttribute("greska");
	if (greska != null) {
	%>
	<h2>${greska}</h2>
	<%
}
%>
<%
	String uspjeh = (String) request.getAttribute("uspjeh");
	if (uspjeh != null) {
	%>
	<h2>${uspjeh}</h2>
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
		action="${pageContext.request.contextPath}/mvc/korisnici/registracija">
		<label for="ime">Ime:</label> 
		<input type="text" id="ime" name="ime" required><br> 
		<label for="prezime">Prezime:</label> 
		<input type="text" id="prezime" name="prezime" required><br> 
		<label for="korisnickoIme">Korisničko ime:</label> 
		<input type="text" id="korisnickoIme" name="korisnickoIme" required><br> 
		<label for="sifra">Šifra:</label> <input type="password" id="sifra" name="sifra" required><br> 
		<button type="submit">Registracija</button>
	</form>
</body>
</html>