package com.ankitgh.mobiledatatrend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import timber.log.Timber.DebugTree


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    abstract fun getLayoutId(): Int
}