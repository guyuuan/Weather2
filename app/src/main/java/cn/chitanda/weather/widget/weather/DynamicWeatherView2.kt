package cn.chitanda.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import cn.chitanda.weather.widget.weather.controller.IController
import kotlin.concurrent.thread

/**
 * @Author:       Chen
 * @Date:         2021/1/6 15:12
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class DynamicWeatherView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private var _controller: IController? = null
    var weatherController: IController?
        get() = _controller
        set(value) {
            _controller = value
            if (isCreated) {
                init()
            } else {
                postDelayed({ init() }, 1000)
            }
        }
    private var isRunning = false
    private lateinit var canvas: Canvas
    private lateinit var drawThread: Thread
    private var isCreated = false
    private val refreshRate: Long
        get() = 1000L / (context.display?.refreshRate ?: 60f).toInt()

    private fun init() {
        if (isRunning) drawThread.interrupt()
        weatherController?.init(this, width, height)
        isRunning = true
        drawThread = thread {
            while (isRunning) {
                try {
                    synchronized(holder) {
                        canvas = holder.lockCanvas()
                        weatherController?.draw(canvas)
                        holder.unlockCanvasAndPost(canvas)
                    }
                    Thread.sleep(refreshRate)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated: ")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surfaceChanged: ")
        isCreated = true
        weatherController?.let {
            init()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: ")
        isRunning = false
        drawThread.interrupt()
    }

    init {
        holder.addCallback(this)
        Log.d(TAG, "init: $holder")
    }

}

private const val TAG = "DynamicWeatherView2"