package cn.chitanda.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import cn.chitanda.weather.utils.dp
import cn.chitanda.weather.widget.weather.controller.IController
import kotlinx.coroutines.*
import kotlin.math.PI

/**
 * @Author:       Chen
 * @Date:         2021/1/6 15:12
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class DynamicWeatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, SensorEventListener {
    private lateinit var mSensorManager: SensorManager
    private var rotationText: String = ""
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var _controller: IController? = null
    var weatherController: IController?
        get() = _controller
        set(value) {
            _controller = value
        }
    private var isRunning = false
    private lateinit var canvas: Canvas
    private lateinit var renderJob: Job
    private var isCreated = false
    val refreshRate: Long
        get() = 1000L / (context.display?.refreshRate ?: 60f).toInt()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 18.dp
        color = Color.WHITE
    }

    private fun init() {
        if (this::renderJob.isInitialized) renderJob.cancel()
        weatherController?.init(this, width, height)
        isRunning = true
        MainScope().launch(Dispatchers.Default) {
            while (isRunning) {
                try {
                    synchronized(holder) {
                        canvas = holder.lockCanvas()
                        weatherController?.draw(canvas)
                        val text = rotationText.split(";")
                        canvas.drawText(text.first(), width / 20f, height / 7f, textPaint)
                        canvas.drawText(text.last(), width / 20f, height / 7f + 22.dp, textPaint)
                        holder.unlockCanvasAndPost(canvas)
                    }
                    delay(refreshRate)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }.also { renderJob = it }
    }

    fun onResume() {
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).also { accelerometer ->
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
        mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).also { field ->
            mSensorManager.registerListener(this, field, SensorManager.SENSOR_DELAY_UI)
        }
        if (this::renderJob.isInitialized) {
            isRunning = true
        }
    }

    fun onPause() {
        mSensorManager.unregisterListener(this)
        isRunning = false
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated: ")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surfaceChanged: ")
        init()
        isCreated = true
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: ")
        isRunning = false
        isCreated = false
        if (this::renderJob.isInitialized && renderJob.isActive) renderJob.cancel()
    }

    init {
        holder.addCallback(this)
        Log.d(TAG, "init: $holder")
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
        val rotationX = (orientationAngles[1] * 180 / PI).toFloat()
        val rotationY = (orientationAngles[2] * 180 / PI).toFloat()
        weatherController?.setOrientationAngles(
            rotationY,
            rotationX
        )
        rotationText = "绕 x 轴旋转的角度: $rotationX° ;绕 y 轴旋转的角度: $rotationY°"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}

private const val TAG = "DynamicWeatherView"