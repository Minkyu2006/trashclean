$(function() {

	//상태값 차트
	circleChart();

	//쓰레기양 차트
	barChart();

});

//상태값 차트
function circleChart(){
	var piechart = c3.generate({
		bindto: "#pie_chart",
		data: {
			columns: [
				['정상', 6],
				['관심', 3],
				['심각', 1]
			],
			type: 'pie'
		},
		tooltip: {
			format: {
				value: function (value) {
					return value + '개';
				}
			}
		},
		color: {
			pattern: ['#007e37', '#ffc600', '#e35f5f']
		}
	});
}

//쓰레기양 차트
function barChart() {
	var barchart = c3.generate({
		bindto: "#bar_chart",
		data: {
			columns: [
				['쓰레기양', 80, 70, 20, 50, 60, 10, 90,30,40,60]
			],
			type: "bar"
		},
		tooltip: {
			format: {
				value: function (value) {
					return value + '%'
				}
			}
		},
		axis: {
			x: {
				label: {
					text: '장비명',
					position: 'outer-center'
				},
				type: 'category',
				categories: ['s1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10'],
				tick: {
					centered: true
				}
			},
			y: {
				label: {
					text: '쓰레기양',
					position: 'outer-middle'
				},
				max: 100,
				min: 10
			}
		},
		legend: {
			hide: true
		},
		color: {
			pattern: ['#007e37']
		}
	});
}

	// var token = $("meta[name='_csrf']").attr("content");
	// var header = $("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
	//
	// $.ajax({
	// 	url:'/api/statistics/dataGraph',
	// 	type : 'post',
	// 	cache:false,
	// 	error:function(request){
	// 		ajaxErrorMsg(request);
	// 	},
	// 	success: function(res){
	// 		if (!Ajax.checkResult(res)) {
	// 			return;
	// 		}
	// 		circle_graph_call(res.data.circle_data_columns);
	// 		disaster_graph_call(res.data.disaster_data_columns);
	// 		fac_graph_call(res.data.fac_data_columns);
	// 		team_graph_call(res.data.team_data_columns,res.data.teamsData);
	// 		month_graph_call(res.data.month_data_columns);
	// 	}
	//})

	// $('.stat__map-tab').on('click', 'a.stat__tab-link', function(e) {
	// 	var $parent = $(this).parent();
	// 	var _index = $parent.index();
	// 	var $content = $('.stat__tab-content li').eq(_index);
	//
	// 	$parent.add($content).addClass('active').siblings().removeClass('active');
	//
	// 	e.preventDefault();
	// }).find('.stat__tab-link').eq(1).trigger('click');
//});

// 원형 그래프데이터
// function circle_graph_call(circle_data_columns) {
// 	var pie = c3.generate({
// 		bindto: "#pie_chart1",
// 		data: {
// 			columns: circle_data_columns,
// 			type: 'pie'
// 		},
// 		tooltip: {
// 			format: {
// 				value: function (value) {
// 					return value + '건';
// 				}
// 			}
// 		}
// 	})
// }
//
// 재해재난 그래프데이터
// function disaster_graph_call(disaster_data_columns) {
// 	var chart = c3.generate({
// 		bindto: "#bar_chart1",
// 		data: {
// 			columns: disaster_data_columns,
// 			type: 'bar'
// 		},
// 		axis: {
// 			x: {
// 				type: 'category',
// 				categories: ['붕괴', '화재/폭발', '지진', '싱크홀', '교통사고', '홍수/가뭄', '환경오염']
// 			}
// 		},
// 		tooltip: {
// 			format: {
// 				value: function (value) {
// 					return value + '건';
// 				}
// 			}
// 		}
// 	})
// }
//
// 조사시설물 그래프데이터
// function fac_graph_call(fac_data_columns) {
// 	var chart = c3.generate({
// 		bindto: "#bar_chart2",
// 		data: {
// 			columns: fac_data_columns,
// 			type: 'bar'
// 		},
// 		axis: {
// 			x: {
// 				type: 'category',
// 				categories: ['교량', '옹벽', '비탈면', '터널', '도로', '기타도로시설', '건축물', '지반', '지하시설물', '기타']
// 			}
// 		},
// 		tooltip: {
// 			format: {
// 				value: function (value) {
// 					return value + '건';
// 				}
// 			}
// 		}
// 	})
// }
//
// 부서별 출동현황 그래프데이터
// function team_graph_call(team_data_columns,teamsData) {
// 	var chart = c3.generate({
// 		bindto: "#bar_team",
// 		data: {
// 			columns: [team_data_columns],
// 			type: 'bar'
// 		},
// 		axis: {
// 			x: {
// 				type: 'category',
// 				categories: teamsData
// 			}
// 		},
// 		tooltip: {
// 			format: {
// 				value: function (value) {
// 					return value + '건';
// 				}
// 			}
// 		},
// 		legend: {
// 			hide: true
// 		}
// 	})
// }
//
// 월별 출동현황 그래프데이터
// function month_graph_call(month_data_columns) {
// 	var chart = c3.generate({
// 		bindto: "#bar_chart3",
// 		data: {
// 			columns: month_data_columns,
// 			type: 'bar'
// 		},
// 		axis: {
// 			x: {
// 				type: 'category',
// 				categories: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
// 			}
// 		},
// 		tooltip: {
// 			format: {
// 				value: function (value) {
// 					return value + '건';
// 				}
// 			}
// 		}
// 	})
// }