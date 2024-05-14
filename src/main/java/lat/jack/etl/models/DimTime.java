package lat.jack.etl.models;

import java.sql.Date;

public class DimTime {

    private int dateId;
    private java.sql.Date date;
    private int year;
    private int quarter;
    private int month;
    private int day;
    private int weekOfYear;
    private int dayOfWeek;
    private boolean isWeekend;

    public DimTime(int dateId, Date date, int year, int quarter, int month, int day, int weekOfYear, int dayOfWeek, boolean isWeekend) {
        this.dateId = dateId;
        this.date = date;
        this.year = year;
        this.quarter = quarter;
        this.month = month;
        this.day = day;
        this.weekOfYear = weekOfYear;
        this.dayOfWeek = dayOfWeek;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }
}
