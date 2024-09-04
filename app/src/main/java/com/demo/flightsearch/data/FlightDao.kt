package com.demo.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.flightsearch.model.Airport
import com.demo.flightsearch.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query(
        """
        SELECT * FROM airport 
        WHERE name LIKE '%' || :query || '%' 
        OR iata_code LIKE '%' || :query || '%' 
        ORDER BY passengers DESC
    """
    )
    fun searchAirports(query: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<Airport>>
}