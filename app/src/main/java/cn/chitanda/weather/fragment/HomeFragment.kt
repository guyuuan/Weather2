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
import cn.chitanda.weather.widget.weather.controller.RainOrSnowController
import com.permissionx.guolindev.PermissionX
import leakcanary.LeakCanary
import kotlin.math.PI

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: WeatherViewModel by viewModels()


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

        binding.dynamicWeatherView.apply {
            weatherController?.resumeAnim()
            onResume()
        }
        super.onResume()
    }

    override fun onPause() {
        binding.dynamicWeatherView.apply {
            weatherController?.stopAnim()
            onPause()
        }
        super.onPause()
    }



    private fun init() {
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
        })
        viewModel.weatherList.observe(viewLifecycleOwner, {
            weatherViewPagerAdapter.submitList(it)
        })
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }

        binding.dynamicWeatherView.weatherController = RainOrSnowController(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}