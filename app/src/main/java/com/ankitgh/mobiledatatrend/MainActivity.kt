package com.ankitgh.mobiledatatrend

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : BaseActivity() {
    private val mainScope = CoroutineScope(Dispatchers.Main)

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

        //error observer
        apiResponse.apiError.observe(this, Observer {
            showErrorInSnackBar(it, apiResponse)
        })

        //data observer
        apiResponse.data
            ?.observe(this,
                Observer<List<Record?>?> { recordsList ->
                    if (recordsList != null && recordsList.isNotEmpty()) {
                        showProgress(false, cyclicProgress)
                        mainScope.launch {
                            recordAdapter.recordsList =
                                recordViewModel.getSortedRecordsPerYearList(recordsList)
                            recordAdapter.notifyDataSetChanged()
                        }
                        Timber.d(
                            "recordViewModel.getAllRecordsFromRepo() -> Change in Data - Observer on MainActivity invoked.  : ${recordsList.size}"
                        )
                    }
                })
    }

    private fun showErrorInSnackBar(message: String?, apiResponse: ApiResponse) {
        if (apiResponse.apiError.value != null) {
            if (apiResponse.data?.value.isNullOrEmpty()) {
                noDataText.visibility = View.VISIBLE
            } else {
                noDataText.visibility = View.GONE
            }
            Snackbar.make(
                rootView,
                message.toString(),
                Snackbar.LENGTH_LONG
            ).show()
            showProgress(false, cyclicProgress)
        }
    }

    private fun showProgress(status: Boolean, cyclicProgress: ProgressBar) {
        when (status) {
            true -> cyclicProgress.visibility = View.VISIBLE
            false -> cyclicProgress.visibility = View.GONE
        }
    }
}