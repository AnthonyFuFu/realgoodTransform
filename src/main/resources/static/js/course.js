
/* 20230805 start */
$(function() {
	
	
// 購物車顯示﻿
$(".btn-select-confirm").on( "click", function() {

	
	var selectNum = $($(".selection-info").find("dd")[0]).text()

	var numA = $('input[name=courseA]:checked').length; 
        var numI = $('input[name=courseI]:checked').length;
        var totalSelected = numA + numI;
        var unSelectNum = selectNum - totalSelected;
//	console.log(totalSelected == selectNum);
//	console.log(selectNum);
//	console.log(totalSelected);
	if( totalSelected == selectNum){

		$("#shoppingCart").attr("class", "drag-window");
		$(".shop-count").attr("class", "shop-count");
		$(".shop-course-title").attr("class", "shop-course-title");
		$($(".btn-close-window")[1]).parents(".window-mask-black").addClass("hide")
//		alert(11);
		document.getElementById("shopContext").scrollIntoView({
		  behavior: "smooth",
		  block: "end",
		  inline: "nearest",
		});
	}else{
		alert('您選取的科數為' + selectNum + '科，尚可選擇' + unSelectNum + '科課程。');
	}	
		    	            
})
	
	
	
	
   // 購物車刪除按鈕
$(".btn-gray-default").on( "click", function() {

//	console.log("clear");
//	$(".shop-courseList").attr("class", "shop-courseList hide");
//	$(".drag-window").attr("class", "drag-window hide");
	$("#shoppingCart").attr("class", "drag-window hide");
	$(".shop-count").attr("class", "shop-count hide");
	$(".shop-course-title").attr("class", "shop-course-title hide");
	$('input[name=courseA]').attr('disabled', false);
    	$('input[name=courseI]').attr('disabled', false);
    	$('input[name=courseA]').prop('checked', false);
    	$('input[name=courseI]').prop('checked', false);	
    	var numA = $('input[name=courseA]:checked').length; 
            var numI = $('input[name=courseI]:checked').length;
            var totalSelected = numA + numI;
            $('#selectedCourses').text(totalSelected);
})

// 視聽課程
$(".preview-item").on("click", function() { 
      // 使用 jQuery 的 data() 方法來獲取對應的 data-index 值 
      var dataIndex = $(this).data("index"); 
//      console.log("data-index:", dataIndex); 
	showContentPreviewDetail(dataIndex); 
    }); 
function showContentPreviewDetail(index) { 
    // 先隱藏所有的 content-preview-detail 
    $(".content-preview-detail").addClass("hide"); 
    $(".content-preview").addClass("slideOut");	 
    // 顯示對應的 content-preview-detail 
    $($(".content-preview-detail")[index-1]).removeClass("hide"); 
  } 
$(".btn-return").on("click", function() { 
      $(".content-preview-detail").addClass("hide"); 
    $(".content-preview").removeClass("slideOut");	 
    });





// 	提醒禁止跨頁籤勾選
	$("#detailList_A").on( "click", function() {
		
// 		console.log("detailList_A");
	
		var inputListI = $("#detailList_I").find("input");
		for(var i = 0; i < inputListI.length; i++){
			if($(inputListI[i]).prop('checked')){
				alert("數位及雲端課程無法混合選課，請取消已勾選之雲端課程，再做選課。")
				break;
			}
			
		}
	})

	$("#detailList_I").on( "click", function() {
		
// 		console.log("detailList_I");
	
		var inputListA = $("#detailList_A").find("input");
		for(var i = 0; i < inputListA.length; i++){
			if($(inputListA[i]).prop('checked')){
				alert("數位及雲端課程無法混合選課，請取消已勾選之數位課程，再做選課。")
				break;
				
				
				
				
			}
			
		}
	})
	



// 控制選課明細頁籤
	var btnList = $(".course-mode-button-list").children();

	for(var i = 0; i < btnList.length; i++){
		$(btnList[i]).on( "click", function() {
	  	
		$(btnList[0]).attr("class", "");
		$(btnList[1]).attr("class", "");
		$(this).attr("class", "active");
		
// 		console.log($(this).html());
		
		if($(this).html() != '數位'){
			$("#detailList_A").hide();
			$("#detailList_I").show();
			
		}else{
			$("#detailList_A").show();
			$("#detailList_I").hide();
		}
		
	} )
	}




	


// 綁定事件處理程序，每當勾選的checkbox改變時，更新顯示的數字
$('input[name=courseA], input[name=courseI]').on('change', function() {
  var numA = $('input[name=courseA]:checked').length; 
  var numI = $('input[name=courseI]:checked').length;
  var totalSelected = numA + numI;
  $('#selectedCourses').text(totalSelected);
});
	 
	  
// 清除按鈕
    $(".btn-clear").on( "click", function() {
//    	console.log("btn-clear");
    	$('input[name=courseA]').attr('disabled', false);
    	$('input[name=courseI]').attr('disabled', false);
    	$('input[name=courseA]').prop('checked', false);
    	$('input[name=courseI]').prop('checked', false);	
    	var numA = $('input[name=courseA]:checked').length; 
            var numI = $('input[name=courseI]:checked').length;
            var totalSelected = numA + numI;
            $('#selectedCourses').text(totalSelected);
    })


// 	+-號轉換
	var buttonList = $(".courseDetailList").find("button");

	for(var i = 0; i < buttonList.length; i++){


		$(buttonList[i]).on( "click", function() {
// 	  		console.log( "Handler for `click` called.");
			var dataTrList = $(this).parent().parent().parent().nextAll()
			if($(this).find("i").attr("class") == 'fas fa-minus'){
// 				console.log("+++++++++++");fas fa-plus fas fa-minus
				$(this).find("i").attr("class", "fas fa-plus");
// 				console.log($(dataTrList[0]).attr("class"));
				for(var j = 0; j < dataTrList.length; j++){
					if($(dataTrList[j]).attr("class") != undefined){
					$(dataTrList[j]).attr("class", "hide");
					}else{
						break;
					}
					
				}
			}else{
// 				console.log("-----------");
				$(this).find("i").attr("class", "fas fa-minus");
// 				console.log(dataTrList);
// 				console.log($(dataTrList[2]).attr("class") == undefined);
			
				for(var j = 0; j < dataTrList.length; j++){
					if($(dataTrList[j]).attr("class") != undefined){
					$(dataTrList[j]).attr("class", "show");
					}else{
						break;
					}
					
				}
			}


			
		} );
		
	}

	

// 立即購客燈箱開啟
	$(".btn-shop").on( "click", function() {
		$($(".window-mask-black.hide")[1]).attr('class', 'window-mask-black show');
	} );
	
	$("#ASelectCourse").click(function() {
		$($(".window-mask-black.hide")[1]).attr('class', 'window-mask-black show');
	})
	
// 	課程試聽燈箱開啟
	$(".btn-play").on( "click", function() {
		$($(".window-mask-black.hide")[2]).attr('class', 'window-mask-black show');
		
		var courseUrl = $($(".chapter")[0]).attr("data-video-path");
		
		
//		alert(courseUrl);
		
		$($(".video-obj")[1]).attr("src", courseUrl);

		
	} );



	
	$("#id_course").val();
	
	if($("#id_selection").val() == '88888' && $("#id_course").val() == ''){
		$(".tab-shop-content-pre").show();
	}else if($("#id_selection").val() == '88888' && $("#id_course").val() != ''){
		$(".tab-shop-content").show();
	} 
});
/* 20230805 end */