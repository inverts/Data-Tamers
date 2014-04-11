(function ($) {
		
	/*$.fn.tooltip.defaults = {
	        opacity: 1.0,
	        offset: 1,
	        delayIn: 500,
	        hoverable: true,
	        hideOnClick: true
	    };*/
	
	var nTooltips = 0;
	
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
			'open'		: { "element": null, "event": "mouseover", "callback": null },	//open element/action; could just be a string depicting the action and it will assume the element is the element portraying the tooltip.
			'close'		: { "element": null, "event":"mouseout", "callback": null  }, // close element/action; "  "
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
    		else if (!settings.open.element) 
	    			settings.open.element = $this;
	    	
    		
    		if(typeof(settings.close) == "string") {
    			var closeObj = {"element": $this, "event": settings.close };
    			settings.close = closeObj;
    		}
    		else if (!settings.close.element)
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
    				"color"		: '#F0F0',
    				"element"	: $("#tooltip").clone(),
    				"closeTimer": 0,
    				"active"	: false,
    				"n"			: nTooltips++
    		};
    		
    		// Mouse on/off tooltip event
    		// Not working at the moment
    		/*if (settings.onHover)
    			tooltip.element.children(".tooltip-content").on("hover", settings.onHover, settings.out);*/
    		
    		/****************************
			 *  		CONTENT			*
			 ****************************/ 
			var content = $this.attr("title");
			
			// if nothing in the title, check data.
			if (!content || !content.length) {
				var dataContent = $this.data("tooltip");
				
				// if we have content in data, verify if its a function and run it or just set content.
				if (dataContent)
					content = (typeof(dataContent) == "function") ? dataContent() : dataContent;
			}
			
			// tooltip-content div
			var ttContent = tooltip.element.find('.tooltip-content');
			
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
				title: function() { return tooltip.element.html(); },
				opacity: settings.opacity
			});
	
			
			/****************************
			 *  Tooltip Trigger Events	*
			 ****************************/ 
			
			var dur = 1500; // timeout duration.
			
			// Event is a toggle
			if (settings.open.element[0] == settings.close.element[0] &&
					settings.open.event == settings.close.event) {
				
				settings.open.element.on(settings.open.event + ".oTooltip",
						function(e) {
						
							if (tooltip.active) {
								$this.tipsy("hide");
								tooltip.active = false;

							}
							else {
								$this.tipsy("show");
								tooltip.active = true;
								
								$("#tipsy").attr("id", "tipsy" + tooltip.n);
							}

						});

			}
			else {
				
				settings.open.element.on(settings.open.event + ".oTooltip", 
						function(e) {
							if (!tooltip.active) {
								$this.tipsy("show");
								tooltip.active = true;
								
								$("#tipsy").attr("id", "tipsy" + tooltip.n);

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
											}, duration);
			}
    		
			// on DOM ready, open the tooltip.
			if (settings.domReady)
				$(function() { settings.open.element.trigger(settings.open.event + ".oTooltip"); });
    			    			
    			
    	});
    		
    		
    };
    	
    	

    
}(jQuery));