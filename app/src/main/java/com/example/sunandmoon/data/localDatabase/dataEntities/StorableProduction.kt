package com.example.sunandmoon.data.localDatabase.dataEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.sunandmoon.data.util.Production

@Entity(tableName = "production")
@TypeConverters(LocalDateTimeConverter::class)
data class StorableProduction(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String,
)
