package com.jacobrobertson.leaguetools.util.riot.dto;

import java.util.Arrays;

/**
  {
    "key": "Set3_Blademaster",
    "name": "Blademaster",
    "description": "Blademasters' Basic Attacks have a chance to trigger two additional attacks against their target. These additional attacks deal damage like Basic Attacks and trigger on-hit effects.",
    "type": "class",
    "sets": [
      {
        "style": "bronze",
        "min": 3,
        "max": 5
      },
      {
        "style": "gold",
        "min": 6
      }
    ]
  }
 */
public class Trait {
	private String key;
	private String name;
	private String description;
    private String type;
    private TraitTier[] sets;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public TraitTier[] getSets() {
		return sets;
	}
	public void setSets(TraitTier[] sets) {
		this.sets = sets;
	}
	@Override
	public String toString() {
		return "Trait [key=" + key + ", name=" + name + ", description=" + description + ", type=" + type + ", sets="
				+ Arrays.toString(sets) + "]";
	}
    
}
