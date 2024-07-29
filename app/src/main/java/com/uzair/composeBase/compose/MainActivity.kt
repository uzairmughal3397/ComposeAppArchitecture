package com.uzair.composeBase.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uzair.composeBase.compose.navigation.Navigation
import com.uzair.composeBase.ui.theme.SampleComposeWithHiltAndRoomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleComposeWithHiltAndRoomTheme {
                Navigation()
            }
        }
    }
}