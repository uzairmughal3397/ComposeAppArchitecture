package com.uzair.composeBase.ui.screens.ship_details_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.uzair.composeBase.R
import com.uzair.composeBase.compose.generic_compose_views.CustomToolbar
import com.uzair.composeBase.compose.navigation.NavigateTo
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import com.uzair.composeBase.utils.extensions.getProgressDrawable
import kotlinx.coroutines.Dispatchers

@Composable
fun ShipDetailContainer(
    shipId: String,
    navigateTo: (NavigateTo) -> Unit,
) {
    val viewModel = hiltViewModel<ShipDetailsViewModel>()
    viewModel.queryShipById(shipId)

    val uiState = viewModel.shipDetails.collectAsState().value
    if (uiState!=null) {
        ShipDetailsScreen(
            shipId = shipId,
            shipDetailsViewModel = uiState,
            navigateTo = navigateTo)
    }
}


@Composable
internal fun ShipDetailsScreen(
    shipId: String,
    shipDetailsViewModel: ShipsModel,
    navigateTo: (NavigateTo) -> Unit,
) {
    Scaffold(
        containerColor = Color.DarkGray,
        topBar = {
            CustomToolbar(shipDetailsViewModel.ship_name ?: "")
        },
        content = { paddingValue ->
            ShipDetailsView(shipDetailsViewModel, paddingValue)
        })
}

@Composable
private fun ShipDetailsView(
    shipData: ShipsModel,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(paddingValues)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = ImageRequest.Builder(context = context).apply {
                    data(shipData.image)
                    scale(Scale.FIT)
                    placeholder(getProgressDrawable(context))
                    error(R.drawable.ic_baseline_image_24)
                    fallback(R.drawable.ic_baseline_image_24)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    dispatcher(Dispatchers.Default)
                }.build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 300.dp)
            )
            Text(
                text = shipData.ship_name ?: "",
                style = TextStyle(
                    fontSize = 21.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
            )
            Text(
                text = shipData.ship_type ?: "",
                style = TextStyle(
                    fontSize = 21.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
            )
        }
    }
}