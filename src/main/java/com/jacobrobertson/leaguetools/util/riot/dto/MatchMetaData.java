package com.jacobrobertson.leaguetools.util.riot.dto;

public class MatchMetaData {

	/*
	"metadata": {
        "data_version": "4",
        "match_id": "NA1_3369315994",
        "participants": [
            "A4Z6dBsWV4h97Qic-fEVGigE0FLzzoN7EJUB6CLfwE4hsyCUhqf2TjP8I401BlwpozXlg9v-wc1xGg",
            "LCs5MPCAzDKQvcOZsW_hE0LIrBpR6EW2iTmpmEiFwbAIbgnJYAvZ9oES9m1Iz6QFvuAP7WqIZMaP1g",
            "autqoYDPECAxr2OGgQ1Mdf3zWSqoNG3hvPizfq1bHgwMIdzzC1OmiHhkE7_vFr41rx-a99D-p3eDtg",
            "yZkMZyYXgmCkUbRhMYUgnciP99ZprhdAJtvQgm4p2UTEdGFTD3Z9n15XvaPz0rgXRmJeOo2PtspHFw",
            "w7fP6afrZArKDHxKOd6DRJlw1wq5Dp9dqiQ1nz8TP7cXkuf6555OKJfqS1uIqsrD2xHerL8i9zunxg",
            "6lQYC6LIfARHbbK0SIuL11m_Tds_pQysOrZZcsb5bMwSjmEI3OUkDEBJ0Be1p21rv_r8mIidsfNK6A",
            "ryzSQxPIMg7IHpEcKw-quJuN4tC73aXaTessRh6Ld-itX7qpXK7adJbrLf_W4bINZWgJZMAjHlNooA",
            "xHoiKv9T0qKguf4WNC7_HRflsAoT3WQqXKldWhAJ_f7yB51rPP_nudlcHUozvkGplk4zcxLN6-Z9Yg"
        ]
	*/
	
	private String data_version;
	private String match_id;
	private String[] participants;
	public String getData_version() {
		return data_version;
	}
	public void setData_version(String data_version) {
		this.data_version = data_version;
	}
	public String getMatch_id() {
		return match_id;
	}
	public void setMatch_id(String match_id) {
		this.match_id = match_id;
	}
	public String[] getParticipants() {
		return participants;
	}
	public void setParticipants(String[] participants) {
		this.participants = participants;
	}

}



