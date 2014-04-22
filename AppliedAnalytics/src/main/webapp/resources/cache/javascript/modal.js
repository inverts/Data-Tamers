/**
 * modal.js
 * 
 * @Author: Andrew Riley
 * 
 * Creates a customized modal using the bootstrap modal.
 * 
 */

var Modal = {
				
				call: function(params) {
					
					var $modal = $('#modal');
					
					var defaults = {
									 'title'	:'Modal title',
									 'buttons' : {
										 			'size': 'default', 
										 			'confirm': {'name': 'Ok', 'action': null}, 
										 			'cancel': 'Cancel', 
										 		  },
									 'content'	: '',
									 'size'		: 'modal-default',
									 'close'	: true,
									 'options'  : false,
									 'submit'	: false,
									 'action'	: function() {}
								   };
					
					var settings = $.extend({}, defaults, params);
					
					$modal.modal(settings.options);
	
					$('.modal-dialog').attr('class', 'modal-dialog ' + settings.size);
					$('.modal-header .close').show();
					$('.modal-title').empty().append((typeof(settings.title) == 'function') ? settings.title() : settings.title);				
					$('.modal-body').empty().append((typeof(settings.content) == 'function') ? settings.content() : settings.content);	
					
					$('.modal-footer').removeClass('prompt').empty();
					
					var buttons = settings.buttons;
					
					if (buttons) {
						
						if (!buttons.size)
							buttons.size = 'default';
						
						if (buttons.cancel) {
							var $cancel = $('<button>').attr({ 'id': 'modal_cancel', 'class': 'btn btn-' + buttons.size})
													   .on('click', function() { $modal.modal('hide'); })
													   .html(buttons.cancel)
													   .appendTo('.modal-footer');
							if (!buttons.confirm)
								$cancel.addClass('btn-primary');
						}
						
						if (buttons.confirm.name) {
							var $confirm = (settings.submit) ? $('<submit>') : $('<button>');
							$confirm.attr({ 'id': 'modal_confirm', 'class': 'btn btn-' + buttons.size})
										 .on('click', buttons.confirm.action || buttons.action || settings.action)
										 .addClass('btn-primary')
										 .html(buttons.confirm.name)
										 .appendTo('.modal-footer');
						}
					}

					$modal.modal('show');
				},
				
				/********************************
				 * Simple One button modal prompt
				 ********************************/
				alert: function(params) {
					
						var $modal = $('#modal');
						var defaults = {
							 'title'	:'Modal title',
							 'content'	: '',
							 'size'		: 'modal-sm',
							 'button'	: 'ok',
							 'options'  : false,
							 'callback'	: function() {}
						};
						
						var settings = $.extend({}, defaults, params);
						
						$('.modal-dialog').attr('class', 'modal-dialog ' + settings.size);
						$('.modal-header .close').hide(); // hide the 
						$modal.modal(settings.options);
						
						// Clear existing content
						$('.modal-title').empty().append(settings.title);
						$('.modal-body').empty().append(settings.content);				
						$('.modal-footer').addClass('prompt').empty();
						
						 // Create 'OK' button
						$('<button>').attr({ 'id': 'modal_confirm', 'class': 'btn btn-default'})
									 .on('click', function(){  
										 $.when(settings.callback())
										  .then(function() {
											  		$modal.modal('hide'); 
											  	}); 
									  })
									 .addClass('btn-primary')
									 .html(settings.button)
									 .appendTo('.modal-footer');
						
						
						$modal.modal('show');	
				},
				
				// ajax get for content
				get: function(url, params, options) {
						$.get(url, params, function(response) {
							Modal.self.modal(options);
							ajaxResponse(response);
							$('#modal').modal('show');
						});
					
					},
				
				// ajax post for content
				post: function(url, params, options) {
						$.post(url, params, function(response) {
							Modal.self.modal(options);
							ajaxResponse(response);
							$('#modal').modal('show');
						});
						
					}
		
		
			};



function ajaxResponse(response) {
	var modalContent = $.parseJSON(response);
	
	if (modalContent.title)
		$('.modal-title').append(modalContent.title);
	if (modalContent.body)
		$('.modal-body').append(modalContent.body);
	if (modalContent.footer)
		$('.modal-body').append(modalContent.footer);

}
