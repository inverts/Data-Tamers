<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id = "keywordInsightData">
	<div id="keywordInsightScatter" class="keywordVisual active"></div>
	<div id="keywordInsightImprove" class="keywordVisual">
		<div class="organic active"></div>
		<div class="paid"></div>
	</div>
	<div id="keywordInsightBest" class="keywordVisual">
		<div class="organic active"></div>
		<div class="paid"></div>
	</div>
	<div id="keywordInsightWorst" class="keywordVisual">
		<div class="organic active"></div>
		<div class="paid"></div>
	</div>
	<div id="keywordInsightAll" class="keywordVisual">
		<div class="organic active"></div>
		<div class="paid"></div>
	</div>
	<div id="keywordInsightBestSub" class="keywordVisual">
		<div class="organic active"></div>
		<div class="paid"></div>
	</div>
	<div id="keywordInsightWorstSub" class="keywordVisual">
		<div class="organic active"></div>
		<div class="paid"></div>
	</div>
</div>
<div class="controls">
	<ul class="pagination">
	  <li><a class="scatter">Scatter Plot</a></li>
	  <li><a class="improve">Improve</a></li>
	  <li><a class="best">Best</a></li>
	  <li><a class="worst">Worst</a></li>
	  <li><a class="all">All</a></li>
	  <li><a class="bestsub">Best Words</a></li>
	  <li><a class="worstsub">Worst Words</a></li>
	</ul>
</div>
