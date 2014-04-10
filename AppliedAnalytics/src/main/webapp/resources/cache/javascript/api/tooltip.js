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
			'size'		: 'default',
			'color'		: '#CCC',
			'opacity'	: 1.0,
			'gravity'	: $.fn.tipsy.autoNS,
			'arrow'		: true,
			'onhover'	: null,
			'out'		: null,
			'open'		: { "element": null, "event": "mouseover", "callback": null },
			'close'		: { "element": null, "event":"mouseout", "callback": null  },
			'domReady'	: false
	};
	
	
	/* function declaration */
    $.fn.tooltip = function (params) {
    	return this.each(function() {
    		var $this = $(this);
    		var settings = $.extend({}, defaults, params);
    		
    		
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
					break;
				case "small" : 
					ttContent.addClass("sm").css("max-width", "200px");
					break;
			}
			
			
			
			
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
	
			$('div.tipsy-arrow');
			
			var elm = $('tipsy-inner');
		
    			
			/*
			// default
			$('#tipsy').href = "tooltip.css";
			// change color
			if(defaults.color != '#CCC'){
				$('#tipsy-inner').href = "tooltip-simple.css";
			}
			else {
				$('#tipsy-inner').href = "tooltip.css";
			}
			
			// to include or hide arrow
			if(defaults.arrow){
				$('#tipsy-arrow').href = "tooltip.css";    				
			}
			else {
				$('#tipsy-arrow').href = "tooltip-simple.css";
			} */
			
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