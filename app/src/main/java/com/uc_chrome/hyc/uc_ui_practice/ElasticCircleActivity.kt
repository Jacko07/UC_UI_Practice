package com.uc_chrome.hyc.uc_ui_practice

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ElasticCircleActivity : AppCompatActivity() {

    companion object{
        fun start(activity:Activity?) {
            var intent:Intent=Intent(activity!!,ElasticCircleActivity().javaClass)
            activity!!.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elastic_circle)
    }
}
