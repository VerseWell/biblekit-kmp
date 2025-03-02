package com.aarkaystudio.biblekit.data

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for BookName enum
 * Verifies the correctness of Bible book names and their total count
 */
class BookNameTest {
    /**
     * Tests that there are exactly 66 books in the Bible
     * This matches the standard Protestant canon
     */
    @Test
    fun testTotalCount() {
        assertEquals(BookName.entries.size, 66)
    }

    /**
     * Tests the string representation of book names
     * Particularly focuses on books with numerical prefixes (1/2/3)
     * Verifies that:
     * - Books are properly formatted (e.g., "1 Samuel" not "1Samuel")
     * - Special cases like "Song of Solomon" are handled correctly
     */
    @Test
    fun testBookRawValues() {
        assertEquals("Genesis", BookName.Genesis.value)
        assertEquals("1 Samuel", BookName.Samuel1.value)
        assertEquals("2 Samuel", BookName.Samuel2.value)
        assertEquals("1 Kings", BookName.Kings1.value)
        assertEquals("2 Kings", BookName.Kings2.value)
        assertEquals("1 Chronicles", BookName.Chronicles1.value)
        assertEquals("2 Chronicles", BookName.Chronicles2.value)
        assertEquals("Song of Solomon", BookName.SongOfSolomon.value)
        assertEquals("1 Corinthians", BookName.Corinthians1.value)
        assertEquals("2 Corinthians", BookName.Corinthians2.value)
        assertEquals("1 Thessalonians", BookName.Thessalonians1.value)
        assertEquals("2 Thessalonians", BookName.Thessalonians2.value)
        assertEquals("1 Timothy", BookName.Timothy1.value)
        assertEquals("2 Timothy", BookName.Timothy2.value)
        assertEquals("1 Peter", BookName.Peter1.value)
        assertEquals("2 Peter", BookName.Peter2.value)
        assertEquals("1 John", BookName.John1.value)
        assertEquals("2 John", BookName.John2.value)
        assertEquals("3 John", BookName.John3.value)
    }
}
