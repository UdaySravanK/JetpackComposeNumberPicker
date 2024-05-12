package com.udaysravank.jetpackcomposenumberpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udaysravank.numberpicker.ItemData
import com.udaysravank.numberpicker.ItemPicker
import com.udaysravank.numberpicker.NumberPicker
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
): ItemData() {
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
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
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
            .padding(16.dp),
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(text = "Date Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                DatePicker()
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text(text = "Month Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                ItemPicker(
                    items = months,
                    value = months[4],
                    dividerHeight = 2.dp,
                    itemHeight = 100.dp,
                    overlayTopItemModifier = Modifier.background(Brush.verticalGradient(listOf(
                        Color.Green,
                        Color.Transparent,
                    ))),
                    overlayMiddleItemModifier = Modifier.background(Color.Transparent),
                    overlayBottomItemModifier = Modifier.background(Brush.verticalGradient(listOf(
                        Color.Transparent,
                        Color.Yellow,
                    ))),
                ) {}
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text(text = "Year Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                NumberPicker(
                    modifier = Modifier.width(100.dp),
                    range = 1900..2100,
                    value = 2023,
                    dividerColor = Color.Transparent,
                ) {}
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text(text = "Gift Picker")
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                ItemPicker(
                    modifier = Modifier.width(120.dp ),
                    items = gifts.toImmutableList(),
                    value = luckyGift.value,
                    itemContent = { data ->
                        Gem(
                            modifier = Modifier.size(72.dp),
                            text = ""
                        )
                    },
                    itemHeight = 120.dp,
                    dividerHeight = 3.dp,
                    overlayTopItemModifier = Modifier.background(Color.Transparent),
                    overlayBottomItemModifier = Modifier.background(Color.Transparent),
                    overlayMiddleItemModifier = Modifier.background(Color.Transparent),
                ) { selectedGift ->
                    luckyGift.value = gifts.first { selectedGift.uniqueId() == it.uniqueId() }
                }
            }
            Text(text = "Selected gift worth is ${luckyGift.value.giftValueInDollars}")
        }
    }
}

@Composable
fun Gem(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier
            .defaultMinSize(32.dp)
            .background(
                color = Color(0x33440000),
                shape = androidx.compose.foundation.shape.CircleShape,
            )
            .alpha(0.9f),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "🎁", style = TextStyle.Default.copy(fontSize = 32.sp))
    }
}