package me.marcooliveira.weather;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marco on 2/2/15.
 *
 * ArrayAdapter to model each day on the weather list
 */
public class ForecastAdapter extends ArrayAdapter<DayForecast> {

    private Activity context;
    private ArrayList<DayForecast> data;

    public ForecastAdapter(Activity context, ArrayList<DayForecast> data){
        super(context, R.layout.list_item, data);
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);
        ImageView image = (ImageView) rowView.findViewById(R.id.list_item_img);
        TextView day = (TextView) rowView.findViewById(R.id.list_item_day);
        TextView high = (TextView) rowView.findViewById(R.id.list_item_high);
        TextView low = (TextView) rowView.findViewById(R.id.list_item_low);

        image.setImageResource(data.get(position).getImage_id());
        day.setText(data.get(position).getDay_of_week());
        high.setText(data.get(position).getHigh());
        low.setText(data.get(position).getLow());

        return rowView;
    }

}
