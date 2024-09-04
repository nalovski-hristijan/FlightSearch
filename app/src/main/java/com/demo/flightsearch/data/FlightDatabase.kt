package com.demo.flightsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.flightsearch.model.Airport
import com.demo.flightsearch.model.Favorite

@Database(entities = [Favorite::class, Airport::class], version = 1, exportSchema = false)
abstract class FlightDatabase : RoomDatabase() {
    abstract fun flightDao() : FlightDao
}