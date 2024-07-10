$(document).ready(function() {

	$(".sidebar-section-title").click(function() {
		$(this).next(".sidebar-section-content").toggle(300,"swing");
	});
	$(".control-open").click(function() {
		$(".sidebar-section-content").slideDown(300,"swing");
	});
	$(".control-close").click(function() {
		$(".sidebar-section-content").slideUp(300,"swing");
	});
	$(".dropDown-title").click(function() {
		$(this).next().slideToggle(300,"swing");
	});

	$('body').click(function(evt) {
		if(evt.target.id != "dropDown-title" && 
			evt.target.id != "dropDown-content" && 
			evt.target.name != "checkbox" && 
			evt.target.id != "fa-down") {
			$('.dropDown-content').slideUp(300);
		}
	});


	//go top------------------------------
	$('.gotop').click(function(event) {
		event.preventDefault();
		$('html, body').animate({scrollTop: 0}, 500);
	});
	//showbox-------------------------------------------------------
	$(".actionbutton").click(function() {
		$("#actionfunction").css({"top":"40%", "left":"50%","margin-top":"-200px","margin-left":"-240px"});
		$("#actionfunction").show();
	});	
	
	$(".fancybutton").click(function() {
		$("#fancyfunction").css({"top":"40%", "left":"50%","margin-top":"-200px","margin-left":"-240px"});
		$("#fancyfunction").show();
	});

	$(".closebtn").click(function() {
		$("#fancyfunction").hide();
	});
	$(".closeActionBtn").click(function() {
		$("#actionfunction").hide();
	});	
});