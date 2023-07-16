<%@page import="org.foi.nwtis.podaci.FilterKlasa"%>
<%@ page import="org.foi.nwtis.Konfiguracija"%>
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
	<%
	ServletContext context = request.getServletContext();
	Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
	%>
	<header>
		<h1>Pogled 5.7. - Dnevnik</h1>
	</header>
	<br>
	<br>
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<%
	int odBroja =
	    request.getParameter("odBroja") != null ? Integer.parseInt(request.getParameter("odBroja")) : 1;
	int broj =
	    request.getParameter("broj") != null ? Integer.parseInt(request.getParameter("broj")) : 20;
	String vrsta =
	    request.getParameter("vrsta") != null ? "&vrsta=" + request.getParameter("vrsta") : "";
	%>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?odBroja=<%=odBroja%>&broj=<%=broj%>&vrsta=AP2">Filtriraj po Aplikaciji 2</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?odBroja=<%=odBroja%>&broj=<%=broj%>&vrsta=AP4">Filtriraj po Aplikaciji 4</a>
	<br>
	<br>
	<a class="gumbi"
		href="${pageContext.servletContext.contextPath}/mvc/dnevnik?odBroja=<%=odBroja%>&broj=<%=broj%>&vrsta=AP5">Filtriraj po Aplikaciji 5</a>
	<br>
	<br>
	<table border=1>
		<tr>
			<th>Vrsta</th>
			<th>Aplikacija</th>
			<th>Url zahtjeva</th>
			<th>Vrijeme</th>
		</tr>
		<%
		List<FilterKlasa> sviZapisi = (List<FilterKlasa>) request.getAttribute("zapisi");

		for (FilterKlasa f : sviZapisi) {
		%>
		<tr>
			<td><%=f.getVrsta()%></td>
			<td><%=f.getAplikacija()%></td>
			<td><%=f.getRequestUrl()%></td>
			<td><%=f.getVrijeme()%></td>
		</tr>
		<%
		}
		%>
	</table>

	<%
	if (odBroja > 0) {
	%>
	<a
		href="?odBroja=<%=Math.max(odBroja - broj, 1)%>&broj=<%=broj%><%=vrsta%>">Prošla
		stranica</a>
	<%
	}

	if (broj < sviZapisi.size() + 1) {
	%>
	<a href="?odBroja=<%=odBroja + broj%>&broj=<%=broj%><%=vrsta%>">Sljedeća
		stranica</a>
	<%
	}
	%>
</body>
</html>