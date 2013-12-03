<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="<c:url value="/cache/css/widget.css" />" rel="stylesheet">
<link href="<c:url value="/cache/css/hypothetical-future.css" />" rel="stylesheet">
    
<div class="widget_wrapper">
	<form id="hypotheticalFutureSettings">
		<div class="widget_header">
			<span class="widget_title">HYPOTHETICAL FUTURE</span>
			<div class="widget_input">
				<label>TRAFFIC SOURCE:</label>
				<select id="traffic_source"></select> 
			</div>
			<div class="widget_input"> 
				<label>CHANGE:</label>
				<select id="change_pct">
				<c:forEach var="option" items="${changeOptions}" >
					<option value="${option.getKey()}" ${option.getValue()}> ${option.getKey()}%</option>
				</c:forEach>
				</select>
			</div>
		</div>
		<div id="hypotheticalFutureGraph" class="hf_visual">
			<!-- add Visualization elements -->
		</div>
	</form>
</div>