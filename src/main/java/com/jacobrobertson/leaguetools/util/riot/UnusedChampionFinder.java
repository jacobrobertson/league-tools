package com.jacobrobertson.leaguetools.util.riot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jacobrobertson.leaguetools.util.riot.dto.Champion;
import com.jacobrobertson.leaguetools.util.riot.dto.Match;
import com.jacobrobertson.leaguetools.util.riot.dto.MatchUnit;
import com.jacobrobertson.leaguetools.util.riot.dto.Summoner;

public class UnusedChampionFinder {

	public static void main(String[] args) throws Exception {
		
		Downloader d = new Downloader();
		
		int myMatchesToDownload = 20;
		int otherMatchesToDownload = 20;
		int matchesToCompare = 10;
		int playersToDownload = 10;
		boolean onlyRanked = false;
		
		// get my puuid
		Summoner me = d.downloadSummonerByName("vanrobestone");
		System.out.println("Got me: " + me);
		
		// download my list of matches
		Set<String> existingMatchIds = new HashSet<>();
		List<Match> myMatches = d.downloadMatches(me.getPuuid(), existingMatchIds, myMatchesToDownload);
		myMatches = d.filterToRankedSorted(myMatches, matchesToCompare, onlyRanked);
		System.out.println("My matches: " + myMatches.size());
		
		// get all the matches
		List<String> otherPlayers = d.findUniquePuuids(myMatches).stream()
				.filter(puuid -> !puuid.equals(me.getPuuid()))
				.limit(playersToDownload)
				.collect(Collectors.toList());
		List<Match> allMatches = new ArrayList<>(myMatches);
		otherPlayers.forEach(puuid -> {
			List<Match> theseMatches = d.downloadMatches(puuid, existingMatchIds, otherMatchesToDownload);
			theseMatches = d.filterToRankedSorted(theseMatches, matchesToCompare, onlyRanked);
			allMatches.addAll(theseMatches);
		});
		System.out.println("All matches: " + allMatches.size());
		
		// for every match get the champion count
		// - the math is a bit weird, it's (3 ^ (tier - 1))
		Map<String, UnitInfo> championCount = new HashMap<>();
		List<Champion> champions = d.getChampions();
		Map<String, Champion> championsById = new HashMap<>();
		champions.forEach(c -> {
			UnitInfo ui = new UnitInfo();
			ui.name = c.getName();
			championCount.put(c.getName(), ui);
			championsById.put(c.getChampionId(), c);
		});
		allMatches.forEach(match -> {
			String variation = match.getInfo().getGame_variation();
			// old matches are coming in for some reason
			if (variation != null && variation.contains("TFT3")) {
				match.getInfo().getParticipants().forEach(player -> {
					player.getUnits().forEach(unit -> {
						try {
						String unitName = championsById.get(unit.getCharacter_id()).getName();
						UnitInfo currentCount = championCount.get(unitName);
						currentCount.add(unit);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				});
			} else {
				System.out.println("Match " + match.getMetadata().getMatch_id() + ": " + match.getInfo().getGame_variation());
			}
		});
		
		// get the list of champions and print them out in order of least-usage
		// - including the zeroes too of course
		List<UnitInfo> infos = new ArrayList<>(championCount.values());
		infos.sort(Comparator.comparing(u -> u.totalCount));
		infos.forEach(u -> 
			System.out.printf("%15s | 1* %4s | 2* %4s | 3* %4s | T: %4s\n", 
					u.name, u.starCount1, u.starCount2, u.starCount3, u.totalCount));
		
		// also get all the summoners so I can use that data
		Set<String> names = new HashSet<>();
		allMatches.forEach(match -> {
			match.getInfo().getParticipants().forEach(player -> {
				Summoner sum = d.downloadSummonerByPuuid(player.getPuuid());
//				System.out.println(sum.getName());
				names.add(sum.getName());
			});
		});
		names.forEach(n -> System.out.println("'" + n + "',"));
		
	}
	
	private static class UnitInfo {
		String name;
		int totalCount;
		int starCount1;
		int starCount2;
		int starCount3;
		public void add(MatchUnit u) {
			totalCount += u.getUnitCountForTier();
			if (u.getTier() == 1) {
				starCount1++;
			} else if (u.getTier() == 2) {
				starCount2++;
			} else {
				starCount3++;
			}
		}
	}
	
}
