/**
 * �]�m��
 * 
 * �]�mtinyMCE��r�s�边
 * �]�mtinyMCE�r�Ʋέp
 * 
 */
//�]�mtinyMCE(��@)
function tinymceSetup(textareaId) {
	tinymce.init({
		selector: "textarea#" + textareaId,	//��ܭn�M�J��textarea
		mode: "textareas",
		theme:"modern",
		max_height: 200,
		min_height: 160,
		height : 300,
		language:"zh_TW",
		font_formats: 
		"�s�ө���=�s�ө���;"+
		"�ө���=�ө���;"+
        "�з���=�з���;"+
        "�L�n������=�L�n������;"+
        "Times New Roman=times new roman,times",
        fontsize_formats: "8pt 9pt 10pt 12pt 14pt 18pt 20pt 24pt 36pt",
	    plugins: [
					"advlist autolink link lists charmap preview hr ",
					"visualblocks visualchars code fullscreen insertdatetime nonbreaking",
					"table directionality textcolor paste"
		],
		toolbar: "insertfile undo redo | fontselect | fontsizeselect | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor",
		forced_root_block: '',	//�N�w�]��p���N��''
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
				alert('�z���������{���w�N�\�����������F�C\n�нվ�z���������{���]�w�A�H���`�ϥΦ��\��!\n�u��->�������{��->���\����');
			}
			return false;
		},
		//tinyMCE�r�Ʋέp
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
		$(".mce-widget.mce-btn.mce-menubtn.mce-flow-layout-item:contains('�s��')").empty();
	}, 500);
}
//�]�mtinyMCE�r�Ʋέp
function fontCount(textareaId) {
	var id = 0;
	if (typeof(textareaId) != "undefined") {
		id = textareaId.replace("content","");
		var fontArea = (1500 - removeHTMLTag(tinyMCE.get(textareaId).getContent()).length);	
		$(".mce-statusbar.mce-container.mce-panel.mce-last.mce-stack-layout-item" + ":eq(" + (id-1) + ")")
		.html('<div class="mce-path mce-flow-layout-item mce-first"><div class="mce-path-item">&nbsp;</div></div><label class="mce-wordcount mce-widget mce-label mce-flow-layout-item">�Ѿl�r�ơG'
		+ fontArea + '</label><div class="mce-flow-layout-item mce-resizehandle mce-last"><i class="mce-ico mce-i-resize"></i></div>');
		if ((parseInt(removeHTMLTag(tinyMCE.get(textareaId).getContent()).length)) > 1500) {
			$(".mce-statusbar.mce-container.mce-panel.mce-last.mce-stack-layout-item" + ":eq(" + (id-1) + ") > label").css("color", "red");
		}
	}
}

/**
 * �\���
 * 
 * �h��HTML����
 */
//�N���]�w���ȥiŪ
//�h��HTML����
function removeHTMLTag(str) {
    str = str.replace(/<\/?[^>]*>/g,'');   //�h��HTML����
    str = str.replace(/[ | ]*\n/g,'\n');   //�h������ť�
    str = str.replace(/\&nbsp\;|\s/g, ''); //�h���Ů�
    str = str.replace(/ /ig,'');           //�h���Ů�
    return str;
}

//����tinymce_textarea�r��
function checkTinymceWordCount(obj, wordCount, msg) {
	if ($.trim(removeHTMLTag(tinyMCE.get(obj).getContent())).length > wordCount) {
		$("#" + obj).focus();
		return msg;
	} else {
		return "";
	}
}
//���otinymce_textarea�r��
function getTinymceWordCount(obj) {
	return removeHTMLTag(tinyMCE.get(obj).getContent()).length;
}