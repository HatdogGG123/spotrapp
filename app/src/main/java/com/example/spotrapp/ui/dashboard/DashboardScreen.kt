package com.example.spotrapp.ui.dashboard

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.data.local.ZoneEntity
import com.example.spotrapp.data.repository.Resource
import com.example.spotrapp.ui.common.TutorialOverlay
import com.example.spotrapp.viewmodel.ItemViewModel
import com.example.spotrapp.viewmodel.ZoneViewModel

@Composable
fun DashboardScreen(
    onItemClick: (ItemEntity) -> Unit,
    showTutorial: Boolean,
    onShowTutorial: () -> Unit,
    onDismissTutorial: () -> Unit,
    onSearchClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onScanClick: () -> Unit,
    itemViewModel: ItemViewModel = hiltViewModel(),
    zoneViewModel: ZoneViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // all muna ang default na selected zone
    var selectedZone by remember { mutableStateOf("All") }

    // zone dialog states
    var showAddZoneDialog by remember { mutableStateOf(false) }
    var showAddZoneSuccessDialog by remember { mutableStateOf(false) }

    // item details sheet states (three dots)
    var showItemDetailsSheet by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ItemEntity?>(null) }

    // scan button state
    var scanButtonBounds by remember { mutableStateOf<Rect?>(null) }

    // data galing sa viewmodels
    val itemState by itemViewModel.itemState.collectAsState()
    val zoneState by zoneViewModel.zoneState.collectAsState()
    val zoneActionState by zoneViewModel.actionState.collectAsState()

    // list ng items, empty muna habang naglo-load
    var items = emptyList<ItemEntity>()
    if (itemState is Resource.Success) {
        items = (itemState as Resource.Success<List<ItemEntity>>).data
    }

    // list ng zones, empty muna habang naglo-load
    var zones = emptyList<ZoneEntity>()
    if (zoneState is Resource.Success) {
        zones = (zoneState as Resource.Success<List<ZoneEntity>>).data
    }

    // pag natapos na ang add zone: success dialog pag ok, toast pag may error (ex. duplicate)
    LaunchedEffect(zoneActionState) {
        if (zoneActionState is Resource.Success) {
            showAddZoneSuccessDialog = true
            zoneViewModel.clearActionState()
        }
        if (zoneActionState is Resource.Error) {
            val message = (zoneActionState as Resource.Error<Unit>).message
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            zoneViewModel.clearActionState()
        }
    }

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
                        zones = zones,
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
                        items
                    } else {
                        items.filter { it.zone == selectedZone }
                    }

                    if (itemState is Resource.Loading) {

                        // wait muna, para hindi mag-flash yung empty state
                        Box(modifier = Modifier.weight(1f)) {}

                    } else if (filteredItems.isEmpty()) {

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
                onConfirm = { zoneName ->
                    // viewmodel na bahala mag-save, success dialog lalabas after
                    zoneViewModel.addZone(zoneName.trim())
                    showAddZoneDialog = false
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
