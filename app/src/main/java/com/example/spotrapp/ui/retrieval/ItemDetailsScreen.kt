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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.ui.common.ConfirmationPopup
import com.example.spotrapp.ui.common.ScreenHeader
import com.example.spotrapp.ui.common.StatusPopup
import com.example.spotrapp.ui.theme.SpotrPrimary
import com.example.spotrapp.ui.theme.SpotrSecondary

@Composable
fun ItemDetailsScreen(
    item: ItemEntity,
    onBack: () -> Unit,
    onEditClick: (ItemEntity) -> Unit,
    onRetrieved: () -> Unit,
    onDeleted: () -> Unit
) {
    var showRetrievedPopup by remember { mutableStateOf(false) }
    var showConfirmDeletePopup by remember { mutableStateOf(false) }
    var showDeletedPopup by remember { mutableStateOf(false) }

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

            ScreenHeader(
                title = item.name,
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

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

                Spacer(modifier = Modifier.height(24.dp))

                Column {

                    DetailField(
                        label = "Item Name",
                        value = item.name
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    DetailField(
                        label = "Location",
                        value = item.zone
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    DetailField(
                        label = "Date Added",
                        value = item.dateAdded.ifBlank { "-" }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    DetailField(
                        label = "Notes",
                        value = item.notes.ifBlank { "-" }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // Retrieve button
                    Button(
                        onClick = {
                            showRetrievedPopup = true
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
                            text = "Retrieve",
                            color = Color.White
                        )
                    }

                    // Edit and Delete buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Button(
                            onClick = {
                                onEditClick(item)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SpotrPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null,
                                tint = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Edit",
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = {
                                showConfirmDeletePopup = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB0BEC5)
                            )
                        ) {
                            Text(
                                text = "Delete",
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        if (showRetrievedPopup) {
            StatusPopup(
                message = "Item Retrieved",
                icon = Icons.Filled.Check,
                onDismiss = {
                    showRetrievedPopup = false
                    onRetrieved()
                }
            )
        }

        if (showConfirmDeletePopup) {
            ConfirmationPopup(
                message = "Are you sure you want to delete this item?",
                confirmLabel = "Yes",
                cancelLabel = "Cancel",
                onConfirm = {
                    showConfirmDeletePopup = false
                    showDeletedPopup = true
                },
                onCancel = {
                    showConfirmDeletePopup = false
                }
            )
        }

        if (showDeletedPopup) {
            StatusPopup(
                message = "Item successfully deleted",
                onDismiss = {
                    showDeletedPopup = false
                    onDeleted()
                }
            )
        }
    }
}

@Composable
private fun DetailField(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {

    val sampleItem = ItemEntity(
        id = 1,
        name = "Keys",
        type = "Keys",
        zone = "Zone 1",
        imageRes = com.example.spotrapp.R.drawable.item_1,
        size = "471.23 KB",
        dateAdded = "July 13, 2026"
    )

    ItemDetailsScreen(
        item = sampleItem,
        onBack = {},
        onEditClick = {},
        onRetrieved = {},
        onDeleted = {}
    )
}