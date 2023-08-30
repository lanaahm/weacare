package com.uc.weacare2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tapadoo.alerter.Alerter
import com.uc.weacare2.Utilites.ApiUtilities
import com.uc.weacare2.databinding.ActivityWeatherFragmentBinding
import com.uc.weacare2.model.weather.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private lateinit var binding: ActivityWeatherFragmentBinding

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManagerCompat
    private val LOCATION_REQUEST_CODE = 101
    private val apiKey = "f70ca239bf30695349b25a9bb3361c69"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityWeatherFragmentBinding.inflate(inflater, container, false)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
        notificationManager = NotificationManagerCompat.from(requireActivity())
        getCurrentLocation()
        binding.citySearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                getCityWeather(binding.citySearch.text.toString())
                val view = requireActivity().currentFocus
                if (view != null) {
                    val imm: InputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE)
                            as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    binding.citySearch.clearFocus()
                }
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        binding.currentLocation.setOnClickListener {
            getCurrentLocation()
        }
        return binding.root
    }
    private fun getCityWeather(city: String) {
        binding.progressBar.visibility = VISIBLE

        ApiUtilities.getApiInterface()?.getCityWeatherData(city, apiKey)
            ?.enqueue(object : Callback<WeatherModel> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = GONE
                        response.body()?.let {
                            setData(it)
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(), "No City Found",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = GONE
                    }
                }
                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {}
            })
    }


    private fun fetchCurrentLocationWeather(latitude: String, longitude: String) {
        ApiUtilities.getApiInterface()?.getCurrentWeatherData(latitude, longitude, apiKey)
            ?.enqueue(object : Callback<WeatherModel> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.GONE
                        response.body()?.let {
                            setData(it)
                        }
                    }
                }
                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Log.d("Test123456", t.getLocalizedMessage())
                }
            })
    }


    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            currentLocation = location
                            binding.progressBar.visibility = View.VISIBLE
                            fetchCurrentLocationWeather(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        }
                    }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_REQUEST_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE)
                as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(body: WeatherModel) {
        binding.apply {
            val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
            dateTime.text = currentDate.toString()
            maxTemp.text = "Max " + k2c(body?.main?.temp_max!!) + "°"
            minTemp.text = "Min " + k2c(body?.main?.temp_min!!) + "°"
            temp.text = "" + k2c(body?.main?.temp!!) + "°"
            weatherTitle.text = body.weather[0].main
            sunriseValue.text = ts2td(body.sys.sunrise.toLong())
            sunsetValue.text = ts2td(body.sys.sunset.toLong())
            pressureValue.text = body.main.pressure.toString()
            humidityValue.text = body.main.humidity.toString() + "%"
            tempFValue.text = "" + (k2c(body.main.temp).times(1.8)).plus(32)
                .roundToInt() + "°"
            citySearch.setText(body.name)
            feelsLike.text = "" + k2c(body.main.feels_like) + "°"
            windValue.text = body.wind.speed.toString() + "m/s"
            groundValue.text = body.main.grnd_level.toString()
            seaValue.text = body.main.sea_level.toString()
            countryValue.text = body.sys.country
        }
        updateUI(body.weather[0].id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ts2td(ts: Long): String {
        val localTime = ts.let {
            Instant.ofEpochSecond(it)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
        }
        return localTime.toString()
    }

    private fun k2c(t: Double): Double {
        var intTemp = t
        intTemp = intTemp.minus(273)
        return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }

    @SuppressLint("ResourceType")
    private fun noitify(title: String, content: String, drawable: Int, @ColorRes colorbg: Int){
        Alerter.create(requireActivity())
            .setTitle(title)
            .setText(content)
            .setIcon(drawable)
            .setBackgroundColorRes(colorbg)
            .show()
    }

    private fun updateUI(id: Int) {
        binding.apply {
            when (id) {
                //Thunderstorm
                in 200..232 -> {
                    weatherImg.setImageResource(R.drawable.ic_storm_weather)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.thunderstrom_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.thunderstrom_bg)
                    noitify("Cuaca Hujan Badai", "Cuaca sendang badai hujan", R.drawable.ic_storm_weather, R.color.thunderstorm)
                }
                //Drizzle
                in 300..321 -> {
                    weatherImg.setImageResource(R.drawable.ic_few_clouds)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.drizzle_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.drizzle_bg)
                    noitify("Cuaca Gerimis", "Cuaca sendang gerimis", R.drawable.ic_few_clouds, R.color.drizzle)
                }
                //Rain
                in 500..531 -> {
                    weatherImg.setImageResource(R.drawable.ic_rainy_weather)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.rain_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.rain_bg)
                    noitify("Cuaca Hujan", "Cuaca sendang hujan", R.drawable.ic_rainy_weather, R.color.rain)
                }
                //Snow
                in 600..622 -> {
                    weatherImg.setImageResource(R.drawable.ic_snow_weather)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.snow_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.snow_bg)
                    noitify("Cuaca Hujan", "Cuaca sendang hujan", R.drawable.ic_snow_weather, R.color.snow)
                }
                //Atmosphere
                in 701..781 -> {
                    weatherImg.setImageResource(R.drawable.ic_broken_clouds)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.atmosphere_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.atmosphere_bg)
                    noitify("Cuaca Badai Awan", "Cuaca sendang Badai Awan", R.drawable.ic_broken_clouds, R.color.atmosphere)
                }
                //Clear
                800 -> {
                    weatherImg.setImageResource(R.drawable.ic_clear_day)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.clear_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.clear_bg)
                    noitify("Cuaca Cerah", "Cuaca sendang Cerah", R.drawable.ic_clear_day, R.color.clear)
                }
                //Clouds
                in 801..804 -> {
                    weatherImg.setImageResource(R.drawable.ic_cloudy_weather)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.clouds_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.clouds_bg)
                    noitify("Cuaca Berawan", "Cuaca berawan", R.drawable.ic_cloudy_weather, R.color.clouds)
                }
                //unknown
                else -> {
                    weatherImg.setImageResource(R.drawable.ic_unknown)
                    mainLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.unknown_bg)
                    optionsLayout.background = ContextCompat
                        .getDrawable(requireActivity(), R.drawable.unknown_bg)
                    noitify("Cuaca Hujan Badai", "Cuaca sendang badai hujan", R.drawable.ic_unknown, R.color.black)
                }
            }
        }
    }
}