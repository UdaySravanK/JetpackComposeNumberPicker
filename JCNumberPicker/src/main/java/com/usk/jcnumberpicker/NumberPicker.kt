package com.usk.jcnumberpicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList

/**
 * The compose version of Android [NumberPicker](https://developer.android.com/reference/android/widget/NumberPicker)
 *
 * ItemPicker will show 3 items and treats the middle one as the selected item.
 *
 * @param range Range of integer values to be displayed in the picker
 * @param value Current selected value i.e the middle value in the picker
 * @param dividerHeight Height of separator lines above and below the middle item to indicate it as the selected item
 * @param dividerColor Color of the middle item separators
 * @param itemHeight Height of one single item in the picker. The full height of the ItemPicker will be calculated based on this parameter.
 * @param itemContent A callback to receive the composable content of each item
 * @param selectedItemContentDescription Content description for the middle selected item on focus announcement
 * @param scrollContainerContentDescription Content description for the scroll container on focus announcement
 * @param overlayTopItemModifier Modifier to be applied to the top overlay item in the ItemPicker
 * @param overlayMiddleItemModifier Modifier to be applied to the top overlay item in the ItemPicker
 * @param overlayBottomItemModifier Modifier to be applied to the top overlay item in the ItemPicker
 * @param onValueChange A callback to receive the current selected value
 * Note: This component will be replaced with the official Compose provided component when available in the future.
 */
@Composable
fun NumberPicker(
  range: Iterable<Int>,
  modifier: Modifier = Modifier,
  value: Int = range.first(),
  dividerHeight: Dp = DEFAULT_SEPARATOR_HEIGHT.dp,
  dividerColor: Color = Color.Red,
  selectedItemContentDescription: String = "",
  scrollContainerContentDescription: String = "",
  itemHeight: Dp = DEFAULT_ITEM_HEIGHT.dp,
  itemContent: (@Composable (itemData: ItemData) -> Unit)? = null,
  overlayTopItemModifier: Modifier? = null,
  overlayMiddleItemModifier: Modifier? = null,
  overlayBottomItemModifier: Modifier? = null,
  onValueChange: (Int) -> Unit = {},
) {
  val items = remember(range) {
    range.map { it.toString() }.toImmutableList()
  }
  ItemPicker(
    items = items,
    value = value.toString(),
    modifier = modifier,
    dividerHeight = dividerHeight,
    dividerColor = dividerColor,
    itemHeight = itemHeight,
    itemContent = itemContent,
    overlayTopItemModifier = overlayTopItemModifier,
    overlayMiddleItemModifier = overlayMiddleItemModifier,
    overlayBottomItemModifier = overlayBottomItemModifier,
    selectedItemContentDescription = selectedItemContentDescription,
    scrollContainerContentDescription = scrollContainerContentDescription,
  ) {
    onValueChange(it.toInt())
  }
}

@Preview
@Composable
fun NumberPickerPreview() {
  Column(
    modifier = Modifier.size(400.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    NumberPicker(
      range = (0..10),
      value = 3,
    ) {}
  }
}