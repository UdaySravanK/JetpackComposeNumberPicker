package com.udaysravank.numberpicker

import kotlinx.collections.immutable.toImmutableList

object PreviewData {
  val itemsList = listOf("one", "two", "three", "four", "five", "six", "seven").map {
    TestItemData(it)
  }.toImmutableList()
  class TestItemData(private val txt: String) : ItemData() {
    override fun uniqueId()= txt
    override fun itemText() = txt
  }
}