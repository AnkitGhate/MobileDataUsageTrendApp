package com.ankitgh.mobiledatatrend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ankitgh.mobiledatatrend.databinding.ActivityMainBinding
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Base activity for all activities. Common elements needs
 * to be initialized from here
 */
abstract class BaseActivity : AppCompatActivity() {

    lateinit var mainLayoutViewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayoutViewBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, getLayoutId())
        Timber.plant(DebugTree())
    }

    abstract fun getLayoutId(): Int

}