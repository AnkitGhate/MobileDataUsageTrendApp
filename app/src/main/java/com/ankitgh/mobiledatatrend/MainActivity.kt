package com.ankitgh.mobiledatatrend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.repository.ApiResponse
import com.ankitgh.mobiledatatrend.ui.RecordAdapter
import com.ankitgh.mobiledatatrend.viewmodel.RecordViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val TAG: String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recordViewModel: RecordViewModel =
            ViewModelProvider(this).get(RecordViewModel::class.java)
        observeData(recordViewModel)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun observeData(recordViewModel: RecordViewModel) {
        val recyclerView: RecyclerView = recyclerview
        val recordAdapter = RecordAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = recordAdapter

        val apiResponse = recordViewModel.getAllRecordsFromRepo()

        apiResponse.apiError.observe(this, Observer {
            updateUi(it, apiResponse)
        })

        apiResponse.data
            ?.observe(this,
                Observer<List<Record?>?> { recordsList ->
                    if (recordsList != null && recordsList.isNotEmpty()) {
                        noDataText.visibility = View.GONE
                        recyclerview.visibility = View.VISIBLE
                        recordAdapter.recordsList =
                            recordViewModel.getSortedRecordsPerYearList(recordsList)
                        recordAdapter.notifyDataSetChanged()
                        Log.d(TAG,"recordViewModel.getAllRecordsFromRepo() -> Change in Data - Observer on MainActivity invoked.  : ${recordsList.size}"
                        )
                    }
                })
    }

    private fun updateUi(message: String?, apiResponse: ApiResponse) {
        if (apiResponse.apiError.value != null) {
            recyclerview.visibility = View.GONE
            noDataText.visibility = View.VISIBLE
            Snackbar.make(
                rootView,
                "There is a problem refreshing data. [ ERROR : ${message.toString()}]",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}