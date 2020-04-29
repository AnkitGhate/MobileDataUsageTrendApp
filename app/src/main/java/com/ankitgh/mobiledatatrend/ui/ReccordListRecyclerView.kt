package com.ankitgh.mobiledatatrend.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ankitgh.mobiledatatrend.R
import com.ankitgh.mobiledatatrend.rest.model.RecordYear
import kotlinx.android.synthetic.main.record_item.view.*

/**
 * Adapter class used to bind data and view for recycler view in
 * UI package-> MainActivity.kt
 */
class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    var recordsList: ArrayList<RecordYear> = ArrayList()

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var yeartv: TextView = itemView.year_tv
        var totaldataconsumedtv: TextView = itemView.mobile_data_usage_total_tv
        var lowdataconsumptionimage: ImageView = itemView.low_data_consmption_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        return RecordViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.yeartv.text = recordsList[position].quater
        holder.totaldataconsumedtv.text = recordsList[position].dataUsage.toBigDecimal().toPlainString()
        holder.lowdataconsumptionimage.visibility = if (recordsList[position].dipInUsage) View.GONE else View.VISIBLE

        holder.lowdataconsumptionimage.setOnClickListener { v ->
            Toast.makeText(v.context, "Mobile Data Usage dipped in one of the Quarter ${recordsList[position].quater}", Toast.LENGTH_SHORT).show()
        }
    }
}