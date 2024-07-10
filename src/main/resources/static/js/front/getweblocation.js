/**
 * 
 */
var toUrl = location.href;
//var getUrl = window.location;
//var baseUrl = getUrl .protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];
var baseUrl = "https://www.daso.com.tw"; 
$(function(){

	toGetLocationList();
	
});
//重新整理此頁面
function reSetData(){
	document.location.href = toUrl;
}
//取得參加地點
function toGetLocationList(){
	var json = {};
	json.WEB = 'R';
	var data = JSON.stringify(json);
	var data2 = DES_Encrypt(data);
	
	$.ajax({
		url : "https://www.tkbtv.com.tw/api/webLocation/queryListByWeb",
		cache: false,
		async: true,
		dataType: "text",
		type:"POST",
		data : data2,
		error: function(xhr) {
			alert('取得參加地點失敗');
		},
		success : function(data) {
			if (data != null && data.length > 0) {
				var str = "<option value='0'>請選擇</option>";
				var result = JSON.parse(data);
				var resultArray = result.data.WEB_LOCATION_LIST;
				$("#toArea").empty();
				for(i = 0 ; i<resultArray.length ; i++){
					var location = resultArray[i];
					str += "<option value='"+location.WEB_LOCATION_ID+"'>"+location.LOCATION+"</option>";
				}
				$("#toArea").append(str);
			} else {
				alert('取得參加地點失敗');
			}
		}
	});
}