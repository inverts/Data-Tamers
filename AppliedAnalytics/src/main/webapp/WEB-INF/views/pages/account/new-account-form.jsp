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
					<input type="button" class="btn btn-primary" value="<fmt:message key="account.new.google.change" />" />
				</c:when>
				<c:otherwise>
					<input type="button" class="btn btn-primary" value="<fmt:message key="account.new.google" />" title='<fmt:message key="account.new.google.required" />'/>
					<c:set var="validation" value="" />
				</c:otherwise>
			</c:choose>
			</a></h4>
		</div>
		<br/>
		<table>
			<tr>
				<td><form:label path="firstname"><fmt:message key="account.name.first" /></form:label></td>
				<c:set var="firstNameError" value="" />
				<c:if test='${not empty errors && not empty errors.getFieldError("firstname")}'>
					<c:set var="firstNameError"><fmt:message key='${errors.getFieldError("firstname").getDefaultMessage()}' /></c:set>
				</c:if>
				
				<td><form:input class="textbox" path="firstname" title="${firstNameError}" /></td>
			</tr>
			<tr>
				<td><form:label path="lastname"><fmt:message key="account.name.last" /></form:label></td>
				<c:set var="lastNameError" value="" />
				<c:if test='${not empty errors && not empty errors.getFieldError("lastname")}'>
					<c:set var="lastNameError"> <fmt:message key='${errors.getFieldError("lastname").getDefaultMessage()}' /> </c:set>
				</c:if>
				<td><form:input class="textbox" path="lastname" title="${lastNameError}" /></td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.email" /></label></td>
				<c:set var="emailError" value="" />
				<c:if test='${not empty errors && not empty errors.getFieldError("email")}'>
					<c:set var="emailError"> <fmt:message key='${errors.getFieldError("email").getDefaultMessage()}' /> </c:set>
				</c:if>
				<td><form:input class="textbox" path="email" title="${emailError}" /></td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.email.confirm" /></label></td>
				<c:set var="confirmEmailError" value="" />
				<c:if test='${not empty errors && not empty errors.getFieldError("confirmEmail")}'>
					<c:set var="confirmEmailError"> <fmt:message key='${errors.getFieldError("confirmEmail").getDefaultMessage()}' /> </c:set>
				</c:if>
				<td><form:input class="textbox" path="confirmEmail" title="${confirmEmailError}" /></td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.name.user" /></label></td>
				<c:set var="usernameError" value="" />
				<c:if test='${not empty errors && not empty errors.getFieldError("username")}'>
					<c:set var="usernameError"> <fmt:message key='${errors.getFieldError("username").getDefaultMessage()}' /> </c:set>
				</c:if>
				<td><form:input class="textbox" path="username" title="${usernameError}" /></td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.password" /></label></td>
				<c:set var="passwordError" value="" />
				<c:if test='${not empty errors && not empty errors.getFieldError("password")}'>
					<c:set var="passwordError"> <fmt:message key='${errors.getFieldError("password").getDefaultMessage()}' /> </c:set>
				</c:if>
				<td><form:input type="password" class="textbox" path="password" title="${passwordError}" /></td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.password.confirm" /></label></td>
				<c:set var="confirmPasswordError" value="" />
				<c:if test='${not empty errors && not empty errors.getFieldError("confirmPassword")}'>
					<c:set var="confirmPasswordError"> <fmt:message key='${errors.getFieldError("confirmPassword").getDefaultMessage()}' /> </c:set>
				</c:if>
				<td><form:input type="password" class="textbox" path="confirmPassword" title="${confirmPasswordError}" /></td>
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
			</tr>
		</table>
	</form:form>
	
</div>
