package com.jacobrobertson.leaguetools.util.riot.dto;

public class MatchTrait {

	/*
                    {
                        "name": "DarkStar",
                        "num_units": 2,
                        "style": 0,
                        "tier_current": 0,
                        "tier_total": 2
                    },
	
	*/
	
	private String name;
	private int num_units;
	private int style;
	private int tier_current;
	private int tier_total;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNum_units() {
		return num_units;
	}
	public void setNum_units(int num_units) {
		this.num_units = num_units;
	}
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	public int getTier_current() {
		return tier_current;
	}
	public void setTier_current(int tier_current) {
		this.tier_current = tier_current;
	}
	public int getTier_total() {
		return tier_total;
	}
	public void setTier_total(int tier_total) {
		this.tier_total = tier_total;
	}
	
	
}
