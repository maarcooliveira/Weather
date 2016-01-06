package me.marcooliveira.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class WeatherActivity extends ActionBarActivity {

    ForecastAdapter forecast_adapter;
    TextView today_temperature;
    ImageView today_icon;
    TextView today_details;
    TextView today_high;
    TextView today_low;
    TextView location_toolbar;
    String unit = "";
    String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        /* Set the Appcompat toolbar object as Action Bar on devices pre-5.0 */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weather);
        setSupportActionBar(toolbar);

        /* Change color of the status bar on Android versions >= 5*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.secondary));

        /* Reference to today's weather data container */
        today_temperature = (TextView) findViewById(R.id.today_temperature);
        today_details = (TextView) findViewById(R.id.today_details);
        today_high = (TextView) findViewById(R.id.today_high);
        today_low = (TextView) findViewById(R.id.today_low);
        today_icon = (ImageView) findViewById(R.id.today_icon);
        location_toolbar = (TextView) findViewById(R.id.location);

        /* Load location and unit preferences; Save new values if it was empty */
        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        location = settings.getString("location", "Invalid location");
        unit = settings.getString("unit", "imperial");
        editor.putString("unit", unit);
        editor.commit();

        location_toolbar.setText(location);

        /* Handle the array of forecast data and the adapter for the ListView */
        ArrayList<DayForecast> week_forecast = new ArrayList<>();
        forecast_adapter = new ForecastAdapter(this, week_forecast);
        ListView forecast_list = (ListView) findViewById(R.id.listview_week_forecast);
        forecast_list.setAdapter(forecast_adapter);

        /* Call to the background work to load the data */
        GetWeather weather = new GetWeather();
        weather.execute(location, unit);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /* Switch between C and F units, save the new preference and reload the data*/
        if (id == R.id.action_change_unit) {
            SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
            unit = settings.getString("unit", "imperial");
            unit = (unit.equals("metric") ? "imperial" : "metric");

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("unit", unit);
            editor.commit();

            GetWeather weather = new GetWeather();
            weather.execute(location, unit);
            return true;
        }

        /* Reopen the WelcomeActivity to receive new location input */
        if (id == R.id.action_change_location) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            return true;
        }

        /* Reload the data making a new call to the weather API */
        if (id == R.id.action_refresh) {
            GetWeather weather = new GetWeather();
            weather.execute(location, unit);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class GetWeather extends AsyncTask<String, Void, ArrayList<DayForecast>> {

        @Override
        protected void onPostExecute(ArrayList<DayForecast> dayForecasts) {
            if(dayForecasts != null){

                forecast_adapter.clear();
                for(int i = 1; i < dayForecasts.size(); i++){
                    forecast_adapter.add(dayForecasts.get(i));
                }

                String unit_symbol = (unit.equals("metric") ? "˚C" : "˚F");

                today_temperature.setText(dayForecasts.get(0).getTemperature_now() + unit_symbol);
                today_details.setText(dayForecasts.get(0).getDetails());
                today_high.setText(dayForecasts.get(0).getHigh());
                today_low.setText(dayForecasts.get(0).getLow());
                today_icon.setImageResource(dayForecasts.get(0).getImage_id());
            }
            else{
                Toast.makeText(getApplicationContext(), "Couldn't load. Try a new location or refresh",
                        Toast.LENGTH_LONG).show();
            }

            findViewById(R.id.progressbar).setVisibility(View.GONE);
        }


        @Override
        protected ArrayList<DayForecast> doInBackground(String... params) {

            String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily";
            String location = params[0];
            String mode = "json";
            String units = params[1];
            String cnt = "7";
            String apiId = "86c6107f8767a0d591baf415052514ac";
            ArrayList<DayForecast> response;

            /* Code below based on "Developing Android Apps" online course, available at Udacity */
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonResponse = null;

            try {
                URL url = new URL(baseUrl
                                + "?q=" + URLEncoder.encode(location, "utf-8")
                                + "&mode=" + mode
                                + "&units=" + units
                                + "&cnt=" + cnt
                                + "&APPID=" + apiId);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                forecastJsonResponse = buffer.toString();

                /* Parse the JSON data to return an array of DayForecast objects */
                JSONParser parser = new JSONParser();
                response = parser.getWeatherDataFromJson(forecastJsonResponse);

            } catch (IOException e) {
                Log.e("WeatherActivity", "Error parsing the data", e);
                return null;

            } catch (JSONException e) {
                Log.e("WeatherActivity", "Error parsing the data", e);
                return null;

            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("WeatherActivity", "Error closing the stream", e);
                    }
                }
            }
            return response;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
