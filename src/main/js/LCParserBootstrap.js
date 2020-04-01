javascript: (function() { 
	var scripts = [
		'http://localhost:8080/static-web-helper/LCParser.js',
		'http://localhost:8080/static-web-helper/LCTraitsParser.js',
	];
	scripts.forEach(function(url) {
		document.body.appendChild(document.createElement('script')).setAttribute('src', url);
	});
	var buttons = [
		"gamesParser", "traitsParser"
	];
	buttons.forEach(function(button) {
		var r = $("#" + button + "Button");
		if (r.length == 0) {
			r = $('<input/>').attr({
	            type: "button",
	            onClick: button + "();",
	            value: button,
	            id: button + "Button"
			});
			$("body").prepend(r);
		}
	});
}());

