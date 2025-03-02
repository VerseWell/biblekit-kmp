package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookCollection
import com.aarkaystudio.biblekit.data.BookName
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for Book model
 * Verifies the structure and content of Bible books
 * Tests book properties, chapter counts, and verse numbering
 */
class BookTest {
    /**
     * Tests the basic properties of Genesis book
     * Verifies:
     * - Book name is correctly set
     * - Total number of chapters
     * - Total verse count
     * - Sum of verses across chapters
     * - Verse count in first chapter
     * - Verse IDs generation for first chapter
     * - Verse ID format
     */
    @Test
    fun testGenesisBookProperties() {
        val book = BookCollection.mapping[BookName.Genesis]!!
        assertEquals(book.bookName, BookName.Genesis)
        assertEquals(book.totalChapters, 50)
        assertEquals(book.allVerseIDs().size, 1533)
        assertEquals(book.verses.reduce { acc, i -> acc + i }, 1533)
        assertEquals(book.totalVerses(chapter = 1), 31)
        assertEquals(book.allVerseIDs(chapter = 1).size, 31)
        assertEquals(book.verseID(chapter = 1, verse = 1).value, "1:1:1")
    }

    /**
     * Tests the start and end verse IDs of Genesis book
     * Verifies:
     * - Start verse ID is Genesis 1:1 (1:1:1)
     * - End verse ID is Genesis 50:26 (1:50:26)
     */
    @Test
    fun testGenesisBookVerseIDs() {
        val book = BookCollection.mapping[BookName.Genesis]!!

        // Verify start verse ID (Genesis 1:1)
        assertEquals(book.startVerseID.value, "1:1:1")

        // Verify end verse ID (Genesis 50:26)
        assertEquals(book.endVerseID.value, "1:50:26")
    }
}
