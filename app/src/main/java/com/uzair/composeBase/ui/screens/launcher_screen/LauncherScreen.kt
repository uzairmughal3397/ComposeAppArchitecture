package com.uzair.composeBase.ui.screens.launcher_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.uzair.composeBase.R
import com.uzair.composeBase.compose.navigation.NavigateTo
import kotlinx.coroutines.delay

@Composable
fun LauncherScreen(navigateTo: (NavigateTo) -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.vp_logo_splash_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(key1 = true) {
        delay(3000L)
        navigateTo.invoke(NavigateTo.ShipsScreen)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },

            )
    }
}