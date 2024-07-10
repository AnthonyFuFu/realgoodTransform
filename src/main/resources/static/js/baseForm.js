/**
 * Form基本配置
 * @author Ken
 * 2016-04-18
 */

//驗證是否為空值
function checkNull(obj, init) {
	if ($.trim(obj.val()) == "" || $.trim(obj.val()) == init) {
		obj.focus();
		return true;
	} else {
		return false;
	}
}
//驗證電話格式
//function checkPhone(obj) {
//	if ($.trim(obj.val()) != "" && obj.val().match(/^[0][0-9][0-9][0-9]{6,8}$/) == null) {
//		obj.focus();
//		return true;
//	} else {
//		return false;
//	}
//}
//驗證手機格式
//function checkCellPhone(obj) {
//	if ($.trim(obj.val()) != "" && obj.val().match(/^[0][9][0-9]{8}$/) == null) {
//		obj.focus();
//		return true;
//	} else {
//		return false;
//	}
//}
//驗證email格式
//function checkEmail(obj) {
//	if ($.trim(obj.val()) != "" && obj.val().match( /^[^\s]+@[^\s]+\.[^\s]{2,3}$/) == null) {
//		obj.focus();
//		return true;
//	} else {
//		return false;
//	}
//}
//驗證身分證字號
//function checkID(obj) {
//	var idStr = $.trim(obj.val());
//	if (idStr != "") {
//		// 依照字母的編號排列，存入陣列備用。
//		var letters = new Array('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
//				'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'W',
//				'Z', 'I', 'O');
//		// 儲存各個乘數
//		var multiply = new Array(1, 9, 8, 7, 6, 5, 4, 3, 2, 1);
//		var nums = new Array(2);
//		var firstChar;
//		var firstNum;
//		var lastNum;
//		var total = 0;
//		// 撰寫「正規表達式」。第一個字為英文字母，
//		// 第二個字為1或2，後面跟著8個數字，不分大小寫。
//		var regExpID = /^[a-z](1|2)\d{8}$/i;
//		// 使用「正規表達式」檢驗格式
//		if (idStr.search(regExpID) == -1) {
//			// 基本格式錯誤
//			return true;
//		} else {
//			// 取出第一個字元和最後一個數字。
//			firstChar = idStr.charAt(0).toUpperCase();
//			lastNum = idStr.charAt(9);
//		}
//		// 找出第一個字母對應的數字，並轉換成兩位數數字。
//		for (var i = 0; i < 26; i++) {
//			if (firstChar == letters[i]) {
//				firstNum = i + 10;
//				nums[0] = Math.floor(firstNum / 10);
//				nums[1] = firstNum - (nums[0] * 10);
//				break;
//			}
//		}
//		// 執行加總計算
//		for (var i = 0; i < multiply.length; i++) {
//			if (i < 2) {
//				total += nums[i] * multiply[i];
//			} else {
//				total += parseInt(idStr.charAt(i - 1)) * multiply[i];
//			}
//		}
//		// 和最後一個數字比對
//		if ((10 - (total % 10)) != lastNum) {
//			return true;
//		}
//		return false;
//	}
//	return "";
//}
//驗證中英文字數長度
function checkByteLen(string, len) {
	var string_length = 0;
	for(var i=0; i<string.length; i++) {
		if(string[i].match(/[^\x00-\xff]/ig) != null) {
			string_length += 3;
		} else {
			string_length ++;
		}
	}
	if(string_length > parseInt(len)) {
		return true;
	} else {
		return false;
	}
}

//驗證連結網址是否有效
function checkURL(obj) {
	if ($.trim(obj.val()) != "" && obj.val().match(/http(s)?:////([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/
   ) == null) {
	obj.focus();
		return "\n請輸入正確連結網址，EX：http://www.google.com";
	} else {
		return "";
	}
}