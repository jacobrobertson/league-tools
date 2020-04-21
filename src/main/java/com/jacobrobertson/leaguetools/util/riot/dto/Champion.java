package com.jacobrobertson.leaguetools.util.riot.dto;

import java.util.Arrays;

/**
 * 
 {
    "name": "Ahri",
    "championId": "TFT3_Ahri",
    "cost": 2,
    "traits": [
        "Star Guardian",
        "Sorcerer"
    ]
  }
 *
 */
public class Champion {

	
    private String name;
    private String championId;
    private int cost;
    private String[] traits;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChampionId() {
		return championId;
	}
	public void setChampionId(String championId) {
		this.championId = championId;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String[] getTraits() {
		return traits;
	}
	public void setTraits(String[] traits) {
		this.traits = traits;
	}
	@Override
	public String toString() {
		return "Champion [name=" + name + ", championId=" + championId + ", cost=" + cost + ", traits="
				+ Arrays.toString(traits) + "]";
	}
    
}
