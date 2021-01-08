package cn.chitanda.weather.widget.polyline

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
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

    private val maxLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 1.5f.dp
    }
    private val minLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#99FFFFFF")
        strokeWidth = 1f.dp
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 14.dp
        textAlign = Paint.Align.CENTER
    }
    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#30FFFFFF")
    }
    private var maxHeight = 0f
    private val proportion: Float
        get() = (maxHeight - 48.dp) / maxTempDiff.toFloat()
    private lateinit var startMaxPoint: PointF
    private lateinit var endMaxPoint: PointF
    private lateinit var midMaxPoint: PointF
    private lateinit var midMinPoint: PointF
    private lateinit var startMinPoint: PointF
    private lateinit var endMinPoint: PointF
    private lateinit var leftPath: Path
    private lateinit var rightPath: Path
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
        maxHeight = 0.7f * viewHeight
        leftPath = Path().apply {
            reset()
            moveTo(startMaxPoint.x, startMaxPoint.y)
            lineTo(midMaxPoint.x, midMaxPoint.y)
            lineTo(midMinPoint.x, midMinPoint.y)
            lineTo(startMinPoint.x, startMinPoint.y)
            close()
        }
        rightPath = Path().apply {
            reset()
            moveTo(midMaxPoint.x, midMaxPoint.y)
            lineTo(endMaxPoint.x, endMaxPoint.y)
            lineTo(endMinPoint.x, endMinPoint.y)
            lineTo(midMinPoint.x, midMinPoint.y)
            close()
        }
        isMeasured = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isMeasured) return
        canvas?.let {
            drawPoly(it)
            drawTempText(it)
            it.drawCircle(midMaxPoint.x, midMaxPoint.y, 3.dp.toFloat(), maxLinePaint)
            it.drawCircle(midMinPoint.x, midMinPoint.y, 3.dp.toFloat(), maxLinePaint)
        }
    }

    private fun drawTempText(canvas: Canvas) {
        canvas.drawText(
            "$currentMaxTemp °",
            midMaxPoint.x,
            midMaxPoint.y - 10.dp.toFloat(),
            textPaint
        )
        canvas.drawText(
            "$currentMinTemp °",
            midMinPoint.x,
            midMinPoint.y + 24.dp.toFloat(),
            textPaint
        )
    }

    private fun drawPoly(canvas: Canvas) {
        calculatePoint()
        canvas.drawLine(
            startMaxPoint.x,
            startMaxPoint.y,
            midMaxPoint.x,
            midMaxPoint.y,
            maxLinePaint
        )
        canvas.drawLine(
            startMinPoint.x,
            startMinPoint.y,
            midMinPoint.x,
            midMinPoint.y,
            minLinePaint
        )
        canvas.drawPath(leftPath, pathPaint)
        canvas.drawLine(
            midMaxPoint.x,
            midMaxPoint.y,
            endMaxPoint.x,
            endMaxPoint.y,
            maxLinePaint
        )
        canvas.drawLine(
            midMinPoint.x,
            midMinPoint.y,
            endMinPoint.x,
            endMinPoint.y,
            minLinePaint
        )
        canvas.drawPath(rightPath, pathPaint)
    }

    var currentList: List<Daily>? = listOf()

    private var currentMaxTemp = 0
    private var currentMinTemp = 0
    private fun calculatePoint() {
        if (position < 0) return
        isFirst = preValue == null
        isLatest = nextValue == null
        currentMaxTemp = (currentValue.tempMax ?: return).toInt()
        currentMinTemp = (currentValue.tempMin ?: return).toInt()
        var nextMinTemp = 0
        var nextMaxTemp = 0
        var preMinTemp = 0
        var preMaxTemp = 0


        midMaxPoint.y = height / 2f + (midTemp - currentMaxTemp) * proportion
        midMinPoint.y = height / 2f + (midTemp - currentMinTemp) * proportion
        if (!isFirst) {
            preMinTemp = (preValue?.tempMin ?: return).toInt()
            preMaxTemp = (preValue?.tempMax ?: return).toInt()
            startMaxPoint.y =
                height / 2f + (midTemp - (currentMaxTemp + preMaxTemp) / 2f) * proportion
            startMinPoint.y =
                height / 2f + (midTemp - (currentMinTemp + preMinTemp) / 2f) * proportion
        }
        if (!isLatest) {
            nextMinTemp = (nextValue?.tempMin ?: return).toInt()
            nextMaxTemp = (nextValue?.tempMax ?: return).toInt()
            endMaxPoint.y =
                height / 2f + (midTemp - (currentMaxTemp + nextMaxTemp) / 2f) * proportion
            endMinPoint.y =
                height / 2f + (midTemp - (currentMinTemp + nextMinTemp) / 2f) * proportion


        }
        if (isFirst) {
            startMaxPoint.y = midMaxPoint.y
            startMinPoint.y = midMinPoint.y
        }
        if (isLatest) {
            endMaxPoint.y = midMaxPoint.y
            endMinPoint.y = midMinPoint.y
        }
        leftPath = Path().apply {
            reset()
            moveTo(startMaxPoint.x, startMaxPoint.y)
            lineTo(midMaxPoint.x, midMaxPoint.y)
            lineTo(midMinPoint.x, midMinPoint.y)
            lineTo(startMinPoint.x, startMinPoint.y)
            close()
        }
        rightPath = Path().apply {
            reset()
            moveTo(midMaxPoint.x, midMaxPoint.y)
            lineTo(endMaxPoint.x, endMaxPoint.y)
            lineTo(endMinPoint.x, endMinPoint.y)
            lineTo(midMinPoint.x, midMinPoint.y)
            close()
        }
    }

    private var position = -1
    fun setPosition(position: Int) {
        this.position = position
        preValue = getItem(position - 1)
        currentValue = getItem(position)!!
        nextValue = getItem(position + 1)
        tag = currentValue.fxDate
//        postDelayed({ calculationPoint() }, 50)
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