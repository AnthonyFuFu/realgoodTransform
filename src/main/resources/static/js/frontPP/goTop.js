$(function(){
	$(".goTop").click(function(){
		$("html,body").animate({scrollTop:0},500);
		return false;
	});
	
    $(window).scroll(function() {
        if ( $(this).scrollTop() > 200){
            $('.goTop').fadeIn(500);
        } else {
            $('.goTop').stop().fadeOut(500);
        }
    });
});