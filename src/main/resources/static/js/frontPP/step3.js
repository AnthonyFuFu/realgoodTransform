document.getElementById("payMethod").value = 1

function confirm (){
	let element = document.getElementById("info");
	element.classList.replace("show", "hide");
}

function info (){
	let element = document.getElementById("info");
	element.classList.replace("hide", "show");
}

function payBtn(payValue){
	var credit = document.getElementById("credit");
	var creditOv = document.getElementById("creditOv");
	var atm = document.getElementById("atm");
	var atmOv = document.getElementById("atmOv");
	var payMethod = document.getElementById("payMethod");

	if(payValue == 1){
		credit.style.display = "none";
		atmOv.style.display = "none";
		creditOv.style.display = "";
		atm.style.display = "";
	}
	if(payValue == 2){
		credit.style.display = "";
		atmOv.style.display = "";
		creditOv.style.display = "none";
		atm.style.display = "none";
	}
	payMethod.value = payValue
	console.log(document.getElementById("payMethod").value)
}

function selectValue(radio){
	let selectedValue = radio.value;
	let unify = document.querySelector(".unifyClass");
	let carrier = document.querySelector(".carrierClass");
	let phoneCarrier = document.querySelector(".phoneCarrierClass");
	let donate = document.querySelector(".donateClass");
	//let sendAddress = document.querySelector(".sendAddressClass");
	let personal = document.querySelector(".personalClass");
	let company = document.querySelector(".companyClass");
	//let studentCarrierClass = document.querySelector(".studentCarrierClass");
	
	$("input[name='carrier'][name='carrier'][value='1']").prop('checked', true);
	if(selectedValue == 1){
		//studentCarrierClass.style.display = "";
		company.style.display = "none";
		unify.style.display = "none";
		carrier.style.display = "";
		phoneCarrier.style.display = "none";
		donate.style.display = "none";
		//sendAddress.style.display = "none";
		personal.style.display = "none";
		$("#unify").val("");
		$("#companyOrName").val("");
		$("#carrierCountry").val("0");
		$("#carrierArea").val("0");
		//$("#carrierSendAddress").val("");
		$("#donate").val("");
	}else if(selectedValue == 2){
		unify.style.display = "";
		//studentCarrierClass.style.display = "none";
		company.style.display = "";
		//sendAddress.style.display = "";
		carrier.style.display = "none";
		phoneCarrier.style.display = "none";
		donate.style.display = "none";
		personal.style.display = "none";
		//$("#studentCarrier").val("");
		$("#phoneCarrier").val("");
		$("#personal").val("");
		$("#carrierCountry").val("0");
		$("#carrierArea").val("0");
		//$("#carrierSendAddress").val("");
		$("#donate").val("");
	}else if(selectedValue == 3){
        company.style.display = "none";
        unify.style.display = "none";
        carrier.style.display = "none";
        phoneCarrier.style.display = "none";
        donate.style.display = "";
        //sendAddress.style.display = "none";
        personal.style.display = "none";
        $("#unify").val("");
        $("#companyOrName").val("");
        $("#carrierCountry").val("0");
        $("#carrierArea").val("0");
        //$("#carrierSendAddress").val("");
        $("#donate").val("");
	}else{
		return false;
	}
}

function selectCarrier(radio){
	let selectedValue = radio.value;
	//let studentCarrier = document.querySelector(".studentCarrierClass");
	let phoneCarrier = document.querySelector(".phoneCarrierClass");
	let donate = document.querySelector(".donateClass");
	let personal = document.querySelector(".personalClass");
	let sendAddress = document.querySelector(".sendAddressClass");

	if(selectedValue == 1){//學員載具
		//studentCarrier.style.display = "";
		//donate.style.display = "";
		donate.style.display = "none";
		phoneCarrier.style.display = "none";
		personal.style.display = "none";
		//sendAddress.style.display = "none";
		$("#phoneCarrier").val("");
		$("#personal").val("");
		$("#carrierCountry").val("0");
		$("#carrierArea").val("0");
		//$("#carrierSendAddress").val("");
	}else if(selectedValue == 2){//手機載具
		//studentCarrier.style.display = "none";
		//studentCarrier.value = ""; 
		phoneCarrier.style.display = "";
		//donate.style.display = "";
		donate.style.display = "none";
		personal.style.display = "none";
		//sendAddress.style.display = "none";
		//$("#studentCarrier").val("");
		$("#personal").val("");
		$("#carrierCountry").val("0");
		$("#carrierArea").val("0");
		//$("#carrierSendAddress").val("");
	}else if(selectedValue == 3){//自然人憑證
		//studentCarrier.style.display = "none";
		phoneCarrier.style.display = "none";
		//donate.style.display = "";
		donate.style.display = "none";
		personal.style.display = "";
		//sendAddress.style.display = "none";
		//$("#studentCarrier").val("");
		$("#phoneCarrier").val("");
		$("#carrierCountry").val("0");
		$("#carrierArea").val("0");
		//$("#carrierSendAddress").val("");
	}else if(selectedValue == 4){
		//studentCarrier.style.display = "none";
		phoneCarrier.style.display = "none";
		donate.style.display = "";
		personal.style.display = "none";
		sendAddress.style.display = "none";
	}else if(selectedValue == 5){
		//studentCarrier.style.display = "none";
		phoneCarrier.style.display = "none";
		donate.style.display = "none";
		personal.style.display = "none";
		sendAddress.style.display = "";
		//$("#studentCarrier").val("");
		$("#phoneCarrier").val("");
		$("#personal").val("");
		$("#donate").val("");
	}else{
		return false;
	}
}


