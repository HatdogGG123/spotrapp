package com.example.spotrapp.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotrapp.R
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.ui.theme.SpotrSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsBottomSheet(
    item: ItemEntity,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.Transparent,
        dragHandle = null,
        shape = RoundedCornerShape(0.dp),
        tonalElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = 0.4f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp, start = 32.dp, end = 32.dp, bottom = 32.dp)
                ) {
                    DetailSection(label = "Item Name", value = item.name)
                    Spacer(modifier = Modifier.height(24.dp))
                    DetailSection(label = "Size", value = item.size)
                    Spacer(modifier = Modifier.height(24.dp))
                    DetailSection(label = "Date Added", value = item.dateAdded)
                }
            }

            // Close Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .padding(top = 96.dp, start = 16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Floating Image Card
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 0.dp)
                    .size(160.dp),
                shape = RoundedCornerShape(24.dp),
                color = SpotrSecondary,
                shadowElevation = 8.dp
            ) {
                if (item.imageRes != null) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSection(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsBottomSheetPreview() {
    ItemDetailsBottomSheet(
        item = ItemEntity(
            id = 1,
            name = "Basketball",
            type = "Sports",
            zone = "Zone 1",
            imageRes = R.drawable.item_1,
            size = "471.23 KB",
            dateAdded = "July 13, 2026"
        ),
        onDismiss = {}
    )
}
