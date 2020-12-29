package cn.chitanda.weather.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cn.chitanda.weather.databinding.FragmentHomeBinding
import cn.chitanda.weather.model.ApiResult
import cn.chitanda.weather.network.Api
import cn.chitanda.weather.network.RetrofitCreator
import cn.chitanda.weather.viewmodel.HomeFragmentViewModel
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Response

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
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
        PermissionX.init(this).permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Weather需要您同意一下权限才能正常使用", "好的", "不")
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去设置当中同意定位权限", "好")
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted && grantedList.isNotEmpty() && deniedList.isEmpty()) {
                    init()
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        viewModel.init()
        viewModel.currentLocation.observe(viewLifecycleOwner, {
            Log.d(TAG, "init: $it")
            if (it.first.isEmpty() || it.second.isEmpty()) return@observe
            RetrofitCreator.create(Api::class.java)
                .getNowWeather(location = "${it.first},${it.second}").enqueue(
                    object : retrofit2.Callback<ApiResult> {
                        override fun onResponse(
                            call: Call<ApiResult>,
                            response: Response<ApiResult>
                        ) {
                            if (response.isSuccessful) {
                                binding.textView.text = response.body().toString()
                            } else {
                                Log.e(TAG, "onResponse: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<ApiResult>, t: Throwable) {
                            Log.e(TAG, "onFailure: ${t.message}")
                        }
                    })
        })
    }
}