
$(function(){
	$(".photo, .photo-s").click(function(){
		$(".video-play").stop().fadeIn(200);
	});
	$(".btn-close-video").click(function(){
		$(".video-play").stop().fadeOut(200);
	});
});






