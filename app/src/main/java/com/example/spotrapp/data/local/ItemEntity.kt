package com.example.spotrapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun currentDateFormatted(): String =
    SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val zone: String,
    val imageRes: Int? = null, // need to paltan pag may cam function na
    val size: String,
    val dateAdded: String = currentDateFormatted(),
    val notes: String = ""
)