$(function(){
	
	$(".sort li").on( "click", function(){
		
		$(this).attr("class", "sort_click").siblings().removeClass("sort_click");
				
	});
	
	var allShow = false;
	$("#sort_region_menu li").on( "click", function(){
		
		//點按或切換menu時，下方子項目點選狀態移除。
		$(".sort_region li").removeClass("sort_region_click");
		
		$(this).attr("class", "sort_click").siblings().removeClass("sort_click");
		

		if($(".sort_region ul").eq($(this).index() - 1).is(':visible') && $(this).index()>0){
			$(".sort_region ul").slideUp(200);
			
			if(allShow && $(this).index() != 0){
				allShow = false;
				$(".sort_region ul").eq($(this).index() - 1).slideDown(200).siblings().slideUp(200);
			}
			
		}else{
			if(allShow){
				allShow = false;
				$(".sort_region ul").slideUp(200);
				return;	
			}
			
			if($(this).index() == 0){
				allShow = true;
				$(".sort_region ul").slideDown(200);
				return;	
			}
			
			allShow = false;
			$(".sort_region ul").eq($(this).index() - 1).slideDown(200).siblings().slideUp(200);
		}

	
	});
	
	var _obj;
	$(".sort_region li").on( "click", function(){
		if(_obj)
			_obj.removeClass("sort_region_click");
		
		_obj = $(this);
		$(this).attr("class", "sort_region_click").siblings().removeClass("sort_region_click");
				
	});
	
});