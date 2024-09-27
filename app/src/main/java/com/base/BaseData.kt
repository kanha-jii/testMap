package com.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

public class BaseData {

    companion object {
        public val currentLocation:LiveData<LatLng> = MutableLiveData()
        val _currentLocationSet get() = currentLocation
    }


}