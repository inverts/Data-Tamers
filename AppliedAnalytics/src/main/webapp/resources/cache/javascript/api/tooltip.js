(function ($) {
    /* do I need this? */
	registerKeyboardHandler = function(callback) {
		  d3.select(window).on("keydown", callback);  
		};
		
	$.fn.tooltip.defaults = {
	        opacity: 1.0,
	        offset: 1,
	        delayIn: 500,
	        hoverable: true,
	        hideOnClick: true
	    };
	
	var defaults = {
			'size'		: 'large',
			'color'		: '#CCC',
			'gravity'	: $.fn.tipsy.autoNS,
			'arrow'		: true,
			'onhover'	: null,
			'out'		: null,
			'open'		: { "element": $(this), "event": "mouseover", "callback": null },
			'close'		: { "element": $(this), "event":"mouseout", "callback": null  }					
	};
	
	
	/* function declaration */
    $.fn.tooltip = function (params) {
    	return this.each(function() {
    		var $this = $(this);
    		var settings = $.extend({}, defaults, params);
    		var id = this.id;
    		
    		var width = $this.width(),
    			height = $this.height();
    		
    		/* TOOLTIP PROPERTIES */
    		var tooltip = {
    				"id"		: this.id,
    				"title"		: null,
    				"size" 		: {"width": 0, "height": 0},
    				"arrow"		: true,
    				"color"		: '#F0F0'
    		};
    		
    			var content = $this.attr("title");
    			$('.tooltip-body').html(content);
    			
    			
    			$('#'+id).tipsy({
    				trigger: 'manual',
    				gravity: settings.gravity,
    				html: true,
    				color: tooltip.color,
    				title: function() { return $('#tooltip').html(); }    				
    			});
    			
    			$('div.tipsy-arrow');
    			console.log($('div.tipsy').css("color", "red"))
    			console.log($('div.tipsy-arrow').css("background", "red"))
    			
    			var elm = $('tipsy-inner');
    			console.log(elm.className);
    			
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
    			   			
    			
    			
    			$('#'+id).tipsy();
    			
    			    			
    			
    		});
    		
    		
    	};
    	
    	

    
}(jQuery));