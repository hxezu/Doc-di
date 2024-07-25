package com.example.doc_di

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.etc.NaviGraph
import com.example.doc_di.ui.theme.Doc_diTheme
import com.example.doc_di.util.SettingsPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        // Check if the user is available and set user ID if not
        if (!SettingsPreferences.getInstance(this).isUserAvailable()) {
            val randomSixDigitNumber = Random.nextInt(100000, 1000000)
            SettingsPreferences.getInstance(this).setUserId(randomSixDigitNumber)
        }

        setContent {
            Doc_diTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NaviGraph(navController)
                }
            }
        }
    }
}