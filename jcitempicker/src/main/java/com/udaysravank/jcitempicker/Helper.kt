package com.udaysravank.jcitempicker

import kotlinx.collections.immutable.ImmutableList

internal fun determineTheSelectedItem(
    offset: Int,
    firstVisibleItemIndex: Int,
    items: ImmutableList<ItemData>
) = if (offset == 0 && firstVisibleItemIndex == 0) {
    getSelectedItem(0, items)
} else if (offset == 0 && firstVisibleItemIndex == 1) {
    getSelectedItem(1, items)
} else if (offset > 0 && firstVisibleItemIndex == 0) {
    getSelectedItem(1, items)
} else if (offset == 0 && firstVisibleItemIndex > 1) {
    getSelectedItem(firstVisibleItemIndex, items)
} else {
    getSelectedItem(firstVisibleItemIndex + 1, items)
}

private fun getSelectedItem(firstVisibleItemIndex: Int, list: List<ItemData>) =
    when {
        firstVisibleItemIndex == 0 -> list.first()
        firstVisibleItemIndex == list.size -> list.last()
        firstVisibleItemIndex > 0 && firstVisibleItemIndex < list.size -> list[firstVisibleItemIndex]
        else -> list.first()
    }