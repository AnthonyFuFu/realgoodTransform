$(function(){
	
	//手機版選單
	$(".nav_rwd").on("click", function() {
		$(".nav").toggle(200);
		if($(".menu").is(':visible')){
		$(".menu div").hide();
		}	   
	});
	
	init();
	//20220223 科系介紹用 桌機版選單按鈕 re-nav a
	$(".nav li, .re-nav a").on("click", function() {
		var _id = parseInt($(this).index()) + 1;
		$(".menu_area").show();
			  
		if($(".menu" + "_0" + _id ).is(':visible')){
			$(".menu" + "_0" + _id ).slideUp();
		}else{
			init();
			$(".menu" + "_0" + _id ).slideDown();
		}	
	});
	function init(){
		$(".nav li, .re-nav a").each(function(){
			var _id = parseInt($(this).index()) + 1;
			$(".menu" + "_0" + _id).hide(); 
		});
	}
	$(".nav li a, .re-nav a").on('click', function(event) {
		$(".nav li a, .re-nav a").removeClass();
		$(this).addClass('active');
	});
});