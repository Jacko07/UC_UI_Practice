package com.uc_chrome.hyc.uc_ui_practice.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * 仿QQ气泡拖动效果
 * Created by hyc on 2017/6/7.
 */
open class ElasticCircleView(context: Context, attrs: AttributeSet, defStyleAttr: Int) : View(context, attrs) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    //    //变化因子  影响半径的变化快慢
//    private val CHANGE_FACTOR = 0.03.toFloat()
    //两圆半径与两圆心距离的影响系数
    private var tmpRate = 0.toFloat()

    private var density: Int = 0
    private var displayWidth: Int = 0
    private var displayHeight: Int = 0
    //静止的圆的半径
    private var mOriginRadius: Float = 0.toFloat()
    //被拖动的圆的半径
    private var mMovingRadius: Float = 0.toFloat()
    //静止的圆的圆心坐标
    private var mOriginX: Float = 0.toFloat()
    private var mOriginY: Float = 0.toFloat()
    //被拖动的圆的圆心坐标
    private var mMovingX: Float = 0.toFloat()
    private var mMovingY: Float = 0.toFloat()
    //初始时圆的大小
    private var mStartRadius: Float = 0.toFloat()
    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()

    private var mEndX: Float = 0.toFloat()
    private var mEndY: Float = 0.toFloat()

    //两圆开始重合的临界拖动距离
    private var mLimitLength: Float = 0.toFloat()
    //未达到临界距离放手时会复位
    private var mRegressAnimator: ValueAnimator? = null
    //    //达到临界距离时原来位置的圆会被新位置的圆吸收,进入自由拖动状态
//    private var mAbsorbAnimator: ValueAnimator? = null
    private var mPath: Path? = null
    private var mOriginCirclePaint: Paint? = null
    private var mMovingCirclePaint: Paint? = null
    private var mPathPaint: Paint? = null

    private var isOutOfRange:Boolean=false


    init {
        density = resources.displayMetrics.density.toInt()
        displayWidth = resources.displayMetrics.widthPixels
        displayHeight = resources.displayMetrics.heightPixels
        mLimitLength = (displayWidth / 5).toFloat()

        initPosition()

        mOriginCirclePaint = Paint()
        mMovingCirclePaint = Paint()
        mPathPaint = Paint()
        mPath = Path()

        mOriginCirclePaint!!.color = Color.parseColor("#FF4081")
//        mOriginCirclePaint!!.color = Color.parseColor("#ffffff")
        mOriginCirclePaint!!.isAntiAlias = true
        mOriginCirclePaint!!.style = Paint.Style.FILL

        mMovingCirclePaint!!.color = Color.parseColor("#FF4081")
        mMovingCirclePaint!!.isAntiAlias = true
        mMovingCirclePaint!!.style = Paint.Style.FILL

        mPathPaint!!.color = Color.parseColor("#FF4081")
//        mPathPaint!!.color = Color.parseColor("#FF6666")
        mPathPaint!!.isAntiAlias = true
        mPathPaint!!.style = Paint.Style.FILL

        initRegressAnim()
//        initAbsorbAnim()
    }

    /**
     * 设置复位动画
     */
    private fun initRegressAnim() {
        mRegressAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(1500)
        mRegressAnimator!!.addUpdateListener { animation ->
            mMovingX = mOriginX + (mEndX - mOriginX) * animation.animatedValue as Float
            mMovingY = mOriginY + (mEndY - mOriginY) * animation.animatedValue as Float
            tmpRate = getRadiusDistance(mMovingX, mMovingY, mOriginX, mOriginY) / mLimitLength
            mOriginRadius = mStartRadius * (1 - tmpRate)
            mMovingRadius = mStartRadius * tmpRate
            updatePath()
            invalidate()
        }
    }

    private fun initPosition() {
        mStartX = (displayWidth / 2).toFloat()
        mStartY = (displayHeight / 2).toFloat()
        mStartRadius = (displayWidth / 25).toFloat()

        mOriginX = mStartX
        mOriginY = mStartY
        mOriginRadius = mStartRadius

        mMovingX = mOriginX
        mMovingY = mOriginY
        mMovingRadius = mOriginRadius
    }

