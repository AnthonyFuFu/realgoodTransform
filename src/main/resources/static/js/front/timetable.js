$(function(){
	$(".timetable_label:first").addClass("close").next("div").slideUp();
	$(".timetable_label:first").addClass("timetable_label_click");
	var i = 1;
			
	$(".timetable_label").on( "click", function() {
		let close = $(this).hasClass("close")
		if( i == 0 || !close){
			$(this).addClass("close").next("div").stop().slideUp();
			$(this).addClass("timetable_label_click");
			i = 1;
		} else {
			$(this).removeClass("close").next("div").slideDown();
			$(this).removeClass("timetable_label_click");
			i = 0;
		}
	});

});
