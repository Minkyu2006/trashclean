$(function() {

});


function alertSusses() { //성공창(삭제성공시),저장성공시

}

function alertCancel() { //에러창(로그인만료),오류

}

function alertCaution() { //경고창

}

function alertCheck() { //정말삭제할껀지확인하는창
    // var popupType = $(this).data('id');
    // console.log("popupType : "+popupType);
    // $('.popup__stat').addClass('popup__stat--' + popupType);
    // $('.popup').addClass('popup--open');
    // $('.popup__text').text(text);

    var $alertTest = $('#alertTest');
    var html = '';
    html +='<div class="popup popup--dim">'
    html +='<div class="popup__box">'
        html +='<div class="popup__content">'
            html +='<div class="popup__stat"></div>'
            html +='<div class="popup__text">저장하시겠습니까?</div>'
        html +='</div>'
        html +='<div class="popup__buttons">'
            html +='<button id="successBtn" class="popup__btn popup__btn--success">확인</button>'
            html +='<button id="cancelBtn" class="popup__btn popup__btn--cancel">취소</button>'
        html +='</div>'
    html +='</div>'
    html +='</div>'
    $alertTest.html(html);

    $('.popup__stat').addClass('popup__stat--confirm');

    // if (confirm('저장하시겠습니까?')) {
    //     console.log("네")
    // } else {
    //     console.log("아니오")
    // }
}