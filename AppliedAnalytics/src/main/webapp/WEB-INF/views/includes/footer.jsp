<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<footer id="footer">
	<div class="footer-content">
		<div class="footer-copyright">
			<fmt:message key="footer.copyright" />
			<jsp:useBean id="year" class="java.util.Date" /><fmt:formatDate value="${year}" pattern="yyyy" />
			<fmt:message key="footer.copyright.references" />
		</div>
	</div>
</footer>