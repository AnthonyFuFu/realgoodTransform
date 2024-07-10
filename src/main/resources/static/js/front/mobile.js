$(function(){		
	// mobile_nav
	var Now = 0;
	$(".mobile_nav_icon").on("click", function(){
		if( Now == 0 ){
			$(".wrapper").animate({"margin-left": "-=156"},400);
			$(".mobile_nav").css("display","block").animate({"right": "0"},400);
			Now = 1;
		} else{
			$(".wrapper").animate({"margin-left": "0"},400);
			$(".mobile_nav").animate({"right": "-156"},400, function(){$(this).css("display","none")});
			Now = 0;
		};
	});

});