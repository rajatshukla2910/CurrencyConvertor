package com.example.currencyconversion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.convertor.ui.ConvertorFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = ConvertorFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
        }
    }
}