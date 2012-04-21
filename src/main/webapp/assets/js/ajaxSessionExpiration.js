function sessionTimeOutModal()
{
	$('#sessionTimeOutModal').modal('show');
	$('#sessionTimeOutModal').css('z-index',5000);
}

!function( $ )
{
	$.ajaxSetup({
		statusCode: 
		{
			901: sessionTimeOutModal
		}
	});
	
	$('.ajaxLink').mouseenter(function()
	{
		if($(this).data('popover') == null)
		{
			
			$(this).popover({
				animation: false,
				placement: 'bottom',
				trigger: 'manual',
				title: 'Ajax PopOver',
				html : true,
				template: $('#popoverTemplate').clone().attr('id','').html()
			});
		}
		$(this).popover('show');
		$.ajax({
			type: 'GET',
			url: "rest/check",
			dataType: "json",
	        success: function(user)
	        {
	        	$('.popover.in .popoverContent').empty().append('<br/>'+user.firstname+' '+user.lastname); 
	        },
	        error: function(jqXHR, textStatus, errorThrown){
	        	console.log(jqXHR);
	        	console.log(textStatus);
	        	console.log(errorThrown);
	        }
	    });
		
	});
	
	$('.ajaxLink').mouseleave(function()
	{
		$(this).popover('hide');
	});
	
	$('.redirectToLogin').click(function()
	{
		window.location.replace("./login");

	});
	
}(window.jQuery);