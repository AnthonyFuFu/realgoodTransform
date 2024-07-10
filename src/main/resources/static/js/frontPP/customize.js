$(function(){
	//主選單
	var mainNavStatus = 1;
    $(".btn-nav-mobile").on('click', function(event) {
        if (mainNavStatus == 1) {
        	$(this).addClass("btn-nav-mobile-ov");
        	$('.nav-items-mobile').toggle();

			mainNavStatus = 0;
        } else if (mainNavStatus == 0) {
        	$(this).removeClass("btn-nav-mobile-ov");
        	$('.nav-items-mobile').toggle();

			mainNavStatus = 1;	
        }
    });


    //搜尋
    var searchStatus = 1;
    $(".search-overview a").on('click', function(event) {
        if (searchStatus == 1) {
            $(this).parent().addClass("btn-search-mobile-ov");
            $('.search-items-mobile').toggle();

            searchStatus = 0;
        } else if (searchStatus == 0) {
            $(this).parent().removeClass("btn-search-mobile-ov");
            $('.search-items-mobile').toggle();

            searchStatus = 1;  
        }
    });


    //課程介紹等選項切換內容
    $('.nav-inner a').each(function(i){
        $(this).prop('nId', i).off('click').on('click', function(){
            var nId = $(this).prop("nId");
            $(this).addClass('active').siblings().removeClass('active');            
            $(".teacher-detail-content section article").eq(nId).show().siblings().hide();
        });
    });


    //課程總覽主選單功能
    $(".nav-1").click(function() {
        $('body,html').animate({scrollTop: $('.overview-intro').offset().top}, 700);
    });
    $(".nav-2").click(function() {
        $('body,html').animate({scrollTop: $('.overview-course').offset().top}, 700);
    });
    $(".nav-3").click(function() {
        $('body,html').animate({scrollTop: $('.overview-teacher').offset().top}, 700);
    });
    $(".nav-4").click(function() {
        $('body,html').animate({scrollTop: $('.overview-student').offset().top}, 700);
    });
    $(".nav-5").click(function() {
        $('body,html').animate({scrollTop: $('.overview-contact').offset().top}, 700);
    });


    //課程分類選單
    var categoryMenuStatus = 1;
    $(".btn-category").on('click', function(event) {
        if (categoryMenuStatus == 1) {
            $(".btn-category").addClass("active");
            $('.category-menu').toggle();
            categoryMenuStatus = 0;
        } else if (categoryMenuStatus == 0) {
            $(".btn-category").removeClass("active");
            $('.category-menu').toggle();
            categoryMenuStatus = 1;  
        }
    });
    //課程分類選單：次選項
    $('.category-menu nav a').each(function(i){
        $(this).prop('nId', i).off('click').on('click', function(){
            var nId = $(this).prop("nId");
            $(this).addClass('active').siblings().removeClass('active');            
            $(".category-menu article section").eq(nId).show().siblings().hide();
        });
    });

    /* 20190327 start */
    //考試介紹燈箱內容
    $(".btn-more-intro").on('click', function() {
        $('.intro-detail').fadeIn();
    });
    $(".intro-detail .btn-close").on('click', function() {
        $('.intro-detail').fadeOut();
    });
    /* 20190327 end */
});