function back(){
	window.history.back();
	//window.location.href="/step/step2";
}

function toPay(){
	let type = $("input[name='type']:checked").val();
	let companyOrName = $("#companyOrName").val().trim();
	let carrier = $("input[name='carrier']:checked").val();
	let phoneCarrier = $("#phoneCarrier").val().trim();
	let donate = $("#donate").val().trim();
	let personal = $("#personal").val().trim();
	let country = $("#carrierCountry").val();
	let area = $("#carrierArea").val();
	/*let sendAddress = $("#carrierSendAddress").val().trim();*/
	let unify = $("#unify").val().trim();
	//let sameAddress = $("input[name='sameAddress']:checked");
	let sameAddress = document.getElementById("sameAddress");

console.log(sameAddress)
console.log($("#orderInfoCountry").val())
	
	if(type == 1){
		if(carrier == 2){
			//電子發票手機條碼格式 斜線(/)加上7碼數字或大寫字母
			let regex = /^\/([0-9A-Z+\-.]{7})$/;
			if(phoneCarrier == ""){
				alert("手機載具不得為空")
				return false;				
			}
			if(!regex.test(phoneCarrier)){
				alert("手機載具不符合格式")
				return false;
			}
		}else if(carrier == 3){
			//自然人憑證格式 2碼英文字母加上14碼數字
			regex = /^[A-Z]{2}[0-9]{14}$/;
			if(personal == ""){
				alert("自然人憑證不得為空")
				return false;
			}
			if(!regex.test(personal)){
				alert("自然人憑證不符合格式")
				return false;
			}
		}else if(carrier == 5){
			if(sameAddress.checked){
				
			}else{
				if(country == 0){
					alert("縣市不得為空")
					return false;
				}
				if(area == 0){
					alert("區域不得為空")
					return false;
				}
				/*if(sendAddress == ""){
					alert("寄送地址不得為空")
					return false;	
				}*/				
			}
		}
		
	}else if(type == 2){
		if(companyOrName == ""){
			alert("姓名/公司名稱不得為空")
			return false;
		}
		if(unify == ""){
			alert("統一編號不得為空")
			return false;
		}
		
		/*if(sameAddress.checked){
				
			}else{
				if(country == 0){
					alert("縣市不得為空")
					return false;
				}
				if(area == 0){
					alert("區域不得為空")
					return false;
				}
				if(sendAddress == ""){
					alert("寄送地址不得為空")
					return false;	
				}				
			}*/
	}else{
		if(donate != ""){
			//愛心捐贈碼格式 總長3-7碼的數字
			let regex = /^\d{3,7}$/;
			if(!regex.test(donate)){
				alert("捐贈碼不符合格式")
				return false;
			}
			
		}else{
			alert("捐贈碼不得為空")
			return false;
		}
		
	}
	
	
	//window.location.href="/step/toPay";
	$(".carrier").attr("action", "[[${@environment.getProperty('weburl')}]]/step/toPay");
	$(".carrier").submit();
}

