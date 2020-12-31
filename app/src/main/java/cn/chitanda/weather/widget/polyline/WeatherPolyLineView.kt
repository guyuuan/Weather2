package cn.chitanda.weather.widget.polyline

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import cn.chitanda.weather.model.Daily
import cn.chitanda.weather.utils.dp

/**
 * @Author:       Chen
 * @Date:         2020/12/30 17:42
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */

class WeatherPolyLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        var maxTempDiff = 30
        var midTemp = 0f
    }

    private lateinit var currentValue: Daily
    private var nextValue: Daily? = null
    private var preValue: Daily? = null

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 2.dp.toFloat()
    }

    private var maxHeight = 0f
    private val proportion: Float
        get() = maxHeight / maxTempDiff.toFloat()
    private lateinit var startMaxPoint: PointF
    private lateinit var endMaxPoint: PointF
    private lateinit var midMaxPoint: PointF
    private lateinit var midMinPoint: PointF
    private lateinit var startMinPoint: PointF
    private lateinit var endMinPoint: PointF

    private var isMeasured = false
    private var isFirst = true
    private var isLatest = false


    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidth = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val viewHeight = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)
        startMaxPoint = PointF(0f, viewHeight * 0.25f)
        startMinPoint = PointF(0f, viewHeight * 0.75f)

        midMaxPoint = PointF(viewWidth * 0.5f, viewHeight * 0.25f)
        midMinPoint = PointF(viewWidth * 0.5f, viewHeight * 0.75f)
        endMaxPoint = PointF(viewWidth.toFloat(), viewHeight * 0.25f)
        endMinPoint = PointF(viewWidth.toFloat(), viewHeight * 0.75f)
        maxHeight = 0.5f * viewHeight
        isMeasured = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isMeasured) return
        canvas?.let {
//            it.drawColor(Color.parseColor("#50FFFFFF"))
            drawPoly(it)
            it.drawCircle(midMaxPoint.x, midMaxPoint.y, 3.dp.toFloat(), linePaint)
            it.drawCircle(midMinPoint.x, midMinPoint.y, 3.dp.toFloat(), linePaint)
        }
    }

    private fun drawPoly(canvas: Canvas) {
        if (!isFirst) {
            canvas.drawLine(
                startMaxPoint.x,
                startMaxPoint.y,
                midMaxPoint.x,
                midMaxPoint.y,
                linePaint
            )
            canvas.drawLine(
                startMinPoint.x,
                startMinPoint.y,
                midMinPoint.x,
                midMinPoint.y,
                linePaint
            )
        }
        if (!isLatest) {
            canvas.drawLine(
                midMaxPoint.x,
                midMaxPoint.y,
                endMaxPoint.x,
                endMaxPoint.y,
                linePaint
            )
            canvas.drawLine(
                midMinPoint.x,
                midMinPoint.y,
                endMinPoint.x,
                endMinPoint.y,
                linePaint
            )
        }
    }

    var currentList: List<Daily>? = listOf()


    private fun calculationPoint() {
        isFirst = preValue == null
        isLatest = nextValue == null
        val currentMaxTemp = (currentValue.tempMax ?: return).toInt()
        val currentMinTemp = (currentValue.tempMin ?: return).toInt()
        var nextMinTemp = 0
        var nextMaxTemp = 0
        var preMinTemp = 0
        var preMaxTemp = 0


        midMaxPoint.y = height / 2f + (midTemp - currentMaxTemp) * proportion
        midMinPoint.y = height / 2f + (midTemp - currentMinTemp) * proportion
        if (!isFirst) {
            nextMinTemp = (nextValue?.tempMin ?: return).toInt()
            nextMaxTemp = (nextValue?.tempMax ?: return).toInt()
            endMaxPoint.y =
                height / 2f + (midTemp - (currentMaxTemp + nextMaxTemp) / 2f) * proportion
            endMinPoint.y =
                height / 2f + (midTemp - (currentMinTemp + nextMinTemp) / 2f) * proportion
        }
        if (!isLatest) {
            preMinTemp = (preValue?.tempMin ?: return).toInt()
            preMaxTemp = (preValue?.tempMax ?: return).toInt()
            startMaxPoint.y =
                height / 2f + (midTemp - (currentMaxTemp + preMaxTemp) / 2f) * proportion
            startMinPoint.y =
                height / 2f + (midTemp - (currentMinTemp + preMinTemp) / 2f) * proportion
        }
        Log.d(
            TAG,
            "calculationPoint: $preMaxTemp $preMinTemp $currentMaxTemp $currentMinTemp $nextMaxTemp $nextMinTemp"
        )
        if (!isFirst) Log.d(TAG, "$position calculationPoint: $startMaxPoint $startMinPoint")
        Log.d(TAG, "calculationPoint: $midMaxPoint $midMinPoint")
        if (!isLatest) Log.d(TAG, "calculationPoint: $endMaxPoint $endMinPoint")
//        Log.d(TAG, "calculationPoint: isFirst=$isFirst  isLast=$isLatest")
        postInvalidate()
    }

    private var position = 0
    fun setPosition(position: Int) {
        this.position = position
        preValue = getItem(position - 1)
        currentValue = getItem(position)!!
        nextValue = getItem(position + 1)
        tag = currentValue.fxDate
        postDelayed({ calculationPoint() }, 50)
    }

    private fun getItem(position: Int): Daily? {
        return try {
            currentList?.get(position)
        } catch (e: Throwable) {
            Log.e(TAG, e.toString())
            null
        }
    }
}

private const val TAG = "WeatherPolyLineView"