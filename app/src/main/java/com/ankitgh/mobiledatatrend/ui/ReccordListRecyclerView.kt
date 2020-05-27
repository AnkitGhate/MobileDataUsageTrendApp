package com.ankitgh.mobiledatatrend.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ankitgh.mobiledatatrend.R
import com.ankitgh.mobiledatatrend.databinding.RecordItemBinding
import com.ankitgh.mobiledatatrend.rest.model.RecordYear


/**
 * Adapter class used to bind data and view for recycler view in
 * UI package-> MainActivity.kt
 */
class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    var recordsList: ArrayList<RecordYear> = ArrayList()

    class RecordViewHolder(recordItemViewBinding: RecordItemBinding) :
        RecyclerView.ViewHolder(recordItemViewBinding.root) {
        val recordItemBinding: RecordItemBinding = recordItemViewBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val itemView: RecordItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.record_item, parent,
            false
        )
        return RecordViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.recordItemBinding.recordYearBindingVariable = recordsList[position]

        holder.recordItemBinding.lowDataConsmptionImage.setOnClickListener { v ->
            Toast.makeText(
                v.context,
                "Mobile Data Usage dipped in one of the Quarter ${recordsList[position].quater}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}