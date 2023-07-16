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
		<h1>Pogled 5.5.2. - Jedan Aerodrom</h1>
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
			var icao = document.getElementById("icao").value.trim();
			var noviUrl = url + icao;
			window.location.href = noviUrl;
			event.preventDefault();
		}
		// običan javascript korišten s DOM elementima kao što smo na WebDiP radili
	</script>
	<form method="GET"
		action="${pageContext.request.contextPath}/mvc/aerodromi/"
		onsubmit="updateajUrl(this)">
		<label>ICAO:</label> <input type="text" id="icao" name="icao">
		<button type="submit">Pronađi aerodrom</button>
	</form>
	<br>
	<br> Icao:${aerodrom.icao}
	<br> Naziv: ${aerodrom.naziv}
	<br> Država: ${aerodrom.drzava}
	<br> Lokacija:${aerodrom.lokacija.latitude} , ${aerodrom.lokacija.longitude}
	<br>
	<br>
	<table border=1>
		<tr>
			<td>Oblaci:</td>
			<td>${meteo.cloudsName}</td>
		</tr>
		<tr>
			<td>Vrijednost oblaka:</td>
			<td>${meteo.cloudsValue}</td>
		</tr>
		<tr>
			<td>Vlažnost:</td>
			<td>${meteo.humidityValue}${meteo.humidityUnit}</td>
		</tr>
		<tr>
			<td>Zadnje ažuriranje:</td>
			<td>${meteo.lastUpdate}</td>
		</tr>
		<tr>
			<td>Pritisak:</td>
			<td>${meteo.pressureValue}${meteo.pressureUnit}</td>
		</tr>
		<tr>
			<td>Zora:</td>
			<td>${meteo.sunRise}</td>
		</tr>
		<tr>
			<td>Zalazak:</td>
			<td>${meteo.sunSet}</td>
		</tr>
		<tr>
			<td>Maksimalna temperatura:</td>
			<td>${meteo.temperatureMax}${meteo.temperatureUnit}</td>
		</tr>
		<tr>
			<td>Minimalna temperatura:</td>
			<td>${meteo.temperatureMin}${meteo.temperatureUnit}</td>
		</tr>
		<tr>
			<td>Trenutna temperatura:</td>
			<td>${meteo.temperatureValue}${meteo.temperatureUnit}</td>
		</tr>
		<tr>
			<td>Ikona vremena:</td>
			<td>${meteo.weatherIcon}</td>
		</tr>
		<tr>
			<td>Broj vremena:</td>
			<td>${meteo.weatherNumber}</td>
		</tr>
		<tr>
			<td>Vrijeme:</td>
			<td>${meteo.weatherValue}</td>
		</tr>
	</table>
</body>
</html>