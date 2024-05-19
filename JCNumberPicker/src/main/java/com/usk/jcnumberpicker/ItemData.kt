package com.usk.jcnumberpicker

/**
 * Interface to enforce the must have methods in the data class provided for a list item in the ItemPicker.
 */
abstract class ItemData {
    /**
     * Returns the text to be displayed in the item.
     */
    open fun itemText(): String = ""
    abstract fun uniqueId(): String
}