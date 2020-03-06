/* raw rules */
var compStrings = [
	/* for things like 6 vs 3 shadow, list higher one first */
	"Ocean-Mage: 4 Ocean, 4 Mage, 2 Warden",
	"Ocean-6-Warden-2: 6 Ocean, 2 Warden",
	"Ocean-4-Warden-4: 4 Ocean, 4 Warden",
	"Mage-6: 6 Mage",
	"Ranger-Poison: 3 Poison, 4 Ranger",
	"Ranger-6-Misc: 6 Ranger",
	"Ranger-4-Misc: 4 Ranger",
	"Ranger-Glacial: 2 Glacial, 2 Warden, 4 Ranger",
	"Warden-Glacial: 4 Glacial, 4 Warden, 2 Ranger",
	"Glacial-6: 6 Glacial",
	"Woodland: 6 Woodland",
	"Summoner-Electric: 6 Summoner, 2 Electric",
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
	"Blademaster-Desert: 3 Blademaster, 2 Desert",
	"Azir-Friends: 2 Lunar, 2 Mystic, 1 Azir, 2 Warden",
];

var parseTrait = function(t) {
	var trait = {};
	t = t.trim();
	var space = t.indexOf(" ");
	trait.count = t.substring(0, space);
	trait.name = t.substring(space + 1);
	return trait;
};

var selectComp = function(traits, units) {

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
};

var compMatches = function(compTraits, compUnits, rule) {
	var matchCount = 0;
	rule.traits.forEach(function(ruleTrait) {
		var foundMatch = false;
		compTraits.forEach(function(compTrait) {
			if (compTrait.name == ruleTrait.name && compTrait.count >= ruleTrait.count) {
				foundMatch = true;
			}
		});
		compUnits.forEach(function(compUnit) {
			if (compUnit == ruleTrait.name) {
				foundMatch = true;
			}
		});
		if (foundMatch) {
			matchCount++;
		}
	});
	return matchCount == rule.traits.length;
};

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

var gameDivs = document.querySelectorAll(".profile__match-history-v2__item__detail");

var allData = [];

gameDivs.forEach(function(gameDiv) {
  		
	var gameId = Math.round(Math.random() * 100000); /* gameDiv.getAttribute("data-id"); */
  
	var gameRows = gameDiv.querySelectorAll("tr");
	gameRows.forEach(function(gameRow) {
		var compData = {};
		if (gameRow.querySelectorAll("td").length > 0) {
			compData.placement = gameRow.querySelector(".placement").innerHTML;
			compData.gameId = gameId;
			
			var traitImages = gameRow.querySelectorAll("div.tft-hexagon img");
			
			compData.traits = [];
			traitImages.forEach(function(traitImage) {
				var traitTitle = traitImage.getAttribute("data-original-title");
				var trait = parseTrait(traitTitle);
				compData.traits.push(trait);
			});

			compData.units = [];
			var unitImages = gameRow.querySelectorAll("div.tft-champion img");
			unitImages.forEach(function(unitImage) {
				var unitTitle = unitImage.getAttribute("data-original-title");
				compData.units.push(unitTitle);
			});
			
			compData.compName = selectComp(compData.traits, compData.units);
			
			allData.push(compData);
		}
	});

});

var output = "";
allData.forEach(function(data) {
	output += (data.gameId + "\t" + data.placement + "\t" + data.compName);
	output += "\n";
});

alert(output);