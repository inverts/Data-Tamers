<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id = "keywordInsightData">
	<div id="keywordInsightScatter" class="keywordVisual active"></div>
	<div id="keywordInsightImprove" class="keywordVisual"></div>
	<div id="keywordInsightBest" class="keywordVisual"></div>
	<div id="keywordInsightWorst" class="keywordVisual"></div>
	<div id="keywordInsightAll" class="keywordVisual"></div>
	<div id="keywordInsightBestSub" class="keywordVisual"></div>
	<div id="keywordInsightWorstSub" class="keywordVisual"></div>
</div>
<div class="controls">
	<ul class="pagination">
	  <li><a class="scatter">Plot</a></li>
	  <li><a class="improve">Improve</a></li>
	  <li><a class="best">Best</a></li>
	  <li><a class="worst">Worst</a></li>
	  <li><a class="all">All</a></li>
	  <li><a class="bestsub">BestStr</a></li>
	  <li><a class="worstsub">WorstStr</a></li>
	</ul>
</div>
