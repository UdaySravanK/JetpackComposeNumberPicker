package com.udaysravank.jetpackcomposenumberpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.udaysravank.jcitempicker.ItemData
import com.udaysravank.jcitempicker.ItemPicker
import com.udaysravank.jcitempicker.NumberPicker
import com.udaysravank.jetpackcomposenumberpicker.ui.theme.JetpackComposeNumberPickerTheme
import kotlinx.collections.immutable.toImmutableList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeNumberPickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ItemPickerDemoComponent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class GiftPickerItem(
    val giftValueInDollars: Int,
    val giftName: String,
    val publicVisibleName: String,
): ItemData {
    override fun itemText(): String {
        return publicVisibleName
    }

    override fun uniqueId(): String {
        return publicVisibleName
    }
}

private val months = listOf(
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
).toImmutableList()

@Composable
fun DatePicker(){
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NumberPicker(
            modifier = Modifier.weight(1f),
            range = (0..31),
            value = 23,
        ) {}
        ItemPicker(
            modifier = Modifier.weight(1f),
            items = months,
            value = months[5],
        ) {}
        NumberPicker(
            modifier = Modifier.weight(1f),
            range = (1900..2024),
            value = 1990,
        ) {}
    }
}

@Composable
fun ItemPickerDemoComponent(modifier: Modifier = Modifier) {
    val gifts = listOf(
        GiftPickerItem(10, "A", "Willamette Valley"),
        GiftPickerItem(20, "B", "Machu Picchu"),
        GiftPickerItem(30, "C", "Piccadilly Circus"),
        GiftPickerItem(40, "D", "Ciel and the Sky"),
        GiftPickerItem(50, "E", "Zinnia"),
        GiftPickerItem(60, "F", "Delhi"),
        GiftPickerItem(70, "G", "Minne haha"),
        GiftPickerItem(80, "H", "Columbus"),
        GiftPickerItem(90, "I", "Windmills"),
    )
    val luckyGift = remember { mutableStateOf(gifts[3]) }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        state = rememberLazyListState(),
    ) {
        item {
            Text(text = "Date Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                DatePicker()
            }

            Text(text = "Month Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                ItemPicker(
                    items = months,
                    value = months[4],
                    dividerColor = Color.Green,
                    listItemHeight = 100.dp,
                ) {}
            }

            Text(text = "Year Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                NumberPicker(
                    range = 1900..2100,
                    value = 2023,
                ) {}
            }

            Text(text = "Gift Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                ItemPicker(
                    items = gifts.toImmutableList(),
                    value = gifts[3],
                ) { selectedGift ->
                    luckyGift.value = gifts.first { selectedGift.uniqueId() == it.uniqueId() }
                }
            }
            Text(text = "Selected gift worth is ${luckyGift.value.giftValueInDollars}")
        }
    }
}