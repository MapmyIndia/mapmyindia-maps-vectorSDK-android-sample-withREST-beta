package com.mapmyindia.sdk.demo.kotlin.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityLocationCameraOptionsBinding


class LocationCameraActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener, OnCameraTrackingChangedListener {
    private lateinit var mBinding:ActivityLocationCameraOptionsBinding
    private var mapmyIndiaMap: MapboxMap? = null
    private lateinit var locationComponent: LocationComponent
    private var locationEngine: LocationEngine? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_location_camera_options)

        mBinding.addMapView.getMapAsync(this)
        mBinding.addMapView.onCreate(savedInstanceState)
        setButtonOnClickListener()
    }

    override fun onMapError(p0: Int, p1: String?) {
    }

    override fun onMapReady(mapmyIndia: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndia
        enableLocation()

    }

    fun enableLocation() {
        val options: LocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .build()
        locationComponent = mapmyIndiaMap!!.locationComponent
        locationComponent.activateLocationComponent(this, options)
        locationComponent.addOnCameraTrackingChangedListener(this)
        locationComponent.isLocationComponentEnabled = true
        locationEngine = locationComponent.locationEngine!!
        locationEngine?.addLocationEngineListener(this)
        locationComponent.cameraMode = CameraMode.TRACKING
        locationComponent.renderMode = RenderMode.COMPASS
    }

    override fun onStart() {
        super.onStart()
        mBinding.addMapView.onStart();
    }

    override fun onResume() {
        super.onResume()
        if (locationEngine != null) {
            locationEngine?.removeLocationEngineListener(this);
            locationEngine?.addLocationEngineListener(this);
        }
        mBinding.addMapView.onResume();

    }

    override fun onPause() {
        super.onPause()
        mBinding.addMapView.onPause();
        if (locationEngine != null)
            locationEngine?.removeLocationEngineListener(this);
    }

    override fun onStop() {
        super.onStop()
        mBinding.addMapView.onStop();
        if (locationEngine != null) {
            locationEngine?.removeLocationEngineListener(this);
            locationEngine?.removeLocationUpdates();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.addMapView.onDestroy();
        if (locationEngine != null) {
            locationEngine?.deactivate();
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.addMapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.addMapView.onSaveInstanceState(outState)
    }

    override fun onLocationChanged(location: Location?) {
        mapmyIndiaMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location!!.longitude), 16.0))


    }


    override fun onConnected() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationEngine!!.requestLocationUpdates();

    }


    private fun setButtonOnClickListener() {
        mBinding.btnLocationMode.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val popupMenu = PopupMenu(this@LocationCameraActivity, mBinding.btnLocationMode)
                popupMenu.menuInflater.inflate(R.menu.location_mode_menu, popupMenu.menu)
                popupMenu.gravity = Gravity.BOTTOM
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    mBinding.btnLocationMode.text = item.title.toString()
                    setRenderMode(item.title.toString())
                    true
                })

                popupMenu.show()

            }
        })
        mBinding.btnTracking.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val popupMenu = PopupMenu(this@LocationCameraActivity, mBinding.btnTracking)
                popupMenu.menuInflater.inflate(R.menu.tracking_mode_menu, popupMenu.menu)
                popupMenu.gravity = Gravity.BOTTOM
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    mBinding.btnTracking.text = item.title.toString()
                    setCameraMode(item.title.toString())
                    true
                })

                popupMenu.show()

            }
        })
    }

    fun setRenderMode(mode: String) {
        if (mode.equals("normal", ignoreCase = true)) {
            locationComponent.renderMode = RenderMode.NORMAL
        } else if (mode.equals("compass", ignoreCase = true)) {
            locationComponent.renderMode = RenderMode.COMPASS
        } else if (mode.equals("GPS", ignoreCase = true)) {
            locationComponent.renderMode = RenderMode.GPS
        } else
            locationComponent.renderMode = RenderMode.NORMAL

    }

    private fun setCameraMode(mode: String) {
        if (mode.equals("None", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.NONE
        } else if (mode.equals("None compass", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.NONE_COMPASS
        } else if (mode.equals("None gps", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.NONE_GPS
        } else if (mode.equals("Tracking", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING
        } else if (mode.equals("Tracking Compass", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING_COMPASS
        } else if (mode.equals("Tracking GPS", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING_GPS
        } else if (mode.equals("Tracking GPS North", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING_GPS_NORTH
        } else locationComponent.cameraMode = CameraMode.TRACKING
    }

    override fun onCameraTrackingChanged(currentMode: Int) {
        if (currentMode == CameraMode.NONE) {
            mBinding.btnTracking.text = "None"
        } else if (currentMode == CameraMode.TRACKING) {
            mBinding.btnTracking.text = "Tracking"
        } else if (currentMode == CameraMode.TRACKING_COMPASS) {
            mBinding.btnTracking.text = "Tracking Compass"
        } else if (currentMode == CameraMode.TRACKING_GPS) {
            mBinding.btnTracking.text = "Tracking GPS"
        } else if (currentMode == CameraMode.TRACKING_GPS_NORTH) {
            mBinding.btnTracking.text = "Tracking GPS North"
        }
    }


    override fun onCameraTrackingDismissed() {
        mBinding.btnTracking.text = "None"
    }
}

