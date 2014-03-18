<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="dropdown">
	<a class="widget_menu glyphicon glyphicon-th-list dropdown-toggle" data-toggle="dropdown" ></a>

	<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
		<li><a role="menu-item" class="removeWidget"><fmt:message key="widget.menu.remove" /></a></li>
		<li class="dropdown-submenu">
			<a href="#"><fmt:message key="widget.menu.add" /></a>
			<ul class="dropdown-menu" id="dashboard-list">
				<li><a role="menu-item">Sample Dashboard</a></li>
			</ul>
		</li>
	</ul>
</div>