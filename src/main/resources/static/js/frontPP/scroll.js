
$(function() {
    $(".scroll-container, .preview-container, .preview-detail-list, .courseDetailList").overlayScrollbars({
        className       : "os-theme-dark",
        resize          : false,
        sizeAutoCapable : true,
        paddingAbsolute : true,
        scrollbars : {
            clickScrolling : true
        }
    });
});