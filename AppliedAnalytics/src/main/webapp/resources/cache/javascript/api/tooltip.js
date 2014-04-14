var currentTooltips = [];
var nTooltips = 0;

(function ($) {

	$.event.trigger({ type:"reposition" });
	
	
	var defaults = {
			'size'		: 'default',	// text size
			'width'		: null,	// specify a min-width. If the content is larger it will expand the width.
			'height'	: null, // specify a min-height. If the content is larger it will expand the height.
			'color'		: '#CCC',		// potentially color of the text or background? I'm currently not using it.
			'opacity'	: 1.0,			// opacity level.
			'gravity'	: $.fn.tipsy.autoNS,	// direction of arrow "n, s, e, w"
			'arrow'		: true,			// show arrow or not
			'onhover'	: null,			// event for when hovering onto a tooltip (currently not implemented)
			'out'		: null,			// event for when hovering out of a tooltip (currently not implemented)
			'open'		: "mouseover",	
			'close'		: "mouseout", 
			'domReady'	: false // show tooltip when element loads
	};
	
	
	/* function declaration */
    $.fn.tooltip = function (params) {
    	return this.each(function() {
    		var $this = $(this);
    		
    		var settings = $.extend({}, defaults, params);
    		
    		// if just a string is passed in for open/close events
    		if(typeof(settings.open) == "string") {
    			var openObj = {"element": $this, "event": settings.open };
    			settings.open = openObj;
    		}
    		else if (settings.open && !settings.open.element) 
	    			settings.open.element = $this;
	    	
    		
    		if(typeof(settings.close) == "string") {
    			var closeObj = {"element": $this, "event": settings.close };
    			settings.close = closeObj;
    		}
    		else if (settings.close && !settings.close.element)
    			settings.close.element = $this;
    		
    		// Does not respond to 'hover' so if passed as the event, 
    		// I will just set it to 'mouseover' and 'mouseout'.
    		if (settings.open.event == "hover")
    			settings.open.event = "mouseover";
    		if (settings.close.event == "hover")
    			settings.close.event = "mouseout";
    		
    		
    		
    		

    		//var id = this.id;
    		
    		/*var width = $this.width(),
    			height = $this.height();*/
    		
    		/* TOOLTIP PROPERTIES */
    		var tooltip = {
    				"id"		: this.id,
    				"title"		: null,
    				"size" 		: {"width": 0, "height": 0},
    				"arrow"		: true,
    				"color"		: "#F0F0",
    				"element"	: { "dom": $("#tooltip").clone(), "position": { 
    																			"left": $this.position().left, 
    																			"top": $this.position().top } 
    																		  },
    				"closeTimer": 0,
    				"active"	: false,
    				"n"			: nTooltips++,
    				"position"  : {"top": 0, "left": 0 }
    		};
    		
    		
    		$this.data("tooltip-n", tooltip.n); // store the tooltip id # so we can call events on the specific tooltip.

    		// Mouse on/off tooltip event
    		// Not working at the moment
    		/*if (settings.onHover)
    			tooltip.element.children(".tooltip-content").on("hover", settings.onHover, settings.out);*/
    		
    		/****************************
			 *  		CONTENT			*
			 ****************************/ 
    		var content = settings.content || $this.attr("title");
			
			// if nothing in the title, check data.
			if (!content || !content.length) {
				var dataContent = $this.data("tooltip");
				
				// if we have content in data, verify if its a function and run it or just set content.
				if (dataContent)
					content = (typeof(dataContent) == "function") ? dataContent() : dataContent;
			}
			
			// tooltip-content div
			var ttContent = tooltip.element.dom.find('.tooltip-content');
			
			// add content to tooltip if there is content,
			// otherwise log it and return out, we do not want to show empty tooltips.
			if (content && content.length)
				ttContent.children(".tooltip-body").html(content);
			else {
				console.error("no content for requested tooltip for element: " + $this.get(0));
				return;
			}
			
			
			/****************************
			 *  		SIZE			*
			 ****************************/ 
			switch (settings.size) {
				case "large": 
					ttContent.addClass("lg").css("max-width", "500px");
					// if an insane width is provided, just null it out. We can change this if we would like 
		    		// tooltips with widths larger than the max.
		    		if (settings.width && settings.width > 500)
		    			settings.width = null;
					break;
				case "small" : 
					ttContent.addClass("sm").css("max-width", "200px");
					if (settings.width && settings.width > 200)
		    			settings.width = null;
					break;
				default:
					if (settings.width && settings.width > 350)
		    			settings.width = null;
			}
			
			if (settings.width)
				ttContent.css("min-width", settings.width + "px");
			if (settings.height)
				ttContent.css("min-height", settings.height + "px");
			
			
			
			
			/****************************
			 *  		TIPSY			*
			 ****************************/ 
			$this.tipsy({
				trigger: 'manual',
				gravity: settings.gravity,
				html: true,
				color: tooltip.color,
				title: function() { return tooltip.element.dom.html(); },
				opacity: settings.opacity
			});
			
			$this.data("hasTooltip", true);
	
			/****************************
			 *  Tooltip Trigger Events	*
			 ****************************/ 
			
			var dur = 500; // timeout duration.
			
			var ttObj = {"dom": $this, "tipsy": null };
			
			// Event is a toggle
			if (settings.open.element[0] == settings.close.element[0] &&
					settings.open.event == settings.close.event) {
				
				settings.open.element.on(settings.open.event + ".oTooltip",
						function(e) {
						
							if (tooltip.active) {
								
								$this.tipsy("hide");
								tooltip.active = false;
								
								// remove tooltip from active list
								var r = currentTooltips.indexOf(ttObj);
								if (r > -1)
									currentTooltips.splice(r, 1);
								
								if (settings.close.callback)
									settings.close.callback();

							}
							else {
								
								$this.tipsy("show"); // show tipsy tooltip.
								tooltip.active = true; // set active flag.
								
								var tipsy = $("#tipsy").attr("id", "tipsy" + tooltip.n);
								ttObj.tipsy = "#tipsy" + tooltip.n;
								currentTooltips.push(ttObj);
								
								tooltip.position = tipsy.position();

								if (settings.open.callback)
									settings.open.callback();
							}

						});

			}
			else {
				
				settings.open.element.on(settings.open.event + ".oTooltip", 
						function(e) {
							if (!tooltip.active) {
								$this.tipsy("show");
								tooltip.active = true;
								
								var tipsy = $("#tipsy").attr("id", "tipsy" + tooltip.n);
								ttObj.tipsy = "#tipsy" + tooltip.n;
								currentTooltips.push(ttObj);
								
								tooltip.position = tipsy.position();
								
								if (settings.open.callback)
									settings.open.callback();

							}
				});
				
				settings.close.element.on(settings.close.event + ".cTooltip", 
					function(e) {
						if (tooltip.active) {
							if (e.type == "mouseout")
								closeTimeout(dur);
							else {
								$this.tipsy("hide");
								tooltip.active = false;
								
								var r = currentTooltips.indexOf(ttObj);
								if (r > -1)
									currentTooltips.splice(r, 1);
								
								if (settings.close.callback)
									settings.close.callback();
							}
						}
				});
				
			}
			
			
			// Set event to allow tooltip to remain open when hovering on it.
			if (settings.open.element[0] == settings.close.element[0] &&
					settings.close.event == "mouseout") {
				
				$("body").on("mouseover", "#tipsy" + tooltip.n, 
						function() { 
							clearTimeout(tooltip.closeTimer);
					})
					.on("mouseout", "#tipsy" + tooltip.n, 
						function() {
							closeTimeout(dur);
					});
			}
			
			

			function closeTimeout(duration) {
				tooltip.closeTimer = setTimeout(function() {
												$this.tipsy("hide");
												tooltip.active = false;
												var r = currentTooltips.indexOf(ttObj);
												if (r > -1)
													currentTooltips.splice(r, 1);
												if (settings.close.callback)
													settings.close.callback();
											}, duration);
			}
			
			// Event to check to see if parent element moved at all.
			$this.on("reposition", function() {
				
				if (tooltip.element.position.left != 0 && tooltip.element.position.top != 0) {
					
					// did the element hide?
					if (!$this.is(":visible")) {
						$this.tipsy("hide");
						tooltip.active = false;
					}
					
					// did the element just move?
					else if ($this.position().left != tooltip.element.position.left ||
						$this.position().top != tooltip.element.position.top) {
						
						$this.tipsy("hide");
						$this.tipsy("show");
					}
				}
					
				tooltip.element.position = $this.position(); 
				
			});

			// on DOM ready, open the tooltip.
			if (settings.domReady)
				$(function() { settings.open.element.trigger(settings.open.event + ".oTooltip"); });
    			    			
    			
    	});
    		
    		
    };
    	
    	

    
}(jQuery));

function resetTooltips() {
	$(".tipsy").remove();
	nTooltips = 0;
	currentTooltips = [];
}


function tooltipReposition($element) {
	
	$.each(currentTooltips, function(i) {
		var el = currentTooltips[i].dom;
		if ($.contains($element.parent()[0], el[0]))
			el.trigger("reposition");
	});
	
}

function removeTooltips($element) {
	
	$.each(currentTooltips, function(i) {
		var el = currentTooltips[i].dom;
		if ($.contains($element[0], el[0])) {
			$(currentTooltips[i].tipsy).remove();		
			currentTooltips.splice(i, 1);
		}
	});
}

