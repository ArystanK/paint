package kz.arctan.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.abs

class PaintView(context: Context?, attributes: AttributeSet?) :
    View(context, attributes) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint: Paint = Paint()
    private val paths: ArrayList<FingerPath> = ArrayList()
    private var mBackgroundColor = Color.WHITE
    private var strokeWidth = 10f
    private var defaultColor = Color.BLACK
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null

    fun setBrushSize(brushSize: Float) {
        strokeWidth = brushSize
    }

    fun setColor(defaultColor: Int) {
        this.defaultColor = defaultColor
    }

    fun init(metrics: DisplayMetrics) {
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
    }

    fun clear() {
        mBackgroundColor = Color.WHITE
        paths.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        mCanvas!!.drawColor(mBackgroundColor)
        for (fp in paths) {
            mPaint.strokeWidth = fp.strokeWidth
            mPaint.color = fp.pathColor
            mCanvas!!.drawPath(fp.path, mPaint)
        }
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mPaint)
        canvas.restore()
    }

    private fun startTouch(x: Float, y: Float) {
        mPath = Path()
        val fp = FingerPath(strokeWidth, mPath!!, defaultColor)
        paths.add(fp)
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun moveTouch(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy = abs(y - mY)
        if (dx >= 0 || dy >= 0) {
            mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun upTouch() {
        mPath!!.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouch(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                moveTouch(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                upTouch()
                invalidate()
            }
        }
        return true
    }

    init {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
    }
}