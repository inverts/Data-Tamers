<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<script src="<c:url value="cache/javascript/sidepanel.js" />"></script>

<div class="sidepanel-background" data-animate="${SIDEPANEL.animate}"></div>
<nav id="sidepanel" class="sidepanel">
	<div class="sidepanel-content" >
		<div class="sidepanel-header">
			<img src="<c:url value="cache/images/logo-280.png" />" />
		</div>
		<div class="sidepanel-nav">
			<div id="dashboard" class="nav-cell">
				<div class="nav-icon" style="background-position:0 0;"></div>
				<span class="nav-txt">Dashboard</span>
			</div>
			<div id="dashlist" class="dashlist"></div>
			<div id="trends"class="nav-cell">
				<div class="nav-icon" style="background-position:0 -30px;"></div>
				<span class="nav-txt">Trends</span>
			</div>
			<div id="forecast" class="nav-cell">
				<div class="nav-icon" style="background-position:0 -60px;"></div>
				<span class="nav-txt">Forecast</span>
			</div>
			<div id="suggestions" class="nav-cell">
				<div class="nav-icon" style="background-position:0 -90px;"></div>
				<span class="nav-txt">Suggestions</span>
			</div>
		</div>	
	</div>
</nav>