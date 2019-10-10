/**
 * @author InSeok <kr.chis@gmail.com>
 * Date : 2019-05-03
 * Time : 10:56
 * Remark : 장기조사데이터에서 그래프를 그리는 자바스크립트함수
 */

//시간 비례 라인그래프
function line_graph_call(targetid, title,unit,data_columns){
    var chart = c3.generate({
        bindto: '#' + targetid,
        title: {
            text: title,
            position: 'top-center'
        },
        data: {
            x: 'x',
            columns: data_columns
        },
        axis: {
            x: {
                tick: {
                    format: function(d) {
                        if (d > 364) {
                            var year = Math.floor(d/365);

                            return year + '년';
                        }else if (d > 29 && d < 364){
                            var month = Math.floor(d/30);
                            return month + '개월';
                        }else {
                            return d + '일';
                        }
                    }
                },
                padding: {
                    left: 80,
                    right: 80
                }
            },
            y: {
                // max: 70,
                // min: 0,
                label: {
                    text: unit,
                    position: 'outer-middle'
                }
            }
        },
        grid: {
            y: {
                show: true
            }
        },
        legend: {
            position: 'bottom'
        }
    })
}

//시간 비례 라인그래프
function line_graph2_call(targetid, title,unit,data_columns){
    var chart = c3.generate({
        bindto: '#' + targetid,
        title: {
            text: title,
            position: 'top-center'
        },
        data: {
            x: 'x',
            columns: data_columns
        },
        axis: {
            x: {
                type: 'category'
            },
            y: {
                // max: 70,
                // min: 0,
                label: {
                    text: unit,
                    position: 'outer-middle'
                }
            }
        },
        grid: {
            y: {
                show: true
            }
        },
        legend: {
            position: 'bottom'
        }
    })
}

//박대그래프
function bar_graph_call(targetid,title,unit,data_columns,categories){
    var barchart = c3.generate({
        bindto: '#' + targetid,
        title: {
            text: title,
            position: 'top-center'
        },
        data: {
            columns: data_columns,
            type: 'bar'
        },
        bar: {
            width: {
                ratio: .3
            }
        },
        axis: {
            x: {
                type: 'category',
                categories: categories
            },
            y: {

                label: {
                    text: unit,
                    position: 'outer-middle'
                }
            }
        },
        grid: {
            y: {
                show: true
            }
        },
        legend: {
            position: 'bottom'
        }
    })
}

//Line 그래프의 하단에 Raw Data 표기 하는 함수
function line_raw_display(targetid,arraydata){
    var $schList = $('#'+targetid);
    var html = '';
    $.each(arraydata, function( index,value) {
        if (index == 0) {
            html += '<tr>';
            $.each(value, function( subindex,subvalue) {
                if (subindex == 0) {
                    html += '<th></th>';
                }else {
                    html += '<th>' + periodtoName(subvalue) + '</th>';
                }

            });
            html += '</tr>';
        }else {
            html += '<tr>';
            $.each(value, function( subindex,subvalue) {
                if (subindex == 0) {
                    html += '<th>' + echoNull2Blank(subvalue) + '</th>';
                }else {
                    if (subvalue == "null") {
                        subvalue = "";
                    }
                    html += '<td>' + echoNull2Blank(subvalue) + '</td>';
                }
            });
            html += '</tr>';

        }
    });
    $schList.html(html);

}
function periodtoName(d){
    if (d > 364) {
        var year = Math.floor(d/365);

        return year + '년';
    }else if (d > 29 && d < 364){
        var month = Math.floor(d/30);
        return month + '개월';
    }else {
        return d + '일';
    }
}


//Line 그래프의 하단에 Raw Data 표기 하는 함수
function line_raw2_display(targetid,arraydata){
    var $schList = $('#'+targetid);
    var html = '';
    $.each(arraydata, function( index,value) {
        if (index == 0) {
            html += '<tr>';
            $.each(value, function( subindex,subvalue) {
                if (subindex == 0) {
                    html += '<th></th>';
                }else {
                    html += '<th>' + subvalue + '</th>';
                }

            });
            html += '</tr>';
        }else {
            html += '<tr>';
            $.each(value, function( subindex,subvalue) {
                if (subindex == 0) {
                    html += '<th>' + echoNull2Blank(subvalue) + '</th>';
                }else {
                    if (subvalue == "null") {
                        subvalue = "";
                    }
                    html += '<td>' + echoNull2Blank(subvalue) + '</td>';
                }
            });
            html += '</tr>';

        }
    });
    $schList.html(html);

}

//Bar 그래프의 데이터를 라인그래프데이터로 변환
function convedrt_bar_to_line(arraydata,categorys){
    var datax = new Array();

    var resultdata = new Array();

    for(var i = 0 ; i < categorys.length   ;i ++) {
        if (i == 0) {
            datax[i] ="x";
            datax[i+1] =categorys[i];
        }else{
            datax[i+1] =categorys[i];
        }
    }
    resultdata[0] = datax;

    //내용
    for(var i = 0 ; i < arraydata.length  ;i ++) {

        var data = new Array();
        for(var j = 0 ; j < arraydata[i].length ; j ++){
            data[j] = arraydata[i][j]
        }
        resultdata[i+1] = data;
    }
    return resultdata;



}

//Bar 그래프의 하단에 Raw Data 표기 하는 함수
function bar_raw_display(targetid,arraydata,categorys){
    var $schList = $('#'+targetid);
    var html = '';

    //타이틀
    html += '<tr>';
    html += '<th></th>';
    for(var i = 0 ; i < categorys.length ;i ++){
        html += '<th>'+ echoNull2Blank(categorys[i]) +'</th>';
    }
    html += '</tr>';

    //내용
    for(var i = 0 ; i < arraydata.length  ;i ++) {
        html += '<tr>';

        for(var j = 0 ; j < arraydata[i].length ; j ++){
            if ( j== 0 ) {
                html += '<th>' + echoNull2Blank(arraydata[i][j]) + '</th>';
            } else{
                if (arraydata[i][j] == "null") {
                    arraydata[i][j] = "";
                }

                html += '<td>' + echoNull2Blank(arraydata[i][j]) + '</td>';
            }
        }

        html += '</tr>';

    }

    $schList.html(html);

}


//Bar 그래프의 하단에 Raw Data 표기 하는 함수 ( X Y 축변경)
function bar_raw_displayXYConvert(targetid,arraydata,categorys){
    var $schList = $('#'+targetid);
    var html = '';

    for(var i = 0 ; i < categorys.length +1 ;i ++){
        html += '<tr>';
        //타이틀
        if (i==0){
            html += '<th></th>';
            for( var j =0 ; j < arraydata.length;j++ ){
                html += '<th>' + echoNull2Blank(arraydata[j][i]) + '</th>';
            }

        }
        //데이타
        else{
            html += '<th>'+ echoNull2Blank(categorys[i-1]) +'</th>';
            for( var j =0 ; j < arraydata.length;j++ ){
                html += '<td>' + echoNull2Blank(arraydata[j][i]) + '</td>';
            }

        }
        html += '</tr>';

    }

    $schList.html(html);

}