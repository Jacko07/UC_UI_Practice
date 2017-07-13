package com.uc_chrome.hyc.uc_ui_practice.view

import android.content.Context
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import java.util.jar.Attributes

/**
 *  能够使子View被拖动的FrameLayout
 * Created by hyc on 2017/6/20.
 */
open class DragViewFrameLayout (context:Context, attributes: AttributeSet, attrStyle:Int): FrameLayout(context,attributes,attrStyle){
    private var mViewDragHelper :ViewDragHelper?=null

    constructor(context: Context,attributes: AttributeSet):this(context,attributes,0)

    init {

        mViewDragHelper= ViewDragHelper.create(this,1.0f,object :ViewDragHelper.Callback(){
            override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
                return true
            }
        })

    }

}