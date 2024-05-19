package com.usk.jcnumberpicker

import kotlinx.collections.immutable.ImmutableList

/**
 * Determines the selected item based on the offset and the first visible item index.
 *
 * @param offset The offset of the first visible item in the scroll view.
 * @param position The index of first visible item index in the scroll view.
 * @param items The list of items.
 *
 * Note: We don't have to do this if `firstVisibleItemIndex` prop of `rememberLazyListState` is accurate.
 * But it is not working as expected. Hence we are going with this workaround.
 * ```
 * list =["one", "two", "three"]
 *
 * Case 1:
 * |     | -> offset = 0 & position = 0
 * | one | -> scroll item index = 1, list item index = 0 ==> selected item is list[0]
 * | two | -> list[1]
 *
 * Case 2:
 * | one   | -> offset = 0 & position = 1
 * | two   | -> selected item is list[1]
 * | three | -> list[2]
 *
 * Case 3:
 * | two   | -> offset = 0 & position = 2
 * | three | -> selected item is list[2]
 * |       | -> list[2]
 *
 * Other edge cases due to scroll offset issues
 * Case 4:
 * | one   | -> offset = 1 & position = 0
 * | two   | -> selected item is list[1]
 * | three | -> list[2]
 *
 * Case 5:
 * | two   | -> offset = 1 & position = 1
 * | three | -> selected item is list[2]
 * |       | ->
 *
 * Case 6: Mostly not possible
 * | two   | -> offset = 1 & position = 2
 * | three | -> selected item is list[2]
 * |       | ->
 *
 * ```
 */
internal fun determineTheSelectedItem(
  offset: Int,
  position: Int,
  items: ImmutableList<ItemData>,
) = if (offset == 0 && position == 0) { getSelectedItem(0, items) } // Case 1
else if (offset == 0 && position == 1) { getSelectedItem(1, items) } // Case 2
else if (offset > 0 && position == 0) { getSelectedItem(1, items) } // Case 4
else if (offset == 0 && position > 1) { getSelectedItem(position, items) } // Case 3
else { getSelectedItem(position + 1, items) } // Case 5, Case 6

/**
 * Returns the selected item based on the index.
 *
 * @param inferredPosition The index value inferred based on the offset and firstVisibleItemIndex
 * which is nothing but the first visible item in the scroll view.
 */
private fun getSelectedItem(inferredPosition: Int, list: List<ItemData>) =
    when {
        inferredPosition == 0 -> list.first() // Case 1
        inferredPosition == list.size -> list.last() // Case 5
        inferredPosition > 0 && inferredPosition < list.size -> list[inferredPosition] // Case 2, Case 3 & Case 4
        else -> list.last() // Case 6 & other edge cases i.e. when the inferredPosition >= list.size. Mostly an invalid Case so just return the last item.
    }