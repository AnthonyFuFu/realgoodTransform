$(function(){
	var i = 1;
	if($.cookie("name")!=null && $.cookie("count")!=null){		
		i = 1;	
		$(".logo_list_label").addClass("logo_list_label_click");
	}
	
	$(".logo_list_label").on( "click", function() {
		if( i == 0 ){
			$(".logo_area").stop().slideUp();
			$(this).addClass("logo_list_label_click");
			i = 1;
		} else {
			$(".logo_area").stop().slideDown();
			$(this).removeClass("logo_list_label_click");
			i = 0;
		}
	});

	//logo滑入特效
	$('.logo_group').each(function(){
		$(this).hover(function(){
			$(this).children(".logo_intro").stop().animate({
				top: 0
			}, 400);
		}, function(){
			$(this).children(".logo_intro").stop().animate({
				top: 135
			}, 400);
		});
	});

	//logo滑入特效_主logo
	$('.logo_group_main').each(function(){
		$(this).hover(function(){
			$(this).children(".logo_intro_main").stop().animate({
				top: 0
			}, 500);
		}, function(){
			$(this).children(".logo_intro_main").stop().animate({
				top: 325
			}, 500);
		});
	});

	$(".logo_list_label").addClass("logo_list_label_click");
});
