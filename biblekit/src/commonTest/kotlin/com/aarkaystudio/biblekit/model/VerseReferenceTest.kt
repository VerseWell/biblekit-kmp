package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test class for VerseReference
 * Verifies the parsing and validation of Bible verse references
 * Tests both valid and invalid verse ID formats
 */
class VerseReferenceTest {
    /**
     * Tests the creation of VerseReference from a valid verse ID
     * Verifies:
     * - Correct parsing of book, chapter, and verse numbers
     * - Proper conversion back to verse ID format
     * Example: "1:1:1" should parse to Genesis 1:1
     */
    @Test
    fun testVerseReference_fromVerseID() {
        val ref = VerseReference.fromVerseID(value = "1:1:1")
        assertNotNull(ref)
        assertEquals(ref.chapter.bookName, BookName.Genesis)
        assertEquals(ref.chapter.index, 1)
        assertEquals(ref.index, 1)
        assertEquals(ref.verseID().value, "1:1:1")
    }

    /**
     * Tests validation of verse IDs with invalid formats
     * Verifies handling of:
     * - Zero values in book/chapter/verse (invalid)
     * - Out of range values
     * - Edge cases like last verse of Revelation
     * Format tested: "book:chapter:verse" where each component must be valid
     */
    @Test
    fun testVerseReference_fromVerseID_invalidCase() {
        assertNotNull(VerseReference.fromVerseID(value = "1:1:1"))
        assertNull(VerseReference.fromVerseID(value = "1:1:0"))
        assertNull(VerseReference.fromVerseID(value = "1:0:1"))
        assertNull(VerseReference.fromVerseID(value = "0:1:1"))
        assertNotNull(VerseReference.fromVerseID(value = "66:22:21"))
        assertNull(VerseReference.fromVerseID(value = "66:22:22"))
        assertNull(VerseReference.fromVerseID(value = "66:23:21"))
        assertNull(VerseReference.fromVerseID(value = "67:22:21"))
    }

    /**
     * Tests the comparison functionality between verse references
     * Verifies:
     * - Comparison between different books
     * - Comparison between different chapters in same book
     * - Comparison between different verses in same chapter
     * - Equality comparison
     */
    @Test
    fun testVerseReference_compareTo() {
        // Create test references
        val genesis1_1 = VerseReference.fromVerseID("1:1:1")!! // Genesis 1:1
        val genesis1_2 = VerseReference.fromVerseID("1:1:2")!! // Genesis 1:2
        val genesis2_1 = VerseReference.fromVerseID("1:2:1")!! // Genesis 2:1
        val exodus1_1 = VerseReference.fromVerseID("2:1:1")!! // Exodus 1:1
        val genesis1_1_copy = VerseReference.fromVerseID("1:1:1")!! // Genesis 1:1

        // Additional test references for multi-digit book numbers
        val secondSamuel1_1 = VerseReference.fromVerseID("10:1:1")!! // 2 Samuel 1:1
        val firstKings1_1 = VerseReference.fromVerseID("11:1:1")!! // 1 Kings 1:1
        val revelation1_1 = VerseReference.fromVerseID("66:1:1")!! // Revelation 1:1

        // Test different books
        assertTrue(genesis1_1 < exodus1_1)
        assertTrue(exodus1_1 > genesis1_1)

        // Test different chapters in same book
        assertTrue(genesis1_1 < genesis2_1)
        assertTrue(genesis2_1 > genesis1_1)

        // Test different verses in same chapter
        assertTrue(genesis1_1 < genesis1_2)
        assertTrue(genesis1_2 > genesis1_1)

        // Test equal verses
        assertEquals(0, genesis1_1.compareTo(genesis1_1_copy))

        // Test books with different digit lengths
        assertTrue(genesis1_1 < secondSamuel1_1, "Genesis should come before 2 Samuel")
        assertTrue(secondSamuel1_1 > genesis1_1, "2 Samuel should come after Genesis")

        // Test consecutive multi-digit books
        assertTrue(secondSamuel1_1 < firstKings1_1, "2 Samuel should come before 1 Kings")
        assertTrue(firstKings1_1 > secondSamuel1_1, "1 Kings should come after 2 Samuel")

        // Test first book vs last book
        assertTrue(genesis1_1 < revelation1_1, "Genesis should come before Revelation")
        assertTrue(revelation1_1 > genesis1_1, "Revelation should come after Genesis")
    }
}
