$(function(){
    $(document).ready(function(){
        var offset = 300,
            //browser window scroll (in pixels) after which the "back to top" link opacity is reduced
            offset_opacity = 1200,
            //duration of the top scrolling animation (in ms)
            navMobileset = 250,
            navset = 765,
            scroll_top_duration = 700,
            //grab the "back to top" link
            $back_to_top = $('.btn-top');

        //hide or show the "back to top" link
        $(window).scroll(function(){
            ( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
            if( $(this).scrollTop() > offset_opacity ) { 
                $back_to_top.addClass('cd-fade-out');
            };
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


