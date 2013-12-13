<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html >
<html>
<head>
        <link href="<c:url value="/cache/css/entry.css" />" rel="stylesheet">
</head>
<body>
        <div id="page">
                <div id="header">
                        <div class="wrapper">
                                <img id="logo" src="<c:url value="/cache/images/logo-280.png" />" />
                                <div class="right">
                                        <span class="nav-item"><a href="#">About</a></span>
                                        <span class="nav-item"><a href="<c:url value="/application" />">Login</a></span>
                                </div>
                        </div>
                </div>
                <div id="content">
                        <div id="center-box">
                                <a href="<c:url value="/application" />"><div id="analyze-button"></div></a>
                        </div>
                </div>
        </div>
</body>
</html>

<!-- 
	<link href="<c:url value="/cache/css/entry.css" />" rel="stylesheet">

<div id="page">
	<div class="page-content">
		<div id="center-box">
			<a href="<c:url value="/application" />"><div id="analyze-button"></div></a>
		</div>
	</div>
</div>
 -->


