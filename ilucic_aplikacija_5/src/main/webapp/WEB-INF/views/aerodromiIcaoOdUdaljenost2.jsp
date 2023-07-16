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
		<h1>Pogled 5.5.7. - Aerodrom udaljenost 2</h1>
	</header>
	<br>
	<br>
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
			var drzava = document.getElementById("drzava").value.trim();
			var km = document.getElementById("km").value.trim();
			var noviUrl = url + icaoOd + "/udaljenost2";

			if (!icaoOd) {
				icaoOd = "icaoOd";
			}
			if (!drzava || !km) {
				window.location.href = noviUrl;
				event.preventDefault();
				return false;
			}

			var noviUrl = noviUrl + "?drzava=" + encodeURIComponent(drzava)
					+ "&km=" + encodeURIComponent(km);

			window.location.href = noviUrl;
			event.preventDefault();
		}
	</script>
	<form method="GET"
		action="${pageContext.request.contextPath}/mvc/aerodromi/"
		onsubmit="updateajUrl(this)">
		<label>Prvi ICAO:</label> <input type="text" id="icaoOd" name="icaoOd"><br>
		<label>Država:</label> <input type="text" id="drzava" name="drzava"><br>
		<label>Udaljenost:</label> <input type="number" id="km" name="km"><br>
		<button type="submit">Pronađi udaljenosti</button>
	</form>
	<br>
	<br>
	<%
	String greska = (String) request.getAttribute("greska");
	if (greska != null) {
	%>
	<h2>${greska}</h2>
	<%
}
%>
	<table border=1>
		<tr>
			<th>ICAO</th>
			<th>Država</th>
			<th>Udaljenost</th>
		</tr>
		<%
		List<UdaljenostAerodromDrzava> sveUdaljenosti =
		    (List<UdaljenostAerodromDrzava>) request.getAttribute("udaljenosti");
		if (sveUdaljenosti != null) {
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