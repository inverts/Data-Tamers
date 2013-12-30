<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<footer id="footer">
	<div class="footer-content">
		<div class="footer-nav"> <a href="#">
			<fmt:message key="dashboard" /></a> | 
			<a href="#"><fmt:message key="trends" /></a> | 
			<a href="#"><fmt:message key="forecast" /></a> | 
			<a href="#"><fmt:message key="suggestions" /></a> | 
			<a href="/appliedanalytics"><fmt:message key="signout" /></a> 
		</div>
		<div class="footer-copyright">
			<fmt:message key="footer.copyright" />
			<jsp:useBean id="year" class="java.util.Date" /><fmt:formatDate value="${year}" pattern="yyyy" />
			<fmt:message key="footer.copyright.references" />
		</div>
	</div>
</footer>