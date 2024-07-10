/**
 * 設置區
 * 
 * 設置tinyMCE文字編輯器
 * 設置tinyMCE字數統計
 * 
 */
//設置tinyMCE(單一)
function tinymceSetup(textareaId) {
	tinymce.init({
		selector: "textarea#" + textareaId,	//選擇要套入的textarea
		mode: "textareas",
		theme:"modern",
		max_height: 200,
		min_height: 160,
		height : 300,
		language:"zh_TW",
		font_formats: 
		"新細明體=新細明體;"+
		"細明體=細明體;"+
        "標楷體=標楷體;"+
        "微軟正黑體=微軟正黑體;"+
        "Times New Roman=times new roman,times",
        fontsize_formats: "8pt 9pt 10pt 12pt 14pt 18pt 20pt 24pt 36pt",
	    plugins: [
					"advlist autolink link lists charmap preview hr ",
					"visualblocks visualchars code fullscreen insertdatetime nonbreaking",
					"table directionality textcolor paste"
		],
		toolbar: "insertfile undo redo | fontselect | fontsizeselect | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor",
		forced_root_block: '',	//將預設的p取代為''
		style_formats: [
		       {title: 'Bold text', inline: 'b'},
		       {title: 'Red text', inline: 'span', styles: {color: '#ff0000'}},
		       {title: 'Red header', block: 'h1', styles: {color: '#ff0000'}},
		       {title: 'Example 1', inline: 'span', classes: 'example1'},
		       {title: 'Example 2', inline: 'span', classes: 'example2'},
		       {title: 'Table styles'},
		       {title: 'Table row 1', selector: 'tr', classes: 'tablerow1'}
						],
		file_browser_callback : function(field_name, url, type, win){
			var connector = "${BASE_PATH}js/tinymce/filemanager/fileManager.jsp";
			tinymcpuk_field = field_name;
			tinymcpuk = win;
			var width = 500;
			var height = 600;
			var LeftPosition=(screen.width)?(screen.width-width)/2:100;
			var TopPosition=(screen.height)?(screen.height-height)/2:100;
			newwin = window.open(connector, "tinymcpuk", "modal,width="+width+",height="+height+",top="+TopPosition+",left="+LeftPosition);
			if (newwin == null) {
				alert('您的快顯封鎖程式已將功能視窗給封鎖了。\n請調整您的快顯封鎖程式設定，以正常使用此功能!\n工具->快顯封鎖程式->允許快顯');
			}
			return false;
		},
		//tinyMCE字數統計
		setup : function(ed) {
            ed.on('change', function(e) {
            	fontCount(textareaId);
            });
			ed.on('onblur', function(e) {
            	fontCount(textareaId);
            });
		}
	});
	setTimeout(function(){
		fontCount(textareaId);
		$(".mce-widget.mce-btn.mce-menubtn.mce-first.mce-flow-layout-item").empty();
		$(".mce-widget.mce-btn.mce-menubtn.mce-flow-layout-item:contains('編輯')").empty();
	}, 500);
}
//設置tinyMCE字數統計
function fontCount(textareaId) {
	var id = 0;
	if (typeof(textareaId) != "undefined") {
		id = textareaId.replace("content","");
		var fontArea = (1500 - removeHTMLTag(tinyMCE.get(textareaId).getContent()).length);	
		$(".mce-statusbar.mce-container.mce-panel.mce-last.mce-stack-layout-item" + ":eq(" + (id-1) + ")")
		.html('<div class="mce-path mce-flow-layout-item mce-first"><div class="mce-path-item">&nbsp;</div></div><label class="mce-wordcount mce-widget mce-label mce-flow-layout-item">剩餘字數：'
		+ fontArea + '</label><div class="mce-flow-layout-item mce-resizehandle mce-last"><i class="mce-ico mce-i-resize"></i></div>');
		if ((parseInt(removeHTMLTag(tinyMCE.get(textareaId).getContent()).length)) > 1500) {
			$(".mce-statusbar.mce-container.mce-panel.mce-last.mce-stack-layout-item" + ":eq(" + (id-1) + ") > label").css("color", "red");
		}
	}
}

/**
 * 功能區
 * 
 * 去除HTML標籤
 */
//將欄位設定為僅可讀
//去除HTML標籤
function removeHTMLTag(str) {
    str = str.replace(/<\/?[^>]*>/g,'');   //去除HTML標籤
    str = str.replace(/[ | ]*\n/g,'\n');   //去除行尾空白
    str = str.replace(/\&nbsp\;|\s/g, ''); //去除空格
    str = str.replace(/ /ig,'');           //去除空格
    return str;
}

//驗證tinymce_textarea字數
function checkTinymceWordCount(obj, wordCount, msg) {
	if ($.trim(removeHTMLTag(tinyMCE.get(obj).getContent())).length > wordCount) {
		$("#" + obj).focus();
		return msg;
	} else {
		return "";
	}
}
//取得tinymce_textarea字數
function getTinymceWordCount(obj) {
	return removeHTMLTag(tinyMCE.get(obj).getContent()).length;
}