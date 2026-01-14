package com.owen.superalliance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.owen.superalliance.ui.theme.SuperallianceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Avoid relying on lifecycle-viewmodel-compose; create VM the old-school way.
        val scoutingViewModel = ViewModelProvider(this)[ScoutingViewModel::class.java]

        setContent {
            SuperallianceTheme {
                ScoutingApp(viewModel = scoutingViewModel)
            }
        }
    }
}
