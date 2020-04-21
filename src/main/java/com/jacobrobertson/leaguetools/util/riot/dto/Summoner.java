package com.jacobrobertson.leaguetools.util.riot.dto;

public class Summoner {

//	{"id":"zFHcPHl3fvoGzhC3uB1gsbz4ngY4vr87CuTjdMDvmb7wP2I",
//		 "accountId":"bo0jjAaHA71qbZ3Sb0ECQrK-lgqUGpNjcfNQdrBypTfbaig",
//		 "puuid":"6lQYC6LIfARHbbK0SIuL11m_Tds_pQysOrZZcsb5bMwSjmEI3OUkDEBJ0Be1p21rv_r8mIidsfNK6A",
//		 "name":"Van Robestone",
//		 "profileIconId":4492,"revisionDate":1586521746000,"summonerLevel":238}

	private String id;
	private String accountId;
	private String puuid;
	private String name;
	private int profileIconId;
	private long revisionDate;
	private int summonerLevel;

	public String getPuuid() {
		return puuid;
	}
	public void setPuuid(String puuid) {
		this.puuid = puuid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProfileIconId() {
		return profileIconId;
	}
	public void setProfileIconId(int profileIconId) {
		this.profileIconId = profileIconId;
	}
	public long getRevisionDate() {
		return revisionDate;
	}
	public void setRevisionDate(long revisionDate) {
		this.revisionDate = revisionDate;
	}
	public int getSummonerLevel() {
		return summonerLevel;
	}
	public void setSummonerLevel(int summonerLevel) {
		this.summonerLevel = summonerLevel;
	}
	@Override
	public String toString() {
		return "Summoner [id=" + id + ", accountId=" + accountId + ", puuid=" + puuid + ", name=" + name
				+ ", profileIconId=" + profileIconId + ", revisionDate=" + revisionDate + ", summonerLevel="
				+ summonerLevel + "]";
	}
	

}
