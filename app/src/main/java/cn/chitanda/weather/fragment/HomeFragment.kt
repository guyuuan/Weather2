package cn.chitanda.weather.fragment

import android.Manifest
import android.hardware.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cn.chitanda.weather.adapter.WeatherViewPagerAdapter
import cn.chitanda.weather.databinding.FragmentHomeBinding
import cn.chitanda.weather.viewmodel.WeatherViewModel
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        ViewCompat.setOnApplyWindowInsetsListener(binding.weatherViewPager) { v, insets ->
            v.updatePadding(
                top=insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom,
            )
            insets
        }
        PermissionX.init(this).permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Weather需要您同意一下权限才能正常使用", "好的", "不")
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去设置当中同意定位权限", "好")
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted && grantedList.isNotEmpty() && deniedList.isEmpty()) {
                    viewModel.init()
                    binding.swipeRefresh.isRefreshing = true
                }
            }
    }

    override fun onResume() {
        binding.dynamicWeatherView.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.dynamicWeatherView.onPause()
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
                        binding.dynamicWeatherView.weatherType =
                            weatherViewPagerAdapter.currentList[position].now
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
            binding.swipeRefresh.isRefreshing = false
        })
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.Default) {
                delay(1000)
                binding.swipeRefresh.isRefreshing = false
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}