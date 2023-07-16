<%@page import="org.foi.nwtis.ilucic.aplikacija_4.ws.WsLetovi.endpoint.LetAviona"%>
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
		<h1>Pogled 5.6.2. - Pregled letova po danu i icao</h1>
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
	<a href="${pageContext.servletContext.contextPath}/mvc/letovi">Početna
		stranica za letove</a>
	<br>
	<br>
	<form method="GET"
		action="${pageContext.request.contextPath}/mvc/letovi/dan">
		<label>Icao:</label> <input type="text" id="icao" name="icao"><br>
		<label>Vrijeme dan (format dd.MM.yyyy.):</label> <input type="text" id="vrijeme" name="vrijeme"><br>
		<button type="submit">Pronađi Letove</button>
	</form>
	<br>
	<br>
	<%
	int odBroja =
	    request.getParameter("odBroja") != null ? Integer.parseInt(request.getParameter("odBroja")) : 1;
	int broj =
	    request.getParameter("broj") != null ? Integer.parseInt(request.getParameter("broj")) : 20;
	String icao =
	    request.getParameter("icao") != null ? request.getParameter("icao") : null;
	String vrijeme =
	    request.getParameter("vrijeme") != null ? request.getParameter("vrijeme") : null;
	%>
	<table border=1>
		<tr>
			<th>Callsign</th>
			<th>Icao polaska</th>
			<th>Polazak kandidati broj</th>
			<th>Polazak horizotalna dužina</th>
			<th>Polazak vertikalna dužina</th>
			<th>Icao dolaska</th>
			<th>Dolazak kandidati broj</th>
			<th>Dolazak horizotalna dužina</th>
			<th>Dolazak vertikalna dužina</th>
			<th>Prvi put viđen</th>
			<th>Icao24</th>
			<th>Zadnji put viđen</th>
		</tr>
		<%
		List<LetAviona> listaLetova =
		    (List<LetAviona>) request.getAttribute("letovi");
		if (listaLetova != null) {
		  for (LetAviona l : listaLetova) {
		%>
		<tr>
			<td><%=l.getCallsign()%></td>
			<td><%=l.getEstDepartureAirport()%></td>
			<td><%=l.getDepartureAirportCandidatesCount()%></td>
			<td><%=l.getEstDepartureAirportHorizDistance()%></td>
			<td><%=l.getEstDepartureAirportVertDistance()%></td>
			<td><%=l.getEstArrivalAirport()%></td>
			<td><%=l.getArrivalAirportCandidatesCount()%></td>
			<td><%=l.getEstArrivalAirportHorizDistance()%></td>
			<td><%=l.getEstArrivalAirportVertDistance()%></td>
			<td><%=l.getFirstSeen()%></td>
			<td><%=l.getIcao24()%></td>
			<td><%=l.getLastSeen()%></td>
		</tr>
		<%
		}
		}
		%>
	</table>

	<%
	if (odBroja > 0) {
	%>
  	<a
  		href="?odBroja=<%=Math.max(odBroja - broj, 1)%>&broj=<%=broj%>&icao=<%=icao%>&vrijeme=<%=vrijeme%>">Prošla
  		stranica</a>
  	<%
	}

	if(listaLetova!= null){
	if (broj < listaLetova.size() + 1) {
	%>
	<a href="?odBroja=<%=odBroja + broj%>&broj=<%=broj%>&icao=<%=icao%>&vrijeme=<%=vrijeme%>">Sljedeća
		stranica</a>
	<%
	}
	}
	%>

</body>
</html>