package com.uc_chrome.hyc.uc_ui_practice.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.jar.Attributes

/**
 * 仿QQ气泡拖动效果
 * Created by hyc on 2017/6/7.
 */
class ElasticCircleView constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : View(context, attrs) {
    private val CHANGE_FACTOR = 7

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

    //两圆开始重合的临界拖动距离
    private var mLimitLength: Float = 0.toFloat()

    private var animator:ValueAnimator?=null
    private var mPath: Path? = null
    private var mPaint: Paint? = null

    init {
        density = resources.displayMetrics.density.toInt()
        displayWidth = resources.displayMetrics.widthPixels
        displayHeight = resources.displayMetrics.heightPixels

        mOriginX = (displayWidth / 2).toFloat()
        mOriginY = (displayHeight / 2).toFloat()
        mOriginRadius = (density / 25).toFloat()

        mStartRadius = mOriginRadius

        mMovingX = mOriginX
        mMovingY = mOriginY
        mMovingRadius = mOriginRadius

        mPaint= Paint()
        mPath= Path()

        mPaint!!.color= Color.parseColor("#ff300")
        mPaint!!.isAntiAlias=true
        mPaint!!.style=Paint.Style.FILL

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventAction = event.action
        val x = event.x.toInt()
        val y = event.y.toInt()
        var temp = 0f
        when (eventAction) {
            MotionEvent.ACTION_DOWN -> {
                if (mOriginX - mOriginRadius > x || mOriginX + mOriginRadius < x || mOriginY - mOriginRadius > y || mOriginY + mOriginRadius < y) {
                    return false
                }
                mMovingX = x.toFloat()
                mMovingY = y.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                mMovingX = x.toFloat()
                mMovingY = y.toFloat()
                var distance = getRadiusDistance(mMovingX, mMovingY, mOriginX, mOriginY)
                var tmp = distance / CHANGE_FACTOR

                mOriginRadius = (1 - tmp) * mStartRadius
                mMovingRadius = tmp * mStartRadius

                if (distance >= mLimitLength) {

                }
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return super.onTouchEvent(event)
    }

    private fun getRadiusDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(Math.pow(Math.abs(x1 - x2).toDouble(), 2.0) + Math.pow(Math.abs(y1 - y2).toDouble(), 2.0)).toFloat()
    }


    private fun updatePath() {
        var corner = Math.atan((Math.abs(mMovingX - mOriginX) / Math.abs(mMovingY - mOriginX)).toDouble())
        var x1Offset = mMovingRadius * Math.sin(corner)
        var y1Offset=mMovingRadius*Math.cos(corner)

        var x2Offset=mOriginRadius*Math.sin(corner)
        var y2Offset=mOriginRadius*Math.cos(corner)

        var x1=mMovingX+x1Offset
        var y1=mMovingY+y1Offset

        var x2=mMovingX

        var midX=(mMovingX+mOriginX)/2
        var midY=(mMovingY+mOriginY)/2

        m
    }



}
