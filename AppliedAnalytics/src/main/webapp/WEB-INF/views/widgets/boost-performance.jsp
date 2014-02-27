<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<head>
<!-- Style elements -->
<style type = "text/css"></style>
<script>

</script>
</head>
<body>

	<div class="widget widgetView boostPerformance">
		<form id="boostPerformanceSettings">
			<div class="widget_header">
				<div class="widget_title">
					<fmt:message key="boostperformance.title" />
				</div>
			</div>
			<div class="widget-content">
				<div id="boostPerformance-table" class="boostPerformanceChart">
					<div class="bp-background">
						<div id="dropdown" class="bp-wrapper">
						<input class="bp-box" id="source" type="checkbox" >
						<label for="source">Google AdWords</label>
							<div><ul id = bp-source>
								<li> 5.3% conversion rate </li>
									<ul><li>132.5% above site average</li></ul>
								<li> $10.23 avg. value per visit </li>
								<li> makes up only 10% of all traffic </li>
							</ul></div>
						</div>
						

					
					</div>

				</div>
			</div>
		</form>
	</div>
</body>

