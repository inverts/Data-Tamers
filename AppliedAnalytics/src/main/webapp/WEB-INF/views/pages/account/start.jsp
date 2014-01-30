<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="start">
	<h1><fmt:message key="account.start.header" /></h1>
	<p class="start-content"><fmt:message key="account.start.summary" /></p>
	<ul class="pager">
	  <li class="previous"><a href="/appliedanalytics/"><fmt:message key="account.start.back" /></a></li>
	  <li class="next"><a href="/appliedanalytics/accounts/getstarted?terms-and-conditions=1"><fmt:message key="account.start.next" /></a></li>
	</ul>
</div>