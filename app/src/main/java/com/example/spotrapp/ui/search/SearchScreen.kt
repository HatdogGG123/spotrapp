package com.example.spotrapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.sampleItems
import com.example.spotrapp.ui.dashboard.DashboardBottomNav
import com.example.spotrapp.ui.theme.SpotrGray
import com.example.spotrapp.ui.theme.SpotrPrimary
import com.example.spotrapp.ui.theme.SpotrSecondary
import com.example.spotrapp.ui.theme.SpotrTextBlack
import com.example.spotrapp.ui.theme.SpotrWhite

@Composable
fun SearchScreen(
    onHomeClick: () -> Unit,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onVoiceSearchClick: () -> Unit,
    onItemClick: (ItemEntity) -> Unit
) {

    // search bar state
    var searchText by remember {
        mutableStateOf("")
    }
    var hasSearched by remember {
        mutableStateOf(false)
    }

    // recent searches states
    var selectedSearch by remember {
        mutableStateOf<ItemEntity?>(null)
    }

    Scaffold(
        bottomBar = {
            DashboardBottomNav(
                // no highlighted icon in bottom nav
                selectedItem = null,
                onHomeClick = onHomeClick,
                onScanClick = onScanClick,
                onHistoryClick = onHistoryClick
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

            // search bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(SpotrWhite)
                        .border(
                            width = 1.5.dp,
                            color = SpotrSecondary,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Search icon
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = SpotrGray
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    // text field for the search bar
                    BasicTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it

                            // If the user starts typing again,
                            // remove the previous "No results found" state.
                            if (it.isNotEmpty()) {
                                hasSearched = false
                                selectedSearch = null
                            }
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = SpotrTextBlack,
                            fontSize = 14.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {

                                // No search logic yet
                                if (searchText.isNotBlank()) {
                                    selectedSearch = null
                                    hasSearched = true
                                }
                            },
                        ),
                        decorationBox = { innerTextField ->

                            if (searchText.isEmpty()) {
                                Text(
                                    text = "Search an item...",
                                    color = SpotrGray,
                                    fontSize = 14.sp
                                )
                            }

                            innerTextField()
                        }
                    )

                    // Shows the microphone when the search field is empty.
                    // Changes to an X when the user types something.
                    IconButton(
                        onClick = {

                            if (searchText.isEmpty()) {

                                // Open voice search
                                onVoiceSearchClick()

                            } else {

                                // Clear the search
                                searchText = ""
                                selectedSearch = null
                                hasSearched = false
                            }
                        }
                    ) {
                        // icon changes based on whether the user is typing
                        Icon(
                            imageVector = if (searchText.isEmpty()) {
                                Icons.Filled.Mic
                            } else {
                                Icons.Filled.Close
                            },
                            contentDescription = if (searchText.isEmpty()) {
                                "Voice search"
                            } else {
                                "Clear search"
                            },
                            tint = if (searchText.isEmpty()) {
                                SpotrPrimary
                            } else {
                                SpotrGray
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // displays this when typing an item (empty state for now)
            if (hasSearched) {

                // No search logic yet
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Filled.SearchOff,
                        contentDescription = "No results",
                        tint = SpotrGray,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "No results found!",
                        color = SpotrTextBlack,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "We couldn't find any matching results. " +
                                "Try searching again or change your filters.",
                        color = SpotrGray,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }

                // displays the result when clicking any recently searched item
            } else if (selectedSearch != null) {

                //hardcoded for now
                Text(
                    text = "1 result found",
                    color = SpotrTextBlack,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {

                    item {

                        SearchResultRow(
                            item = selectedSearch!!,
                            onClick = {
                                onItemClick(
                                    selectedSearch!!
                                )
                            }
                        )
                    }
                }

            // displays recently searched items' names
            } else {

                Text(
                    text = "Recent Searches",
                    color = SpotrTextBlack,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {

                    items(sampleItems) { item ->

                        RecentSearchesRow(
                            item = item,
                            onClick = {

                                // Put the selected item's name
                                // into the search field.
                                searchText = item.name

                                // Show the hardcoded result.
                                selectedSearch = item

                                hasSearched = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

    SearchScreen(
        onHomeClick = {},
        onScanClick = {},
        onHistoryClick = {},
        onVoiceSearchClick = {},
        onItemClick = {}
    )
}