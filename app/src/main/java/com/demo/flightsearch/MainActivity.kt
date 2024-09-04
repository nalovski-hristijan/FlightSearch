package com.demo.flightsearch

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.demo.flightsearch.screens.HomeScreen
import com.demo.flightsearch.ui.theme.FlightSearchTheme
import com.demo.flightsearch.utils.VoiceToTextParser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlightSearchTheme {
                val context = applicationContext as Application
                val voiceToTextParser = remember {
                    VoiceToTextParser(context)
                }
                HomeScreen(voiceToTextParser = voiceToTextParser)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlightSearchTheme {

    }
}