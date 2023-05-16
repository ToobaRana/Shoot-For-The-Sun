package com.example.sunandmoon.data.localDatabase.dataEntities

import android.location.Location
import androidx.room.*
import com.example.sunandmoon.data.util.Shoot
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "shoot")
@TypeConverters(LocalDateTimeConverter::class)
data class StorableShoot(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "parent_production_id") val parentProductionId: Int?,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location_name") val locationName: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "date_time") val date: LocalDateTime,
    @ColumnInfo(name = "time_zone_offset") val timeZoneOffset: Double
)

// this class lets us store LocalDateTime objects in the room database
class LocalDateTimeConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}
