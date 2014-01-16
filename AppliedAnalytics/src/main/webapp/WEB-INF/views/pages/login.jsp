<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="login">
	<form name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
		<h1><fmt:message key="login.title" /></h1>
		<div class="register-div">
			<a href="#"><fmt:message key="login.register.link" /></a>
		</div>
		<c:if test="${not empty model.invalid}">
			<div class="error"><fmt:message key="${model.invalid}" /></div>
		</c:if>
		<table>
			<tr>
				<td><input type="text" class="input-text" name="j_username" placeholder="<fmt:message key="login.placeholder.username" />" /></td>
			</tr>
			<tr>
				<td><input type="password" class="input-text" name="j_password" placeholder="<fmt:message key="login.placeholder.password" />" /></td>
			</tr>
			<tr>
				<td>
					<input type="checkbox" class="input-check" name="remember" />
					<span class="remember"><fmt:message key="login.remember" /></span>
					<input type="submit" class="input-submit" name="submit" value="<fmt:message key="login.signin" />" />
				</td>
			</tr>
		</table>
	</form>
</div>

