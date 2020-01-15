'use strict';

(function(Utils) {
	var localUrl = '/assets/js/charts/chartjs-chart-matrix.js';
	var remoteUrl = 'https://cdn.jsdelivr.net/npm/chartjs-chart-matrix/dist/chartjs-chart-matrix.js';

	function addScript(url, done, error) {
		var head = document.getElementsByTagName('head')[0];
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.onreadystatechange = function() {
			if (this.readyState === 'complete') {
				done();
			}
		};
		script.onload = done;
		script.onerror = error;
		script.src = url;
		head.appendChild(script);
		return true;
	}

	function loadError() {
		var msg = document.createTextNode('Error loading chartjs-chart-matrix');
		document.body.appendChild(msg);
		return true;
	}

	Utils.load = function(done) {
		addScript(localUrl, done, function(event) {
			event.preventDefault();
			event.stopPropagation();
			addScript(remoteUrl, done, loadError);
		});
	};
}(this.Utils = this.Utils || {}));
