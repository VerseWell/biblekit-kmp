package com.aarkaystudio.biblekitapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.aarkaystudio.biblekit.BibleProvider
import com.aarkaystudio.biblekit.model.Verse
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(provider: BibleProvider) {
    val coroutineScope = rememberCoroutineScope()
    var latestJob by remember { mutableStateOf<Job?>(null) }

    var searchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf<Verse>() }

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    var shouldResetList by remember { mutableStateOf(false) }
    val showScrollToTopButton by remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index
            listState.firstVisibleItemIndex > 0 && lastVisibleItemIndex != listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(shouldResetList) {
        if (shouldResetList) {
            listState.scrollToItem(0, 0)
            shouldResetList = false // Reset the trigger
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        SearchBar(
            searchText = searchText,
            focusManager = focusManager,
            onSearchTextChanged = { newText ->
                searchText = newText
                searchActive = false
                shouldResetList = true
                latestJob?.cancel()
                if (newText.isNotEmpty()) {
                    latestJob =
                        coroutineScope.launch {
                            delay(500)
                            val results =
                                provider.search(
                                    query = searchText,
                                    verseIDs = null,
                                )

                            searchResults.clear()
                            searchResults.addAll(results)
                            searchActive = true
                        }
                } else {
                    searchResults.clear()
                }
            },
        )

        if (searchActive) {
            if (searchResults.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No verses found matching your search")
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    SearchResultsList(
                        searchResults = searchResults,
                        listState = listState,
                        scrollToTop = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0, 0)
                            }
                        },
                    )

                    if (showScrollToTopButton) {
                        FloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(0, 0)
                                }
                            },
                            modifier =
                                Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp),
                        ) {
                            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Scroll back to top")
                        }
                    }
                }
            }
        } else if (searchText.isEmpty()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Enter text to search through the Bible")
            }
        } else {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp,
                )
            }
        }
    }
}
