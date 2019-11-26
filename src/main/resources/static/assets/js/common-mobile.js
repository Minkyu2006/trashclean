
//20180328최인석 myoffice 에있는 js 추가함

//Ajax 호출시 에러가났을경우의 메세지 함수
function ajaxErrorMsg(request) {
    if (request.status == "403") {
        alert("로그인 정보가 만료되었거나 권한이 없습니다. 다시 로그인 하세요");
    } else {
        alert("code:" + request.status + "\n" + "message:" + request.responseText);
    }
}

//접속장비 확인하기
//리스트페이지 -> 모바일인지 체크 // 2019.9.24 김민규추가
function mobileCheck() {
        if (navigator.userAgent.match(/iPad/) == null && navigator.userAgent.match(/iPhone|Mobile|UP.Browser|Android|BlackBerry|Windows CE|Nokia|webOS|Opera Mini|SonyEricsson|opera mobi|Windows Phone|IEMobile|POLARIS/) != null) {
            return true;
        }else{
            return false;
        }
}
//인덱스페이지 -> 모바일인지 체크
function mobileCheckIndex() {
        if (navigator.userAgent.match(/iPad/) == null && navigator.userAgent.match(/iPhone|Mobile|UP.Browser|Android|BlackBerry|Windows CE|Nokia|webOS|Opera Mini|SonyEricsson|opera mobi|Windows Phone|IEMobile|POLARIS/) != null) {
            return location.href = "/record/mreg";
        }else{
            return location.href = "/record/reg";
        }
}

//20180328최인석 myoffice 에있는 js 추가함

//JSON KEY to lowercase
function echoNull2Blank(str) {
    if (str == null) return '';
    return str;
}

//20191126 최인석 장비상태조회시 값이없을경우 미확인 을 ㅗ처리
function echoNull2Undefined(str) {
    if (str == null) return '미확인';
    return str;
}

/** 20191126 최인석 추가
 * 문자열이 빈 문자열인지 체크하여 기본 문자열로 리턴한다.
 * @param str           : 체크할 문자열
 * @param defaultStr    : 문자열이 비어있을경우 리턴할 기본 문자열
 */
function nvl(str, defaultStr){

    if(typeof str == "undefined" || str == null || str == "")
        str = defaultStr ;

    return str ;
}

(function($) {
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

            if (sPage > 1) {
                html += '<div class="c-paging__arrow">';
                html += '<a href="javascript:'+ clickEvent +'(' + (sPage-pageBlock) + ');" class="c-paging__item c-paging__prev"><img src="/images/button__page-left--hover.png" alt="이전"></a>';
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
               html+= '<a href="javascript:'+ clickEvent +'(' + (ePage+1) + ');" class="c-paging__item c-paging__next"><img src="/images/button__page-right--hover.png" alt="다음"></a>';
               html += '</div>';
            }
            html += '</div>';

            $(this).empty().html(html);
      });
    };
})(jQuery);

// header menu icon
$(document).ready(function(){
    // nav icon animation
    $('.nav-icon').click(function(){
        $(this).toggleClass('open');
        $(this).parents('.wrapper').toggleClass('nav--open');
    });

    // nav accordion
    $('.toggle').click(function(e) {
      	e.preventDefault();

        var $this = $(this);

        if ($this.next().hasClass('show')) {
            $this.next().removeClass('show');
            $this.next().slideUp(350);
        } else {
            $this.parent().parent().find('li .nav__list-depth').removeClass('show');
            $this.parent().parent().find('li .nav__list-depth').slideUp(350);
            $this.next().toggleClass('show');
            $this.next().slideToggle(350);
        }
    });

    // $('.c-progress-pie').each(function(){
    //     var $ppc = $(this),
    //         percent = 0,
    //         deg = 0,
    //     _total = $ppc.data('total'),
    //     _value = $ppc.data('value');
    //     _total = typeof _total !== 'undefined' ? _total : 0;
    //     _value = typeof _value !== 'undefined' ? _value : 0;
    //     percent = _total > 0 ? (_value/_total) * 100 : 0;
    //     deg = percent > 0 ? (_value/_total) * 360 : 0;
    //     if (percent > 50) {
    //         $ppc.addClass('gt-50')
    //     }
    //
    //     $ppc.find('.c-progress-pie__fill').css('transform','rotate('+ deg +'deg)');
    //     $ppc.find('.c-progress-pie__percent span').html(Math.floor(percent) +'%');
    //     $ppc.find('.c-progress-pie__range').html(_value + ' / ' +_total);
    // });
    //
    // $('.c-progress-bar__base').each(function() {
    //     var $progressBar = $(this),
    //         percent = parseInt($progressBar.data('percent'))
    //
    //     $progressBar.find('.c-progress-bar__percent').css('margin-left', percent + '%');
    //     $progressBar.find('.c-progress-bar__range').html(percent + '%');
    // });

    $('.c-progress-pie').each(function() {
        var $this = $(this),
            percent = 0,
            _total = $this.data('total'),
            _value = $this.data('value');

            _total = typeof _total !== 'undefined' ? _total : 0;
            _value = typeof _value !== 'undefined' ? _value : 0;
            percent = _total > 0 ? (_value/_total) * 100 : 0;

            $(this).find('.c-progress-pie__base').attr('data-percent', Math.floor(percent));
            $(this).find('.c-progress-pie__percent span').html(Math.floor(percent) +'%');
            $(this).find('.c-progress-pie__range').html(_value + ' / ' +_total);
    })

    $('.c-progress-pie__base').percircle();


});
