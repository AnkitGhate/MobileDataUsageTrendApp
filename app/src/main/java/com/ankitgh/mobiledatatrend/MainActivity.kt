package com.ankitgh.mobiledatatrend

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.ui.RecordAdapter
import com.ankitgh.mobiledatatrend.viewmodel.RecordViewModel

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recordViewModel: RecordViewModel = ViewModelProvider(this).get(RecordViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val recordAdapter = RecordAdapter()
        recyclerView.adapter = recordAdapter

        recordViewModel.getAllRecordsFromRepo()
            ?.observe(this,
                Observer<List<Record?>?> { t ->
                    if (t != null && t.isNotEmpty()) {
                        recordAdapter.recordsList = recordViewModel.getSortedRecordsPerYearList(t)
                        recordAdapter.notifyDataSetChanged()
                        Log.d(TAG, "recordViewModel.getAllRecordsFromRepo() -> Change in Data - Observer on MainActivity invoked.  : ${t.size}")
                    }
                })
    }
}
