package com.jacobrobertson.leaguetools.util.riot.dto;

import java.util.List;

public class Participant {

	/*
                "gold_left": 2,
                "last_round": 37,
                "level": 7,
                "placement": 2,
                "players_eliminated": 0,
                "puuid": "A4Z6dBsWV4h97Qic-fEVGigE0FLzzoN7EJUB6CLfwE4hsyCUhqf2TjP8I401BlwpozXlg9v-wc1xGg",
                "time_eliminated": 2035.87890625,
                "total_damage_to_players": 0,

	*/

	private int gold_left;
	private int last_round;
	private int level;
	private int placement;
	private int players_eliminated;

	private String puuid;
	private float time_eliminated;
	
	private int total_damage_to_players;

	private List<MatchTrait> traits;
	private List<MatchUnit> units;
	public int getGold_left() {
		return gold_left;
	}
	public void setGold_left(int gold_left) {
		this.gold_left = gold_left;
	}
	public int getLast_round() {
		return last_round;
	}
	public void setLast_round(int last_round) {
		this.last_round = last_round;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPlacement() {
		return placement;
	}
	public void setPlacement(int placement) {
		this.placement = placement;
	}
	public int getPlayers_eliminated() {
		return players_eliminated;
	}
	public void setPlayers_eliminated(int players_eliminated) {
		this.players_eliminated = players_eliminated;
	}
	public String getPuuid() {
		return puuid;
	}
	public void setPuuid(String puuid) {
		this.puuid = puuid;
	}
	public float getTime_eliminated() {
		return time_eliminated;
	}
	public void setTime_eliminated(float time_eliminated) {
		this.time_eliminated = time_eliminated;
	}
	public int getTotal_damage_to_players() {
		return total_damage_to_players;
	}
	public void setTotal_damage_to_players(int total_damage_to_players) {
		this.total_damage_to_players = total_damage_to_players;
	}
	public List<MatchTrait> getTraits() {
		return traits;
	}
	public void setTraits(List<MatchTrait> traits) {
		this.traits = traits;
	}
	public List<MatchUnit> getUnits() {
		return units;
	}
	public void setUnits(List<MatchUnit> units) {
		this.units = units;
	}
	
	
}
