package com.jacobrobertson.leaguetools.util.riot.dto;

/**
 {
        "style": "bronze",
        "min": 3,
        "max": 5
      },
 * @author jacob
 */
public class TraitTier {

	private String style;
	private int min;
	private int max;
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	@Override
	public String toString() {
		return "TraitTier [style=" + style + ", min=" + min + ", max=" + max + "]";
	}
	
}
