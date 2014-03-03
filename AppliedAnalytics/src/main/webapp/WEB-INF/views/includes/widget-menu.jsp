<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="dropdown">
	<a class="widget_menu glyphicon glyphicon-th-list" data-toggle="dropdown"></a>

	<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
		<c:choose>
			<c:when test="${pageview == 'DASHBOARD'}">
				<li><a role="menu-item" class="removeWidget" onclick="removeWidget(this);"><fmt:message key="widget.menu.remove" /></a></li>
			</c:when>
			<c:otherwise>
				<li><a role="menu-item" class="addWidget"><fmt:message key="widget.menu.add" /></a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>