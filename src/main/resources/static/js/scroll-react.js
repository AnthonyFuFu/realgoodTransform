$(function(){
    $(document).ready(function(){
        var offset = 300,
            //browser window scroll (in pixels) after which the "back to top" link opacity is reduced
            offset_opacity = 1200,
            //duration of the top scrolling animation (in ms)
            navMobileset = 250,
            navset = 765,
            scroll_top_duration = 700,
            mobile = 100
            //grab the "back to top" link
            $back_to_top = $('.btn-top');

        //hide or show the "back to top" link
        $(window).scroll(function(){
            ( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
            if( $(this).scrollTop() > offset_opacity ) { 
                $back_to_top.addClass('cd-fade-out');
            };
            let device = $(window).innerWidth()
            if(device > 640){
                ( $(this).scrollTop() > offset ) ? $(".re-header").addClass('re-header-white') : $(".re-header").removeClass('re-header-white');
                ( $(this).scrollTop() > offset ) ? $(".re-top-nav-area").addClass('re-top-nav-area-white') : $(".re-top-nav-area").removeClass('re-top-nav-area-white');
                ( $(this).scrollTop() > offset ) ? $(".re-top-nav a").addClass('re-color-gray') : $(".re-top-nav a").removeClass('re-color-gray');
                ( $(this).scrollTop() > offset ) ? $(".re-nav a").addClass('re-color-black') : $(".re-nav a").removeClass('re-color-black');
                ( $(this).scrollTop() > offset ) ? $(".re-search-box .btn-search-go").addClass('re-color-black') : $(".re-search-box .btn-search-go").removeClass('re-color-black');
                ( $(this).scrollTop() > offset ) ? $(".menu-area-mb a").addClass('re-color-black') : $(".menu-area-mb a").removeClass('re-color-black');


                ( $(this).scrollTop() > offset ) ? $(".line-vertical").addClass('line-vertical-red') : $(".line-vertical").removeClass('line-vertical-red');
                ( $(this).scrollTop() > offset ) ? $(".re-sideBar a.btn-line").addClass('active-line') : $(".re-sideBar a.btn-line").removeClass('active-line');
                ( $(this).scrollTop() > offset ) ? $(".re-sideBar a.btn-fb").addClass('active-fb') : $(".re-sideBar a.btn-fb").removeClass('active-fb');
                ( $(this).scrollTop() > offset ) ? $(".re-sideBar a.btn-blogger").addClass('active-blogger') : $(".re-sideBar a.btn-blogger").removeClass('active-blogger');
            }else {

                ( $(this).scrollTop() > mobile ) ? $(".re-header").addClass('re-header-white') : $(".re-header").removeClass('re-header-white');
                ( $(this).scrollTop() > mobile ) ? $(".re-top-nav-area").addClass('re-top-nav-area-white') : $(".re-top-nav-area").removeClass('re-top-nav-area-white');
                ( $(this).scrollTop() > mobile ) ? $(".re-top-nav a").addClass('re-color-gray') : $(".re-top-nav a").removeClass('re-color-gray');
                ( $(this).scrollTop() > mobile ) ? $(".re-nav a").addClass('re-color-black') : $(".re-nav a").removeClass('re-color-black');
                ( $(this).scrollTop() > mobile ) ? $(".re-search-box .btn-search-go").addClass('re-color-black') : $(".re-search-box .btn-search-go").removeClass('re-color-black');
                ( $(this).scrollTop() > mobile ) ? $(".menu-area-mb a").addClass('re-color-black') : $(".menu-area-mb a").removeClass('re-color-black');


                ( $(this).scrollTop() > mobile ) ? $(".line-vertical").addClass('line-vertical-red') : $(".line-vertical").removeClass('line-vertical-red');
                ( $(this).scrollTop() > mobile ) ? $(".re-sideBar a.btn-line").addClass('active-line') : $(".re-sideBar a.btn-line").removeClass('active-line');
                ( $(this).scrollTop() > mobile ) ? $(".re-sideBar a.btn-fb").addClass('active-fb') : $(".re-sideBar a.btn-fb").removeClass('active-fb');
                ( $(this).scrollTop() > mobile ) ? $(".re-sideBar a.btn-blogger").addClass('active-blogger') : $(".re-sideBar a.btn-blogger").removeClass('active-blogger');
            }
        
        });

        //smooth scroll to top
        $back_to_top.on('click', function(event){
            event.preventDefault();
            $('body,html').animate({
                scrollTop: 0 ,
                }, scroll_top_duration
            );
        });

        //收納/展開按鈕
        var featureStatus = 1;
        $(".btn-feature").on('click', function(event) {
            if (featureStatus == 1) {
                $(this).addClass("btn-feature-ov");
                $('.btn-course').toggle();
                $('.fb_reset').toggle();

                featureStatus = 0;
            } else if (featureStatus == 0) {
                $(this).removeClass("btn-feature-ov").addClass("btn-feature");
                $('.btn-course').toggle();
                $('.fb_reset').toggle();

                featureStatus = 1;  
            }
        });
    });
});


