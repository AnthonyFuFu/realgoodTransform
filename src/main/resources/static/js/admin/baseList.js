/**
 * List基本功能
 * @autohr Ken
 * @version 創建時間：2016-02-12
 */

$(function(){
	
	//全選
	var check = false;
	checkAll = function() {
		
		if(!check) {
			$(":checkbox[name='selectList']").each(function() {
				this.checked = true;
			});
			check = true;
		} else {
			$(":checkbox[name='selectList']").each(function() {
				this.checked = false;
			});
			check = false;
		}
		
	},
	
	//搜尋
	searchQuery = function() {
		check = true;
		if (check) {
			$("#mainForm").submit();
		}
		
	},
	
	//刪除
	deleteData = function(msg) {
		var checkArray = new Array();
		var checkList;
		$("input[name='selectList']:checked").each(function(i) {
			checkArray[i] = this.value;
		});
		checkList = checkArray.join(",");
		$("#deleteList").val(checkList);
		
//		console.log("deleteList:"+$("#deleteList").val());
		
		if($("input[name='selectList']:checked").length <= 0) {
			alert("請選擇要刪除的資料！");
			return false;
		}
		if(typeof(msg) == "undefined") {
			if(window.confirm("您真的確定要刪除嗎？")) {
				var frm = $("#mainForm").attr("action", "delete");
//				alert("刪除成功");
				frm.submit();
			}
		} else {
			if(window.confirm(msg)) {
				var frm = $("#mainForm").attr("action", "delete");
				frm.submit();
			}
		}
		
	},
	
	//修改排序
	updateSort = function(id) {
		
		$("#id").val(id);
		$("#sort").val($("#sort" + id).val());
		$("#mainForm").attr("action", "updateSort");
		$("#mainForm").submit();
		
	},
	
	//重新排序
	resetSort = function() {
		
		if(confirm("您是否要重新排序?")) {
			$("#mainForm").attr("action", "resetSort");
			$("#mainForm").submit();
		}
		
	},
	
	actionControl = function(action, link) {
		
		$("#action").val(action);
		$("#mainForm").attr("action", link);
		$("#mainForm").submit();
		
	},
	
	//不顯示
	updateUnShow = function(msg) {
		var checkArray = new Array();
		var checkList;
		$("input[name='selectList']:checked").each(function(i) {
			checkArray[i] = this.value;
		});
		checkList = checkArray.join(",");
		$("#updateList").val(checkList);
		
//		console.log("deleteList:"+$("#deleteList").val());
		
		if($("input[name='selectList']:checked").length <= 0) {
			alert("請選擇要不顯示的資料！");
			return false;
		}
		if(typeof(msg) == "undefined") {
			if(window.confirm("您真的確定要不顯示嗎？")) {
				var frm = $("#mainForm").attr("action", "updateUnShow");
//				alert("刪除成功");
				frm.submit();
			}
		} else {
			if(window.confirm(msg)) {
				var frm = $("#mainForm").attr("action", "updateUnShow");
				frm.submit();
			}
		}
		
	}
	
	//顯示
	updateShow = function(msg) {
		var checkArray = new Array();
		var checkList;
		$("input[name='selectList']:checked").each(function(i) {
			checkArray[i] = this.value;
		});
		checkList = checkArray.join(",");
		$("#updateList").val(checkList);
		
//		console.log("deleteList:"+$("#deleteList").val());
		
		if($("input[name='selectList']:checked").length <= 0) {
			alert("請選擇要顯示的資料！");
			return false;
		}
		if(typeof(msg) == "undefined") {
			if(window.confirm("您真的確定要顯示嗎？")) {
				var frm = $("#mainForm").attr("action", "updateShow");
//				alert("刪除成功");
				frm.submit();
			}
		} else {
			if(window.confirm(msg)) {
				var frm = $("#mainForm").attr("action", "updateShow");
				frm.submit();
			}
		}
		
	}
	
});