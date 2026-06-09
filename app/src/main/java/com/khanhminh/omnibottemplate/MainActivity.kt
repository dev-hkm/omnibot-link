package com.khanhminh.omnibottemplate

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)
            setBackgroundColor(Color.rgb(12, 14, 18))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val title = TextView(this).apply {
            text = getString(R.string.home_title)
            textSize = 28f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = getString(R.string.home_subtitle)
            textSize = 16f
            setTextColor(Color.rgb(190, 198, 210))
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 32)
        }

        val counter = TextView(this).apply {
            text = getString(R.string.counter_text, count)
            textSize = 18f
            setTextColor(Color.rgb(138, 180, 248))
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }

        val button = Button(this).apply {
            text = getString(R.string.counter_button)
            textSize = 16f
            setOnClickListener {
                count += 1
                counter.text = getString(R.string.counter_text, count)
            }
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(counter)
        root.addView(button)
        setContentView(root)
    }
}
