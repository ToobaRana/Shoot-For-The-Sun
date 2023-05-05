package com.example.sunandmoon.data.localDatabase.dataEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sunandmoon.data.util.Production

@Entity(tableName = "production")
data class StorableProduction(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "production") val production: Production?
)
