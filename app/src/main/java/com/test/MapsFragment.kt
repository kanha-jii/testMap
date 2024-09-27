package com.test

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.CancellationToken

class MapsFragment : Fragment() {
    lateinit var polylineOptions:PolylineOptions

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//        val secondMarker = LatLng(-37.4220, 122.0841)
//        googleMap.addMarker(MarkerOptions().position(secondMarker).title("second"))
//
//        val location1= Location("providerName")
//        location1.latitude = -34.0
//        location1.longitude  = 151.0

//        val location2 = Location("2")
//        location2.latitude = -37.4220
//        location2.longitude = 122.0841

        val location = LocationServices.getFusedLocationProviderClient(requireActivity())


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return@OnMapReadyCallback
        }

        location.lastLocation.addOnSuccessListener {
            googleMap.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)).title("Current Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude,it.longitude)))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude,it.longitude),6F))
        }.addOnFailureListener {
            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
        }

        var flag1 = false
        var flag2 = false

        var location1 = Location("1")
        var location2 = Location("2")


        googleMap.setOnMapClickListener {
            if(!flag1) {
                googleMap.addMarker(MarkerOptions().position(it))
                location1.latitude = it.latitude
                location1.longitude = it.longitude
                flag1 = true
            }
            else if(!flag2) {
                googleMap.addMarker(MarkerOptions().position(it))
                location2.latitude = it.latitude
                location2.longitude = it.longitude
                flag2 = true

                Toast.makeText(requireContext(),location1.distanceTo(location2).toString(),Toast.LENGTH_LONG).show()


                polylineOptions = PolylineOptions().add(LatLng(location1.latitude,location1.longitude),LatLng(location2.latitude,location2.longitude))
                googleMap.addPolyline(
                    polylineOptions
                )

            }
            else {
                googleMap.addMarker(MarkerOptions().position(it))
                googleMap.addPolyline(polylineOptions.add(it))
            }
        }

//        val distance = location1.distanceTo(location2)
//        Toast.makeText(requireContext(),distance.toString(),Toast.LENGTH_LONG).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}