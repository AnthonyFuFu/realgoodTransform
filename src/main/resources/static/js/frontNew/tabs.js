$(function(){
	
	$(".tabs li").eq(0).css({
		'background':'#FFF',
		'border-bottom':'1px solid #FFF',
		'box-shadow':'none'
	});
	
	$(".tab_container").hide().eq(0).show();
	
	$(".tabs li").click(function(){
		$(this).css({
			'background':'#FFF',
			'border-bottom':'1px solid #FFF',
			'box-shadow':'none'
		}).siblings().css({
			'background':'#EFEFEF',
			'border-bottom':'1px solid #CCC',
			'box-shadow':'0 -2px 4px rgba(200, 200, 200, 0.5) inset'
		});
		$(".tab_container").hide();
		$(".tab_container").eq($(this).index()).show();
    });
	

	//20180104 start
	$(".winner_nav li").click(function(){
		$(this).siblings().removeClass();
		$(this).addClass('selected');
		$(this).parent().next().find(".winner_content").eq($(this).index()).fadeIn(700).siblings().fadeOut(700);
    });
});