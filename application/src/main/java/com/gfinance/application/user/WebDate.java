package com.gfinance.application.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Calendar;
// Date object used to processes a string date submitted from a form in the view to be delivered to the application layer
@Validated
public class WebDate {

    private String date;

    // spring validation criteria
    @Pattern(regexp = "[0-9][0-9][0-9][0-9]-[0-1][1-9]-([0-2][1-9]|3[0-1])", message = "Invalid date, required format: 2023-07-12")
    private String day;
    // 2023-W27
    @Pattern(regexp = "[0-9][0-9][0-9][0-9]-W([0-4][1-9]|5[0-2])", message = "Invalid date, required format: 2023-W27")
    private String week;


    // 2023-08
    @Pattern(regexp = "[0-9][0-9][0-9][0-9]-[0-1][1-9]", message = "Invalid date, required format: 2023-08")
    private String month;

    private String name;


    public LocalDateTime processDate() {
        if (day != null) {
            // day
            // 2023-07-12
            this.name = "day";
            String[] array = day.split("-");
            int year = Integer.valueOf(array[0]);
            int month = Integer.valueOf(array[1]);
            int day = Integer.valueOf(array[2]);
            LocalDateTime t = LocalDateTime.of(year, month, day, 0,0,0);
            return t;
        } else if (week != null) {
            // week
            // 2023-W27
            this.name = "week";
            String[] array = week.split("-");
            int year = Integer.valueOf(array[0]);
            String tempWeek = array[1];

            tempWeek = "" + tempWeek.charAt(1) + "" + tempWeek.charAt(2);
            int week = Integer.valueOf(tempWeek);

            LocalDateTime t = LocalDateTime.of(year,1, 1, 0,0, 0);
            t = t.plusWeeks(week-1);
            t = t.plusDays(1);

            return t;
        } else {
            // month
            // 2023-08
            this.name = "month";
            String[] array = month.split("-");
            int year = Integer.valueOf(array[0]);
            int month = Integer.valueOf(array[1]);
            LocalDateTime t = LocalDateTime.of(year, month, 1, 0,0,0);
            return t;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
