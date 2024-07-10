$(function(){
	// 幫 div.qa_title 加上 hover 及 click 事件
	// 同時把兄弟元素 div.qa_content 隱藏起來
	$('#qaContent ul.accordionPart li div.qa_title').hover(function(){
		$(this).addClass('qa_title_on');
	}, function(){
		$(this).removeClass('qa_title_on');
	}).click(function(){
		//點選其他選項，其餘選項不收合
		// 當點到標題時，若答案是隱藏時則顯示它；反之則隱藏
		$(this).next('div.qa_content').slideToggle();
		
		
		// 當點到標題時，若答案是隱藏時則顯示它，同時隱藏其它已經展開的項目
		// 反之則隱藏
		var $qa_content = $(this).next('div.qa_content');
		if(!$qa_content.is(':visible')){
			$('#qaContent ul li div.qa_content:visible').slideUp();
		}
		/*$qa_content.slideToggle();*/	/*點選其他選項，其餘選項不收合-關閉*/
	}).siblings('div.qa_content').hide();

	// 第一個qa設定為顯示
	$('#qaContent ul li div.qa_content').eq(0).slideDown();

	// 全部展開
	$('#qaContent .qa_showall').click(function(){
		$('#qaContent ul.accordionPart li div.qa_content').slideDown();
		return false;
	});

	// 全部隱藏
	$('#qaContent .qa_hideall').click(function(){
		$('#qaContent ul.accordionPart li div.qa_content').slideUp();
		return false;
	});

	// 關閉
	$('#qaContent .qa_close').click(function(){
		$(this).parents('.qa_content').prev().click();
		return false;
	});
});