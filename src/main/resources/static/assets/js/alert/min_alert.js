$(function() {

    $(document).on("click","#checksuccessBtn",function(){
        //console.log("확인버튼누름")
        alertSuccess('성공!');
    });

    $(document).on("click","#cancelBtn",function(){
        //console.log("취소버튼누름")
        $('#popupId').remove();
    });

    $(document).on("click","#successBtn",function(){
        $('#popupId').remove();
    });

    // $('#success').on('click', function() {
    //     //console.log("성공버튼 실행");
    //     alertSuccess('성공!');
    // });
    //
    // $('#fail').on('click', function() {
    //     //console.log("실패버튼 실행");
    //     alertCancel('실패!');
    // });
    //
    // $('#caution').on('click', function() {
    //     //console.log("경고버튼 실행");
    //     alertCaution("경고!");
    // });
    //
    // $('#confirm').on('click', function() {
    //     //console.log("확인버튼 실행")
    //     alertCheck('확인!');
    // });

});


function alertSuccess(text) { //성공창(삭제성공시),저장성공시

    var html = '';

    html +='<div id="popupId" class="popup popup--dim">';
        html +='<div class="popup__box">';
            html +='<div class="popup__content">';
                html +='<div class="popup__stat success"></div>';
                html +='<div class="popup__text">'+text+'</div>';
            html +='</div>';
            html +='<div class="popup__buttons">';
                html +='<button id="successBtn" class="popup__btn popup__btn--success">확인</button>';
            html +='</div>';
        html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);

}

function alertCancel(text) { //에러창(로그인만료),오류

    var html = '';

    html +='<div id="popupId" class="popup popup--dim">';
        html +='<div class="popup__box">';
            html +='<div class="popup__content">';
                html +='<div class="popup__stat cancel"></div>';
                html +='<div class="popup__text">'+text+'</div>';
           html +='</div>';
            html +='<div class="popup__buttons">';
               html +='<button id="successBtn" class="popup__btn popup__btn--success">확인</button>';
            html +='</div>';
        html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);

}

function alertCaution(text) { //경고창
    var cau = "!";

    var html = '';

    html +='<div id="popupId" class="popup popup--dim">';
        html +='<div class="popup__box">';
            html +='<div class="popup__content">';
                html +='<div class="popup__stat caution">'+cau+'</div>';
                html +='<div class="popup__text">'+text+'</div>';
            html +='</div>';
            html +='<div class="popup__buttons">';
                html +='<button id="successBtn" class="popup__btn popup__btn--success">확인</button>';
            html +='</div>';
        html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);

}

// 추후에 다시만들기 삭제 알림창.
function alertCheck(text) { //정말삭제할껀지확인하는창

    var html = '';

    html +='<div id="popupId" class="popup popup--dim">';
        html +='<div class="popup__box">';
            html +='<div class="popup__content">';
                html +='<div class="popup__stat check"></div>';
                html +='<div class="popup__text">'+text+'</div>';
            html +='</div>';
            html +='<div class="popup__buttons">';
                html +='<button id="checksuccessBtn" class="popup__btn popup__btn--success" value="true" onclick="a(this.value)">확인</button>';
                html +='<button id="cancelBtn" class="popup__btn popup__btn--cancel" value="false" onclick="a(this.value)">취소</button>';
            html +='</div>';
        html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
}
