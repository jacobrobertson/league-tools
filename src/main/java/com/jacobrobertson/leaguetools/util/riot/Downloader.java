package com.jacobrobertson.leaguetools.util.riot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.commons.io.IOUtils;

import com.jacobrobertson.leaguetools.util.riot.dto.Champion;
import com.jacobrobertson.leaguetools.util.riot.dto.Item;
import com.jacobrobertson.leaguetools.util.riot.dto.Match;
import com.jacobrobertson.leaguetools.util.riot.dto.Summoner;
import com.jacobrobertson.leaguetools.util.riot.dto.Trait;

public class Downloader {
	
	public static void main(String[] args) throws Exception {
		Downloader d = new Downloader();
//		String get = d.downloadApiUrl(Downloader.MATCH_URL, "NA1_3369315994");
//		System.out.println(get);
		Object got = d.downloadMatch("NA1_3354899668");
		System.out.println(got);
//		System.out.println(d.downloadSummoner("vanrobestone"));
	}
	
	public static void main2(String[] args) throws Exception {
		Downloader p = new Downloader();
		p.parseItems().forEach(i -> System.out.println(i));
		p.getChampions().forEach(i -> System.out.println(i));
		p.parseTraits().forEach(i -> System.out.println(i));
	}

	
	
	
	private static final String REGION_HOST = "na1.api.riotgames.com";
	private static final String SUPERREGION_HOST = "americas.api.riotgames.com";

	private static final String SUMMONER_BY_NAME_URL = REGION_HOST + "/tft/summoner/v1/summoners/by-name/%s";
	private static final String MATCHES_URL = SUPERREGION_HOST + "/tft/match/v1/matches/by-puuid/%s/ids?count=%s";
	private static final String MATCH_URL = SUPERREGION_HOST + "/tft/match/v1/matches/%s";
	private static final String SUMMONER_BY_PUUID_URL = REGION_HOST + "/tft/summoner/v1/summoners/by-puuid/%s";

	private static final String TOKEN = "MOVETOFILE";
	
	/*
	The rate limit for a personal keys is by design very limited:
    20 requests every 1 second
    100 requests every 2 minutes
	*/
//	private long last1SecondRequestTimestamp = System.currentTimeMillis();
//	private long last2MinuteRequestTimestamp = System.currentTimeMillis();
	private int current1SecondRequestCount = 0;
	private int current2MinutesRequestCount = 0;

	private static int TWO_MINUTES_MS = 60 * 2 * 1000;
	private static int MAX_current1SecondRequestCount = 20;
	private static int MAX_current2MinutesRequestCount = 100;
	
	private List<Champion> champions;

	public Summoner downloadSummonerByName(String name) {
		return downloadObject(SUMMONER_BY_NAME_URL, true, Summoner.class, name);
	}
	public Summoner downloadSummonerByPuuid(String puuid) {
		return downloadObject(SUMMONER_BY_PUUID_URL, true, Summoner.class, puuid);
	}
	public Match downloadMatch(String matchId) {
		return downloadObject(MATCH_URL, true, Match.class, matchId);
	}
	public List<String> downloadMatchIds(String summonerPuuid, int count) {
		return downloadObjectList(MATCHES_URL, false, String.class, summonerPuuid, count);
	}

	public Predicate<Match> ranked(boolean onlyRanked) {
		return (m -> 
				onlyRanked ? m.getInfo().getQueue_id() == 1100 : true
			);
	}
//	public List<Match> toRankedList(List<Match> matches) {
//		return matches.stream()
//			.filter(m -> m.getInfo().getQueue_id() == 1100)
//			.collect(Collectors.toList());
//	}
	
	public List<String> findUniquePuuids(List<Match> matches) {
		List<String> puuids = new ArrayList<>();
		
		matches.forEach(m -> {
			m.getInfo().getParticipants().stream()
				.map(p -> p.getPuuid())
				.collect(Collectors.toCollection(() -> puuids));
		});
		return puuids;
	}
	
	public List<Match> filterToRankedSorted(List<Match> matches, int limit, boolean onlyRanked) {
		return matches.stream()
				// TODO not sure that sorting is working at all (is the order right, and is the cast going to be ok)
				.sorted((m1, m2) -> (int) Math.signum(m1.getInfo().getGame_datetime() - m2.getInfo().getGame_datetime()))
				.filter(ranked(onlyRanked)).limit(limit).collect(Collectors.toList());
	}
	
