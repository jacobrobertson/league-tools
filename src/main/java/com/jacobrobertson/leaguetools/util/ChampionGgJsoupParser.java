package com.jacobrobertson.leaguetools.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChampionGgJsoupParser {

	public static void main(String[] args) {
		ChampionGgJsoupParser p = new ChampionGgJsoupParser();
		p.run();
//		p.runOne("Brand");
	}
	
	public void run() {
		String homePage = downloadPage("https://champion.gg/");
		List<String> names = getChampNames(homePage);
		names = toUniqueSortedList(names);
		names.forEach(this::runOne);
	}
	
	private List<String> getChampNames(String page) {
		// <a href="/champion/Chogath">
		return parsePatternFinds(page, "<a href=\"/champion/([A-Za-z]*?)\">");
	}
	
	private List<String> parsePatternFinds(String page, String toFind) {
		return parsePatternFinds(page, toFind, 1000);
	}
	private List<String> toUniqueSortedList(List<String> list) {
		Set<String> set = new HashSet<>(list);
		List<String> foundList = new ArrayList<>(set);
		foundList.sort(String.CASE_INSENSITIVE_ORDER);
		return foundList;
	}
	private List<String> parsePatternFinds(String page, String toFind, int maxFind) {
		Matcher matcher = Pattern.compile(toFind, Pattern.DOTALL).matcher(page);

		List<String> foundList = new ArrayList<>();
		while (matcher.find()) {
			foundList.add(matcher.group(1));
			if (foundList.size() > maxFind) {
				break;
			}
		}
		return foundList;
	}
	
	
	public Champion runOne(String champName) {
		
		champName = Jsoup.parse(champName).text();
		
		Champion champ = new Champion();
		champ.championName = champName;
		String page = downloadPage("https://champion.gg/champion/" + champName);
		// <a href="/champion/Aatrox/Top">
		List<String> rolesList = parsePatternFinds(page, "<a href=\"/champion/" + champName + "/(.*?)\"");
		rolesList = toUniqueSortedList(rolesList);
		rolesList.forEach(role -> runOne(champ, role));
		return champ;
	}
	public void runOne(Champion champ, String roleName) {
		String page = downloadPage("https://champion.gg/champion/" + champ.championName + "/" + roleName);
		Document doc = Jsoup.parse(page);
		System.out.println(champ.championName + "/" + roleName);
		ChampionRole role = new ChampionRole();
		role.roleName = roleName;
		champ.roles.add(role);
		
		parseSkillOrders(doc, role);
		parseItems(doc, role);
		
		String patch = parsePatternFinds(page, "Patch\\s*<strong>(.*?)</strong>").get(0);
		
		output(champ, patch);
	}
	
	private void parseItems(Document doc, ChampionRole champ) {
		// <h2 class="champion-stats">Most Frequent Completed Build</h2>
		champ.frequentCompletedBuild = parseItems(doc, "Most Frequent Completed Build", "Build (Frequent)");
		champ.highestWinCompletedBuild = parseItems(doc, "Highest Win % Completed Build", "Build (Highest)");

		champ.frequentStarters = parseItems(doc, "Most Frequent Starters", "Start (Frequent)");
		champ.highestWinStarters = parseItems(doc, "Highest Win % Starters", "Start (Highest)");
	}
	private ItemSet parseItems(Document doc, String htmlName, String jsonName) {
		Elements els = doc.select(".champion-stats");
		ItemSet items = new ItemSet();
		
		Optional<Element> el = els.stream().filter(e -> e.text().equals(htmlName)).findFirst();
		if (!el.isPresent()) {
			return items;
		}
		
		Element buildElement = el.get().nextElementSibling();
		Elements buildRows = buildElement.children();
		
  		//<strong>46.16%</strong> Win Rate | <strong>2981</strong> Games
		Element buildText = buildRows.select(".build-text").first();
		items.winRate = buildText.child(0).text();
		
		Set<String> itemIdsSet = new HashSet<>();
		
		buildRows = buildRows.select("a");
		for (int i = 0; i < buildRows.size(); i++) {
			Element buildRow = buildRows.get(i);
			String dataId = buildRow.select("img").first().attr("data-id");
			
			if (itemIdsSet.add(dataId)) {
				items.itemIds.add(dataId);
				String itemUrl = buildRow.attr("href");
				int index = itemUrl.lastIndexOf('/');
				items.itemNames.add(itemUrl.substring(index + 1));
			}
		}

		items.title = jsonName;
		
		return items;
	}
	
	private void parseSkillOrders(Document doc, ChampionRole champ) {
		champ.frequentSkillOrder = parseSkillOrder(doc, "Most Frequent Skill Order", "Build (Frequent)");
		champ.highestWinSkillOrder = parseSkillOrder(doc, "Highest Win % Skill Order", "Build (Highest)");
	}
	private SkillOrder parseSkillOrder(Document doc, String htmlName, String jsonName) {
		Elements els = doc.select(".champion-stats");
		Element el = els.stream().filter(e -> e.text().equals(htmlName)).findFirst().get();
		
		Element skillOrders = el.nextElementSibling();
		Elements skillRows = skillOrders.children();

		StringBuilder buf = new StringBuilder("                  ");
		
		parseOneSkillRow(skillRows, 1, "Q", buf);
		parseOneSkillRow(skillRows, 2, "W", buf);
		parseOneSkillRow(skillRows, 3, "E", buf);
		parseOneSkillRow(skillRows, 4, "R", buf);
		
		SkillOrder skillOrder = new SkillOrder();
		skillOrder.skillOrderString = buf.toString();
		skillOrder.title = jsonName;
		
  		//<strong>46.16%</strong> Win Rate | <strong>2981</strong> Games
		Element buildText = skillOrders.nextElementSibling();
		if (buildText != null && buildText.children() != null && !buildText.children().isEmpty()) {
			skillOrder.winRate = buildText.child(0).text();
		} else {
			skillOrder.winRate = "?";
		}
		
		return skillOrder;
	}
	
	private void parseOneSkillRow(Elements skillRows, int index, String button, StringBuilder buf) {
		if (skillRows.isEmpty()) {
			return;
		}
		Element row = skillRows.get(index);
		Elements skillSelections = row.select(".skill > .skill-selections");
		Elements cols = skillSelections.first().children();
		for (int i = 0; i < cols.size(); i++) {
			Element col = cols.get(i);
			if (col.className().equals("selected")) {
				buf.setCharAt(i, button.charAt(0));
			}
		}
	}
	
	private String downloadPage(String url) {
		try {
			return IOUtils.toString(new URL(url), "utf-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static class Champion {
		public List<ChampionRole> roles = new ArrayList<>();
		public String championName;
	}

	public static class ChampionRole {
		public String roleName;
		
		public SkillOrder frequentSkillOrder;
		public SkillOrder highestWinSkillOrder;
		
		public ItemSet frequentCompletedBuild;
		public ItemSet highestWinCompletedBuild;

		public ItemSet frequentStarters;
		public ItemSet highestWinStarters;
	}
	public static class ItemSet {
		public List<String> itemIds = new ArrayList<>();
		// doesn't actually get used, it's more for debugging
		public List<String> itemNames = new ArrayList<>();
		public String title;
		public String winRate;
	}
	public static class SkillOrder {
		// "type": "Build (Frequent) - 42.32% win / Skills: QWEQQRQWQWRWWEEREE"}
		public String title;
		public String winRate;
		public String skillOrderString;
	}

	
	/*
	 { "map": "any", "isGlobalForChampions": false,	"blocks": [	
	 
	 {
	 	"items": [
				{
					"count": 1,
					"id": "1082"
				},
				{
					"count": 3,
					"id": "2003"
				},
				{
					"count": 1,
					"id": "3340"
				},
				{
					"count": 1,
					"id": "3341"
				},
				{
					"count": 1,
					"id": "3363"
				},
				{
					"count": 1,
					"id": "2004"
				},
				{
					"count": 1,
					"id": "2031"
				},
				{
					"count": 1,
					"id": "2055"
				},
				{
					"count": 1,
					"id": "2047"
				}
			],
			"type": "Start (Highest) - 50.83% win"
		},
		
		{
			"items": [
				{
					"count": 1,
					"id": "1054"
				},
				{
					"count": 1,
					"id": "2003"
				}
			],
			"type": "Start (Frequent) - 42.32% win"
		},
		
		{
			"items": [
				{
					"count": 1,
					"id": "3047"
				},
				{
					"count": 1,
					"id": "3146"
				},
				{
					"count": 1,
					"id": "3157"
				},
				{
					"count": 1,
					"id": "3100"
				},
				{
					"count": 1,
					"id": "3089"
				},
				{
					"count": 1,
					"id": "3026"
				}
			],
			"type": "Build (Highest) - 50.83% win / Skills: QWEQQRQWQWRWWEEREE"
		},
		{
			"items": [
				{
					"count": 1,
					"id": "3146"
				},
				{
					"count": 1,
					"id": "3020"
				},
				{
					"count": 1,
					"id": "3157"
				},
				{
					"count": 1,
					"id": "3100"
				},
				{
					"count": 1,
					"id": "3135"
				},
				{
					"count": 1,
					"id": "3089"
				}
			],
			"type": "Build (Frequent) - 42.32% win / Skills: QWEQQRQWQWRWWEEREE"
		},
		{
			"items": [
				{
					"count": 1,
					"id": "3362"
				},
				{
					"count": 1,
					"id": "3364"
				},
				{
					"count": 1,
					"id": "3342"
				},
				{
					"count": 1,
					"id": "3363"
				},
				{
					"count": 1,
					"id": "2139"
				},
				{
					"count": 1,
					"id": "2138"
				},
				{
					"count": 1,
					"id": "2140"
				}
			],
			"type": "Trinkets and Consumables"
		}
	],
	"associatedChampions": [],
	"title": "Akali Middle 9.18",
	"priority": false,
	"mode": "any",
	"isGlobalForMaps": true,
	"associatedMaps": [],
	"type": "custom",
	"sortrank": 1,
	"champion": "Akali"
}
	 */
	public void output(Champion champ, String patch) {
		champ.roles.forEach(role -> output(role, champ.championName, patch));
	}
	public void output(ChampionRole champ, String champName, String patch) {
		StringBuilder buf = new StringBuilder();
		buf.append("{\"map\": \"any\", \"isGlobalForChampions\": false,	\"blocks\": [");
	
		String[] trinkets = {"3340", "3364", "3363", "2055"}; 
//				"{\"count\": 1,\"id\": \"3340\"}," + // yellow ward
//				"{\"count\": 1,\"id\": \"3364\"}," + // oracle lense red
//				"{\"count\": 1,\"id\": \"3363\"}," + // farsight blue
//				"{\"count\": 1,\"id\": \"2055\"}"    // pink control ward
				;
		String[] potions = {"2003", "2031", "2033", "2047"}; 
//				"{\"count\": 1,\"id\": \"2003\"}," + // health potion red
//				"{\"count\": 1,\"id\": \"2031\"}," + // refillable potion green
//				"{\"count\": 1,\"id\": \"2033\"}"    // corrupting potion
		// 2047 - oracles extract
//				;
		
		appendItems(champ.highestWinStarters, null, buf, trinkets);
		appendItems(champ.frequentStarters, null, buf, potions);
		appendItems(champ.highestWinCompletedBuild, champ.highestWinSkillOrder.skillOrderString, buf);
		appendItems(champ.frequentCompletedBuild, champ.frequentSkillOrder.skillOrderString, buf);
		
		buf.append("{\"items\": [{\"count\": 1, \"id\": \"2139\"}, {\"count\": 1, \"id\": \"2138\"}, {\"count\": 1, \"id\": \"2140\"}], \"type\": \"Potions and Elixers\"}],");
		
		buf.append("\"associatedChampions\": [], \"title\": \"");
		buf.append(champName + " " + patch);
		buf.append("\", \"priority\": false, \"mode\": \"any\", \"isGlobalForMaps\": true, \"associatedMaps\": [], \"type\": \"custom\", \"sortrank\": 1, \"champion\": \"");
		buf.append(champName);
		buf.append("\"}");
		
		File target = new File("./target");
		if (!target.isDirectory() || !target.exists()) {
			throw new RuntimeException("Can't write to target");
		}
		
		File champDir = new File(target, "Champions/" + champName + "/Recommended");
		champDir.mkdirs();
		
		File file = new File(champDir, champ.roleName + patch.replace('.', '_') + ".json");
		
		try {
			IOUtils.write(buf.toString().getBytes(), new FileOutputStream(file));
		} catch (IOException e) {
			throw new RuntimeException("Problem writing to file", e);
		}
	}
	
	/*
	 {
			"items": [
				{
					"count": 1,
					"id": "1054"
				},
				{
					"count": 1,
					"id": "2003"
				}
			],
			"type": "Start (Frequent) - 42.32% win"
		},
	 */
	public void appendItems(ItemSet items, String skillInfo, StringBuilder buf, String... additionalItems) {
		buf.append("{\"items\":[");
		
		for (int i = 0; i < items.itemIds.size(); i++) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("{ \"count\": 1,");
			buf.append("\"id\": \"");
			buf.append(items.itemIds.get(i));
			buf.append("\"}");
		}
		
		for (int i = 0; i < additionalItems.length; i++) {
			if (!items.itemIds.contains(additionalItems[i])) {
				buf.append(",");
				buf.append("{ \"count\": 1,");
				buf.append("\"id\": \"");
				buf.append(additionalItems[i]);
				buf.append("\"}");
			}
		}
		
		buf.append("], \"type\": \"");
		buf.append(items.title);
		buf.append(" - ");
		buf.append(items.winRate);
		
		if (skillInfo != null) {
			buf.append(" / Skills: ");
			buf.append(skillInfo);
		}
		buf.append("\"");
		
		buf.append("}, ");
	}
	
	
}
