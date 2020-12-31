package cn.chitanda.weather.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cn.chitanda.weather.adapter.WeatherViewPagerAdapter
import cn.chitanda.weather.databinding.FragmentHomeBinding
import cn.chitanda.weather.viewmodel.WeatherViewModel
import com.permissionx.guolindev.PermissionX

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
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

    private fun init() {
        binding.weatherViewPager.adapter = weatherViewPagerAdapter
        viewModel.currentLocation.observe(viewLifecycleOwner, {
            Log.d(TAG, "init: $it")
            if (it.first.isEmpty() || it.second.isEmpty()) return@observe
            viewModel.getWeather("${it.first},${it.second}")
//            viewModel.getWeather("101010100")
        })
        viewModel.weatherList.observe(viewLifecycleOwner, {
            weatherViewPagerAdapter.submitList(it)
        })
    }
}