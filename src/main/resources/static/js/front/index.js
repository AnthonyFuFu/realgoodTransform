	$(function(){
		if($.cookie("name")==null && $.cookie("count")==null){

			var _hostname=window.location.hostname; //取得網址
			$.cookie("name", _hostname, { expires: 7 ,path:'/'}); //cookie紀錄一天"path:/"為整個網站有效
			$.cookie("count", 1, { expires: 7 ,path:'/' });//cookie紀錄一天"path:/"為整個網站有效
			$(".logo_area").attr("style","display:block");
		}else{
			//$(".logo_area").attr("style","display:none");
			$(".logo_area").hide();
		}
		if($("#changeAreaId").val() != 0) {
			$("#changeArea").val('[[${area.id}]]');
		}

		//訊息公告提醒輪播
//		$('.marquee').each(function(i){
//			$('.marquee_'+i).on('click', function(){
//				if($("#marquee_link_"+i).val() != "" && $("#marquee_link_"+i).val() != null ){
//					window.open($("#marquee_link_"+i).val());
//				}
//				else{
//					layer.alert($("#marquee_content_"+i).val(), {
//					  skin: 'layui-layer-molv' //樣式類別
//					  ,closeBtn: 1
//					  ,title: [$("#marquee_title_"+i).val()]
//					  ,btn: ['確定']
//					  ,shift: 5 //動畫類型
//					});
//				}

//			});
//		})
//		,
		checkLogin = function() {

			if($("#login_id").val() == "") {
				alert("請輸入帳號！");
				return false;
			}

			if($("#secret").val() == "") {
				alert("請輸入密碼！");
				return false;
			}

			$("#loginForm").attr("action", "/doLogin");

			$("#loginForm").submit();

		},

		checkLogout = function() {

			$("#logoutForm").attr("action", "/doLogout");
			$("#logoutForm").submit();
		},

		changeArea = function(id) {
//			if(id == "ALL") {
//				$("#area_id").val("0");
//			} else {
//				$("#area_id").val(id);
//			}
//
//			$.ajax({
//				url: "schoolBulletinList",
//				cache: false,
//				async: false,
//				dataType: "json",
//				type:"POST",
//				data:{
//					id : $("#area_id").val()
//				},
//				error: function(xhr) {
//					alert('Ajax request 發生錯誤');
//				},
//				success: function(data) {
//					var str = "";
//					$("#winner_content").empty();
//					if(data != null && data.length > 0){
//						for(i = 0 ; i < data.length; i++){
//							var area = data[i];
//							str += "<dl>";
//							str += "<dt>";
//							str += "<div class='type_widen'>"+area.area_name.substring(0,2)+"</div>";
//							if(area.category_name == "系統公告"){
//							   str +=  "<a style='color:red;' href='/schoolBulletin/inside?str="+area.encrypturl+"' )'>"+area.title+"</a>";
//							}else{
//							   str +=  "<a href='/schoolBulletin/inside?str="+area.encrypturl+"' '>"+area.title+"</a>";
//							}
//							str +=  "</dt>";
//							str +=  "<dd>"+area.begin_date.substring(5, 7)+"/"+area.begin_date.substring(8, 10)+"</dd>";
//							str += "</dl>";
//						}
//						$("#winner_content").append(str);
//					}
//
//				}
//			});

		},
		toInside = function(id,action_name) {

			$.ajax({
				url: "$/"+action_name+"/toEncrypt",
				cache: false,
				async: false,
				dataType: "text",
				type:"POST",
				data:{
					id : id
				},
				error: function(xhr) {
					alert('Ajax request 發生錯誤');
				},
				success: function(data) {
					$("#str").val(data);
					$("#getForm").attr("action", "/"+action_name+"/inside");
					$("#getForm").submit();
				}
			});

		},

		banner_toInside = function(id) {

			$.ajax({
				url: "/banner_updateClickRate",
				cache: false,
				async: false,
				type:"POST",
				data:{
					id : id
				},
				error: function(xhr) {
					alert('Ajax request 發生錯誤');
				},
				success: function(data) {

				}
			});

		},
		marquee_toInside = function(id) {

			$.ajax({
				url: "/marquee_updateClickRate",
				cache: false,
				async: false,
				type:"POST",
				data:{
					id : id
				},
				error: function(xhr) {
					alert('Ajax request 發生錯誤');
				},
				success: function(data) {

				}
			});


		},


		changeArea($("#changeArea").val());
		$(".logo_area").hide();
	});
		//首頁諮詢表
