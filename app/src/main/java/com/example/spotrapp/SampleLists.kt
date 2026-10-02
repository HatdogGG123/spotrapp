package com.example.spotrapp

import com.example.spotrapp.data.local.ItemEntity

// temporary lang na hardcoded list ng items since wala pang camera function
val suggestedZones = listOf(
    "Zone 1",
    "Zone 2",
    "Zone 3"
)

val otherZones = listOf(
    "Zone 4",
    "Zone 5"
)
val sampleItems = listOf(
    ItemEntity(id = 1, name = "Keys", type = "Keys", zone = "Zone 1", imageRes = R.drawable.item_1, size = "471.23 KB", dateAdded = "July 13, 2026"),
    ItemEntity(id = 2, name = "Remote", type = "Remote", zone = "Zone 2", imageRes = R.drawable.item_2, size = "67.41 KB", dateAdded = "August 2, 2026")
)