<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled 5.3</title>
<style>
		header {
			background-color: lightblue;
			color: black;
			padding: 10px;
		}
        .centriraj {
        	display: flex;
        	flex-direction: column;
        	justify-content: center;
        	align-items: center;
            text-align: center;
            margin-top: 50px;
        }

        .odgovor {
            width: 300px;
            height: 30px;
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 5px;
            font-size: 16px;
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
		<h1>Pogled 5.3. - Komunikacija s aplikacijom 1</h1>
	</header>
	<br>
	<br>
	<a href="${pageContext.servletContext.contextPath}">Početna
		stranica</a>
	<br>
	<br>
	<div class="centriraj">
        <p class="odgovor">${odgovor}</p>
        <br>
        <a href="${pageContext.request.contextPath}/mvc/pregled53/nadzor" class="gumbi">STATUS</a>
        <a href="${pageContext.request.contextPath}/mvc/pregled53/nadzor/KRAJ" class="gumbi">KRAJ</a>
        <a href="${pageContext.request.contextPath}/mvc/pregled53/nadzor/INIT" class="gumbi">INIT</a>
        <a href="${pageContext.request.contextPath}/mvc/pregled53/nadzor/PAUZA" class="gumbi">PAUZA</a>
        <a href="${pageContext.request.contextPath}/mvc/pregled53/nadzor/INFO/DA" class="gumbi">INFO DA</a>
        <a href="${pageContext.request.contextPath}/mvc/pregled53/nadzor/INFO/NE" class="gumbi">INFO NE</a>
    </div>
    <br>
    <br>
</body>
</html>