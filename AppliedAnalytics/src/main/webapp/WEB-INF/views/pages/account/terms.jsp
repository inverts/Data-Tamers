<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="terms">
	<div class="terms-wrapper">
		<h1><fmt:message key="account.terms.header" /></h1>
		<form action="<c:url value="/accounts/acceptterms" />" method="POST">
			<div class="termstext">
				<fmt:message key="account.terms.text.1" /><br/><br/>
				<fmt:message key="account.terms.text.2" /><br/><br/>
				<fmt:message key="account.terms.text.3" /><br/><br/>
				<fmt:message key="account.terms.text.4" /><br/><br/>
				<fmt:message key="account.terms.text.5" /><br/><br/>
			</div>
			<div class="accept-div">
				<input type="checkbox" class="checkbox" name="accept_terms" id="terms-check" />
				<fmt:message key="account.terms.accept" />
			</div>
			<div class="continue">
				<a href="<c:url value="/appliedanalytics/" />"><fmt:message key="account.terms.cancel" /></a>
				<input type="submit" class="button disabled" id="terms-submit" value="<fmt:message key="account.terms.continue" />" disabled />
			</div>
		</form>
	</div>
</div>
