$(function(){
	
	if($("#type").val() == null || $("#type").val() == ""){
		var _showTab = 0;
	}else{
		var _showTab = $("#type").val() - 1;
	}
	
	$(".tabs li").eq(_showTab).css({
		'background':'#FFF',
		'border-bottom':'1px solid #FFF',
		'box-shadow':'none'
	});
	if($(".tab_container").length == 1){
		$(".tab_container").hide().eq(0).show();
		$(".tabContainer").hide().eq(0).show();
	}else{
		$(".tab_container").hide().eq(_showTab).show();
		$(".tabContainer").hide().eq(_showTab).show();
	}
	
	
	$(".tabs li").click(function(){
		$(this).css({
			'background':'#FFF',
			'border-bottom':'1px solid #FFF',
			'box-shadow':'none'
		}).siblings().css({
			'background':'#EFEFEF',
			'border-bottom':'1px solid #CCC',
			'box-shadow':'0 -2px 4px rgba(200, 200, 200, 0.5) inset'
		});
		$(".tab_container").hide();
		$(".tab_container").eq($(this).index()).show();
    });
	
	//20220223 start 新科系介紹頁籤使用
	$(".tabs-area .tabItem").eq(_showTab).addClass("active");
	if($(".tabContainer").length == 1){
		$(".tabContainer").hide().eq(0).show();
	}else{
		$(".tabContainer").hide().eq(_showTab).show();
	}
	
	$(".tabs-area .tabItem").click(function(){
		$(".tabContainer").hide();
		$(".tabContainer").eq($(this).index()).show();		
	})
	//20220223 end 新科系介紹頁籤使用
	
	//20180104 start
	$(".winner_nav li").click(function(){
		$(this).siblings().removeClass();
		$(this).addClass('selected');
		$(this).parent().next().find("[class^='winner_content']").eq($(this).index()).fadeIn(500).siblings().hide(); /*20210417 Linda*/
    });
	
//20211018 手機版頁籤功能優化
	function arrow(){
		let tabItemW = 0
		for(let i = 0; i < $(".tabItem").length; i++){
			tabW = $(".tabItem").eq(i).outerWidth() + 5
			tabItemW += tabW
		}
		if($(window).innerWidth() > 1180){
//			console.log($(window).innerWidth() > 1180)
			if(tabItemW < $(".slick-list").width()){
//				console.log("頁籤長度小於外框")
				$(".slick-list").css({"margin":"0","width":"100%"})
				$(".slick-arrow").css("display","none")
			}else if(tabItemW > $(".slick-list").width()){
				$(".slick-list").css({"margin":"auto","width":"93%"})
				$(".slick-arrow").css("display","block")
			}
		}else if($(window).innerWidth() > 768){
//			console.log("頁籤長度大於外框")
			if(tabItemW < $(".slick-list").width()){
//				console.log("頁籤長度小於外框")
				$(".slick-list").css({"margin":"0","width":"100%"})
				$(".slick-arrow").css("display","none")
			}else if(tabItemW > $(".slick-list").width()){
				$(".slick-list").css({"margin":"auto","width":"93%"})
				$(".slick-arrow").css("display","block")
			}
		}else if($(window).innerWidth() > 480){
			$(".slick-list").css({"margin":"auto","width":"88%"})
			$(".slick-arrow").css("display","block")
		}else if($(window).innerWidth() > 320){
			$(".slick-list").css({"margin":"auto","width":"84%"})
			$(".slick-arrow").css("display","block")
		}
	}
	if(!$(".slick-list").parent().hasClass('ttc-area')){
		//不含師資頁內容ttc-area就執行
		arrow();
	}
	function tabNow(){
		if($("#type").val() == null || $("#type").val() == ""){
			var _showTab = 0;
		}else{
			var _showTab = $("#type").val() - 1;
		}
		$(".slick-track .tabItem").eq(_showTab).css({
			'background':'#FFF',
			'border-bottom':'1px solid #FFF',
			'box-shadow':'none'
		});
		if($(".tab_container").length == 1){
			$(".tab_container").hide().eq(0).show();
		}else{
			$(".tab_container").hide().eq(_showTab).show();
		}
	}
	tabNow();
	$(".slick-track .tabItem").click(function(){
		$(this).css({
			'background':'#FFF',
			'border-bottom':'1px solid #FFF',
			'box-shadow':'none'
		}).siblings().css({
			'background':'#EFEFEF',
			'border-bottom':'1px solid #CCC',
			'box-shadow':'0 -2px 4px rgba(200, 200, 200, 0.5) inset'
		});
		$(".tab_container").hide();
		$(".tab_container").eq($(this).index()).show();
	});
	$(window).resize(function(){
		let tabItemW = 0
		for(let i = 0; i < $(".tabItem").length; i++){
			tabW = $(".tabItem").eq(i).outerWidth() + 5
			tabItemW += tabW
		}
		if(!$(".slick-list").parent().hasClass('ttc-area')){
			//不含師資頁內容ttc-area就執行
			arrow();
		}

		tabNow()
	})
});


