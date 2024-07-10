$(function(){
	 var i = 0; 
	 $(".member, .trainee").on("click", function() {
	  if( i == 0 ){
	   $(this).addClass("memberClick");
	   i = 1;
	  }else{
	   $(".member, .trainee").removeClass("memberClick");
	   i = 0;
	  }	
	 });
});