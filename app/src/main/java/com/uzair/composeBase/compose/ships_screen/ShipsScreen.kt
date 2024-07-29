package com.uzair.composeBase.compose.ships_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.uzair.composeBase.compose.generic_compose_views.ShowDialog
import com.uzair.composeBase.compose.generic_compose_views.StartDefaultLoader
import com.uzair.composeBase.compose.navigation.NavigateTo
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import com.uzair.composeBase.utils.Resource
import com.uzair.composeBase.utils.extensions.getProgressDrawable
import kotlinx.coroutines.Dispatchers

@Composable
fun ShipsScreen(navigateTo: (NavigateTo, String) -> Unit) {
    val shipsViewModel: ShipsViewModel = hiltViewModel()
    val shipUi=shipsViewModel.shipsModelStateFlow.collectAsState().value
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            CustomToolbar(stringResource(R.string.list_of_ships))
        },
        content = { paddingValue ->
            ListOfShips(navigateTo, paddingValues = paddingValue,shipUi)
        })
}

@Composable
private fun ListOfShips(
    navigateTo: (NavigateTo, String) -> Unit,
    paddingValues: PaddingValues,
    shipUi: Resource<MutableList<ShipsModel>>,
) {
    val context = LocalContext.current

    when (shipUi) {
        is Resource.Loading -> {
            StartDefaultLoader()
        }

        is Resource.Error -> {
            ShowDialog(
                title = stringResource(id = R.string.error),
                message = shipUi.message
            )
        }

        is Resource.Success -> {
            ListComposable(paddingValues, shipUi.data, context, navigateTo)
        }
    }


}

@Composable
private fun ListComposable(
    paddingValues: PaddingValues,
    shipUi: MutableList<ShipsModel>,
    context: Context,
    navigateTo: (NavigateTo, String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(paddingValues), content = {
        items(items = shipUi) {
            ShipItemView(shipModel = it) { selectedShipDataValue ->
                Toast.makeText(
                    context,
                    selectedShipDataValue.ship_name.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                navigateTo.invoke(NavigateTo.DetailScreen, selectedShipDataValue.ship_id)
            }

        }
    })
}

@Composable
private fun ShipItemView(
    shipModel: ShipsModel,
    listener: (ShipsModel) -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .height(125.dp)
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                listener(shipModel)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(9.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = context).apply {
                    data(shipModel.image)
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
                    .fillMaxHeight()
                    .width(width = 150.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .padding(start = 15.dp),
            ) {
                Column {
                    Text(
                        shipModel.ship_name.toString(),
                        style = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center),
                        color = Color.White,
                    )
                    Text(
                        shipModel.ship_type.toString(),
                        style = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center),
                        color = Color.White,
                    )
                }
            }
        }
    }
}