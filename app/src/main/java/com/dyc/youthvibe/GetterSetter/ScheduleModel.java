package com.dyc.youthvibe.GetterSetter;

public class ScheduleModel {

    private String time;
    private String eventName, eventLocation, imageUrl;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public ScheduleModel(String eventName, String eventLocation, String time){

        this.time = time;
        this.eventName = eventName;
        this.eventLocation = eventLocation;

    }


}
