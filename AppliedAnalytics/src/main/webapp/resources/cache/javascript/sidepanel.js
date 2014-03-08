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
	$("#dashboard").hover(function() {
		$(".dashlist").slideDown('fast');
		$('.sidepanel-content').hover(null, function() {
								$(".dashlist").slideUp('fast');
							});
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
	
	$.post(applicationRoot + "application/deleteDashboard", {dashboardId: dashboardId}, 
			function(result) {
				result = $.parseJSON(result);
				var $dash = $("#dashlist #" + result.dashboardId);
				var name = $dash.children("a").html();
				if (name != "Default Dashboard" && name != "Second Dashboard")
					$dash.remove();
				
				Modal.alert({
					"title": "Remove Dashboard",
					"content": "Dashboard \"" + name + "\" has been successfully removed!",
				});
	});
}

/* Creates a dashboard link */
function createDashboardLink(dashboard) {
	var $dashlist = $("#dashlist");
	
	var $li = $("<li>").attr("id", dashboard.id).appendTo($dashlist);

	var $link = $("<a>").attr("onclick", "loadDashboard(" + dashboard.id + ");")
						.html((dashboard.name)? dashboard.name : dashboard.id).appendTo($li);
	var $delete = $("<span>").addClass("deleteDash glyphicon glyphicon-remove-circle")
							 .attr("title", "remove " + $link.html())
							 .appendTo($li);
	
	/* Set remove dashboard event */
	$delete.click(function(e){
		e.preventDefault();
		var name = $(this).siblings("a").html();
		var id = $(this).parent().attr("id");
		Modal.call({
			"title": "Remove Dashboard",
			"content": "Delete dashboard \"" + name + "\"?",
			"action": function(e) { e.preventDefault(); removeDashboard(id); }
		});
		
	});
	
	$("#dashlist #" + dashboard.id).hover(function() {
		$delete.toggle();
	});
	
}

/* Opens up right pane */
function showAddDashboardPanel(width, showPage) {        
    if ($(".addDashboard").length) {
    	addDashboardPage();
    	$(".addDashboard")
    		.show()
    		.animate({width: width}, 500, "swing", function() {
                    $(".addDashboard-content").show();                        
                    $(window).on("click.dashboard", function() {
                    	hideAddDashboardPanel();
                    });        
                    
                    /* Create dashboard submission */
                	$("#dash-submit").on("click", function(e) {
                		e.preventDefault();
                		
                		var name = $("#dashboardName").val();
                		
                		Modal.call({
                			"title": "Create Dashboard",
                			"content": "Create new dashboard \"" + name + "\"?",
                			"action": function(e) { 
                				e.preventDefault();
                				$.post(applicationRoot + "application/createDashboard", { name: name },
                        				function(result) {
                        						createDashboardLink($.parseJSON(result));
                        						Modal.alert({
                        							"title": "Create Dashboard",
                        							"content": name + " has been successfully created!",
                        						})
                        						hideAddDashboardPanel();
                        				});
                			}
                		
                		});
                		
                	});
                    
                    
            }).click(function(e){ e.stopPropagation(); });
    }
}

/* Hides right pane */
function hideAddDashboardPanel() {
	$(".addDashboard").animate({width: 0}, 500, "swing", function() {
			$(".right-pane")
				.removeClass("addDashboard")
				.hide();
            $(window).off("click.dashboard");
            $(".addDashboard-content").hide(); 
    });
}


