package com.usk.jcnumberpicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.usk.jcnumberpicker.PreviewData.itemsList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


/**
 * The compose version of Android [NumberPicker](https://developer.android.com/reference/android/widget/NumberPicker)
 *
 * ItemPicker will show 3 items and treats the middle one as the selected item.
 *
 * @param items the list of strings to be displayed in the ItemPicker
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
fun ItemPicker(
  items: ImmutableList<String>,
  modifier: Modifier = Modifier,
  value: String = items.first(),
  dividerHeight: Dp = DEFAULT_SEPARATOR_HEIGHT.dp,
  dividerColor: Color = Color.Red,
  selectedItemContentDescription: String = "",
  scrollContainerContentDescription: String = "",
  itemHeight: Dp = DEFAULT_ITEM_HEIGHT.dp,
  itemContent: (@Composable (itemData: ItemData) -> Unit)? = null,
  overlayTopItemModifier: Modifier? = null,
  overlayMiddleItemModifier: Modifier? = null,
  overlayBottomItemModifier: Modifier? = null,
  onValueChange: (String) -> Unit = {},
) {
    val itemDataList = remember(items) {
        items.map {
            object : ItemData() {
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemPicker(
  items: ImmutableList<ItemData>,
  modifier: Modifier = Modifier,
  value: ItemData = items.first(),
  dividerHeight: Dp = DEFAULT_SEPARATOR_HEIGHT.dp,
  dividerColor: Color = Color.Red,
  selectedItemContentDescription: String = "",
  scrollContainerContentDescription: String = "",
  itemHeight: Dp = DEFAULT_ITEM_HEIGHT.dp,
  itemContent: (@Composable (itemData: ItemData) -> Unit)? = null,
  overlayTopItemModifier: Modifier? = null,
  overlayMiddleItemModifier: Modifier? = null,
  overlayBottomItemModifier: Modifier? = null,
  onValueChange: (ItemData) -> Unit = {},
) {
    val numberOfItemsToVisibleInTheList = 3 // Like the standard NumberPicker, we have considered showing only 3 items from the list at a time
    val containerHeight = itemHeight * numberOfItemsToVisibleInTheList + dividerHeight * 2
    val indexOfSelectedItem = items.indexOfFirst { it.uniqueId() == value.uniqueId() }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = if (indexOfSelectedItem != -1) indexOfSelectedItem else 1)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val coroutineScope = rememberCoroutineScope()
    val dummyItemData = object : ItemData() {
        override fun itemText() = ""
        override fun uniqueId() = ""
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .map { offset ->
                /**
                 * firstVisibleItemIndex is not giving the accurate value when scrolling between first 3 elements of list.
                 * Hence, we are using firstVisibleItemScrollOffset with work around logic .
                 */
                determineTheSelectedItem(
                    offset = offset,
                    position = listState.firstVisibleItemIndex,
                    items = items,
                )
            }
            .distinctUntilChanged()
            .collect { item ->
                onValueChange(item)
            }
    }

    Box(
        modifier = modifier.height(containerHeight)
    ) {
        /**
         * Actual number of items in the scrollable list is 2 more than the number of items in the list.
         * One is for the first blank item in the list and the other is for the last blank item in the list.
         * So, the first item from the given list can still be in the center of 3 visible items in the ItemPicker. Similarly for the last item.
         * We don't have to do this if the list is infinite scrolling.
         */
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .fillMaxWidth()
                .height(containerHeight)
                .semantics {
                    contentDescription = scrollContainerContentDescription
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                // Need this to be able to scroll the first item in list to center selector of ItemPicker
                ItemView(
                    modifier = Modifier
                        .focusable(false)
                        .semantics(mergeDescendants = true) {},
                    itemData = dummyItemData,
                    listItemHeight = itemHeight,
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
                    itemData = it,
                    listItemHeight = itemHeight,
                    content = itemContent,
                )
            }
            item {
                // Need this to be able to scroll the last item in list to center selector of ItemPicker
                ItemView(
                    modifier = Modifier
                        .focusable(false)
                        .semantics(mergeDescendants = true) {},
                    itemData = dummyItemData,
                    listItemHeight = itemHeight,
                )
            }
        }
        OverlayLayout(
            listItemHeight = itemHeight,
            dividerHeight = dividerHeight,
            dividerColor = dividerColor,
            topItemModifier = overlayTopItemModifier,
            middleItemModifier = overlayMiddleItemModifier,
            bottomItemModifier = overlayBottomItemModifier,
        )
    }
}

@Preview
@Composable
fun ItemPickerPreview() {
    Column(modifier = Modifier.width(200.dp)) {
        ItemPicker(
            items = itemsList,
        ) {
            // Do nothing
        }
    }
}
 
/**
 * This component is to draw separator lines above and below the middle item in the ItemPicker. 
 * Separators are to indicate the middle one as the selected item.
 * We can improve the performance of the component by directly drawing lines in the layout phase of composition cycle.
 * Leaving it as is, since this component is an interim solution until the official version of it is available from compose libraries.
 *
 * Feel free to improve, if interested.
 */
@Composable
private fun OverlayLayout(
    listItemHeight: Dp,
    dividerHeight: Dp,
    dividerColor: Color,
    modifier: Modifier = Modifier,
    topItemModifier: Modifier? = null,
    middleItemModifier: Modifier? = null,
    bottomItemModifier: Modifier? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // For fade effect on top and bottom items
        val mTopItemModifier = remember(topItemModifier) {
            topItemModifier ?: Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(DEFAULT_ITEM_GRADIENT_COLOR, Color.Transparent),
                )
            )
        }
        val mMiddleItemModifier = remember(middleItemModifier) {
            middleItemModifier ?: Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, DEFAULT_ITEM_GRADIENT_COLOR),
                )
            )
        }
        val mBottomItemModifier = remember(bottomItemModifier) {
            bottomItemModifier ?: Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, DEFAULT_ITEM_GRADIENT_COLOR),
                )
            )
        }
        Box(
            modifier = mTopItemModifier
                .fillMaxWidth()
                .height(listItemHeight)
        )
        Divider(
            thickness = dividerHeight,
            color = dividerColor,
        )
        Box(modifier = mMiddleItemModifier.height(listItemHeight))
        Divider(
            thickness = dividerHeight,
            color = dividerColor,
        )
        Box(
            modifier = mBottomItemModifier
                .fillMaxWidth()
                .height(listItemHeight)
        )
    }
}

@Preview
@Composable
private fun OverlayLayoutPreview() {
    Column(modifier = Modifier.size(400.dp)) {
        OverlayLayout(
            listItemHeight = 50.dp,
            dividerHeight = 2.dp,
            dividerColor = Color.Magenta,
            modifier = Modifier.width(200.dp),
        )
    }
}

@Composable
private fun ItemView(
  itemData: ItemData,
  listItemHeight: Dp,
  modifier: Modifier,
  content: (@Composable (itemData: ItemData) -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .height(listItemHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (content != null) {
            content(itemData)
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = itemData.itemText(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun ItemViewPreview() {
    Column(modifier = Modifier.size(400.dp)) {
        ItemView(
            itemData = object : ItemData() {
                override fun itemText() = "Item 1"
                override fun uniqueId() = "Item 1"
            },
            listItemHeight = 50.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}
