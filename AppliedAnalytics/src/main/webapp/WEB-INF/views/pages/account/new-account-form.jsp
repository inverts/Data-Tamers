<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="personal-info">
	<h1><fmt:message key="account.new.title" /></h1>
	<br/>
	<form:form id="accountAction" action="ProcessNewAccountInfo" modelAttribute="accountForm" method="POST">
		<div class="googleAuth">
			<h4><a onclick="invokeGoogleAuthentication('accountAction');">
			<c:choose>
				<c:when test="${googleSuccess}">
					<fmt:message key="account.new.google.change" />
				</c:when>
				<c:otherwise>
					<fmt:message key="account.new.google" />
					<c:set var="validation" value="" />
				</c:otherwise>
			</c:choose>
			</a></h4>
		</div>
		<br/>
		<table>
			<tr>
				<td><form:label path="firstname"><fmt:message key="account.name.first" /></form:label></td>
				<td><form:input class="textbox" path="firstname"/></td>
				<td class="error">
					<c:if test='${not empty errors && not empty errors.getFieldError("firstname")}'>
						<fmt:message key='${errors.getFieldError("firstname").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><form:label path="lastname"><fmt:message key="account.name.last" /></form:label></td>
				<td><form:input class="textbox" path="lastname"/></td>
				<td class="error">
					<c:if test='${not empty errors && not empty errors.getFieldError("lastname")}'>
						<fmt:message key='${errors.getFieldError("lastname").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.email" /></label></td>
				<td><form:input class="textbox" path="email"/></td>
				<td class="error">
					<c:if test='${not empty errors && not empty errors.getFieldError("email")}'>
						<fmt:message key='${errors.getFieldError("email").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.email.confirm" /></label></td>
				<td><form:input class="textbox" path="confirmEmail"/></td>
				<td class="error">
					<c:if test='${not empty errors && not empty errors.getFieldError("confirmEmail")}'>
						<fmt:message key='${errors.getFieldError("confirmEmail").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.name.user" /></label></td>
				<td><form:input class="textbox" path="username"/></td>
				<td class="error">
					<c:if test='${not empty errors && not empty errors.getFieldError("username")}'>
						<fmt:message key='${errors.getFieldError("username").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.password" /></label></td>
				<td><form:input type="password" class="textbox" path="password"/></td>
				<td class="error">
					<c:if test='${not empty errors && not empty errors.getFieldError("password")}'>
						<fmt:message key='${errors.getFieldError("password").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.password.confirm" /></label></td>
				<td><form:input type="password" class="textbox" path="confirmPassword"/></td>
				<td class="error">
					<c:if test='${not empty errors && not empty errors.getFieldError("confirmPassword")}'>
						<fmt:message key='${errors.getFieldError("confirmPassword").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td></td>
				<c:choose>
					<c:when test="${googleSuccess}">
						<td><input type="submit" class="btn btn-primary active" id="account-submit" /></td>
					</c:when>
					<c:otherwise>
						<td><input type="button" class="btn btn-primary active disabled" id="account-submit" value="<fmt:message key="submit" />" /></td>
					</c:otherwise>
				</c:choose>
				<td></td>
			</tr>
		</table>
	</form:form>
	
</div>