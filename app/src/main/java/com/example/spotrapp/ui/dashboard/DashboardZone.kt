package com.example.spotrapp.ui.dashboard

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotrapp.data.local.ZoneEntity
import com.example.spotrapp.ui.theme.SpotrPrimary
import com.example.spotrapp.ui.theme.SpotrSecondary
import com.example.spotrapp.ui.theme.SpotrWhite

@Composable
fun ZoneFilterRow(
    zones: List<ZoneEntity>,
    selectedZone: String,
    onZoneSelected: (String) -> Unit
) {
    // "All" muna sa una, tapos yung zones sa database, tapos "+" sa dulo
    val zoneOptions = mutableListOf("All")
    zones.forEach { zone ->
        zoneOptions.add(zone.name)
    }
    zoneOptions.add("+")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        zoneOptions.forEach { zone ->
            val isSelected = zone == selectedZone

            Surface(
                shape = RoundedCornerShape(75),
                color = if (isSelected) SpotrPrimary else SpotrSecondary,
                onClick = { onZoneSelected(zone) }
            ) {
                Text(
                    text = zone,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSelected) SpotrWhite else SpotrPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}