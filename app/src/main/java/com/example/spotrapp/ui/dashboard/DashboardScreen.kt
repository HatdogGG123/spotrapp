package com.example.spotrapp.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.sampleItems
import com.example.spotrapp.ui.common.TutorialOverlay

@Composable
fun DashboardScreen(
    onItemClick: (ItemEntity) -> Unit,
    showTutorial: Boolean,
    onShowTutorial: () -> Unit,
    onDismissTutorial: () -> Unit,
    onSearchClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onScanClick: () -> Unit
) {
    // all muna value, ala pa logic
    var selectedZone by remember { mutableStateOf("All") }

    // zone dialog states
    var showAddZoneDialog by remember { mutableStateOf(false) }
    var showAddZoneSuccessDialog by remember { mutableStateOf(false) }

    // item details sheet states (three dots)
    var showItemDetailsSheet by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ItemEntity?>(null) }

    // scan button state
    var scanButtonBounds by remember { mutableStateOf<Rect?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                DashboardTopBar(
                    onSearchClick = onSearchClick,
                    onHelpClick = onShowTutorial
                )
            },
            bottomBar = {
                DashboardBottomNav(
                    selectedItem = "home",
                    onHomeClick = {},
                    onScanClick = onScanClick,
                    onHistoryClick = onHistoryClick,
                    onScanButtonPositioned = { bounds ->
                        scanButtonBounds = bounds
                    }
                )
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    ZoneFilterRow(
                        selectedZone = selectedZone,
                        onZoneSelected = { zone ->

                            if (zone == "+") {
                                // open add zone
                                showAddZoneDialog = true
                            } else {
                                selectedZone = zone
                            }
                        }
                    )

                    // filter logic
                    val filteredItems = if (selectedZone == "All") {
                        sampleItems
                    } else {
                        sampleItems.filter { it.zone == selectedZone }
                    }

                    if (filteredItems.isEmpty()) {

                        DashboardEmptyState(
                            modifier = Modifier.weight(1f)
                        )

                    } else {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(filteredItems) { item ->
                                ItemCard(
                                    item = item,
                                    onClick = {
                                        onItemClick(item)
                                    },
                                    onMenuClick = {
                                        // open item details bottom sheet
                                        selectedItem = item
                                        showItemDetailsSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAddZoneDialog) {
            AddZoneDialog(
                onDismiss = {
                    showAddZoneDialog = false
                },
                onConfirm = { _ ->
                    showAddZoneDialog = false
                    showAddZoneSuccessDialog = true
                }
            )
        }

        if (showAddZoneSuccessDialog) {
            AddZoneSuccessDialog(
                onBackHome = {
                    showAddZoneSuccessDialog = false
                }
            )
        }

        if (showItemDetailsSheet && selectedItem != null) {
            ItemDetailsBottomSheet(
                item = selectedItem!!,
                onDismiss = {
                    showItemDetailsSheet = false
                    selectedItem = null
                }
            )
        }

        if (showTutorial && scanButtonBounds != null) {
            TutorialOverlay(
                message = "Tap the \"Scan Icon\" to log a new item",
                onDismiss = onDismissTutorial,
                highlightBounds = scanButtonBounds
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        onItemClick = {},
        showTutorial = false,
        onShowTutorial = {},
        onDismissTutorial = {},
        onSearchClick = {},
        onHistoryClick = {},
        onScanClick = {}
    )
}