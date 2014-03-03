<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="terms">
	<div class="terms-wrapper">
		<h2><fmt:message key="account.terms.header" /></h2>
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
				<label style="font-weight: normal;" for="terms-check"><fmt:message key="account.terms.accept" /></label>
			</div>
			<div class="continue">
				<input type="button" class="btn btn-default active" id="terms-cancel" value="<fmt:message key="account.terms.cancel" />" />
				<input type="submit" class="btn btn-primary active" id="terms-submit" value="<fmt:message key="account.terms.continue" />" disabled />
			</div>
		</form>
	</div>
</div>