	public List<Match> downloadMatches(String summonerPuuid, int count) {
		return downloadMatches(summonerPuuid, new HashSet<String>(), count);
	}
	public List<Match> downloadMatches(String summonerPuuid, Set<String> existingMatchIds, int count) {
		List<String> matchIds = downloadMatchIds(summonerPuuid, count);
		matchIds.removeAll(existingMatchIds);
		existingMatchIds.addAll(matchIds);
		List<Match> matches = downloadMatches(matchIds);
		return matches;
	}
	public List<Match> downloadMatches(List<String> matchIds) {
		List<Match> matches = new ArrayList<>();
		matchIds.forEach(matchId -> {
			Match match = downloadMatch(matchId);
			matches.add(match);
		});
		return matches;
	}

	private <T> T downloadObject(String url, boolean useCache, Class<T> objectType, Object... args) {
		String json = downloadApiUrl(url, useCache, args);
		T object = parseJson(json, objectType);
		return object;
	}

	private <T> List<T> downloadObjectList(String url, boolean useCache, Class<T> objectType, Object... args) {
		String json = downloadApiUrl(url, useCache, args);
		List<T> objectList = parseJsonList(json, objectType);
		return objectList;
	}

	private String downloadApiUrl(String urlPart, boolean useCache, Object... args) {
		String urlString = "https://" + String.format(urlPart, args);
		if (useCache) {
			String cache = loadFromCache(urlString);
			if (cache != null) {
				return cache;
			}
		}
		try {
			checkAndWait();
			String urlStringWithToken = urlString;
			if (urlPart.indexOf('?') > 0) {
				urlStringWithToken += "&";
			} else {
				urlStringWithToken += "?";				
			}
			urlStringWithToken += "api_key=" + TOKEN;
			URL url = new URL(urlStringWithToken);
			String response = IOUtils.toString(url, "UTF-8");
			if (useCache) {
				storeToCache(urlString, response);
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void checkAndWait() {
		// just because... I keep getting rate limit exceeded error even though I try
		doSleepMs(300); 
		// check for the last second
		current1SecondRequestCount++;
		if (current1SecondRequestCount >= MAX_current1SecondRequestCount) {
			doSleepMs(1000);
			current1SecondRequestCount = 0;
		}

		// check for the last two minutes
		current2MinutesRequestCount++;
		if (current2MinutesRequestCount >= MAX_current2MinutesRequestCount) {
			System.out.print("Waiting 2 minutes... ");
			doSleepMs(TWO_MINUTES_MS);
			System.out.println("Done!");
			current2MinutesRequestCount = 0;
		}
	}
	private void doSleepMs(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("serial")
	public List<Item> parseItems() {
		return parseJsonList("/riot/items.json", new ArrayList<Item>() {});
	}
	@SuppressWarnings("serial")
	public List<Champion> getChampions() {
		if (champions == null) {
			champions = parseJsonList("/riot/champions.json", new ArrayList<Champion>() {});
		}
		return champions;
	}
	@SuppressWarnings("serial")
	public List<Trait> parseTraits() {
		return parseJsonList("/riot/traits.json", new ArrayList<Trait>() {});
	}

	private static <T> List<T> parseJsonList(String json, Object objectType) {
		json = loadStringFromFile(json);
		Jsonb jsonb = JsonbBuilder.create();
		List<T> parsedList = jsonb.fromJson(
				json, objectType.getClass().getGenericSuperclass());
		return parsedList;
	}
	private static <T> T parseJson(String json, Class<T> objectType) {
		json = loadStringFromFile(json);
		Jsonb jsonb = JsonbBuilder.create();
		T parsedObject = jsonb.fromJson(json, objectType);
		return parsedObject;
	}
	private static String loadStringFromFile(String path) {
		if (path.charAt(0) == '/') {
			try {
				return IOUtils.toString(Downloader.class.getResourceAsStream(path), "UTF-8");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			return path;
		}
	}
	private static final String CACHE_DIR = "D:/tft-cache";
	private void storeToCache(String url, String data) {
		File file = toFile(url);
		file.getParentFile().mkdirs();
		try {
			IOUtils.write(data, new FileOutputStream(file), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	private String loadFromCache(String url) {
		File file = toFile(url);
		if (file.exists()) {
			try {
				return IOUtils.toString(new FileInputStream(file), "UTF-8");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}
	private File toFile(String url) {
		String check = "/tft";
		int pos = url.indexOf(check);
		String path = url.substring(pos + check.length());
		path = path.replace('?', '-');
		path = path.replace('=', '-');
		File file = new File(CACHE_DIR + path);
		return file;
	}
}
