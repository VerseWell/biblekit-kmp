package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookName
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for ChapterReference model
 * Tests chapter reference functionality and book information retrieval
 */
class ChapterReferenceTest {
    /**
     * Tests chapter reference for Genesis 1
     * Verifies:
     * - Book name is correctly set to Genesis
     * - Total chapters in Genesis (50)
     * - Total verses in Genesis 1 (31)
     */
    @Test
    fun testGenesisChapter1Properties() {
        val ref = ChapterReference(bookName = BookName.Genesis, index = 1)
        val book = ref.book()
        assertEquals(book.bookName, BookName.Genesis)
        assertEquals(book.totalChapters, 50)
        assertEquals(ref.totalVerses(), 31)
    }

    /**
     * Tests start and end verse IDs for Genesis chapter 1
     * Verifies:
     * - Start verse ID is Genesis 1:1
     * - End verse ID is Genesis 1:31
     */
    @Test
    fun testChapterReferenceVerseIDs() {
        val ref = ChapterReference(bookName = BookName.Genesis, index = 1)

        // Verify start verse ID (Genesis 1:1)
        assertEquals(ref.startVerseID, ref.verseID(verse = 1))

        // Verify end verse ID (Genesis 1:31)
        assertEquals(ref.endVerseID, ref.verseID(verse = 31))
    }
}
