
$(function(){
    var len = 150; // 超過150個字以"..."取代
    $(".video-txt").each(function(i){
        if($(this).text().length>len){
            $(this).attr("title",$(this).text());
            var text=$(this).text().substring(0,len-1)+"...";
            $(this).text(text);
        }
    });

    var hotlen = 78; // 超過78個字以"..."取代
    $(".spec-txt").each(function(i){
        if($(this).text().length>hotlen){
            $(this).attr("title",$(this).text());
            var text=$(this).text().substring(0,hotlen-1)+"...";
            $(this).text(text);
        }
    });

    var len2 = 45; // 超過80個字以"..."取代
    $(".video-inside-txt").each(function(i){
        if($(this).text().length>len2){
            $(this).attr("title",$(this).text());
            var text=$(this).text().substring(0,len2-1)+"...";
            $(this).text(text);
        }
    });
});



