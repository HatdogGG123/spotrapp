package com.example.spotrapp.ui.retrieval

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.otherZones
import com.example.spotrapp.suggestedZones
import com.example.spotrapp.ui.common.ConfirmationPopup
import com.example.spotrapp.ui.common.OtherZoneButton
import com.example.spotrapp.ui.common.ScreenHeader
import com.example.spotrapp.ui.common.StatusPopup
import com.example.spotrapp.ui.common.ZoneButton
import com.example.spotrapp.ui.theme.SpotrPrimary
import com.example.spotrapp.ui.theme.SpotrSecondary
import com.example.spotrapp.ui.theme.SpotrWhite

@Composable
fun EditItemScreen(
    item: ItemEntity,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    // dialog states
    var showConfirmEditPopup by remember { mutableStateOf(false) }
    var showSavedPopup by remember { mutableStateOf(false) }
    var showConfirmCancelPopup by remember { mutableStateOf(false) }

    // Static text field state
    var itemName by remember { mutableStateOf(item.name) }

    var selectedZone by remember { mutableStateOf(item.zone) }

    // error state
    var nameError by remember { mutableStateOf<String?>(null) }
    var zoneError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
        ) {

            // Screen header
            ScreenHeader(
                title = "Edit Item",
                onBack = {
                    // same logic just like in add item screen
                    showConfirmCancelPopup = true
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // Item image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(
                            color = SpotrSecondary,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.imageRes != null) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Item name
                Column {
                    Text(
                        text = "Item Name",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    TextField(
                        value = itemName,
                        onValueChange = {
                            itemName = it
                            if (nameError != null) nameError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = nameError != null,
                        supportingText = {
                            if (nameError != null) {
                                Text(
                                    text = nameError!!,
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    // No functionality yet
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Mic,
                                    contentDescription = "Voice input",
                                    tint = SpotrPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = SpotrPrimary,
                            unfocusedIndicatorColor = Color(0xFFCCCCCC),
                            cursorColor = SpotrPrimary,
                            errorIndicatorColor = Color.Red,
                            errorContainerColor = Color.White
                        )
                    )
                }

                // Suggested zones
                Column {
                    Text(
                        text = "Suggested Zones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    suggestedZones.forEach { zone ->
                        ZoneButton(
                            zoneName = zone,
                            isSelected = selectedZone == zone,
                            onClick = {
                                selectedZone = zone
                                if (zoneError != null) zoneError = null
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Other zones
                Column {
                    Text(
                        text = "Other Zones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        otherZones.forEach { zone ->
                            OtherZoneButton(
                                zoneName = zone,
                                isSelected = selectedZone == zone,
                                onClick = {
                                    selectedZone = zone
                                    if (zoneError != null) zoneError = null
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (zoneError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = zoneError!!,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }

                // Save button
                Button(
                    onClick = {
                        val trimmedName = itemName.trim()
                        nameError = if (trimmedName.isBlank()) "Please enter an item name." else null
                        zoneError = if (selectedZone.isBlank()) "Please select a zone." else null

                        if (nameError == null && zoneError == null) {
                            showConfirmEditPopup = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SpotrPrimary
                    )
                ) {
                    Text(
                        text = "Save",
                        color = SpotrWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // confirm dialog
        if (showConfirmEditPopup) {
            ConfirmationPopup(
                message = "Are you sure you want to edit?",
                confirmLabel = "Yes",
                cancelLabel = "Cancel",
                onConfirm = {
                    showConfirmEditPopup = false
                    showSavedPopup = true
                },
                onCancel = {
                    showConfirmEditPopup = false
                }
            )
        }

        // saved dialog
        if (showSavedPopup) {
            StatusPopup(
                message = "Item Edited Successfully!",
                onDismiss = {
                    showSavedPopup = false
                    onSaved()
                }
            )
        }

        if (showConfirmCancelPopup) {
            ConfirmationPopup(
                message = "Are you sure you want to cancel?",
                confirmLabel = "Yes",
                cancelLabel = "No",
                onConfirm = {
                    showConfirmCancelPopup = false
                    onBack()
                },
                onCancel = {
                    showConfirmCancelPopup = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditItemScreenPreview() {
    val sampleItem = ItemEntity(
        id = 1,
        name = "Keys",
        type = "Keys",
        zone = "Zone 1",
        imageRes = com.example.spotrapp.R.drawable.item_1,
        size = "471.23 KB",
        dateAdded = "July 13, 2026"
    )

    EditItemScreen(
        item = sampleItem,
        onBack = {},
        onSaved = {}
    )
}