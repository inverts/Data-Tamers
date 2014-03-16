<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id = "keywordInsightData">
	<div id="keywordInsightScatter" class="keywordVisual active"></div>
	<div id="keywordInsightImprove" class="keywordVisual"></div>
	<div id="keywordInsightBest" class="keywordVisual"></div>
	<div id="keywordInsightBestSub" class="keywordVisual"></div>
	<div id="keywordInsightWorstSub" class="keywordVisual"></div>
</div>
<div class="controls">
	<ul class="pagination">
	  <li><a class="prev">&laquo;</a></li>
	  <li><a class="scatter">Scatter</a></li>
	  <li><a class="improve">Improvement</a></li>
	  <li><a class="best">Best</a></li>
	  <li><a class="bestsub">Best Cont</a></li>
	  <li><a class="worstsub">Worst</a></li>
	  <li><a class="next">&raquo;</a></li>
	</ul>
</div>
