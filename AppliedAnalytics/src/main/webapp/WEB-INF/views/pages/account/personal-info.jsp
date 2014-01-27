<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<div class="personal-info">
	<h1>New Account</h1>
	<br/><br/>
	<form:form action="processpersonalinfo" modelAttribute="personalForm" method="POST">
		<table>
			<tr>
				<td><form:label path="firstname"><fmt:message key="personal.name.first" /></form:label></td>
				<td><form:input class="textbox" path="firstname"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("firstname")}'>
						<fmt:message key='${model.validation.getFieldError("firstname").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><form:label path="lastname"><fmt:message key="personal.name.last" /></form:label></td>
				<td><form:input class="textbox" path="lastname"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("lastname")}'>
						<fmt:message key='${model.validation.getFieldError("lastname").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.email" /></label></td>
				<td><form:input class="textbox" path="email"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("email")}'>
						<fmt:message key='${model.validation.getFieldError("email").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.email.confirm" /></label></td>
				<td><form:input class="textbox" path="confirmEmail"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("confirmEmail")}'>
						<fmt:message key='${model.validation.getFieldError("confirmEmail").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.name.user" /></label></td>
				<td><form:input class="textbox" path="username"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("username")}'>
						<fmt:message key='${model.validation.getFieldError("username").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.password" /></label></td>
				<td><form:input type="password" class="textbox" path="password"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("password")}'>
						<fmt:message key='${model.validation.getFieldError("password").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.password.confirm" /></label></td>
				<td><form:input type="password" class="textbox" path="confirmPassword"/></td>
				<td class="error">
					<c:if test='${not empty model.validation && not empty model.validation.getFieldError("confirmPassword")}'>
						<fmt:message key='${model.validation.getFieldError("confirmPassword").getDefaultMessage()}' />
					</c:if>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" class="button" /></td>
				<td></td>
			</tr>
		</table>
	</form:form>
	
</div>