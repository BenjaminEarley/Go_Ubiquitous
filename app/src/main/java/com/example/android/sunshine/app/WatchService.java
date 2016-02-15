package com.example.android.sunshine.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class WatchService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final IBinder mBinder = new MyBinder();

    private double highTemp;
    private double lowTemp;
    private int weatherId;

    private GoogleApiClient mGoogleApiClient;

    public WatchService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("TESTTEST", "Start Command Started");

        highTemp = intent.getDoubleExtra("HIGH_TEMP", -999);
        lowTemp = intent.getDoubleExtra("LOW_TEMP", -999);
        weatherId = intent.getIntExtra("WEATHER_ID", -999);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        WatchService getService() {
            return WatchService.this;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        sendWeather(highTemp,lowTemp,weatherId);
        stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopSelf();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        stopSelf();
    }

    private void sendWeather(double highTemp, double lowTemp, int weatherId) {

        Log.d("TESTTEST", "Send Weather");

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/weather");

        putDataMapRequest.getDataMap().putDouble("HIGH_TEMP", highTemp);
        putDataMapRequest.getDataMap().putDouble("LOW_TEMP", lowTemp);
        putDataMapRequest.getDataMap().putInt("WEATHER_ID", weatherId);

        PutDataRequest request = putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request);

        Log.d("TESTTEST", highTemp + " put in");
    }
}
