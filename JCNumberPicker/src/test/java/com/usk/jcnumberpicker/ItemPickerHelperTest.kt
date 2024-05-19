package com.usk.jcnumberpicker

import kotlinx.collections.immutable.toImmutableList
import org.junit.Test

import org.junit.Assert.assertEquals

class ItemPickerHelperTest {

    private val testList = listOf("one", "two", "three").map {
        TestItemData(it)
    }.toImmutableList()

    class TestItemData(private val txt: String) : ItemData() {
        override fun uniqueId()= txt
        override fun itemText() = txt
    }

    @Test
    fun `Case 1 - get first item from the list as selected item`() {
        // Execute
        val result = determineTheSelectedItem(
            offset = 0,
            position = 0,
            items = testList
        )

        // Verify
        assertEquals(testList.first(), result)
    }
}