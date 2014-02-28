/**
 * Sidepanel.js
 */


$(function() {
	
	$(".sidepanel-background").css("width", $(".sidepanel").width());
	
	/* Navigation hover effect */
	$(".nav-cell").hover(function(){
		$(this).animate({ backgroundColor: "gray" }, 100, "swing");
	}, function() {
		$(this).animate({ backgroundColor: "transparent" }, 0, "swing");
	});
	
	/* dashboard Navigation action */
	$("#dashboard").click(function() {
		$(".dashlist").slideToggle();
	});
	
	/* add new dashboard */
	$("#addDashboard").click(function() {
		$(".right-pane")
			.addClass("addDashboard");
			
		showAddDashboardPanel(550, addDashboardPage);
	});

	
});


function getDashboardList(list) {
	
	if (!list.length)
		console.error("No Dashboards");
	
	for(var i = 0; i < list.length; i++) {	
		createDashboardLink(list[i]);
	}
}

/* Populates the right pane with the new dashboard form */
function addDashboardPage() {
	$.post(applicationRoot + "addDashboard", {}, function( data ) {
		  $(".addDashboard").html( data );
	});
}

/* Removes a dashboard */
function removeDashboard(dashboardId) {
	
	$.post(applicationRoot + "application/deleteDashboard", {dashboardId: dashboardId}, function(result) {
		alert("removed " + result);
	});
}

/* Creates a dashboard link */
function createDashboardLink(dashboard) {
	var $dashlist = $("#dashlist");
		
	var $linkdiv = $("<div>").addClass("dashlink-cell");
	var $link = $("<a>").attr("href", "#")
						.attr("onclick", "loadDashboard(" + dashboard.id + ");")
						.html((dashboard.name)? dashboard.name : dashboard.id).appendTo($linkdiv);
	var $delete = $("<div>").addClass("deleteDash").html("<span class='glyphicon glyphicon-remove-circle'></span>");
	
	/* Set remove dashboard event */
	$delete.click(function(e){
		e.preventDefault();
		removeDashboard($(this).parent().attr("id"));
	});
	
	var $removediv = $("<div>").attr("id", dashboard.id).append($linkdiv).append($delete);
	
	var $li = $("<li>").addClass("dashlink").append($removediv).appendTo($dashlist); // add list item
}

/* Opens up right pane */
function showAddDashboardPanel(width, showPage) {        
    if ($(".addDashboard").length) {
    	addDashboardPage();
    	$(".addDashboard")
    		.show()
    		.animate({width: width}, 500, 'swing', function() {
                    $('.addDashboard-content').show();                        
                    $(window).on('click.dashboard', function() {
                    	hideAddDashboardPanel();
                    });        
                    
                    /* Create dashboard submission */
                	$("#dash-submit").on("click", function(e) {
                		e.preventDefault();
                		$.post(applicationRoot + "application/createDashboard", 
                				{ name: $("#dashboardName").val() },
                				function(result) {
                						createDashboardLink($.parseJSON(result));
                						hideAddDashboardPanel();
                				});
                	});
                    
                    
            }).click(function(e){ e.stopPropagation(); });
    }
}

/* Hides right pane */
function hideAddDashboardPanel() {
	$(".addDashboard").animate({width: 0}, 500, 'swing', function() {
			$('.right-pane')
				.removeClass('addDashboard')
				.hide();
            $(window).off('click.dashboard');
            $('.addDashboard-content').hide(); 
    });
}


