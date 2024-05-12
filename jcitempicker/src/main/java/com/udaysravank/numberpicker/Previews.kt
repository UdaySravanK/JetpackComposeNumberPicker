package com.udaysravank.numberpicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList

@Preview
@Composable
private fun Previews() {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp), verticalArrangement = Arrangement.Center) {
        item {
            Text(text = "Quantity Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                NumberPicker(
                    range = (0..10),
                    value = 3,
                ) {}
            }

            Text(text = "Month Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                ItemPicker(
                    items = listOf(
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    ).toImmutableList(),
                    value = "May",
                    dividerColor = Color.Green
                ) {}
            }

            Text(text = "Year Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                NumberPicker(
                    range = 1900..2100,
                    value = 2023,
                ) {}
            }
        }
    }
}