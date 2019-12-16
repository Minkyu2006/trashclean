$(function() {

    $('#checksuccessBtn').on('click', function() {
        alertSusses("성공성공!");
    })

    $('#successBtn').on('click', function() {
        $('.popup').removeClass('popup--open');
    })

    $('.popup__btn--cancel').on('click', function() {
        $('.popup').removeClass('popup--open');
    })

    $('#success').on('click', function() {
        console.log("성공버튼 실행");
        alertSusses('성공!');
    })

    $('#fail').on('click', function() {
        console.log("실패버튼 실행");
        alertCancel('실패!');
    })

    $('#caution').on('click', function() {
        console.log("경고버튼 실행");
        alertCaution("경고!");
    })

    $('#confirm').on('click', function() {
        console.log("확인버튼 실행")
        alertCheck('확인!');
    })

});


function alertSusses(text) { //성공창(삭제성공시),저장성공시
    var $alertTest = $('#alertTest');

    var html = '';

    html +='<div class="popup popup--dim">'
    html +='<div class="popup__box">'
    html +='<div class="popup__content">'
    html +='<div class="popup__stat"></div>'
    html +='<div class="popup__text">'+text+'</div>'
    html +='</div>'
    html +='<div class="popup__buttons">'
    html +='<button id="successBtn" class="popup__btn popup__btn--success">확인</button>'
    html +='</div>'
    html +='</div>'
    html +='</div>'

    $alertTest.html(html);

    $('.popup__stat').addClass('popup__stat--confirm');
}

function alertCancel(text) { //에러창(로그인만료),오류
    var $alertTest = $('#alertTest');

    var html = '';

    html +='<div class="popup popup--dim">'
    html +='<div class="popup__box">'
    html +='<div class="popup__content">'
    html +='<div class="popup__stat"></div>'
    html +='<div class="popup__text">'+text+'</div>'
    html +='</div>'
    html +='<div class="popup__buttons">'
    html +='<button id="successBtn" class="popup__btn popup__btn--success">확인</button>'
    html +='</div>'
    html +='</div>'
    html +='</div>'

    $alertTest.html(html);

    $('.popup__stat').addClass('popup__stat--confirm');
}

function alertCaution(text) { //경고창
    var $alertTest = $('#alertTest');

    var html = '';

    html +='<div class="popup popup--dim">'
    html +='<div class="popup__box">'
    html +='<div class="popup__content">'
    html +='<div class="popup__stat"></div>'
    html +='<div class="popup__text">'+text+'</div>'
    html +='</div>'
    html +='<div class="popup__buttons">'
    html +='<button id="successBtn" class="popup__btn popup__btn--success">확인</button>'
    html +='</div>'
    html +='</div>'
    html +='</div>'

    $alertTest.html(html);

    $('.popup__stat').addClass('popup__stat--confirm');
}

function alertCheck(text) { //정말삭제할껀지확인하는창
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
                html +='<div class="popup__text">'+text+'</div>'
            html +='</div>'
            html +='<div class="popup__buttons">'
                html +='<button id="checksuccessBtn" class="popup__btn popup__btn--success">확인</button>'
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

//
// var popMsg = window.createPopup();
//
// // 인자야 임의로 알아서 하시면 됩니다.
// // x, y, width, height, : 시작, 크기~
// // msg : 메세지
// // flag : 닫기 클릭 시의 포커스
// function showAlert(x, y, width, height, msg, flag){
//     var innerCtnt = "<table border='0' cellspacing='0' cellpadding='0' onclick=parent.closeAlert('";
//     innerCtnt += flag;
//     innerCtnt += "');><tr><td width='395' height='192' background='./img/alert.gif' border='0' align='center'><br><br>";
//     innerCtnt += "<textarea id='alertBox' style='border:0px;font-size:12px;overflow:hidden;' rows='2' cols='46'>";
//     innerCtnt += "</textarea></td></tr></table>";
//     var popBody = popMsg.document.body;
//     popBody.style.border = "solid 0px #ffffff";
//     popBody.innerHTML = innerCtnt;
//     popMsg.document.getElementById('alertBox').value = msg;
//     popMsg.show(x, y, width, height, document.body);
// }
//
// // showAlert 에 의해 전달됨
// function closeAlert(flag){
//
//     popMsg.hide();
//
//     if(flag=="dbName"){
//         fm.dbName.focus();
//     }
//     if(flag=="tblName"){
//         fm.tblName.focus();
//
//     }
//     if(flag=="pDate"){
//         fm.pDate.focus();
//     }
// }