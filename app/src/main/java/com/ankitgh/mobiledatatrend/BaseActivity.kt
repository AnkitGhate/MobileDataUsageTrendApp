package com.ankitgh.mobiledatatrend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Base activity for all activities. Common elements needs
 * to be initialized from here
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        Timber.plant(DebugTree())
    }
    abstract fun getLayoutId(): Int
}