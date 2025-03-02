package com.aarkaystudio.biblekitapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aarkaystudio.biblekit.model.Verse

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultsList(
    searchResults: List<Verse>,
    listState: LazyListState,
    scrollToTop: () -> Unit,
) {
    LazyColumn(state = listState) {
        stickyHeader {
            Text(
                text = "Total verses: ${searchResults.size}",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                textAlign = TextAlign.Center,
                style =
                    TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
            )
        }

        itemsIndexed(searchResults) { _, result ->
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp,
                    ),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                ) {
                    Text(
                        text = result.id.bookChapterVerse(),
                        modifier =
                            Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                    )

                    Text(
                        text = result.text,
                        modifier =
                            Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
            }
        }

        item {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Button(onClick = scrollToTop, modifier = Modifier.fillMaxWidth()) {
                    Text("Scroll to Top")

                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Scroll to Top",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                }
            }
        }
    }
}
