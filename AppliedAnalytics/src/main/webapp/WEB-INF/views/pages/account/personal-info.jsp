<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:if test="${model.googleFail}">
	<div class="new-account alert alert-danger"><fmt:message key="account.alert.google.fail" /></div>
</c:if>
<c:if test="${model.googleSuccess}">
	<div class="new-account alert alert-success">
		<fmt:message key="account.alert.google.success.1" />
		<span class="googleName"><c:out value="${model.googleAccountName}" /></span>
		<fmt:message key="account.alert.google.success.2" />
	</div>
</c:if>
<div class="personal-info">
	<h1><fmt:message key="account.new.title" /></h1>
	<br/>
	
		<form id="googleAuth" method="GET" action="GoogleAuthenticateHandler">
			<input type="hidden" name="login" value="1" />
			<div class="googleAuth">
				<h4><a onclick="$('#googleAuth').submit(); return false;">
				<c:choose>
					<c:when test="${model.googleSuccess}">
						<fmt:message key="account.new.google.change" />
					</c:when>
					<c:otherwise>
						<fmt:message key="account.new.google" />
					</c:otherwise>
				</c:choose>
				</a></h4>
			</div>
		</form>
	<br/>
	<form:form action="ProcessNewAccountInfo" modelAttribute="accountForm" method="POST">
		<table>
			<tr>
				<td><form:label path="firstname"><fmt:message key="account.name.first" /></form:label></td>
				<td><form:input class="textbox" path="firstname"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("firstname")}'>
						<fmt:message key='${model.validation.getFieldError("firstname").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><form:label path="lastname"><fmt:message key="account.name.last" /></form:label></td>
				<td><form:input class="textbox" path="lastname"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("lastname")}'>
						<fmt:message key='${model.validation.getFieldError("lastname").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.email" /></label></td>
				<td><form:input class="textbox" path="email"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("email")}'>
						<fmt:message key='${model.validation.getFieldError("email").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.email.confirm" /></label></td>
				<td><form:input class="textbox" path="confirmEmail"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("confirmEmail")}'>
						<fmt:message key='${model.validation.getFieldError("confirmEmail").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.name.user" /></label></td>
				<td><form:input class="textbox" path="username"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("username")}'>
						<fmt:message key='${model.validation.getFieldError("username").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.password" /></label></td>
				<td><form:input type="password" class="textbox" path="password"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("password")}'>
						<fmt:message key='${model.validation.getFieldError("password").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="account.password.confirm" /></label></td>
				<td><form:input type="password" class="textbox" path="confirmPassword"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("confirmPassword")}'>
						<fmt:message key='${model.validation.getFieldError("confirmPassword").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td></td>
				<c:choose>
					<c:when test="${model.googleSuccess}">
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