$(function(){
	$(".ad img, .bulletin, .bulletin_img, .bulletin_ad_img, .winner_photo, .offer_group div, .examNews_group img, .essay_photo, .examInfo").on( "mouseover", function() {
		$(this).stop().animate({opacity: 0.7});
	});
	
	$(".ad img, .bulletin, .bulletin_img, .bulletin_ad_img, .winner_photo, .offer_group div, .examNews_group img, .essay_photo, .examInfo").on( "mouseout", function() {
		$(this).stop().animate({opacity: 1});
	});

});