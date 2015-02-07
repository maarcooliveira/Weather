package me.marcooliveira.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class WelcomeActivity extends ActionBarActivity {

    EditText input_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        /* Change color of the status bar on Android versions >= 5*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.secondary));

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        input_location = (EditText) findViewById(R.id.input_location);
    }


    /* Save the location entered by the user and start the main activity */
    public void loadWeather(View view) {
        String location = input_location.getText().toString();

        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("location", location);
        editor.commit();

        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
