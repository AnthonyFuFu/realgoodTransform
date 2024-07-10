
	function next(){
		var weburl = '[[${@environment.getProperty('weburl')}]]' ;
		let checkbox = document.getElementById("check");
		if(checkbox.checked){
			window.location.href=weburl+"/step/step2";
		}else{
			alert("請勾選我已閱讀過反詐騙聲明")
		}
	}

var sshDasoPath = '[[${@environment.getProperty('sshDasoPath')}]]' ;
	
	function google_search() {
		location.href = sshDasoPath+"search?keyword=" + $("#google_search").val();
	}

	function google_search_rwd() {
		location.href = sshDasoPath+"search?keyword=" + $("#google_search_rwd").val();
	}
	