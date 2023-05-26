package com.example.sunandmoon.data.localDatabase.dataEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

//used to fetch and store productions in local database
@Entity(tableName = "production")
@TypeConverters(LocalDateTimeConverter::class)
data class StorableProduction(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "start_date_time") val startDateTime: LocalDateTime?,
    @ColumnInfo(name = "end_date_time") val endDateTime: LocalDateTime?,
)
