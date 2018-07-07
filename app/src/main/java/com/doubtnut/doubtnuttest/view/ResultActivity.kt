package com.doubtnut.doubtnuttest.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.doubtnut.doubtnuttest.R
import com.doubtnut.doubtnuttest.api.GetDataAPI
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val strData: String = intent.getStringExtra("data")
        val adapter = ResultAdapter(this)
        txtResult.text = strData

        val getDataAPI = object : GetDataAPI() {
            override fun onComplete(asJsonArray: JsonArray) {

                adapter.setdata(asJsonArray);
            }

            override fun onFailed() {
                Toast.makeText(this@ResultActivity, "Something went wrong..", Toast.LENGTH_SHORT).show()
            }
        }

        getDataAPI.GetData(this, strData)

        // Creates a vertical Layout Manager
        rv_result_list.layoutManager = LinearLayoutManager(this)
        rv_result_list.adapter = adapter

    }
}