//    /**
//     * 设置新圆吸收旧圆动画
//     */
//    private fun initAbsorbAnim() {
//        mAbsorbAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(1500)
//        mAbsorbAnimator!!.addUpdateListener { animation ->
//            mOriginX = mMovingX + (mEndX - mOriginX) * animation.animatedValue as Float
//            mOriginY = mMovingY + (mEndY - mOriginY) * animation.animatedValue as Float
//
//            tmpRate = getRadiusDistance(mMovingX, mMovingY, mOriginX, mOriginY) / mLimitLength
//            mOriginRadius = mStartRadius * (1 - tmpRate)
//            mMovingRadius = mStartRadius * tmpRate
//
////            mOriginRadius = mOriginRadius * getRadiusDistance(mOriginX, mOriginY, mMovingX, mOriginY) * CHANGE_FACTOR
//            updatePath()
//            invalidate()
//        }
//    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventAction = event.action
        val x = event.x.toInt()
        val y = event.y.toInt()
        var temp = 0f
        mMovingX = x.toFloat()
        mMovingY = y.toFloat()
        Log.i("xy", "mMovingX:" + mMovingX + ",mMovingY" + mMovingY)
        var distance = getRadiusDistance(mMovingX, mMovingY, mOriginX, mOriginY)
        isOutOfRange = distance >= mLimitLength
        Log.i("Touch", "distance:" + distance + ",OriginR:" + mOriginRadius + ",MovingR:" + mMovingRadius + ",isOutOfRange:" + isOutOfRange)
        var tmp = distance / mLimitLength
        if (tmp > 1) {
            tmp = 0.95.toFloat()
        }
        when (eventAction) {
            MotionEvent.ACTION_DOWN -> {
                if (mOriginX - mOriginRadius > x || mOriginX + mOriginRadius < x || mOriginY - mOriginRadius > y || mOriginY + mOriginRadius < y) {
                    return false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isOutOfRange) {
                    mOriginRadius = (1 - tmp) * mStartRadius
                    mMovingRadius = tmp * mStartRadius
                    Log.i("Touch", "ACTION_MOVE" + " mOriginRadius:" + mOriginRadius + " mMovingRadius" + mMovingRadius)
                    updatePath()
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
//                Log.i("Touch","ACTION_UP"+" mEndX"+mEndX+" mEndY"+mEndY)
                mEndX = mMovingX
                mEndY = mMovingY
                if (isOutOfRange) {
                    mPath!!.reset()
                    isOutOfRange=false
                    initPosition()
                } else {
                    mRegressAnimator!!.start()
                }
            }
        }
        return true
    }

    private fun getRadiusDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(Math.pow(Math.abs(x1 - x2).toDouble(), 2.0) + Math.pow(Math.abs(y1 - y2).toDouble(), 2.0)).toFloat()
    }


    private fun updatePath() {
        var corner = Math.atan((Math.abs(mMovingX - mOriginX) / Math.abs(mMovingY - mOriginX)).toDouble())

        val offsetX1 = (mOriginRadius * Math.sin(corner)).toFloat()
        val offsetY1 = (mOriginRadius * Math.cos(corner)).toFloat()

        val offsetX2 = (mMovingRadius * Math.sin(corner)).toFloat()
        val offsetY2 = (mMovingRadius * Math.cos(corner)).toFloat()

        val x1 = mOriginX - offsetX1
        val y1 = mOriginY + offsetY1

        val x2 = mMovingX - offsetX2
        val y2 = mMovingY + offsetY2

        val x3 = mMovingX + offsetX2
        val y3 = mMovingY - offsetY2

        val x4 = mOriginX + offsetX1
        val y4 = mOriginY - offsetY1

        val midpointX = (mOriginX + mMovingX) / 2
        val midpointY = (mOriginY + mMovingY) / 2

        mPath!!.reset()
        mPath!!.moveTo(x1, y1)
        mPath!!.quadTo(midpointX, midpointY, x2, y2)
        mPath!!.lineTo(x3, y3)
        mPath!!.quadTo(midpointX, midpointY, x4, y4)
        mPath!!.lineTo(x1, y1)


//        var x1Offset = mMovingRadius * Math.sin(corner)
//        var y1Offset=mMovingRadius*Math.cos(corner)
//
//        var x2Offset=mOriginRadius*Math.sin(corner)
//        var y2Offset=mOriginRadius*Math.cos(corner)
//
//        var x1=mMovingX+x1Offset
//        var y1=mMovingY+y1Offset
//
//        var x2=mMovingX
//
//        var midX=(mMovingX+mOriginX)/2
//        var midY=(mMovingY+mOriginY)/2
//
//        m
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isOutOfRange) {
            canvas.drawCircle(mOriginX, mOriginY, mOriginRadius, mOriginCirclePaint!!)
            canvas.drawPath(mPath!!, mPathPaint!!)
        }
        canvas.drawCircle(mMovingX, mMovingY, mMovingRadius, mMovingCirclePaint!!)

    }

}
