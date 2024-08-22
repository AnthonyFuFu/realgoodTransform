/**
 * 送單功能
 */
$(function() {
	toGetLocationList();
	//取得參加地點
	function toGetLocationList(){
		$.ajax({
			url: "https://www.reallygood.com.tw/edmPage/getWebLocationList",
			cache: false,
			async: false,
			dataType: "json",
			type:"POST",
			data:{

			},
			error: function(xhr) {
				alert('取得參加地點失敗1');
			},
			success: function(data) {
				if(data != null && data.length > 0){
					var str = "<option value='0'>請選擇</option>";
					$("#toArea").empty();
					for(i = 0 ; i < data.length; i++){
						var area = data[i];
						str += "<option value='"+area.id+"'>"+area.location+"</option>";
					}
					$("#toArea").append(str);
				}else{
					alert('取得參加地點失敗2');
				}
			}
		});
	}
	
	toReceive = function(){
		var checkIp_count = "";
//		$.ajax({
//			url : "https://www.reallygood.com.tw/edmPage/checkIpReceive",
//			cache: false,
//			async: false,
//			type : "POST",
//			dataType : 'text',
//			data : {
//			},
//			success : function(msg) {
//				if(msg == "false") {
//					checkIp_count = "F";
//				}
//			},
//			error : function(xhr, ajaxOptions, thrownError) {
//				alert("錯誤");
//			}
//		});
		
		var phonecheck = /^[09]{2}[0-9]{8}$/;//檢查手機正則表達式
		var printcheck = /^[0-9]*$/;//檢查print_id正則表達式
		
		if($("input[name='print_id']").val() == null || $("input[name='print_id']").val() == ""){
			$("input[name='print_id']").val(0);
		}
		
		if(!printcheck.test($("input[name='print_id']").val())){
			$("input[name='print_id']").val(0);
		}
		
		if($("#name").val() == ""){
			alert("請輸入真實姓名");
			return false;
		}else if($("#cellphone").val() == ""){
			alert("請輸入行動電話");
			return false;
		}else if(!phonecheck.test($("#cellphone").val())){
			alert("請輸入正確的行動電話");
			return false;
		}else if($("#email").val() == ""){
			alert("請輸入電子郵件");
			return false;
		}else if(!isEmail($("#email").val())){
			alert("不是正確的信箱格式");
			return false;
		}
		else if($("#toArea").val() == 0 || $("#toArea").val() == ""){
			alert("請選擇諮詢地點");
			return false;
		}
	  	else if($("input[name='sale_agree']:checked").val() != 1 && $("input[name='sale_agree']:checked").val() != 0){
	  		alert("請勾選是否寄送電子報");
	  		return false;
	 	}
		else if(!$("#is_read").prop("checked")){
			alert("請勾選已詳細閱讀及了解本站之個資法相關規範");
			return false;
		}	
		else{
			$(".send a").attr("disabled",true);
			$(".send a").css("pointer-events","none");
				$.ajax({
					url: "https://www.reallygood.com.tw/edmPage/toReceiveInsideForm",
					cache: false,
					async: true,
					dataType: "text",
					type:"POST",
					data:{
						id:"0",
						title : $("input[name='project_name']").val(),
						name : $("#name").val(),
						cellphone : $("#cellphone").val(),
						email : $("#email").val(),
						contact_time : $("#take").val(),
						memo : $("textarea[name='ps']").val(),
						edm_type_id : "0",
						type_code : "LECTURE",
						login_equipment : $("input[name='login_equipment']").val(),
						sale_agree : $("input[name='sale_agree']:checked").val(),
						web_location : $("#toArea").val(),
						keyword : $("#keyword").val(),
						network : $("#network").val(),
						device : $("#device").val(),
						campaignid : $("#campaignid").val(),
						is_google_ad : $("#is_google_ad").val(),
					},
				error: function(xhr) {
					alert('資料送出失敗');
				//	reSetData();
				},
				success: function(data) {
					if(data=="T"){
						window.location.href = '${BASE_PATH}EDM/load/sendFormSuccess.jsp';
//						alert("送出成功");
					}else{
						alert("送出失敗");
						reSetData();
					}
					if(data=="N")
						{
							alert("驗證錯誤");
						}
//					reSetData();
				}
			});
			
		}
			
	}

});
//重新整理此頁面
function reSetData(){
	var toUrl = location.href;
	document.location.href = toUrl;
}
//RFC822 正規則
function isEmail(email){

	return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test( email );	
}
