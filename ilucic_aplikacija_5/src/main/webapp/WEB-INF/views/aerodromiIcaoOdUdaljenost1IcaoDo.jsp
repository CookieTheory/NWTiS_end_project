<%@page import="org.foi.nwtis.podaci.UdaljenostAerodromDrzava"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom</title>
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
		<h1>Pogled 5.5.6. - Aerodrom udaljenost 1</h1>
	</header>
	<br>
	<br>
	<h1>Aerodrom udaljenosti</h1>
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Početna
		stranica za aerodrome</a>
	<br>
	<br>
	<script>
		function updateajUrl(form) {
			var url = form.getAttribute("action");
			var icaoOd = document.getElementById("icaoOd").value.trim();
			var icaoDo = document.getElementById("icaoDo").value.trim();
			var noviUrl = url + icaoOd + "/udaljenost1/" + icaoDo;
			window.location.href = noviUrl;
			event.preventDefault();
		}
	</script>
	<form method="GET"
		action="${pageContext.request.contextPath}/mvc/aerodromi/"
		onsubmit="updateajUrl(this)">
		<label>Prvi ICAO:</label> <input type="text" id="icaoOd" name="icaoOd"><br>
		<label>Drugi ICAO:</label> <input type="text" id="icaoDo"
			name="icaoDo"><br>
		<button type="submit">Pronađi aerodrom</button>
	</form>
	<br>
	<br>
	<table border=1>
		<tr>
			<th>ICAO</th>
			<th>Država</th>
			<th>Udaljenost</th>
		</tr>
		<%
		List<UdaljenostAerodromDrzava> sveUdaljenosti = (List<UdaljenostAerodromDrzava>) request.getAttribute("udaljenosti");
		if(sveUdaljenosti != null){
		for (UdaljenostAerodromDrzava u : sveUdaljenosti) {
		%>
		<tr>
			<td><%=u.icao()%></td>
			<td><%=u.drzava()%></td>
			<td><%=u.km()%></td>
		</tr>
		<%
		}
		}
		%>
	</table>

</body>
</html>