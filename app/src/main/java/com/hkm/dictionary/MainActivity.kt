package com.hkm.dictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hkm.dictionary.ui.screen.DictionaryScreen
import com.hkm.dictionary.ui.theme.DictionaryTheme
import com.hkm.dictionary.viewmodel.DictionaryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DictionaryTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: DictionaryViewModel = viewModel()
                    DictionaryScreen(viewModel = viewModel)
                }
            }
        }
    }
}
