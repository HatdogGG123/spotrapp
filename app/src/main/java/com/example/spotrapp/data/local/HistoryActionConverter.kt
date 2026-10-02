package com.example.spotrapp.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromAction(action: HistoryAction): String = action.name

    @TypeConverter
    fun toAction(value: String): HistoryAction = HistoryAction.valueOf(value)
}
