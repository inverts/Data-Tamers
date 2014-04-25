<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- HOME MASTER -->
<!-- CSS -->
<link href="<c:url value="/cache/css/libs/bootstrap.css" />" rel="stylesheet">
<link href="<c:url value="/cache/css/about.css" />" rel="stylesheet">

<link rel="shortcut icon" href="<c:url value="/cache/images/icon.ico" />" />

<script src="<c:url value="/cache/javascript/libs/jquery-1.10.2.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/jquery-ui-1.10.3.custom.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/bootstrap.min.js" />"></script>


<title>${TITLE}</title>
</head>

<body>

	<div class="page">
	
		<header id="header">
			<div class="header-home">
				<div class="entry-wrapper">
					<div class="logo">
						<a href="/appliedanalytics/"><img src="<c:url value="/cache/images/logo-280.png" />" /></a>
					</div>
					<div class="right">
								<span class="nav-item"><a href="#"><fmt:message key="header.about" /></a></span>
								
								<div class="nav-item dropdown">
								  <a id="loglabel" role="button" data-toggle="dropdown">
								  <c:choose>
								  	<c:when test="${not empty model.loggedin }">
								  		${model.name}
								  	</c:when>
								  	<c:otherwise>
								    	<fmt:message key="header.guest" />
								    </c:otherwise>
								  </c:choose>
								  	<span class="caret"></span>
								  </a>
								  <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
								  	<!-- both -->
								  	<li><a href="<c:url value="/accounts/getstarted" />">New Account</a></li>
								  	<li><a href="http://www.google.com/analytics/" target="_blank">Google Analytics</a></li>
								  	<c:choose>
								  		<c:when test="${not empty model.loggedin}">
								  			<li><a href="application">Dashboard</a></li>
								  			<li class="divider"></li>
											<li><a href="<c:url value="signout" />"><fmt:message key="header.logout" /></a></li>
								    	</c:when>
								    	<c:otherwise>
										    <li class="divider"></li>
											<li><a href="<c:url value="login" />"><fmt:message key="header.login" /></a></li>
								    	</c:otherwise>
								    </c:choose>
								  </ul>
								</div>
					</div>
				</div>
			</div>
		</header>
		<div class="top-section">	
			<div class="wrapper">
				<canvas id="line-canvas" width="1300px" height="1227px"></canvas>
				<span class="catch" id="catch-top">What's hiding in <span class="emp-orange">your</span> data?</span>
				<span class="linetext" id="linetext-1">Seasonal trends?</span>	
				<span class="linetext" id="linetext-2">Bad keywords?</span>	
				<span class="linetext" id="linetext-3">Declining referral traffic?</span>	
				
				<div class="info-box" id="top-info-box">
					<div class="info-box-content">
						<span class="lead-word">Sometimes</span> you need to look a little closer to see important trends in your website visits. The success or failure of one referral site, or one keyword, can easily be obscured by all the others.<br />
You may have no idea that last month, the referring website that drives 10% of your traffic has begun to slowly deliver less visits to your website. Applied Analytics will detect these kinds of changes for you, so you can extinguish problems before they grow.
					</div>
					<a href="<c:url value="/accounts/getstarted" />" class="btn-container"><span class="blue-btn"><span class="btn-label">What's in my data?</span></span></a>
				</div>
			</div>
		</div>

		<footer id="footer">
			<div class="footer-content">
				<div class="footer-copyright">
					<fmt:message key="footer.copyright" />
					<jsp:useBean id="year" class="java.util.Date" /><fmt:formatDate value="${year}" pattern="yyyy" />
					<fmt:message key="footer.copyright.references" />
				</div>
			</div>
		</footer>
		
	</div>
	<script type="text/javascript">
	window.onload = function() {
		op = 0;
		setInterval(function() {
			op = parseFloat($(".top-section").css("opacity"));
			$(".top-section").css("opacity", (op + 0.01));
			if (op >= 1.0)
				clearInterval();
		}, 10);
		$(".top-section").fadeIn();
		var canvas = document.getElementById('line-canvas');
		var c = canvas.getContext("2d");

		var startX = 402;
		var startY = 172;
		var endX = 402;
		var endY = 248;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);
		
		var startX = 402;
		var startY = 298;
		var endX = 402;
		var endY = 325;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);
		
		var startX = 402;
		var startY = 325;
		var endX = 960;
		var endY = 325;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);



		var startX = 960;
		var startY = 325;
		var endX = 960;
		var endY = 355;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);

		var startX = 960;
		var startY = 410;
		var endX = 960;
		var endY = 540;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);

		var startX = 960;
		var startY = 540;
		var endX = 877;
		var endY = 540;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);

		var startX = 877;
		var startY = 540;
		var endX = 877;
		var endY = 650;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);

		var startX = 877;
		var startY = 705;
		var endX = 877;
		var endY = 750;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);
		
		var startX = 877;
		var startY = 750;
		var endX = 550;
		var endY = 750;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);
		
		var startX = 550;
		var startY = 750;
		var endX = 550;
		var endY = 790;
		var amount = 0;
		drawLine(canvas, c, startX, startY, endX, endY, amount);
	};
	
	function drawLine(canvas, c, startX, startY, endX, endY, amount, callback) {
		setInterval(function() {
		    amount += 0.02; // change to alter duration
		    if (amount > 1) amount = 1;
		    c.clearRect(0, 0, canvas.width, canvas.height);
		    c.strokeStyle = "white";
		    c.lineWidth = 2;
		    c.moveTo(startX, startY);
		    // lerp : a  + (b - a) * f
		    c.lineTo(startX + (endX - startX) * amount, startY + (endY - startY) * amount);
		    c.stroke();
		    if (amount >= 1) {
		    	clearInterval();
		    }
		}, 20);
	}
	</script>
</body>
</html>
