package com.midina.registration_data.database

import androidx.room.TypeConverter
import java.sql.Date

object DateConverter {
    @TypeConverter
    fun toDate(dateStr: String): Date? {
        return Date.valueOf(dateStr)
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.time?.toString()
    }
}