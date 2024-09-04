package com.demo.flightsearch.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.flightsearch.R
import com.demo.flightsearch.components.FlightTopBar
import com.demo.flightsearch.components.InputField
import com.demo.flightsearch.model.Airport
import com.demo.flightsearch.model.Favorite
import com.demo.flightsearch.utils.VoiceToTextParser

@Composable
fun HomeScreen(voiceToTextParser: VoiceToTextParser) {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val airports by viewModel.airport.collectAsState()
    val favorites by viewModel.favorite.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var isDropDownExpanded by remember { mutableStateOf(false) }
    var canRecord by remember {
        mutableStateOf(false)
    }


    var searchQueryState by remember {
        mutableStateOf(searchQuery)
    }

    val filteredAirports = airports.filter {
        it.name.contains(searchQueryState, ignoreCase = true) || it.iataCode.contains(
            searchQueryState,
            ignoreCase = true
        )
    }

    val recordAudioLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                canRecord = isGranted
            })

    LaunchedEffect(key1 = recordAudioLauncher) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }


    val voiceState by voiceToTextParser.state.collectAsState()

    LaunchedEffect(voiceState.spokenText) {
        if (voiceState.spokenText.isNotEmpty()) {
            searchQueryState = voiceState.spokenText
            viewModel.saveSearchQuery(searchQueryState)
            viewModel.searchAirports(searchQueryState)
        }
    }

    Scaffold(
        topBar = {
            FlightTopBar(title = stringResource(id = R.string.app_name))
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    valueState = searchQueryState,
                    onValueChange = {
                        searchQueryState = it
                        viewModel.saveSearchQuery(searchQueryState)
                        isDropDownExpanded = it.isNotEmpty()
                    },
                    label = "Enter departure point",
                    onSearchClicked = {
                        viewModel.searchAirports(searchQueryState)
                    },
                    onMicClicked = {
                        if (canRecord) {
                            voiceToTextParser.startListening("en-US")
                        }
                    }
                )


                if (searchQueryState.isEmpty()) {
                    FavoritesList(favorites = favorites, viewModel = viewModel)
                } else {
                    AirportsList(airports = airports, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AirportsList(airports: List<Airport>, viewModel: HomeScreenViewModel) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = airports) { airport ->
            AirportRow(airport = airport, viewModel = viewModel)
        }
    }
}


@Composable
fun AirportRow(airport: Airport, viewModel: HomeScreenViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "DEPART")
                Row {
                    Text(
                        modifier = Modifier.padding(end = 10.dp),
                        text = airport.iataCode,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = airport.name, fontWeight = FontWeight.Light)
                }
                Text(text = "ARRIVE")
                Row {
                    Text(
                        modifier = Modifier.padding(end = 10.dp),
                        text = airport.iataCode,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = airport.name, fontWeight = FontWeight.Light)
                }
            }

            Icon(modifier = Modifier.clickable {
                viewModel.insertFavorite(
                    Favorite(
                        id = airport.id,
                        departureCode = airport.iataCode,
                        destinationCode = airport.iataCode
                    )
                )
                Toast.makeText(context, "Airport add to favorites", Toast.LENGTH_LONG).show()
            }, imageVector = Icons.Default.Star, contentDescription = "favorite icon")

        }
    }
}

@Composable
fun FavoritesList(favorites: List<Favorite>, viewModel: HomeScreenViewModel) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = favorites) { favorite ->
            FavoritesRow(favorite = favorite, viewModel = viewModel)
        }
    }
}

@Composable
fun FavoritesRow(favorite: Favorite, viewModel: HomeScreenViewModel) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "DEPART")
                Row {
                    Text(
                        modifier = Modifier.padding(end = 10.dp),
                        text = favorite.departureCode,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(text = "ARRIVE")
                Row {
                    Text(
                        modifier = Modifier.padding(end = 10.dp),
                        text = favorite.destinationCode,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Icon(
                modifier = Modifier.clickable {
                    viewModel.deleteFavorite(favorite)
                    Toast.makeText(context, "Airport removed from favorites", Toast.LENGTH_LONG)
                        .show()
                },
                imageVector = Icons.Default.Star,
                contentDescription = "favorite icon",
                tint = Color.Yellow
            )

        }
    }
}

