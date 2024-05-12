package com.udaysravank.numberpicker

import com.udaysravank.numberpicker.PreviewData.itemsList
import kotlinx.collections.immutable.toImmutableList
import org.junit.Test

import org.junit.Assert.*

class ItemPickerHelperTest {

    private val testList = itemsList.take(3).toImmutableList()

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

    @Test
    fun `Case 2 - get second item from the list as selected item`() {
        // Execute
        val result = determineTheSelectedItem(
            offset = 0,
            position = 1,
            items = testList
        )

        // Verify
        assertEquals(testList[1], result)
    }

    @Test
    fun `Case 2 - get last but one item from the list as selected item`() {
        // Execute
        val positionOfLastButOne = testList.size - 2 // position 1
        val result = determineTheSelectedItem(
            offset = 0,
            position = positionOfLastButOne,
            items = testList
        )

        // Verify
        assertEquals(testList[positionOfLastButOne], result)
    }

    @Test
    fun `Case 3 - get the last item from list as selected item`() {
        // Execute
        val positionOfLastItem = testList.size - 1 // position 2
        val result = determineTheSelectedItem(
            offset = 0,
            position = positionOfLastItem,
            items = testList
        )

        // Verify
        assertEquals(testList[positionOfLastItem], result)
    }

    @Test
    fun `Case 4 - get the second item from list as selected item if position is 0 but offset is grater than 0`() {
        // Execute
        val result = determineTheSelectedItem(
            offset = 1,
            position = 0,
            items = testList
        )

        // Verify
        assertEquals(testList[1], result)
    }

    @Test
    fun `Case 5 - get the last item from list as selected item if position is 1 but offset is grater than 0`() {
        // Execute
        val result = determineTheSelectedItem(
            offset = 1,
            position = 1,
            items = testList
        )

        // Verify
        assertEquals(testList[2], result)
    }

    @Test
    fun `Case 6 - get the last item from list as selected item if position is 2 but offset is grater than 0`() {
        // Execute
        val result = determineTheSelectedItem(
            offset = 1,
            position = 2,
            items = testList
        )

        // Verify
        assertEquals(testList[2], result)
    }

    @Test
    fun `offset can be greater than 1`() {
        // Execute
        val result = determineTheSelectedItem(
            offset = 5,
            position = 0,
            items = testList
        )

        // Verify
        assertEquals(testList[1], result)
    }

    @Test
    fun `return the last item for invalid offset and position combination`() {
        // Execute
        val result = determineTheSelectedItem(
            offset = 5,
            position = 2,
            items = testList
        )

        // Verify
        assertEquals(testList[2], result)
    }

}