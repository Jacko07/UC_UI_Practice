package com.uc_chrome.hyc.uc_ui_practice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() , View.OnClickListener{
   var mBtnCircleSpringBack :Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtnCircleSpringBack=findViewById(R.id.btn_circle_springback) as Button
        mBtnCircleSpringBack!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_circle_springback->{
                ElasticCircleActivity.start(this)
            }
        }
    }
}
