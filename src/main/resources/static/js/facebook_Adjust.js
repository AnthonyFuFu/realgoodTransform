
$(function(){
	var meta = '<iframe src="https://www.facebook.com/plugins/likebox.php?href=https://www.facebook.com/reallygood.com.tw/;width&amp;height=558&amp;colorscheme=light&amp;show_faces=true&amp;header=false&amp;stream=true&amp;show_border=false&amp;appId=570708622978728" scrolling="no" frameborder="0" allowtransparency="true" style="border:none;overflow:hidden;height:558px;" data-reactid="872"></iframe>'
	function isMobileDevice() {
		const mobileDevice = ['Android', 'webOS', 'iPhone', 'iPad', 'iPod', 'BlackBerry', 'Windows Phone']
	    let isMobileDevice = mobileDevice.some(e => navigator.userAgent.match(e))
	    return isMobileDevice
	}
	console.log(isMobileDevice())
	if(isMobileDevice() == true){
		$(".metaBox").empty()
	}else{
		$(".metaBox").append(meta)
	}
})