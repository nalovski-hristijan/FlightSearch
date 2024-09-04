package com.demo.flightsearch.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.demo.flightsearch.data.FlightDao
import com.demo.flightsearch.model.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlightRepository @Inject constructor(
    private val flightDao: FlightDao,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val SEARCH_QUERY_STATE = stringPreferencesKey("search_query")
    }

    suspend fun saveSearchQuery(query: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_QUERY_STATE] = query
        }
    }

    fun getSearchQuery(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[SEARCH_QUERY_STATE] ?: ""

        }
    }

    suspend fun insertFavorite(favorite: Favorite) = flightDao.insertFavorite(favorite)
    suspend fun deleteFavorite(favorite: Favorite) = flightDao.deleteFavorite(favorite)
    fun getAllFavorites() = flightDao.getAllFavorites().flowOn(Dispatchers.IO).conflate()
    fun searchAirports(query: String) = flightDao.searchAirports(query)
    fun getAllAirports() = flightDao.getAllAirports().flowOn(Dispatchers.IO).conflate()


}

