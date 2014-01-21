<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<div class="personal-info">
	<h1>New Account</h1>
	<br/><br/>
	<form:form action="processpersonalinfo" commandName="personalForm" method="POST">
		<table>
			<tr>
				<td><label><fmt:message key="personal.name.first" /></label></td>
				<td><form:input class="textbox" path="firstname" value="${model.personalForm.getFirstname()}"></form:input></td>
				<td><form:errors path="firstname" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.name.last" /></label></td>
				<td><form:input type="text" class="textbox" path="lastname"></form:input></td>
				<td class="error"><form:errors path="lastname"></form:errors></td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.email" /></label></td>
				<td><form:input type="text" class="textbox" path="email"></form:input></td>
				<td><form:errors path="email" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.email.confirm" /></label></td>
				<td><form:input type="text" class="textbox" path="confirmEmail"></form:input></td>
				<td><form:errors path="confirmEmail" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.name.user" /></label></td>
				<td><form:input type="text" class="textbox" path="username"></form:input></td>
				<td><form:errors path="username" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.password" /></label></td>
				<td><form:input type="password" class="textbox" path="password" value=""></form:input></td>
				<td><form:errors path="password" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td><label><fmt:message key="personal.password.confirm" /></label></td>
				<td><form:input type="password" class="textbox" path="confirmPassword"></form:input></td>
				<td><form:errors path="confirmPassword" cssClass="error"></form:errors></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" class="button" /></td>
				<td></td>
			</tr>
		</table>
	</form:form>
	
</div>