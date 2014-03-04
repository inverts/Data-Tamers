<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="widget widgetView ${widget.getHTMLClass()}">
	<div class="widget_header">
		<div class="widget_title"><fmt:message key="${widget.getTitle()}" /></div>
		<c:import url="../includes/widget-menu.jsp" />
	</div>
	<div class="widget-content">
		<c:import url="${view}" />
	</div>
</div>