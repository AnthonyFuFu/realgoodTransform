  window.fbAsyncInit = function() {
    FB.init({
      appId            : '325386731321406',
      autoLogAppEvents : true,
      xfbml            : true,
      version          : 'v2.11'
    });
  };
  (function(d, s, id){ 
     var js, fjs = d.getElementsByTagName(s)[0]; 
     if (d.getElementById(id)) {return;} 
     js = d.createElement(s); js.id = id; 
     js.src = "https://connect.facebook.net/zh_TW/sdk/xfbml.customerchat.js "; 
     fjs.parentNode.insertBefore(js, fjs); 
   }(document, 'script', 'facebook-jssdk'));
