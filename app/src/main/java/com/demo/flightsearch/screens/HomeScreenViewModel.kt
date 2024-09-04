package com.demo.flightsearch.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.flightsearch.model.Airport
import com.demo.flightsearch.model.Favorite
import com.demo.flightsearch.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FlightRepository) :
    ViewModel() {
    private val _airports = MutableStateFlow<List<Airport>>(emptyList())
    val airport = _airports.asStateFlow()

    private val _favorite = MutableStateFlow<List<Favorite>>(emptyList())
    val favorite = _favorite.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    init {
        getAllAirports()
        getAllFavorites()
        getSearchQuery()
    }

    fun getSearchQuery() {
        viewModelScope.launch {
            repository.getSearchQuery().collectLatest { query ->
                _searchQuery.value = query
            }
        }
    }


    fun saveSearchQuery(query: String) {
        viewModelScope.launch {
            repository.saveSearchQuery(query)
            _searchQuery.value = query
        }
    }


    fun getAllAirports() {
        viewModelScope.launch {
            repository.getAllAirports().collect { airportList ->
                _airports.value = airportList
            }
        }
    }

    fun getAllFavorites() {
        viewModelScope.launch {
            repository.getAllFavorites().collect { favoriteList ->
                _favorite.value = favoriteList
            }
        }
    }

    fun searchAirports(query: String) {
        viewModelScope.launch {
            repository.searchAirports(query).collect { searchedAirportsList ->
                _airports.value = searchedAirportsList
            }
        }
    }

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.insertFavorite(favorite)
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }
}