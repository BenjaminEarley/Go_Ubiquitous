package com.example.android.sunshine.app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService {


    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        Log.d("TESTTEST", "Data Changed");


        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equals("/weather")) {
                    double high = dataMap.getDouble("HIGH_TEMP");
                    double low = dataMap.getDouble("LOW_TEMP");
                    int id = dataMap.getInt("WEATHER_ID");

                    Log.d("TESTTEST", high + " put out");

                    saveWeatherValuesInPreferences(high,low,id);
                }
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void saveWeatherValuesInPreferences(double high, double low, int id) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt("HIGH_TEMP", (int) Math.round(high));
        spe.putInt("LOW_TEMP", (int) Math.round(low));
        spe.putInt("WEATHER_ID", id);
        spe.commit();

    }
}
