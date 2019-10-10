$(function() {
	// c3 line chart
	var chart = c3.generate({
		bindto: '#c3-line',
		title: {
			text: '노출환경에 따른 주기별 압축강도',
			position: 'top-center'
		},
		data: {
			x: 'x',
			columns: [
				['x', 28, 365, 3650],
				['내륙환경', 32.6, 38.5, 42.6],
				['해안환경', 31.9, 38.0, 57.2]
			]
		},
		axis: {
			x: {
				tick: {
					format: function(d) {						
						if (d > 364) {
							var year = Math.floor(d/365);
							
							return year + '년';
						} else {
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
				max: 70,
				min: 0,
				label: {
					text: '압축강도(Mpa)',
					position: 'outer-middle'
				}
			}
		},
		grid: {
			x: {
				show: true
			},
			y: {
				show: true
			}
		},
		legend: {
			position: 'right'
		}
	})

	var barchart = c3.generate({
		bindto: '#c3-bar',
		title: {
			text: '10년차 노출환경에 따른 주기별 압축강도',
			position: 'top-center'
		},
		data: {
			columns: [
				['고로슬래그', 28.2, 57.9],
				['플라이애쉬', 57.1, 58.4]
			],
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
				categories: ['내륙환경', '해안환경']
			},
			y: {
				max: 70,
				label: {
					text: '압축강도(Mpa)',
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
			position: 'right'
		}
	})
})