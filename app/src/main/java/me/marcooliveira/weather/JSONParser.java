package me.marcooliveira.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by marco on 2/5/15.
 */
public class JSONParser {

    private Date convertDate(long time){
        Date date = new Date(time * 1000);
        return date;
    }


    /* Define which is the correct icon depending on the API response
     * Icons used in this application: Climacons by @adamwhitcroft
     * available at http://adamwhitcroft.com/climacons/ */

    private int findIconResource(String icon){
        if(icon.equals("01d")){
            return R.drawable.clear_d;
        }
        else if(icon.equals("01n")){
            return R.drawable.clear_n;
        }
        else if(icon.equals("02d")){
            return R.drawable.few_clouds_d;
        }
        else if(icon.equals("02n")){
            return R.drawable.few_clouds_n;
        }
        else if(icon.equals("03d") || icon.equals("03n")){
            return R.drawable.scattered_clouds;
        }
        else if(icon.equals("04d") || icon.equals("04n")){
            return R.drawable.broken_clouds;
        }
        else if(icon.equals("09d") || icon.equals("09n")){
            return R.drawable.shower_rain;
        }
        else if(icon.equals("10d")){
            return R.drawable.rain_d;
        }
        else if(icon.equals("10n")){
            return R.drawable.rain_n;
        }
        else if(icon.equals("11d") || icon.equals("11n")){
            return R.drawable.thunderstorm;
        }
        else if(icon.equals("13d") || icon.equals("13n")){
            return R.drawable.snow;
        }
        else if(icon.equals("50d") || icon.equals("50n")){
            return R.drawable.mist;
        }
        else {
            return R.drawable.clear_d;
        }
    }

    /* Main parsing method */
    public ArrayList<DayForecast> getWeatherDataFromJson(String forecastJsonStr) throws JSONException {

        final String LIST = "list";
        final String WEATHER = "weather";
        final String TEMPERATURE = "temp";
        final String MAX = "max";
        final String MIN = "min";
        final String DATETIME = "dt";
        final String DETAILS = "main";
        final String ICON = "icon";
        final String DAY = "day";
        final String NIGHT = "night";
        final String EVENING = "eve";
        final String MORNING = "morn";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(LIST);

        ArrayList<DayForecast> week_forecast = new ArrayList<DayForecast>();

        for(int i = 0; i < weatherArray.length(); i++) {
            Date date;
            String high;
            String low;
            String details;
            String temp_day;
            String temp_night;
            String temp_evening;
            String temp_morning;
            int image_id;

            JSONObject dayForecast = weatherArray.getJSONObject(i);

            date = convertDate(dayForecast.getLong(DATETIME));

            JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
            details = weatherObject.getString(DETAILS);
            image_id = findIconResource(weatherObject.getString(ICON));

            JSONObject temperatureObject = dayForecast.getJSONObject(TEMPERATURE);
            long high_ = Math.round(temperatureObject.getDouble(MAX));
            long low_ = Math.round(temperatureObject.getDouble(MIN));
            long temp_day_ = Math.round(temperatureObject.getDouble(DAY));
            long temp_night_ = Math.round(temperatureObject.getDouble(NIGHT));
            long temp_evening_ = Math.round(temperatureObject.getDouble(EVENING));
            long temp_morning_ = Math.round(temperatureObject.getDouble(MORNING));

            high = Long.toString(high_);
            low = Long.toString(low_);
            temp_day = Long.toString(temp_day_);
            temp_night = Long.toString(temp_night_);
            temp_evening = Long.toString(temp_evening_);
            temp_morning = Long.toString(temp_morning_);

            week_forecast.add(new DayForecast(date, high, low, details, temp_day, temp_night,
                    temp_evening, temp_morning, image_id));
        }
        return week_forecast;
    }
}
