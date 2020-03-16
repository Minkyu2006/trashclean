$(function() {

    $(document).on("click","#checkSuccessBtn",function(){
        startDel($("#delId").val(),true)
    });
    $(document).on("click","#checkCancelBtn",function(){
        startDel($("#delId").val(),false)
    });

    $(document).on("click","#successBtn",function(id){
        $('#popupId').remove();
    });

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

// 삭제 알림창.
function alertCheck(text,id) { //정말삭제할껀지확인하는창
    var html = '';

    html +='<div id="popupId" class="popup popup--dim">';
        html +='<div class="popup__box">';
            html +='<div class="popup__content">';
                html +='<div class="popup__stat check"></div>';
                html +='<div class="popup__text">'+text+'</div>';
            html +='</div>';
            html +='<div class="popup__buttons">';
                html +='<input type="hidden" id="delId" value="'+id+'" />';
                html +='<button id="checkSuccessBtn" class="popup__btn popup__btn--success">확인</button>';
                html +='<button id="checkCancelBtn" class="popup__btn popup__btn--cancel">취소</button>';
            html +='</div>';
        html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
}
