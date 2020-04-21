package com.jacobrobertson.leaguetools.util.riot.dto;

public class Match {

	private MatchMetaData metadata;
	private MatchInfo info;

	public MatchInfo getInfo() {
		return info;
	}

	public void setInfo(MatchInfo info) {
		this.info = info;
	}

	public MatchMetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(MatchMetaData metadata) {
		this.metadata = metadata;
	}
	
	
}
