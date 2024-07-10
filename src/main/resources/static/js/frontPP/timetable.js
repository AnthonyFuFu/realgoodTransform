$(function(){

	// 第三個表格預設為隱藏
	$(".timetable_label").eq(2).addClass("timetable_label_click");
	$(".timetable_label").eq(2).next("div").hide();

	$(".timetable_label").click(function(){	
		var _table= $(this).next("div");					
		if($(_table).css("display")=="none"){
			$(_table).slideDown();
			$(this).removeClass("timetable_label_click");
		}else{
			$(_table).slideUp();
			$(this).addClass("timetable_label_click");
		}			
		return false;			
	});	
		
});