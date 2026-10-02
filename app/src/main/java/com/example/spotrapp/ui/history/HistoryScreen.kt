package com.example.spotrapp.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.sampleItems
import com.example.spotrapp.ui.dashboard.DashboardBottomNav
import com.example.spotrapp.ui.theme.SpotrGray
import com.example.spotrapp.ui.theme.SpotrSecondary
import com.example.spotrapp.ui.theme.SpotrTextBlack
import com.example.spotrapp.ui.theme.SpotrWhite

@Composable
fun HistoryScreen(
    onHomeClick: () -> Unit,
    onScanClick: () -> Unit,
    onItemClick: (ItemEntity) -> Unit
) {
    Scaffold(
        bottomBar = {
            DashboardBottomNav(
                selectedItem = "history",
                onHomeClick = onHomeClick,
                onScanClick = onScanClick,
                onHistoryClick = {}
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
        ) {

            Text(
                text = "History",
                color = SpotrTextBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Recent Activity",
                color = SpotrTextBlack,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {

                items(sampleItems) { item ->

                    HistoryItemRow(
                        item = item,
                        onClick = {
                            onItemClick(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItemRow(
    item: ItemEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SpotrWhite,
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 14.dp,
                            bottomStart = 14.dp
                        )
                    )
                    .background(SpotrSecondary),
                contentAlignment = Alignment.Center
            ) {

                if (item.imageRes != null) {

                    Image(
                        painter = painterResource(
                            id = item.imageRes
                        ),
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    // nardcoded event
                    // no retrieve, delete, update etc. yet
                    text = "Added ${item.name}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = SpotrTextBlack
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = item.dateAdded,
                    fontSize = 12.sp,
                    color = SpotrGray
                )
            }

            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go to Item",
                tint = SpotrGray,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {

    HistoryScreen(
        onHomeClick = {},
        onScanClick = {},
        onItemClick = {}
    )
}