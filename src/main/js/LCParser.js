var compStrings = [
	/* for things like 6 vs 3 shadow, list higher one first */
	"Cybernetic-6-Blademaster-Chrono: 6 Cybernetic, 3 Blademaster, 2 Chrono",
	
	// set 2
	"Ocean-Mage: 4 Ocean, 4 Mage, 2 Warden",
	"Ocean-6-Warden-2: 6 Ocean, 2 Warden",
	"Ocean-4-Warden-4: 4 Ocean, 4 Warden",
	"Mage-6: 6 Mage",
	"Ranger-Poison: 3 Poison, 4 Ranger",
	"Ranger-6-Misc: 6 Ranger",
	"Ranger-4-Misc: 4 Ranger",
	"Ranger-Glacial: 2 Glacial, 2 Warden, 4 Ranger",
	"Warden-Glacial: 4 Glacial, 4 Warden, 2 Ranger",
	"Warden-Mystic: 4 Warden, 4 Mystic",
	"Warden-Mage: 4 Warden, 3 Mage, 3 Ocean, 2 Mountain",
	"Warden-6: 6 Warden",
	"Glacial-6: 6 Glacial",
	"Woodland: 6 Woodland",
	"Summoner-Electric: 6 Summoner, 2 Electric",
	"Zed-Summoner-Electric: 3 Summoner, 3 Electric, 1 Zed",
	"Summoner-Assassin-Desert: 3 Summoner, 3 Assassin, 2 Desert",
	"Summoner-Misc: 6 Summoner",
	"Light-6-Misc: 6 Light",
	"Light-Yorick: 4 Light, 1 Yorick",
	"Inferno-6-Misc: 6 Inferno",
	"Inferno-Shadow: 6 Inferno, 3 Shadow",
	"Shadow-6-Misc: 6 Shadow",
	"Shadow-3-Misc: 3 Shadow",
	"Shadow-6-Inferno-3: 6 Shadow, 3 Inferno",
	"Shadow-3-Inferno-3: 3 Shadow, 3 Inferno",
	"Predator-Transitioned: 3 Poison, 2 Crystal, 1 Ranger",
	"Predator: 3 Predator",
	"Berserker-6-Misc: 6 Berserker",
	"Berserker-Glacial: 6 Berserker, 4 Glacial",
	"Berserker-6-Poison: 6 Berserker, 3 Poison",
	"Berserker-3-Poison: 3 Berserker, 3 Poison, 2 Glacial, 2 Ranger",
	"Blademaster-Desert-4: 2 Blademaster, 4 Desert, 1 Azir",
	"Blademaster-Desert-2: 4 Blademaster, 2 Desert",
	"Azir-Friends: 2 Lunar, 2 Mystic, 1 Azir, 2 Warden",
	"Zed-Poison: 1 Zed, 3 Poison, 3 Electric",
	"Zed-Summoner-Electric: 3 Summoner, 3 Electric, 1 Zed",
	"Zed-Assassin-Electric: 3 Assassin, 3 Electric, 1 Zed",
];

function parseTrait(t) {
	var trait = {};
	t = t.trim();
	var space = t.indexOf(" ");
	trait.count = t.substring(0, space);
	trait.name = t.substring(space + 1);
	return trait;
}

function selectComp(traits, units) {

	var bestComp = false;
	compRules.forEach(function(compRule) {
		var matches = compMatches(traits, units, compRule);
		if (matches) {
			if (!bestComp || compRule.traits.length > bestComp.traits.length) {
				bestComp = compRule;
			}
		}
	});
	if (bestComp) {
		return bestComp.name;
	} else {
		return "UNKNOWN-COMP";
	}
}

function compMatches(compTraits, compUnits, rule) {
	var matchCount = 0;
	rule.traits.forEach(function(ruleTrait) {
		var foundMatch = false;
		compTraits.forEach(function(compTrait) {
			if (compTrait.name == ruleTrait.name && compTrait.count >= ruleTrait.count) {
				foundMatch = true;
			}
		});
		compUnits.forEach(function(compUnit) {
			if (compUnit.title == ruleTrait.name) {
				foundMatch = true;
			}
		});
		if (foundMatch) {
			matchCount++;
		}
	});
	return matchCount == rule.traits.length;
}

