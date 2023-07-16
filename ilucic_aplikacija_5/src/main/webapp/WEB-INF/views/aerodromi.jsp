<%@page import="org.foi.nwtis.podaci.Aerodrom"%>
<%@ page import="org.foi.nwtis.Konfiguracija" %>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi</title>
<style>
header {
	background-color: lightblue;
	color: black;
	padding: 10px;
}
</style>
</head>
<body>
<script>
    function dodajZaPreuzimanje(icao) {
    	var httpzahtjev = new XMLHttpRequest();
    	httpzahtjev.open('GET', '<%= request.getContextPath() %>/mvc/aerodromi/' + icao + '/preuzimanje');
        var odgovor = document.getElementById('odgovor');
        httpzahtjev.onload = function() {
            if (httpzahtjev.status === 200) {
                odgovor.textContent = 'Aerodrom uspješno dodan.';
            } else if (httpzahtjev.status === 401) {
            	odgovor.textContent = 'Niste ulogirani!';
            } else {
            	odgovor.textContent = 'Problem s dodavanjem aerodroma: ' + xhr.status;
            }
        };
        httpzahtjev.onerror = function() {
        	odgovor.textContent = 'Greška sa zahtjevom';
        };
        httpzahtjev.send();
    }
  </script>
<%
  	ServletContext context = request.getServletContext();
	Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
	
%>
	<header>
		<h1>Pogled 5.5.1. - Aerodromi</h1>
	</header>
	<br>
	<br>
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<script>
		function updateajUrl(form) {
			var url = form.getAttribute("action");
			var naziv = document.getElementById("naziv").value.trim();
			var drzava = document.getElementById("drzava").value.trim();
			vae noviUrl = url;
			
			var noviUrl = noviUrl + "?odBroja=1&broj=20&" "naziv=" + encodeURIComponent(naziv)
					+ "&drzava=" + encodeURIComponent(km);

			window.location.href = noviUrl;
			event.preventDefault();
		}
	</script>
	<form method="GET"
		action="${pageContext.request.contextPath}/mvc/aerodromi/svi"
		onsubmit="updateajUrl(this)">
		<label>Traži naziv:</label> <input type="text" id="naziv" name="naziv"><br>
		<label>Traži državu:</label> <input type="text" id="drzava" name="drzava"><br>
		<button type="submit">Pronađi udaljenosti</button>
	</form>
	<br>
	<br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Početna
		stranica za aerodrome</a>
	<br>
	<br>
		<div id="odgovor"></div>
	<br>
	<br>
	<%
	int odBroja =
	    request.getParameter("odBroja") != null ? Integer.parseInt(request.getParameter("odBroja")) : 1;
	int broj =
	    request.getParameter("broj") != null ? Integer.parseInt(request.getParameter("broj")) : 20;
	String naziv =
	    request.getParameter("naziv") != null ? request.getParameter("naziv") : null;
	String drzava =
	    request.getParameter("drzava") != null ? request.getParameter("drzava") : null;
	%>
	<table border=1>
		<tr>
			<th>Icao</th>
			<th>Naziv</th>
			<th>Drzava</th>
			<th>Koordinate</th>
			<th>Pregled</th>
			<th>Dodaj za preuzimanje</th>
		</tr>
		<%
		List<Aerodrom> sviAerodromi = (List<Aerodrom>) request.getAttribute("aerodromi");

		if(sviAerodromi!= null){
		for (Aerodrom a : sviAerodromi) {
		%>
		<tr>
			<td><%=a.getIcao()%></td>
			<td><%=a.getNaziv()%></td>
			<td><%=a.getDrzava()%></td>
			<td><%=a.getLokacija().getLatitude() + ", " + a.getLokacija().getLongitude()%></td>
			<td><a href="<%=a.getIcao()%>"> Pregled </a></td>
			<td><a href="#" onclick="dodajZaPreuzimanje('<%= a.getIcao() %>'); return false;">Dodaj za preuzimanje</a></td>
		</tr>
		<%
		}
		}
		%>
	</table>

	<%
	if (odBroja > 0) {
	  if(naziv == null && drzava == null){
	%>
	<a
		href="?odBroja=<%=Math.max(odBroja - broj, 1)%>&broj=<%=broj%>">Prošla
		stranica</a>
	<%
  		}else if(naziv != null && drzava != null){
  		%>
  		<a
  			href="?odBroja=<%=Math.max(odBroja - broj, 1)%>&broj=<%=broj%>&naziv=<%=naziv%>&drzava=<%=drzava%>">Prošla
  			stranica</a>
  		<%
  		}else if(naziv != null && drzava == null){
  		%>
  		<a
  			href="?odBroja=<%=Math.max(odBroja - broj, 1)%>&broj=<%=broj%>&naziv=<%=naziv%>">Prošla
  			stranica</a>
  		<%
  		}else if(naziv == null && drzava != null){
  		%>
  		<a
  			href="?odBroja=<%=Math.max(odBroja - broj, 1)%>&broj=<%=broj%>&drzava=<%=drzava%>">Prošla
  			stranica</a>
  		<%
  		}
	}

	if(sviAerodromi!= null){
	if (broj < sviAerodromi.size() + 1) {
	  if(naziv == null && drzava == null){
	%>
	<a href="?odBroja=<%=odBroja + broj%>&broj=<%=broj%>">Sljedeća
		stranica</a>
	<%
  		}else if(naziv != null && drzava != null){
  		%>
		<a href="?odBroja=<%=odBroja + broj%>&broj=<%=broj%>&naziv=<%=naziv%>&drzava=<%=drzava%>">Sljedeća
			stranica</a>
		<%
  		}else if(naziv != null && drzava == null){
  		%>
		<a href="?odBroja=<%=odBroja + broj%>&broj=<%=broj%>&naziv=<%=naziv%>">Sljedeća
			stranica</a>
		<%
  		}else if(naziv == null && drzava != null){
  		%>
		<a href="?odBroja=<%=odBroja + broj%>&broj=<%=broj%>&drzava=<%=drzava%>">Sljedeća
			stranica</a>
		<%
  		}
	}
	}
	%>
</body>
</html>