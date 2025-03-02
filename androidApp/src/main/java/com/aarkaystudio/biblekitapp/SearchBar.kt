package com.aarkaystudio.biblekitapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardCapitalization

@Composable
fun SearchBar(
    searchText: String,
    focusManager: FocusManager,
    onSearchTextChanged: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = false,
            ),
        placeholder = { Text("Search...") },
        trailingIcon = {
            IconButton(onClick = {
                onSearchTextChanged("")
                focusManager.clearFocus()
            }) {
                Icon(Icons.Filled.Close, contentDescription = "Clear")
            }
        },
        singleLine = true,
        modifier =
            Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
