$(function(){

	$('#btn1').click(function(){ 
		$('html,body').animate({scrollTop:$('#location1').offset().top}, 500);
	});
	
	$('#btn2').click(function(){ 
		$('html,body').animate({scrollTop:$('#location2').offset().top}, 500);
	});
	
	$('#btn3').click(function(){ 
		$('html,body').animate({scrollTop:$('#location3').offset().top}, 500);
	});
	
	$('#btn4').click(function(){ 
		$('html,body').animate({scrollTop:$('#location4').offset().top}, 500);
	});
	
	$('#btn5').click(function(){ 
		$('html,body').animate({scrollTop:$('#location5').offset().top}, 500);
	});
	
	$('#btn6').click(function(){ 
		$('html,body').animate({scrollTop:$('#location6').offset().top}, 500);
	});
	
	$('.quick-tag ul li').eq(0).find('a').click(function(){ 
		$('html,body').animate({scrollTop:$('#exexam-table-1').offset().top}, 500);
	});
	$('.quick-tag ul li').eq(1).find('a').click(function(){ 
		$('html,body').animate({scrollTop:$('#exexam-table-2').offset().top}, 500);
	});
	$('.quick-tag ul li').eq(2).find('a').click(function(){ 
		$('html,body').animate({scrollTop:$('#exexam-table-3').offset().top}, 500);
	});
	$('.quick-tag ul li').eq(3).find('a').click(function(){ 
		$('html,body').animate({scrollTop:$('#exexam-table-4').offset().top}, 500);
	});
	$('.quick-tag ul li').eq(4).find('a').click(function(){ 
		$('html,body').animate({scrollTop:$('#exexam-table-5').offset().top}, 500);
	});
});



