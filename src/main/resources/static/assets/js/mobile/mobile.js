$(function() {

    $.fn.jqueryPager = function(options) {
        var defaults = {
            pageSize: 10,
            currentPage: 1,
            pageTotal: 0,
            pageBlock: 10,
            clickEvent: 'callList'
        };

        var subOption = $.extend(true, defaults, options);

        return this.each(function() {
            var currentPage = subOption.currentPage*1;
            var pageSize = subOption.pageSize*1;
            var pageBlock = subOption.pageBlock*1;
            var pageTotal = subOption.pageTotal*1;
            var clickEvent = subOption.clickEvent;

            if (!pageSize) pageSize = 10;
            if (!pageBlock) pageBlock = 10;

            var pageTotalCnt = Math.ceil(pageTotal/pageSize);
            var pageBlockCnt = Math.ceil(currentPage/pageBlock);
            var sPage, ePage;
            var html = '';

            if (pageBlockCnt > 1) {
                sPage = (pageBlockCnt-1)*pageBlock+1;
            } else {
                sPage = 1;
            }

            if ((pageBlockCnt*pageBlock) >= pageTotalCnt) {
                ePage = pageTotalCnt;
            } else {
                ePage = pageBlockCnt*pageBlock;
            }

            html += '<div class="c-paging__wrapper">';

            html += '<div class="c-paging__arrow">';
            html += '<a href="javascript:'+ clickEvent +'(1);" class="c-paging__item c-paging__control"><img src="/assets/images/icon__first-page.png" alt="첫 페이지"></a>';
            html += '</div>';

            if (sPage > 1) {
                html += '<div class="c-paging__arrow">';
                html += '<a href="javascript:'+ clickEvent +'(' + (sPage-pageBlock) + ');" class="c-paging__item c-paging__control"><img src="/assets/images/icon__prev.png" alt="이전 페이지"></a>';
                html += '</div>';
            }

            for (var i=sPage; i<=ePage; i++) {

                html += '<div class="c-paging__group">';
                if (currentPage == i) {
                    html+= '<a href="javascript:'+ clickEvent +'(' + i + ');" class="c-paging__item check" >' + i + '</a>';
                } else {
                    html+= '<a href="javascript:'+ clickEvent +'(' + i + ');" class="c-paging__item" >' + i + '</a>';
                }
                html += '</div>';
            }


            if (ePage < pageTotalCnt) {
                html += '<div class="c-paging__arrow">';
                html+= '<a href="javascript:'+ clickEvent +'(' + (ePage+1) + ');" class="c-paging__item c-paging__control"><img src="/assets/images/icon__next.png" alt="다음 페이지"></a>';
                html += '</div>';
            }

            html += '<div class="c-paging__arrow">';
            html+= '<a href="javascript:'+ clickEvent +'(' + (pageTotalCnt) + ');" class="c-paging__item c-paging__control"><img src="/assets/images/icon__last-page.png" alt="마지막 페이지"></a>';
            html += '</div>';


            html += '</div>';

            $(this).empty().html(html);
        });
    };

});

//Ajax 호출시 에러가났을경우의 메세지 함수
function ajaxErrorMsg(request) {
    if (request.status == "403") {
        alertCancel("로그인 정보가 만료되었거나, <BR>권한이 없습니다. <BR>다시 로그인 하세요.",1);
    } else {
        alertCancel("데이터 문제가 생겼습니다.",1);
    }
}

function echoNull2Blank(str) {
    if (str == null) return '';
    return str;
}

function nvl(str, defaultStr){
    if(typeof str == "undefined" || str == null || str == "")
        str = defaultStr ;
    return str ;
}