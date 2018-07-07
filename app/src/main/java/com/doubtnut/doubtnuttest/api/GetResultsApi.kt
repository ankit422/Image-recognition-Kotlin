package com.doubtnut.doubtnuttest.api

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.doubtnut.doubtnuttest.util.Connectivity
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.util.*

abstract class GetDataAPI {

    private val timeOutTime = 15000

    fun GetData(mContext: Context, reqText: String) {
        if (Connectivity.isConnected(mContext)) {
            try {
                val queue = Volley.newRequestQueue(mContext)

                val completeApiUrl = "http://doubtnut.in/doubtnut_test/public/api/questions/get-matched-questions"
                Log.e("completeApiUrl", completeApiUrl)
                val sr = object : StringRequest(Request.Method.POST, completeApiUrl,
                        Response.Listener<String> { response ->
                            try {
                                Log.e("response", response)
                                val p = JsonParser()
                                val jele = p.parse(response)
                                val obj = if (jele.isJsonObject()) jele.getAsJsonObject() else null
                                if (obj != null && obj.has("data") && obj.get("data").isJsonObject) {
                                    val data = obj.get("data").asJsonObject
                                    if (data.has("matched_questions") && data.get("matched_questions").isJsonArray) {
                                        onComplete(data.get("matched_questions").asJsonArray)
                                    } else {
                                        onFailed()
                                    }
                                } else {
                                    onFailed()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                onFailed()
                            }
                        },
                        Response.ErrorListener { onFailed() }) {

                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["ocr_text"] = reqText
                        return params
                    }

                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/x-www-form-urlencoded"
                        return headers
                    }
                }
                sr.retryPolicy = DefaultRetryPolicy(timeOutTime,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

                queue.add(sr)
            } catch (e: Exception) {
                e.printStackTrace()
                onFailed()
            }

        } else {
            onFailed()
        }
    }

    abstract fun onComplete(asJsonArray: JsonArray)
    abstract fun onFailed()
}
