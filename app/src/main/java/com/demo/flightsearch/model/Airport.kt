package com.demo.flightsearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "iata_code")
    val iataCode: String,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val passengers: Int
)
