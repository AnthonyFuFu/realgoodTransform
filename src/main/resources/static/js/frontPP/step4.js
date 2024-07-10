function confirm (){
	let element = document.getElementById("info");
	element.classList.replace("show", "hide");
}

function info (){
	let element = document.getElementById("info");
	element.classList.replace("hide", "show");
}

function index(){
	window.location.href="[[${@environment.getProperty('sshDasoPath')}]]/index";
}