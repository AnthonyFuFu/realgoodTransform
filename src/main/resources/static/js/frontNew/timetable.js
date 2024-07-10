$(function(){
	
	var i = 0;
			
	$(".timetable_label").on( "click", function() {
		if( i == 0 ){
			$(this).next("div").stop().slideUp();
			$(this).addClass("timetable_label_click");
			i = 1;
		} else {
			$(this).next("div").slideDown();
			$(this).removeClass("timetable_label_click");
			i = 0;
		}
	});

});