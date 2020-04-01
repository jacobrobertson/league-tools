function traitsParser() {

	var edges = [];
	var traitDivs = $("div.guide-synergy-table__synergy");
	traitDivs.each(function () {
		var traitDiv = $(this);
		var traitName = traitDiv.find("div.tft-hexagon img").attr("alt");

		var edgeDivs = traitDiv.find("div.tft-champion");
		edgeDivs.each(function () {
			var edgeDiv = $(this);
			var edge = {};
			edge.unit = edgeDiv.find("img").attr("alt");
			edge.trait = traitName;
			edges.push(edge);
		});
	});

	
	var output = "";
	edges.forEach(function(edge) {
		output = output + "{\"" + edge.unit + "\",\"" + edge.trait + "\"},\n";
	});
	
	alert(output);
	
}