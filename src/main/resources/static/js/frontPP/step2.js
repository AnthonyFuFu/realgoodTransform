getArea();

if($("#background").val() == 01){
	$("#service").hide();
	$("#svService").show();
	$("#eduNoPart").hide();
	$("#svLocation").on('change', function() {
		getSvService();
	});
}else if($("#background").val() == 02){
	$("#service").show();
	$("#svService").hide();
	$("#eduNoPart").show();
	getServicePeople();
}else{
	$("#service").hide();
	$("#svService").hide();
	$("#eduNoPart").hide();
	$("#paidNamePart").hide();
	$("#calssPart").hide();
}

$("#birthDay").datepicker({
	dateFormat: 'yy-mm-dd',
	changeMonth: true, // 启用月份选择
    changeYear: true,
	yearRange: "1923:2123"
})

function back(){
	window.history.back();
}
function next(){
	let eduNo = $("#eduNo").val().trim();
	let service = $("#service").val();
	let paidName = $("#paidName").val().trim();
	let svArea = $("#svArea").val();
	let svLocation = $("#svLocation").val();
	let svService = $("#svService").val();
	
	//在職
	if($("#background").val() == 01){
		if(svArea == 0){
			alert("請選擇地區")
			return false;
		}
		if(svLocation == 0){
			alert("請選擇學堂據點")
			return false;
		}
		if(svService == 0){
			alert("請選擇服務人員")
			return false;
		}
	//在校
	}else if($("#background").val() == 02){
		if(service == 0){
			alert("請選擇服務人員")
			return false;
		}
		if(eduNo == ""){
			alert("學號不得為空")
			return false;
		}
		if(svArea == 0){
			alert("請選擇地區")
			return false;
		}
		if(svLocation == 0){
			alert("請選擇學堂據點")
			return false;
		}
	}else{
		alert("目前背景無資料，請與服務人員聯繫")
		return false;
	}
	
	$(".orderInfo").attr("action", "[[${@environment.getProperty('weburl')}]]/step/step3");
	$(".orderInfo").submit();
}

function confirm (){
	let element = document.getElementById("info");
	element.classList.replace("show", "hide");
}

function info (){
	let element = document.getElementById("info");
	element.classList.replace("hide", "show");
}

//在校 服務人員
function getServicePeople(){
	console.log($("#departmentNo").val())
	console.log($("#schoolNo").val())
	$.ajax({
		url : "[[${@environment.getProperty('weburl')}]]/step/getServicePeople",
		cache : false,
		async : false,
		type :"POST",
		data : {
			department :$("#departmentNo").val(),
			school :$("#schoolNo").val()
		},
		success : function(data){
			console.log(data)
			var str = "";
			$("#service").empty();
			str += "<option value='0'>服務人員</option>";
			str += "<option value='-1'>不指定</option>";
			for(i = 0 ; i < data.length; i++){
				str += "<option value='"+data[i].employee_code+"' data-employee='"+data[i].employee_code +"' >"+data[i].chinese_name+"</option>";
			}
			$("#service").append(str);
			
		},
		error : function(){
			alert("搜尋時發生錯誤");
		}
	})
}
//在職&在校 地區
function getArea(){
	$.ajax({
		url : "[[${@environment.getProperty('weburl')}]]/step/getArea",
		cache : false,
		async : false,
		type :"POST",
		data : {
			
		},
		success : function(data){
			console.log(data)
			var str = "";
			$("#svArea").empty();
			str += "<option value='0'>地區</option>";
			for(i = 1 ; i < data.length; i++){
				str += "<option value='"+data[i].id+"' data-area='"+data[i].id +"' >"+data[i].name+"</option>";
			}
			$("#svArea").append(str);
		},
		error : function(){
			alert("地區搜尋時發生錯誤");
		}
	})
}

$("#svArea").on('change', function() {
	getLocation();
});
//在職&在校 學堂據點
function getLocation(){
	$.ajax({
		url : "[[${@environment.getProperty('weburl')}]]/step/getLocation",
		cache : false,
		async : false,
		type :"POST",
		data : {
			area : $("#svArea").find(":selected").data("area")
		},
		success : function(data){
			console.log(data)
			var str = "";
			$("#svLocation").empty();
			str += "<option value='0'>學堂據點</option>";
			for(i = 0 ; i < data.length; i++){
				str += "<option value='"+data[i].academy_branch_no+"' data-location='"+data[i].id+"' >"+data[i].academy_name+"</option>";
			}
			$("#svLocation").append(str);
		},
		error : function(){
			alert("地區搜尋時發生錯誤");
		}
	})
}


//在職 服務人員
function getSvService(){
	console.log($("#svLocation").find(":selected").data("location"))
	$.ajax({
		url : "[[${@environment.getProperty('weburl')}]]/step/getAcademyServicePeople",
		cache : false,
		async : false,
		type :"POST",
		data : {
			location : $("#svLocation").find(":selected").data("location")
		},
		success : function(data){
			console.log(data)
			var str = "";
			$("#svService").empty();
			str += "<option value='0'>服務人員</option>";
			str += "<option value='-1'>不指定</option>";
			for(i = 0 ; i < data.length; i++){
				str += "<option value='"+data[i].employee_code+"' data-location='"+data[i].employee_code+"' >"+data[i].chinese_name+"</option>";
			}
			$("#svService").append(str);
		},
		error : function(){
			alert("地區搜尋時發生錯誤");
		}
	})
}







