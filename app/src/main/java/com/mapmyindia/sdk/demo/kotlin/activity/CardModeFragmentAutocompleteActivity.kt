package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaPlaceWidgetSetting
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.mmi.services.api.autosuggest.model.ELocation


class CardModeFragmentAutocompleteActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mapView: MapView
    private var mapmyIndiaMap: MapboxMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun callAutoComplete() {
        val placeOptions: PlaceOptions = if (MapmyIndiaPlaceWidgetSetting.instance.isDefault) {
            PlaceOptions.builder().build(PlaceOptions.MODE_CARDS)
        } else {
            PlaceOptions.builder()
                    .location(MapmyIndiaPlaceWidgetSetting.instance.location)
                    .filter(MapmyIndiaPlaceWidgetSetting.instance.filter)
                    .saveHistory(MapmyIndiaPlaceWidgetSetting.instance.isEnableHistory)
                    .enableTextSearch(MapmyIndiaPlaceWidgetSetting.instance.isEnableTextSearch)
                    .hint(MapmyIndiaPlaceWidgetSetting.instance.hint)
                    .pod(MapmyIndiaPlaceWidgetSetting.instance.pod)
                    .attributionHorizontalAlignment(MapmyIndiaPlaceWidgetSetting.instance.signatureVertical)
                    .attributionVerticalAlignment(MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal)
                    .logoSize(MapmyIndiaPlaceWidgetSetting.instance.logoSize)
                    .backgroundColor(resources.getColor(MapmyIndiaPlaceWidgetSetting.instance.backgroundColor))
                    .toolbarColor(resources.getColor(MapmyIndiaPlaceWidgetSetting.instance.toolbarColor))
                    .build(PlaceOptions.MODE_CARDS)
        }

            val placeAutocompleteFragment: PlaceAutocompleteFragment = PlaceAutocompleteFragment.newInstance(placeOptions)
            placeAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onCancel() {

                }

                override fun onPlaceSelected(eLocation: ELocation?) {
                    if (mapmyIndiaMap != null) {
                        mapmyIndiaMap?.clear()
                        val latLng = LatLng(eLocation?.latitude?.toDouble()!!, eLocation.longitude?.toDouble()!!)
                        mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0))
                        mapmyIndiaMap?.addMarker(MarkerOptions().position(latLng).setTitle(eLocation.placeName).setSnippet(eLocation.placeAddress))
                    }
                }

            })
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, placeAutocompleteFragment, PlaceAutocompleteFragment::class.java.simpleName)
                    .commit()


    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap

        //mapmyIndiaMap?.setPadding(20, 20, 20, 20)

        mapmyIndiaMap!!.setMinZoomPreference(4.0)
        mapmyIndiaMap.setMaxZoomPreference(18.0)
        mapmyIndiaMap.cameraPosition = CameraPosition.Builder().target(LatLng(28.0, 77.0)).zoom(4.0).build()
        callAutoComplete()

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()

        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()

        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()

        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }


}