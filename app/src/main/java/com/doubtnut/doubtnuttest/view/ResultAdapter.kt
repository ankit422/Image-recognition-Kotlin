package com.doubtnut.doubtnuttest.view

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doubtnut.doubtnuttest.R
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.item_view.view.*

class ResultAdapter(val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    var jsonArray = JsonArray()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val id = jsonArray.get(position).asJsonObject.get("_id").asString
            var data = jsonArray.get(position).asJsonObject.get("_source").asJsonObject.get("ocr_text").asString
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                data = Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT).toString()

            } else {
                data = Html.fromHtml(data).toString()
            }
            holder.tvData?.text = data
            holder.tvId?.text = "ID: $id"

        } catch (e: Exception) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false))
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return jsonArray.size()
    }

    fun setdata(asJsonArray: JsonArray) {
        this.jsonArray = asJsonArray
        notifyDataSetChanged()
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvData = view.tv_data
    val tvId = view.tv_id
}