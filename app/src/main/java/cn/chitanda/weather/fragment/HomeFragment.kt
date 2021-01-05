package cn.chitanda.weather.fragment

import android.Manifest
import android.content.Context
import android.hardware.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import cn.chitanda.weather.adapter.WeatherViewPagerAdapter
import cn.chitanda.weather.databinding.FragmentHomeBinding
import cn.chitanda.weather.viewmodel.WeatherViewModel
import cn.chitanda.weather.widget.weather.controller.SunnyController
import com.permissionx.guolindev.PermissionX
import kotlin.math.PI

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var mSensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private val weatherViewPagerAdapter by lazy { WeatherViewPagerAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PermissionX.init(this).permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Weather需要您同意一下权限才能正常使用", "好的", "不")
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去设置当中同意定位权限", "好")
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted && grantedList.isNotEmpty() && deniedList.isEmpty()) {
                    viewModel.init()
                }
            }
    }

    override fun onResume() {
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).also { accelerometer ->
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
        mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).also { field ->
            mSensorManager.registerListener(this, field, SensorManager.SENSOR_DELAY_UI)
        }
        binding.dynamicWeatherView.weatherController?.resumeAnim()
        super.onResume()
    }

    override fun onPause() {
        mSensorManager.unregisterListener(this)
        binding.dynamicWeatherView.weatherController?.stopAnim()
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }
        updateOrientationAngles()
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    private fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        // "mRotationMatrix" now has up-to-date information.
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
        // "orientationAngles" now has up-to-date information.
        binding.dynamicWeatherView.weatherController?.setOrientationAngles(
            (orientationAngles[2] * 180 / PI).toFloat(),
            (orientationAngles[1] * 180 / PI).toFloat()
        )
    }

    private var tempX = 0f
    private var tempY = 0f
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun init() {
        mSensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.weatherViewPager.apply {
            adapter = weatherViewPagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Handler(Looper.myLooper()!!).postDelayed({
                        binding.cityName.text =
                            weatherViewPagerAdapter.currentList[position].location.name
                    }, 20)
                }
            })
        }
        viewModel.currentLocation.observe(viewLifecycleOwner, {
            Log.d(TAG, "init: $it")
            if (it.first.isEmpty() || it.second.isEmpty()) return@observe
            viewModel.getWeather("${it.first},${it.second}")
//            viewModel.getWeather("101010100")
        })
        viewModel.weatherList.observe(viewLifecycleOwner, {
            weatherViewPagerAdapter.submitList(it)
//            Handler(Looper.myLooper()!!).postDelayed({binding.cityName.text =
//                weatherViewPagerAdapter.currentList[binding.weatherViewPager.currentItem].location.name},1000)
        })
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
        binding.dynamicWeatherView.weatherController = SunnyController()
    }
}