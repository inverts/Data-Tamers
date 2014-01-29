<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="login">
	<form:form action="login-validation" modelAttribute="loginForm" method="POST">
		<h1><fmt:message key="login.title" /></h1>
		<div class="register-div">
			<h4><fmt:message key="login.register.1" />
				<a href="/appliedanalytics/accounts/getstarted"><fmt:message key="login.register.link" /></a>
				<fmt:message key="login.register.2" />
			</h4>
		</div>
		<c:if test="${model.hasErrors}">
			<div class="error"><fmt:message key="login.invalid" /></div>
		</c:if>
		<table>
			<tr>
				<td>
					<fmt:message key="login.placeholder.username" var="uStr"/>
					<form:input class="textbox input-text" name="j_username" placeholder="${uStr}" path="user" />
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="login.placeholder.password" var="pStr" />
					<form:input type="password" class="textbox input-text" name="j_password" placeholder="${pStr}" path="pass" />
				</td>
			</tr>
			<tr>
				<td><a href="#"><fmt:message key="login.forgotpass" /></a></td>
			</tr>
			<tr>
				<td class="submit">
					<span class="remember">
						<input type="checkbox" class="checkbox" name="remember" />
						<fmt:message key="login.remember" />
					</span>
					<input type="submit" class="button" name="submit" value="<fmt:message key="login.signin" />" />
				</td>
			</tr>
		</table>
	</form:form>
</div>

