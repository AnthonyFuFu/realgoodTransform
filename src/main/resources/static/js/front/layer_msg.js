//http://www.51xuediannao.com/js/jquery/jquery_layer/layer.html

$(function(){
	
	//會員登入
	/*
	$('.login_window').on('click', function(){		
		layer.alert(' <div class="login"> <div><input class="id" name="" type="text" placeholder="ID"></div> <div><input class="password" name="" type="text" placeholder="Password"></div> <div class="remember"><input name="" type="checkbox" value="">記住密碼</div> <div class="register"><a href="register.htm">加入會員</a></div> <div class="forgot"><a href="forgot.htm">忘記密碼</a></div> <div class="login_btn"><a href="">登入</a></div> </div><!-- login -->', {
		  skin: 'layui-layer-molv' //樣式類別
		  ,closeBtn: 1
		  ,title: ['會員登入']
		  ,btn: false
		  ,shift: 5 //動畫類型
		});
	});
	*/
	
	//反詐騙申明
	$('.declare, #declare,.new_declare').on('click', function(){		
		layer.alert('感謝同學報名<font color="#FF0000">TKB臺灣知識庫—大碩研究所、百官網公職、龍門轉學考、洋碩美語、甄戰學習顧問，數位學堂、雲端函授、千碩STEAM教育等課程</font>，衷心預祝您金榜題名！<p>提醒您：<br />繳費請洽櫃台或依公司指定個人專用帳號匯款，並索取發票。<br />本公司不會以電話通知【繳費入帳操作錯誤】、【分期繳費扣款錯誤】、【扣款錯誤需操作ATM退款】、【提供信用卡安全碼或扣款銀行電話】、【假冒郵局或銀行職員說晚上12點不到ATM操作會重複扣款】、【課程、教材購買套數key錯】、【依指示到超商買儲值點數卡並給密碼】、【未顯示來電或大陸地區來電】等，以上均為詐騙電話，若發生上述情形時或您懷疑有個人資料外洩時，請於上班時間內撥打下列服務專線洽詢本公司原報名單位窗口，非上班時間請撥打警政署165反詐騙電話進行查詢，為維護您的個人資料權利，如未撥打詢問而遭詐騙損失時，概與本公司無關。<br>大碩研究所全國據點聯絡資訊：<br><a href="https://www.daso.com.tw/location/index" style="text-decoration: underline; color: #0000FF">https://www.daso.com.tw/location/index</a></p>', {
		  skin: 'layui-layer-molv' //樣式類別
		  ,closeBtn: 1
		  ,title: ['TKB反詐騙宣導']
		  ,btn: ['確定']
		  ,shift: 5 //動畫類型
		  ,offset:'5%'
		});
	});
	
	//低收入戶優惠
	$('#low-income').on('click', function(){		
		layer.alert('TKB甄戰學習顧問中心為回饋社會，凡清寒(領有縣市政府低收入戶證明書)、 身心障礙 (中度以上) 學子，一律五折優惠 (代理課程及雲端產品除外) ， 我們真心希望清寒子弟也能享有同齡學子的夢想！', {
		  skin: 'layui-layer-molv' //樣式類別
		  ,closeBtn: 1
		  ,title: ['低收入戶優惠']
		  ,btn: ['確定']
		  ,shift: 5 //動畫類型
		  ,offset: '30%'
		});
	});
	
	//停課需知
	$('#suspend-classes').on('click', function(){		
		layer.alert('為顧及本公司同學及職員的人身安全，如因地震、颱風、大豪雨等天候因素，造成無法上課時；本公司所有學員及職員，請依<font color="#FF0000">行政院人事行政總處</font>所發佈之各縣市<font color="#FF0000">「停止上班」</font>公告，全面配合<font color="#FF0000">「停止上課」</font>及<font color="#FF0000">「停止上班」</font>。', {
		  skin: 'layui-layer-molv' //樣式類別
		  ,closeBtn: 1
		  ,title: ['停課需知']
		  ,btn: ['確定']
		  ,shift: 5 //動畫類型
		  , offset: '30%'
		});
	});
	
	//訊息公告提醒輪播1(marquee_01~03需用class，因跑馬燈語法會產生新的一組li，id會被重複使用而失效)
	$('.marquee_01').on('click', function(){		
		layer.alert('2017寒假課程開跑囉！想要知道附近有哪些千碩創新教育課程開課嗎？歡迎與我們聯絡！<br/>全省千碩教室點這邊：<br/><a href="https://www.long-men.com.tw/location/index"><font color="#FF0000">http://robot.tkb.com.tw/location/index</font></a>', {
		  skin: 'layui-layer-molv' //樣式類別
		  ,closeBtn: 1
		  ,title: ['2017寒假課程開跑囉']
		  ,btn: ['確定']
		  ,shift: 5 //動畫類型
		});
	});
	
	//訊息公告提醒輪播2
//	$('.marquee_02').on('click', function(){		
//		layer.alert('重大訊息公告2內容', {
//		  skin: 'layui-layer-molv' //樣式類別
//		  ,closeBtn: 1
//		  ,title: ['重大訊息公告2']
//		  ,btn: ['確定']
//		  ,shift: 5 //動畫類型
//		});
//	});
	
	//訊息公告提醒輪播3
//	$('.marquee_03').on('click', function(){		
//		layer.alert('重大訊息公告3內容', {
//		  skin: 'layui-layer-molv' //樣式類別
//		  ,closeBtn: 1
//		  ,title: ['重大訊息公告3']
//		  ,btn: ['確定']
//		  ,shift: 5 //動畫類型
//		});
//	}); 
});