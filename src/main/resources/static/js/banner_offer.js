$(function(){
	var adWidth = 1180,
		animateSpeed = 500,
		timer,
		speed = 3000;

	function showNext(){
		var $li = $('.showbox_btn li');
		var no = $(".showbox_btn li.selected").index();
		
		no = (no + 1) % $li.length;
		
		$li.eq(no).mouseover();
		
		timer = setTimeout(showNext, speed);
	}

	$('.showbox_btn li').each(function(i){
		
		$(this).css('left', i * 20);
		
	}).hover(function(){
		
		var $this = $(this),
			no = $this.index();

		$('.showbox_btn li').removeClass('selected');
		$this.addClass('selected');
		
		$('.showbox').stop().animate({
			left: adWidth * no * -1
		}, animateSpeed);

		clearTimeout(timer);
		
	}, function(){
		
		timer = setTimeout(showNext, speed);
		
	}).focus(function(){$(this).blur();}).eq(0).addClass('selected');
	
	$('.showbox li').hover(function(){
		clearTimeout(timer);
	}, function(){
		timer = setTimeout(showNext, speed);
	});
	
	timer = setTimeout(showNext, speed);
});