package com.dyc.youthvibe.GetterSetter;

public class WorkShopsModel {

    String workshopName, registrationFee, startDate, endDate, days, venue, URL;

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public WorkShopsModel(String workshopName, String registrationFee, String startDate, String endDate, String days, String venue, String URL) {
        this.workshopName = workshopName;
        this.registrationFee = registrationFee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.venue = venue;
        this.URL = URL;
    }

    public String getURL() {

        return URL;

    }
}
