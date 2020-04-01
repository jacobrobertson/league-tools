javascript: (function() { 
	var scripts = [
		'http://localhost:8080/static-web-helper/LCParserBootstrap.js',
	];
	scripts.forEach(function(url) {
		document.body.appendChild(document.createElement('script')).setAttribute('src', url);
	});
}());

