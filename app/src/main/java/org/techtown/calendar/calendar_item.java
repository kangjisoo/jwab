package org.techtown.calendar;

public class calendar_item {
    private int startDate;
    private int endDate;
    private int currentDate;
    private long schedule;

    public calendar_item(int startDate, int endDate, int currentDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentDate = currentDate;

    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getCurrentDate(){
        return currentDate;
    }

    public void setCurrentDate(int currentDate){
        this.currentDate=currentDate;
    }

    public long getSchedule() {
        schedule=startDate-endDate;
        return schedule;
    }
}