function insert(){
	if(!sameAddress.checked){
		const container = document.getElementById('countryContainer');
		
		const selectElement  = document.createElement('select');
	  	selectElement.id = 'carrierCountry';
	  	selectElement.name = 'carrierCountry';
	  	selectElement.classList.add('select-css') ;
	  	const inputElement = document.getElementById('carrierCountry');
	  	
	  	container.replaceChild(selectElement, inputElement);
	  	
	  	const areaContainer = document.getElementById('areaContainer');
	  	
	  	const carrierAreaSelectElement = document.createElement('select');
	  	carrierAreaSelectElement.id = 'carrierArea';
	  	carrierAreaSelectElement.name = 'carrierArea';
	  	carrierAreaSelectElement.classList.add('select-css') ;
	  	
	  	const option = document.createElement('option');
		option.value = '0';
		option.text = '區域';
		carrierAreaSelectElement.appendChild(option);
		
	  	const carrierAreaInputElement = document.getElementById('carrierArea');
	  	
	  	areaContainer.replaceChild(carrierAreaSelectElement, carrierAreaInputElement);
	  	
	  	let str = "";
	    $("#carrierCountry").empty();
	    str += "<option value='0'>請選擇縣市</option>";
	    for(let i = 0 ; i < locationData.length ; i++){
	    	str += "<option value='"+locationData[i].name+"'>"+locationData[i].name+"</option>";
	    }
	    $("#carrierCountry").append(str);
	    
	    $("#carrierCountry").on('change', function() {
		    let areaStr = "";
	    	$("#carrierArea").empty();
	    	areaStr += "<option value='0'>請選擇區域</option>";
	    		console.log($("#carrierCountry").val())
	    	for(let i = 0 ; i < locationData.length ; i++){
	    		if($("#carrierCountry").val() == locationData[i].name){
	    			for(let j = 0 ; j < locationData[i].districts.length ; j++){
			    		areaStr += "<option value='"+locationData[i].districts[j].name+"'>"+locationData[i].districts[j].name+"</option>";    			    				
	    			}
	    		}
	        }
	    	$("#carrierArea").append(areaStr);
	    });
	    
	    $("#carrierSendAddress").val("");
	    $("#carrierSendAddress").prop("readOnly", false);
	    $("#carrierSendAddress").attr("placeholder", "請輸入寄送地址");
	  	
	}else{		
		const container = document.getElementById('countryContainer');
		
		const inputElement = document.createElement('input');
	  	inputElement.type = 'text';
	  	inputElement.id = 'carrierCountry';
	  	inputElement.name = 'carrierCountry';
	  	inputElement.readOnly = true;
	  	const selectElement = document.getElementById('carrierCountry');
	  	
	  	container.replaceChild(inputElement, selectElement);
	  	
	  	const areaContainer = document.getElementById('areaContainer');
	  	
	  	const carrierAreaInputElement = document.createElement('input');
	  	carrierAreaInputElement.type = 'text';
	  	carrierAreaInputElement.id = 'carrierArea';
	  	carrierAreaInputElement.name = 'carrierArea';
	  	carrierAreaInputElement.readOnly = true;
	  	const carrierAreaSelectElement = document.getElementById('carrierArea');
	  	
	  	areaContainer.replaceChild(carrierAreaInputElement, carrierAreaSelectElement);
		
		$("#carrierCountry").val($("#country").val());
		$("#carrierArea").val($("#area").val());
		$("#carrierSendAddress").val($("#address").val());
		$("#carrierSendAddress").prop("readOnly", true);
	}
}

function handleBlur(element){
	let carrier = $("input[name='carrier']:checked").val();
	let receiptData = carrier == 2 ? $("#phoneCarrier").val() : $("#personal").val();
	let placeholder = "";
	let status400 = "";
	let status404 = "";
	if(carrier == 2){
		placeholder = "您的手機載具";
		status400 = "手機條碼格式錯誤";
		status404 = "手機條碼不存於電子發票整合平台";
	}
	if(carrier == 3){
		placeholder = "您的自然人憑證";
		status400 = "自然人憑證條碼格式錯誤";
		status404 = "自然人憑證條碼不存於電子發票整合平台";
	}
	
	element.placeholder = placeholder;

	$.ajax({
		url : "/verify/carrier",
		cache : false,
		async : false,
		type :"POST",
		data : {
			receiptType : carrier,
			receiptData : receiptData
		},
		success : function(data){
			
		},
		error : function(xhr){
			$("#phoneCarrier").val("");
			$("#personal").val("");
			
			if(xhr.status == 400){
				alert(status400)
				receiptData.value = "";
			}
			if(xhr.status == 404){
				alert(status404)
				receiptData.value = "";
			}
			if(xhr.status == 500){
				alert("發生異常，請通知相關人員")
				receiptData.value = "";
			}
		}
	})
}

function unifyBlur(element,num){
	let phoneCarrier = num == 1 ? $("#unify").val() : $("#donate").val();
	let placeholder = num == 1 ? "您的統一編號" : "捐贈碼";
	let status400 = num == 1 ? "統一編號格式錯誤" : "捐贈碼格式錯誤";
	let status404 = num == 1 ? "統一編號不為營業人" : "捐贈碼不存於電子發票整合平台";
	
	element.placeholder = placeholder;

	$.ajax({
		url : "/verify/tax",
		cache : false,
		async : false,
		type :"POST",
		data : {
			receiptData : phoneCarrier,
			num : num
		},
		success : function(data){
			
		},
		error : function(xhr){
			$("#unify").val("");
			$("#donate").val("");
			
			if(xhr.status == 400){
				alert(status400)
			}
			if(xhr.status == 404){
				alert(status404)
			}
			if(xhr.status == 500){
				alert("發生異常，請通知相關人員")
			}
		}
	})
}









