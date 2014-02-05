<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<footer id="footer">
	<div class="footer-content">
		<c:choose>
			<c:when test="${FOOTER.type == 'APPLICATION'}">
				<div class="footer-nav"> 
					<a href="#"><fmt:message key="dashboard" /></a> | 
					<a href="/appliedanalytics/trends"><fmt:message key="trends" /></a> | 
					<a href="/appliedanalytics/forecast"><fmt:message key="forecast" /></a> | 
					<a href="#"><fmt:message key="alerts" /></a> | 
					<a href="/appliedanalytics/signout"><fmt:message key="signout" /></a> 
				</div>
			</c:when>
		</c:choose>
		<div class="footer-copyright">
			<fmt:message key="footer.copyright" />
			<jsp:useBean id="year" class="java.util.Date" /><fmt:formatDate value="${year}" pattern="yyyy" />
			<fmt:message key="footer.copyright.references" />
		</div>
	</div>
</footer>