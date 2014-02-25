<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<!-- Style elements -->
<style type = "text/css">
	.column:nth-child(1) .fill{
		background:#17AC08;
		height:110%; 
	}
	.column:nth-child(2) .fill{
		background: #0EA8AE;
		height:150%;
	}
	.column:nth-child(3) .fill{
		background: #DFB611;
		height:160%;
	}
	.column:nth-child(4) .fill{
		background: #9400D3;
		height:120%;
	}
	
	.legend:nth-child(1) .fill-legend{
		background:#17AC08;
		height:100%;
		width:100%;
	}
	.legend:nth-child(2) .fill-legend{
		background: #0EA8AE;
		height:100%;
		width:100%;
	}
	.legend:nth-child(3) .fill-legend{
		background: #DFB611;
		height:100%;
		width:100%;
	}
	.legend:nth-child(4) .fill-legend{
		background: #9400D3;
		height:100%;
		width:100%;
	}
	
	

</style>
</head>

<body>

<div class="widget widgetView keyContributingFactors">
	<form id = "keyContributingFactorsSettings">
	<div class="widget_header">
		<div class="widget_title"><fmt:message key="revenuesources..title" /></div>	
	</div>
	<div class="widget-content">
	<div class = "keyContributingFactorsChart">	
		<!-- Sources -->
		<div class = "chart-background">		
		<!-- Column values -->	
		<div class = "column-wrapper">
					
			<div class="column">	
				<div class="fill"></div>	
				<div class="tooltip"></div>										
				<p class="source"><span>Source1</span></p>
			</div>	
			
			
			<div class="column">
				<div class="fill"></div>
				<p class="source">Source2</p>
			</div>
			<div class="column">
				<div class="fill"></div>
				<p class="source">Source3</p>
			</div>
			<div class="column">
				<div class="fill"></div>
				<p class="source">Source4</p>
			</div>
		</div>	
			<!-- Legend -->
			<div class = "legend-wrapper">
				<div class = "legend">
					<div class = "fill-legend"></div>
					<div class="inline">Source1</div>		
				</div>
				<div class = "legend">
					<div class = "fill-legend"></div>
					<div class="inline">Source2</div>		
				</div>
				<div class = "legend">
					<div class = "fill-legend"></div>
					<div class="inline">Source3</div>		
				</div>
				<div class = "legend">
					<div class = "fill-legend"></div>
					<div class="inline">Source4</div>		
				</div>
			</div>
		
		
		
		
		</div>		
	
	</div>
	</div>			
	</form>
</div>
</body>