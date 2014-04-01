<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:if test="${model.googleFail}">
	<div class="new-account alert alert-danger">
		<div class="alert-msg"><fmt:message key="account.alert.google.fail" /></div>
	</div>
</c:if>
<c:if test="${model.googleSuccess}">
	<div class="new-account alert alert-success">
		<div class="alert-msg">
			<fmt:message key="account.alert.google.success.1" />
			<span class="googleName"><c:out value="${model.googleAccountName}" /></span>
			<fmt:message key="account.alert.google.success.2" />
		</div>
	</div>
</c:if>

<c:import url="/WEB-INF/views/pages/account/start.jsp" />