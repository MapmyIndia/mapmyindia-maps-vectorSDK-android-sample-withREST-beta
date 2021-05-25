package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.BaseLayoutBinding;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class AddCustomMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BaseLayoutBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.base_layout);
        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {


      mapmyIndiaMap.setPadding(20, 20, 20, 20);


        IconFactory iconFactory = IconFactory.getInstance(this);
        Icon icon = iconFactory.fromResource(R.drawable.placeholder);
      mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(
          25.321684, 82.987289)).icon(icon));

        /* this is done for animating/moving camera to particular position */
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
          25.321684, 82.987289)).zoom(8).tilt(0).build();
      mapmyIndiaMap.setCameraPosition(cameraPosition);
    }

    @Override
    public void onMapError(int i, String s) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }
}