/* clean the rules */
var compRules = [];
compStrings.forEach(function(s) {
	var rule = {};
	rule.traits = [];
	var colon = s.indexOf(":");
	rule.name = s.substring(0, colon);
	var traitStrings = s.substring(colon + 1).split(",");
	traitStrings.forEach(function(t) {
		var trait = parseTrait(t);
		rule.traits.push(trait);
	});
	compRules.push(rule);
});

//cdn.lolchess.gg/images/tft/stars/cost5_stars2@2x.png 2x
function parseStars(link, unit) {
	var costPos = link.indexOf("/cost");
	unit.cost = link.substring(costPos + 5, costPos + 6);

	var starsPos = link.indexOf("_stars");
	unit.stars = link.substring(starsPos + 6, starsPos + 7);
}

function showComps() {
	var gameDivs = document.querySelectorAll(".profile__match-history-v2__item__detail");
	
	var allData = [];
	
	gameDivs.forEach(function(gameDiv) {
	  		
		var gameId = Math.round(Math.random() * 100000); /* gameDiv.getAttribute("data-id"); */
	  
		var gameRows = gameDiv.querySelectorAll("tr");
		gameRows.forEach(function(gameRow) {
			var compData = {};
			if (gameRow.querySelectorAll("td").length > 0) {
				compData.placement = gameRow.querySelector(".placement").innerHTML.trim();
				compData.gameId = gameId;
				
				compData.summonerName = gameRow.querySelector(".summoner a").innerHTML.trim();
				
				var traitImages = gameRow.querySelectorAll("div.tft-hexagon img");
				
				compData.traits = [];
				traitImages.forEach(function(traitImage) {
					var traitTitle = traitImage.getAttribute("alt"); // "data-original-title");
					var trait = parseTrait(traitTitle);
					compData.traits.push(trait);
				});
	
				compData.units = [];
				var unitDivs = gameRow.querySelectorAll("div.unit");
				unitDivs.forEach(function(unitDiv) {
					var unit = {};
					
//					 <img src="//cdn.lolchess.gg/images/tft/stars/cost5_stars2.png" 
					var starsImg = unitDiv.querySelector("img.stars");
					var starsLink = starsImg.getAttribute("src");
					parseStars(starsLink, unit);

					var unitImage = unitDiv.querySelector("div.tft-champion img");
					unit.title = unitImage.getAttribute("data-original-title");
					

					compData.units.push(unit);
				});
				
				compData.compName = selectComp(compData.traits, compData.units);
				
				allData.push(compData);
			}
		});
	
	});
	
	var output = "";
	allData.forEach(function(data) {
		output += (data.gameId + ",\t" + data.placement + ",\t\"" + data.summonerName + "\",\t" + data.compName);
		output += "<br/>";
	});
	
	var selectText = function(clicked) {
		var select = window.getSelection();
		var range  = document.createRange();
		range.selectNodeContents(clicked);
		select.addRange(range);
	};
	
	var dataBox = document.querySelector('#dataBox');
	if (!dataBox) {
		dataBox = document.createElement('div');
		document.body.prepend(dataBox);
		dataBox.setAttribute('id', 'dataBox');
		dataBox.setAttribute("style", "width:100%; display: inline-block; white-space: nowrap;");
		dataBox.setAttribute("onClick", "selectText(this);");
	}
	$('#dataBox').show();
	dataBox.innerHTML = output;
}

function init() {
	var showCompsButton = $('#showComps');
	if (showCompsButton.length == 0) {
		showCompsButton = $('<button/>', {
			text: "Show Comps",
	        id: "showComps",
	        click: function () { showComps(); } 
		});
		$('body').prepend(showCompsButton);
	}
	$('#dataBox').hide();
}

init();