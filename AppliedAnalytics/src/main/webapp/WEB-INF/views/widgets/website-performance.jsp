<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script>
//var WebsitePerformanceData = JSON.parse('${DATA}');
</script>  
  
<div class="widget_wrapper widgetView">
	<form id="websitePerformanceSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="websiteperformance.title" /></div>
			
			
		</div>
		<div id="websitePerformanceGraph" class="rs_visual">
			<!-- add Visualization elements -->
			<canvas id="websitePerformanceData"></canvas>
								
		</div>
	</form>
</div>
