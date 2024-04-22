package com.udaysravank.jcitempicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val DEFAULT_ITEM_HEIGHT = 48
private const val DEFAULT_SEPARATOR_HEIGHT = 1

/**
 * The compose version of Android [NumberPicker](https://developer.android.com/reference/android/widget/NumberPicker)
 *
 * ItemPicker will show 3 items and treats the middle one as the selected item.
 *
 * @param range The range of integer values
 * @param value Current selected value i.e the middle value in the picker
 * @param listItemHeight Height of one single item in the picker. The full height of the ItemPicker will be calculated based on this parameter.
 * @param dividerHeight Height of separator lines above and below the middle item to indicate it as the selected item
 * @param dividerColor Color of the middle item separators
 * @param onValueChange A callback to receive the current selected value
 * @param selectedItemContentDescription Content description for the middle selected item on focus announcement
 * @param scrollContainerContentDescription Content description for the scroll container on focus announcement
 * Note: This component will be replaced with the official Compose provided component when available in the future.
 */
@Composable
fun NumberPicker(
    range: Iterable<Int>,
    value: Int,
    modifier: Modifier = Modifier,
    listItemHeight: Dp = DEFAULT_ITEM_HEIGHT.dp,
    dividerHeight: Dp = DEFAULT_SEPARATOR_HEIGHT.dp,
    dividerColor: Color = Color.Red,
    selectedItemContentDescription: String = "",
    scrollContainerContentDescription: String = "",
    onValueChange: (Int) -> Unit,
) {
    val items = remember(range) {
        range.map { it.toString() }.toImmutableList()
    }
    ItemPicker(
        items = items,
        value = value.toString(),
        modifier = modifier,
        listItemHeight = listItemHeight,
        dividerHeight = dividerHeight,
        dividerColor = dividerColor,
        selectedItemContentDescription = selectedItemContentDescription,
        scrollContainerContentDescription = scrollContainerContentDescription,
    ) {
        onValueChange(it.toInt())
    }
}

/**
 * The compose version of Android [NumberPicker](https://developer.android.com/reference/android/widget/NumberPicker)
 *
 * ItemPicker will show 3 items and treats the middle one as the selected item.
 *
 * @param items The list of items
 * @param value Current selected value i.e the middle value in the picker
 * @param listItemHeight Height of one single item in the picker. The full height of the ItemPicker will be calculated based on this parameter.
 * @param dividerHeight Height of separator lines above and below the middle item to indicate it as the selected item
 * @param dividerColor Color of the middle item separators
 * @param onValueChange A callback to receive the current selected value
 * @param selectedItemContentDescription Content description for the middle selected item on focus announcement
 * @param scrollContainerContentDescription Content description for the scroll container on focus announcement
 * Note: This component will be replaced with the official Compose provided component when available in the future.
 */
@Composable
fun ItemPicker(
    items: ImmutableList<String>,
    value: String,
    modifier: Modifier = Modifier,
    listItemHeight: Dp = DEFAULT_ITEM_HEIGHT.dp,
    dividerHeight: Dp = DEFAULT_SEPARATOR_HEIGHT.dp,
    dividerColor: Color = Color.Red,
    selectedItemContentDescription: String = "",
    scrollContainerContentDescription: String = "",
    onValueChange: (String) -> Unit,
) {
    val itemDataList = remember(items) {
        items.map {
            object : ItemData {
                override fun itemText(): String {
                    return it
                }

                override fun uniqueId(): String {
                    return it
                }
            }
        }.toImmutableList()
    }

    ItemPicker(
        items = itemDataList,
        value = itemDataList.first { it.itemText() == value },
        modifier = modifier,
        listItemHeight = listItemHeight,
        dividerHeight = dividerHeight,
        dividerColor = dividerColor,
        selectedItemContentDescription = selectedItemContentDescription,
        scrollContainerContentDescription = scrollContainerContentDescription,
    ) {
        onValueChange(it.itemText())
    }
}

