<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="addDashboard-content">
	<h2><fmt:message key="dashboard.add.title" /></h2>
	<form:form id="addDashForm" action="#" modelAttribute="dashboardForm" onsubmit="return false;">
		<table>
			<tr>
				<td><form:label path="dashboardName"><fmt:message key="dashboard.add.name" /></form:label></td>
				<td><form:input path="dashboardName" id="dashboardName" class="textbox" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" class="btn btn-primary btn-sm active" value="<fmt:message key='dashboard.add.submit' />" id="dash-submit" /></td>
			</tr>
		</table>
	</form:form>
</div>