function inputForm() {
	var phonecheck = /^[09]{2}[0-9]{8}$/;//檢查手機正則表達式
	//		var emailcheck = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;//檢查信箱正則表達式
	//		var emailendcheck = /\.$/;//信箱結尾不可為.
	if ($("#name").val() == "") {
		alert("請輸入真實姓名");
		return false;
	} else if ($("#cellphone").val() == "") {
		alert("請輸入行動電話");
		return false;
	} else if (!phonecheck.test($("#cellphone").val())) {
		alert("請輸入正確的行動電話");
		return false;
	} else if ($("#email").val() == "") {
		alert("請輸入電子郵件");
		return false;
	}
	else if (!isEmail($("#email").val())) {
		alert("不是正確的信箱格式");
		return false;
	}
	else if ($("#toArea").val() == 0 || $("#toArea").val() == "") {
		alert("請選擇參加地點");
		return false;
	} else if (!$("#is_read").prop("checked")) {
		alert("請勾選已詳細閱讀及了解本站之個資法相關規範");
		return false;
	} else {
		$(".consultation_send a").attr("disabled", true);
		$(".consultation_send a").css("pointer-events", "none");
		
		var json = {};
		var childname = $("#name").val();
		var phone = $("#cellphone").val();
		var email = $("#email").val();
		var time = new Date();
		var create_time = DateFormat(time, 'yyyy-MM-dd HH:mm:ss');
		var webLocationId = $("#toArea :selected").val();
		var remark = "參加地點 : " + $("#toArea :selected").text() +" 來源 : 甄戰首頁諮詢表" ;
		var loginEquipment = $("input[name='loginEquipment']").val();
		
		json.NAME = childname;
		json.PHONE = phone;
		json.E_MAIL = email;
		json.CREATE_DATE = create_time;
		json.MEMO = remark;
		json.PRINT_ID = "3325";
		json.LOGIN_EQUIPMENT = loginEquipment;
		json.IS_READ = '1';
		json.IS_DEL = '0';
		json.ANATOMIC = 'R';
		json.WEB_LOCATION_ID = webLocationId;
		json.TYPE_ID = "0";
		json.TYPE_CODE = "INDEX";
		json.IS_IMPORT = '0';
		json.IS_POST_EC = '0';
		
		var data = JSON.stringify(json);
		var data2 = DES_Encrypt(data);
		
		
		console.log("data2" + data);
		
		$.ajax({
			url : "https://www.tkbtv.com.tw/api/formList/import",
			cache : false,
			async : true,
			dataType : "text",
			type : "POST",
			data : data2,
			error: function(xhr) {
				alert('送出諮詢表資料失敗');
				reSetForm();
			},
			success: function(data) {
				window.location.assign("/common/sendFormSuccess.html");
			}
		});
	}
}

function DateFormat(date, format) {
	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	const day = date.getDate();
	const hour = date.getHours();
	const minute = date.getMinutes();
	const second = date.getSeconds();
	const map = {
		'yyyy': year,
		'MM': String(month).padStart(2, '0'),
		'dd': String(day).padStart(2, '0'),
		'HH': String(hour).padStart(2, '0'),
		'mm': String(minute).padStart(2, '0'),
		'ss': String(second).padStart(2, '0')
	};
	return format.replace(/yyyy|MM|dd|HH|mm|ss/g, match => map[match]);
}


	function reSetForm(){
		$("#name").val("");
		$("#cellphone").val("");
		$("#email").val("");
		$("#toArea").val("");
		$("#is_read").prop("checked",false);
	}
	//RFC822 正規則
	function isEmail(email){

		return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test( email );
	}

	function changeIndexVideo(id,category_id){
		$.ajax({
			url: "changeIndexVideoData",
			cache: false,
			async: false,
			dataType: 'json',
			type: "POST",
			data:{
				id : id
			},
			error: function(xhr) {
				alert('Ajax request 發生錯誤:changeIndexVideoData()');
			},
			success: function(data) {
								console.log(data)

				$("#mainVideo").empty();
				var str = "";
				var nameFix="";
				if(data != null && data.length > 0){
					var winner = data[0];
					if(winner.name.length==2){
						nameFix+=winner.name[0]+"○"
					}else if(winner.name.length==3){
						nameFix+=winner.name[0]+"○"+winner.name[2]
					}else if(winner.name.length==4){
						nameFix+=winner.name[0]+"○○"+winner.name[3]
					}
					str += "<aside><img src='/images/front/logo.png'></aside>";
					str += "<div class='video'><div class='video-frame'>";
					str += "<iframe width='100%' height='100%' src='" + winner.video + "' frameborder='0' allow='autoplay' allow='encrypted-media' allowfullscreen></iframe>";
					str += "</div></div>";
					str += "<article><div class='left'>";
					str += "<figcaption>" + winner.year + "年考取" + winner.admitted + "<font>"+nameFix+"</font></figcaption>";
					str += "<figure class='video-txt'>" + winner.summary + "</figure>";
					str += "<a class='btn-more' href='/winner/inside?str=" + winner.encrypturl + "'>More &gt;</a></div>";
					str += "<div class='right'><dl>";
					str += "<dt>" + winner.click_rate + "</dt><dd>觀看次數</dd>";
					str += "</dl></div></article>";

				}
				console.log("str:"+str)
				$("#mainVideo").append(str);
			}
		});

		$.ajax({
			url: "changeIndexVideoList",
			cache: false,
			async: false,
			dataType: 'json',
			type: "POST",
			data:{
				id : id,
				category_id : category_id
			},
			error: function(xhr) {
				alert('Ajax request 發生錯誤');
			},
			success: function(data) {
				$("#recommendVideo").empty();
				var str = "";
				for(var i = 0 ; i < data.length ; i++){
					if(data != null && data.length > 0){
						var winner = data[i];
						str += "<div class='photo'><a href='/winner/inside?str=" + winner.encrypturl + "'>";
						str += "<div class='info'>"
						str += "<div class='photo-title'>" + winner.year + "年" + winner.admitted +"</div></div>";
						str += "<img src='" + winner.photo + "'></a></div>";
					}
				}
				$("#recommendVideo").append(str);
				$(".BK-Mask").attr("style","display:block");
			}
		});
	}
	
	function backIndexVideo(){
		$("#mainVideo").empty();
		$("#recommendVideo").empty();
		$(".BK-Mask").attr("style","display:none");

	}