/**
 * The compose version of Android [NumberPicker](https://developer.android.com/reference/android/widget/NumberPicker)
 *
 * ItemPicker will show 3 items and treats the middle one as the selected item.
 *
 * @param items The list of items
 * @param value Current selected value i.e the middle value in the picker
 * @param listItemHeight Height of one single item in the picker. The full height of the ItemPicker will be calculated based on this parameter.
 * @param dividerHeight Height of separator lines above and below the middle item to indicate it as the selected item
 * @param dividerColor Color of the middle item separators
 * @param onValueChange A callback to receive the current selected value
 * @param selectedItemContentDescription Content description for the middle selected item on focus announcement
 * @param scrollContainerContentDescription Content description for the scroll container on focus announcement
 * Note: This component will be replaced with the official Compose provided component when available in the future.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemPicker(
    items: ImmutableList<ItemData>,
    value: ItemData,
    modifier: Modifier = Modifier,
    listItemHeight: Dp = DEFAULT_ITEM_HEIGHT.dp,
    dividerHeight: Dp = DEFAULT_SEPARATOR_HEIGHT.dp,
    dividerColor: Color = Color.Red,
    selectedItemContentDescription: String = "",
    scrollContainerContentDescription: String = "",
    onValueChange: (ItemData) -> Unit = {},
) {
    val numberOfItemsToVisibleInTheList = 3 // Like the standard NumberPicker, we have considered showing only 3 items from the list at a time
    val containerHeight = listItemHeight * numberOfItemsToVisibleInTheList + dividerHeight * 2
    val indexOfSelectedItem = items.indexOfFirst { it.uniqueId() == value.uniqueId() }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = if (indexOfSelectedItem != -1) indexOfSelectedItem else 1)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .map { offset ->
                /**
                 * firstVisibleItemIndex is not giving the accurate value when scrolling between first 3 elements of list.
                 * Hence, we are using firstVisibleItemScrollOffset with work around logic .
                 */
                determineTheSelectedItem(offset, listState.firstVisibleItemIndex, items)
            }
            .distinctUntilChanged()
            .collect { item ->
                onValueChange(item)
            }
    }

    Box(
        modifier = modifier.height(containerHeight)
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .fillMaxWidth()
                .height(containerHeight)
                .semantics {
                    contentDescription = scrollContainerContentDescription
                }
        ) {
            item {
                // Need this to be able to scroll the first item in list to center selector of ItemPicker
                ItemView(
                    modifier = Modifier
                        .focusable(false)
                        .semantics(mergeDescendants = true) {},
                    text = "",
                    listItemHeight = listItemHeight,
                    textColor = Color.Black,
                )
            }
            items(items) {
                ItemView(
                    modifier = Modifier
                        .semantics(mergeDescendants = true) {
                            contentDescription = selectedItemContentDescription
                        }
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(items.indexOf(it))
                            }
                        },
                    text = it.itemText(),
                    listItemHeight = listItemHeight,
                )
            }
            item {
                // Need this to be able to scroll the last item in list to center selector of ItemPicker
                ItemView(
                    modifier = Modifier
                        .focusable(false)
                        .semantics(mergeDescendants = true) {},
                    text = "",
                    listItemHeight = listItemHeight,
                    textColor = Color.Black,
                )
            }
        }
        OverlayLayout(
            listItemHeight = listItemHeight,
            dividerHeight = dividerHeight,
            dividerColor = dividerColor,
        )
    }
}
 
/**
 * This component is to draw separator lines above and below the middle item in the ItemPicker. 
 * Separators are to indicate the middle one as the selected item.
 * We can improve the performance of the component by directly drawing lines in the layout phase of composition cycle.
 * Leaving it as is, since this component is an interim solution until the official version of it is available.
 *
 * Feel free to improve, if interested.
 */
@Composable
private fun OverlayLayout(
    listItemHeight: Dp,
    dividerHeight: Dp,
    dividerColor: Color,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // For fade effect on top and bottom items
        val gradientColor = Color.LightGray
        val topItemBackground = remember {
            Brush.verticalGradient(
                colors = listOf(gradientColor, Color.Transparent),
            )
        }
        val bottomItemBackground = remember {
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, gradientColor),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(listItemHeight)
                .background(topItemBackground)
        )
        Divider(
            thickness = dividerHeight,
            color = dividerColor,
        )
        Box(modifier = Modifier.height(listItemHeight))
        Divider(
            thickness = dividerHeight,
            color = dividerColor,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(listItemHeight)
                .background(bottomItemBackground)
        )
    }
}

@Composable
private fun ItemView(
    text: String,
    listItemHeight: Dp,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    textColor: Color = Color.Black,
) {
    Row(
        modifier = modifier
            .height(listItemHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
