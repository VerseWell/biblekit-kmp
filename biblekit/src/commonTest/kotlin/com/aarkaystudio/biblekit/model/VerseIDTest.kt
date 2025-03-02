package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookName
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for VerseID model
 * Tests verse ID constants and component parsing
 * Verifies correct handling of verse ID formats
 */
class VerseIDTest {
    /**
     * Tests the start verse ID constant
     * Verifies it represents Genesis 1:1 (1:1:1)
     */
    @Test
    fun testVerseID_start() {
        assertEquals(VerseID.start.value, "1:1:1")
    }

    /**
     * Tests the end verse ID constant
     * Verifies it represents Revelation 22:21 (66:22:21)
     */
    @Test
    fun testVerseID_end() {
        assertEquals(VerseID.end.value, "66:22:21")
    }

    /**
     * Tests parsing of verse ID components
     * Verifies:
     * - Handling of different component lengths (1-4 components)
     * - Correct parsing of book, chapter, verse, and subverse numbers
     * Examples:
     * - "66:22:21:11" -> [66, 22, 21, 11]
     * - "66:22:21" -> [66, 22, 21]
     * - "66:22" -> [66, 22]
     * - "66" -> [66]
     */
    @Test
    fun testVerseID_components() {
        val testRepeatMap =
            mapOf(
                "66:22:21:11" to listOf(66, 22, 21, 11),
                "66:22:21" to listOf(66, 22, 21),
                "66:22" to listOf(66, 22),
                "66" to listOf(66),
            )

        testRepeatMap.forEach { (key, value) ->
            assertEquals(VerseID(value = key).components(), value)
        }
    }

    /**
     * Tests the bookChapterVerse() method
     * Verifies that it correctly formats the verse reference with book name and chapter:verse
     * Example: "Genesis 1:1" for verse ID "1:1:1"
     */
    @Test
    fun testVerseID_bookChapterVerse() {
        val testCases =
            mapOf(
                "1:1:1" to "Genesis 1:1",
                "66:22:21" to "Revelation 22:21",
                "19:23:6" to "Psalms 23:6",
            )

        testCases.forEach { (input, expected) ->
            assertEquals(expected, VerseID(value = input).bookChapterVerse())
        }
    }

    /**
     * Tests the bookName() method
     * Verifies that it correctly returns the BookName enum for the verse's book number
     */
    @Test
    fun testVerseID_bookName() {
        val testCases =
            mapOf(
                "1:1:1" to BookName.Genesis,
                "66:22:21" to BookName.Revelation,
                "19:23:6" to BookName.Psalms,
            )

        testCases.forEach { (input, expected) ->
            assertEquals(expected, VerseID(value = input).bookName())
        }
    }

    /**
     * Tests the chapterVerseNumbers() method
     * Verifies that it correctly extracts chapter and verse numbers as a list
     * Example: [1, 1] for verse ID "1:1:1"
     */
    @Test
    fun testVerseID_chapterVerseNumbers() {
        val testCases =
            mapOf(
                "1:1:1" to listOf(1, 1),
                "66:22:21" to listOf(22, 21),
                "19:23:6" to listOf(23, 6),
            )

        testCases.forEach { (input, expected) ->
            assertEquals(expected, VerseID(value = input).chapterVerseNumbers())
        }
    }
}
