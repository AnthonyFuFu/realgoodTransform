$(function(){
	
	/*
	var i = 0;

	$(".footer_label").on( "click", function() {
		if( i == 0 ){
			$(".link").stop().slideDown();
			$(this).addClass("footer_label_click");
			i = 1;
		} else {
			$(".link").stop().slideUp();
			$(this).removeClass("footer_label_click");
			i = 0;
		}
	});
	*/
	
	var x;        
	var mobile = false;
	
	 checkSize();

	$(window).resize(function(){
		checkSize();
		});

	 $(".link aside").click(function(){	
		 if(!mobile)
		 	return;
			
		 $(".link ul").slideUp();	
		 if( $(this).next().is(":hidden") ){
			 $(".link aside").next().slideUp();
			 $(this).next().slideDown();	
		 };	
	 })
	 
	 function checkSize(){
		 
		 x=$(window).width();
		  if (x<640){//手機版寬度  
			 mobile = true;
		   }else{      
			 mobile = false;	
			 $(".link ul").show();
		  } 
	}

});

	 

	
	
	