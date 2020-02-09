package com.dyc.youthvibe.GetterSetter;

public class RegisteredModel {

    private String event_name, team_name, team_id;

    public RegisteredModel(String event_name, String team_name, String team_id) {
        this.event_name = event_name;
        this.team_name = team_name;
        this.team_id = team_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }
}
