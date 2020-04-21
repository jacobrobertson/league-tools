package com.jacobrobertson.leaguetools.util.riot.dto;

import java.util.List;

public class MatchInfo {
/*
	    "game_datetime": 1586521742636,
        "game_length": 2044.1669921875,
        "game_variation": "TFT3_GameVariation_None",
        "game_version": "Version 10.7.314.9802 (Mar 26 2020/14:16:38) [PUBLIC] <Releases/10.7>",
        "participants": [
*/
	
    private int queue_id;
    private int tft_set_number;
	
	private long game_datetime;
	private float game_length;
	private String game_variation;
	private String game_version;
	private List<Participant> participants;
	
	
	public long getGame_datetime() {
		return game_datetime;
	}
	public void setGame_datetime(long game_datetime) {
		this.game_datetime = game_datetime;
	}
	public float getGame_length() {
		return game_length;
	}
	public void setGame_length(float game_length) {
		this.game_length = game_length;
	}
	public String getGame_variation() {
		return game_variation;
	}
	public void setGame_variation(String game_variation) {
		this.game_variation = game_variation;
	}
	public String getGame_version() {
		return game_version;
	}
	public void setGame_version(String game_version) {
		this.game_version = game_version;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public int getQueue_id() {
		return queue_id;
	}
	public void setQueue_id(int queue_id) {
		this.queue_id = queue_id;
	}
	public int getTft_set_number() {
		return tft_set_number;
	}
	public void setTft_set_number(int tft_set_number) {
		this.tft_set_number = tft_set_number;
	}
	
	
	
}
