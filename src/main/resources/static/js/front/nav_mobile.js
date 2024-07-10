$(function(){
	//主選單行動版
	var mainNavStatus = 1;
    $(".btn-nav-area, .btn-re-nav").on('click', function(event) {
        if (mainNavStatus == 1) {
        	$('.nav-mobile').css("display","block");
            $('.nav-mobile, .nav-mobile .category, .nav-mobile .category>*').each(function(i){
                $(this).transition({
                    x: 0,
                    opacity: 1
                 }, 1);
            });
			mainNavStatus = 0;
        } else if (mainNavStatus == 0) {
        	$('.nav-mobile').toggle();
            $('.nav-mobile, .nav-mobile .category, .nav-mobile .category>*').each(function(i){
                $(this).transition({
                    x:(-50)* i,
                    opacity: 0
                 },0);
            });

            $(".nav-mobile .category-sub-container").css({
                display: 'none'
            });
			mainNavStatus = 1;	
        }
    });

    $('.nav-mobile, .nav-mobile .category, .nav-mobile .category>*').each(function(i){
        $(this).transition({
            x:(-50)* i,
            opacity: 0
        },0);
    }); 

    //主選單關閉按鈕
    $(".nav-mobile aside a, .nav-mobile").on('click', function(event) {
        $('.nav-mobile').fadeOut();
        $('.nav-mobile, .nav-mobile .category, .nav-mobile .category>*').each(function(i){
            $(this).transition({
                x:(-50)* i,
                opacity: 0
            },0);
        });
        $(".nav-mobile .category-sub-container").css({
            display: 'none'
        });
        $(".nav-mobile .category-basic").css({
            display: 'none'
        });;
        mainNavStatus = 1;
        event.stopPropagation()
    });
    $(".scroll-container").on("click",function(){
        event.stopPropagation()
    })
    

    //共通
    $(".category").on('click', function() {
        $(this).next(".category-sub-container").toggle();
        $(this).next(".category-sub-container").siblings('.category-sub-container').css({
            display: 'none'
        });;
    });
    $(".category-sub").on('click', function() {
        $(this).next(".category-basic").toggle();
        $(this).siblings().next(".category-basic").css({
            display: 'none'
        });;
    });
});

