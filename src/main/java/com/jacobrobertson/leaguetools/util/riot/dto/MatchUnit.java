package com.jacobrobertson.leaguetools.util.riot.dto;

import java.util.Arrays;

public class MatchUnit {

	/*

	{
                        "character_id": "TFT3_JarvanIV",
                        "items": [
                            79,
                            19,
                            19
                        ],
                        "name": "",
                        "rarity": 0,
                        "tier": 3
                    },
	*/
	
	private String character_id;
	private String name;
	private int rarity;
	private int tier;
	private int[] items;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRarity() {
		return rarity;
	}
	public void setRarity(int rarity) {
		this.rarity = rarity;
	}
	/**
	 * @return 3 ^ (tier - 1) :: meaning the number of units it takes to get that star level
	 */
	public int getUnitCountForTier() {
		return (int) Math.pow(3, (tier - 1));
	}
	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	public int[] getItems() {
		return items;
	}
	public void setItems(int[] items) {
		this.items = items;
	}
	public String getCharacter_id() {
		return character_id;
	}
	public void setCharacter_id(String character_id) {
		this.character_id = character_id;
	}
	@Override
	public String toString() {
		return "MatchUnit [character_id=" + character_id + ", name=" + name + ", rarity=" + rarity + ", tier=" + tier
				+ ", items=" + Arrays.toString(items) + "]";
	}
	
}
