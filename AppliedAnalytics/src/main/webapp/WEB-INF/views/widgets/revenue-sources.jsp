<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  
<script>
//var RevenueSourceData = JSON.parse('${DATA}');
</script>  
  
<div class="widget_wrapper widgetView">
	<form id="revenueSourcesSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="revenuesources.title" /></div>
			
			
		</div>
		<div id="revenueSourcesGraph" class="rs_visual">
			<!-- add Visualization elements -->
			<canvas id="revenueSourcesData"></canvas>
								
		</div>
	</form>
</div>
