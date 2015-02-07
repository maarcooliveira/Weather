package me.marcooliveira.weather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by marco on 2/1/15.
 *
 * Model for the weather data of a day
 */
public class DayForecast {
    private String day_of_week;
    private String high;
    private String low;
    private String details;
    private String temp_day;
    private String temp_night;
    private String temp_evening;
    private String temp_morning;
    private int image_id;


    /* Return the temperature depending on the time of the day */
    public String getTemperature_now(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        if(hour >= 18 && hour <= 23){
            return temp_evening;
        }
        else if(hour >= 0 && hour <= 5){
            return temp_night;
        }
        else if(hour >= 6 && hour <= 11){
            return temp_morning;
        }
        else if(hour >= 12 && hour <= 17){
            return temp_day;
        }
        return temp_day;
    }


    public DayForecast(Date date, String high, String low, String details, String temp_day,
                       String temp_night, String temp_evening, String temp_morning, int image_id) {
        this.day_of_week = new SimpleDateFormat("EE").format(date);
        this.high = high + "˚";
        this.low = low + "˚";
        this.details = details;
        this.temp_day = temp_day;
        this.temp_night = temp_night;
        this.temp_evening = temp_evening;
        this.temp_morning = temp_morning;
        this.image_id = image_id;
    }

    public String getDay_of_week() {
        return day_of_week;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getDetails() {
        return details;
    }

    public int getImage_id() {
        return image_id;
    }
}
