package com.example.spotrapp.ui.putaway

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
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotrapp.otherZones
import com.example.spotrapp.suggestedZones
import com.example.spotrapp.ui.common.ConfirmationPopup
import com.example.spotrapp.ui.common.OtherZoneButton
import com.example.spotrapp.ui.common.ScreenHeader
import com.example.spotrapp.ui.common.StatusPopup
import com.example.spotrapp.ui.common.TutorialOverlay
import com.example.spotrapp.ui.common.ZoneButton
import com.example.spotrapp.ui.theme.SpotrPrimary
import com.example.spotrapp.ui.theme.SpotrSecondary
import com.example.spotrapp.ui.theme.SpotrWhite

@Composable
fun AddItemScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    showTutorial: Boolean = false,
    onDismissTutorial: () -> Unit = {}
) {
    // popup states
    var showConfirmAddPopup by remember { mutableStateOf(false) }
    var showSavedPopup by remember { mutableStateOf(false) }
    var showConfirmCancelPopup by remember { mutableStateOf(false) }

    // Static text field state
    var itemName by remember { mutableStateOf("") }

    // zone select state
    var selectedZone by remember { mutableStateOf("") }

    // error handling states
    var nameError by remember { mutableStateOf<String?>(null) }
    var zoneError by remember { mutableStateOf<String?>(null) }

    // states for tutorial overlay
    var localTutorialStep by remember { mutableIntStateOf(0) }
    var nameFieldBounds by remember { mutableStateOf<Rect?>(null) }
    var zoneSectionBounds by remember { mutableStateOf<Rect?>(null) }
    var saveButtonBounds by remember { mutableStateOf<Rect?>(null) }

    val tutorialSteps = listOf(
        "Type or say your item's name here" to nameFieldBounds,
        "Pick which zone you're storing it in" to zoneSectionBounds,
        "Tap Save when you're ready" to saveButtonBounds
    )

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
                title = "Item Details",
                // shows cancel popup when clicking the back arrow
                onBack = {
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
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Item Image",
                        tint = SpotrPrimary,
                        modifier = Modifier.size(180.dp),
                    )
                }

                // Item name
                Column(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        nameFieldBounds = coordinates.boundsInRoot()
                    }
                ) {
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
                            // error will disappear if user types something
                            if (nameError != null) nameError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = nameError != null,
                        placeholder = {
                            Text(
                                text = "Enter item name"
                            )
                        },
                        // error text
                        supportingText = {
                            if (nameError != null) {
                                Text(
                                    text = nameError!!,
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        // voice function for item name
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    // no function yet
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

                // Zones
                Column(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        zoneSectionBounds = coordinates.boundsInRoot()
                    }
                ) {
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
                                    // same as name error condition
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
                    }

                    // error text
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
                        // prevents whitespaces
                        val trimmedName = itemName.trim()
                        nameError = if (trimmedName.isBlank()) "Please enter an item name." else null
                        zoneError = if (selectedZone.isBlank()) "Please select a zone." else null

                        if (nameError == null && zoneError == null) {
                            showConfirmAddPopup = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .onGloballyPositioned { coordinates ->
                            saveButtonBounds = coordinates.boundsInRoot()
                        },
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
        if (showConfirmAddPopup) {
            ConfirmationPopup(
                message = "Are you sure you want to add this item?",
                confirmLabel = "Yes",
                cancelLabel = "Cancel",
                onConfirm = {
                    showConfirmAddPopup = false
                    showSavedPopup = true
                },
                onCancel = {
                    showConfirmAddPopup = false
                }
            )
        }

        // saved dialog
        if (showSavedPopup) {
            StatusPopup(
                message = "Item Added Successfully!",
                onDismiss = {
                    showSavedPopup = false
                    // goes to dashboard
                    onSaved()
                }
            )
        }

        // cancel adding item dialog
        if (showConfirmCancelPopup) {
            ConfirmationPopup(
                message = "Are you sure you want to cancel?",
                confirmLabel = "Yes",
                cancelLabel = "No",
                onConfirm = {
                    showConfirmCancelPopup = false
                    // only pops back stack to previous screen if user tapped yes
                    onBack()
                },
                onCancel = {
                    showConfirmCancelPopup = false
                }
            )
        }

        // steps for the tutorial
        // used local tutorial step since there are multiple tutorials in this single screen
        if (showTutorial && localTutorialStep < tutorialSteps.size) {
            val (stepMessage, stepBounds) = tutorialSteps[localTutorialStep]

            if (stepBounds != null) {
                TutorialOverlay(
                    message = stepMessage,
                    highlightBounds = stepBounds,
                    onDismiss = {
                        if (localTutorialStep < tutorialSteps.lastIndex) {
                            localTutorialStep++
                        } else {
                            // last local step done
                            onDismissTutorial()
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    AddItemScreen(
        onBack = {},
        onSaved = {}
    )
}