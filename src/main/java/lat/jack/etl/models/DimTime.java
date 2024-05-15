package lat.jack.etl.models;

import java.sql.Date;

public class DimTime {

    private int dateId;
    private java.sql.Date date;
    private int minute;
    private int hour;
    private int dayOfWeek;
    private int month;
    private int quarter;
    private int year;
    private boolean isWeekend;

    public DimTime(int dateId, Date date, int minute, int hour, int dayOfWeek, int month, int quarter, int year, boolean isWeekend) {
        this.dateId = dateId;
        this.date = date;
        this.minute = minute;
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
        this.month = month;
        this.quarter = quarter;
        this.year = year;
        this.isWeekend = isWeekend;
    }

    public DimTime(int dateId) {
        this.dateId = dateId;
    }

    public DimTime() {
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }
}
