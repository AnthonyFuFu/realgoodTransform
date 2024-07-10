$(function(){
	$(".submenu_btn").next().hide().eq(0).show();	
	$(".submenu_btn").click(function(){		
		if( $(this).next().is(":hidden") ){
		$(".submenu_btn").next().slideUp();
		$(this).next().slideDown();	
	};	
	})
});