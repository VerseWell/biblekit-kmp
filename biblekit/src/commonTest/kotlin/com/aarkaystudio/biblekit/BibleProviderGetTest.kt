package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekit.data.BookCollection
import com.aarkaystudio.biblekit.data.BookName
import com.aarkaystudio.biblekit.mocks.MockBibleStoreProvider
import com.aarkaystudio.biblekit.model.ChapterReference
import com.aarkaystudio.biblekit.model.VerseID
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test class for BibleProvider
 * Tests verse and chapter retrieval functionality
 * Uses a mock Bible store for testing
 */
class BibleProviderGetTest {
    /**
     * Tests verse retrieval functionality
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Retrieve verses by their IDs
     * 3. Verify that correct verses are returned
     */
    @Test
    fun testGetVerses() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val verseIds =
                listOf(
                    VerseID("1:1:1"),
                    VerseID("1:1:3"),
                    VerseID("1:1:5"),
                )

            val verses =
                provider.verses(
                    ids = verseIds,
                    limit = 3,
                    offset = 2,
                )

            assertTrue(verses.isEmpty())
            assertNull(mockStore.searchText)
            assertEquals(verseIds, mockStore.verseIDs)
            assertNull(mockStore.startVerseID)
            assertNull(mockStore.endVerseID)
            assertEquals(3, mockStore.limit)
            assertEquals(2, mockStore.offset)
        }

    /**
     * Tests verse retrieval functionality with default limit and offset values
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Retrieve verses by their IDs
     * 3. Verify default pagination values are correctly used
     */
    @Test
    fun testGetWithDefaultLimitAndOffset() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val verseIds =
                listOf(
                    VerseID("1:1:1"),
                    VerseID("1:1:3"),
                    VerseID("1:1:5"),
                )

            val verses =
                provider.verses(
                    ids = verseIds,
                )

            assertTrue(verses.isEmpty())
            assertNull(mockStore.searchText)
            assertEquals(verseIds, mockStore.verseIDs)
            assertNull(mockStore.startVerseID)
            assertNull(mockStore.endVerseID)
            // Verify default values
            assertEquals(Long.MAX_VALUE, mockStore.limit)
            assertEquals(Long.MIN_VALUE, mockStore.offset)
        }

    /**
     * Tests chapter retrieval functionality
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Retrieve verses from a specific chapter
     * 3. Verify that correct verses are returned
     */
    @Test
    fun testGetChapter() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val chapterRef =
                ChapterReference(
                    bookName = BookName.Genesis,
                    index = 1,
                )

            val verses =
                provider.chapter(
                    chapter = chapterRef,
                    limit = 7,
                    offset = 4,
                )

            assertTrue(verses.isEmpty())
            assertNull(mockStore.searchText)
            assertNull(mockStore.verseIDs)
            assertEquals(VerseID("1:1:1"), mockStore.startVerseID)
            assertEquals(VerseID("1:1:31"), mockStore.endVerseID)
            assertEquals(7, mockStore.limit)
            assertEquals(4, mockStore.offset)
        }

    /**
     * Tests book retrieval functionality
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Retrieve verses from a specific book
     * 3. Verify that correct verses are returned
     */
    @Test
    fun testGetBook() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val book = BookCollection.mapping[BookName.Genesis]!!

            val verses =
                provider.book(
                    book = book,
                    limit = 12,
                    offset = 6,
                )

            assertTrue(verses.isEmpty())
            assertNull(mockStore.searchText)
            assertNull(mockStore.verseIDs)
            assertEquals(VerseID("1:1:1"), mockStore.startVerseID)
            assertEquals(VerseID("1:50:26"), mockStore.endVerseID)
            assertEquals(12, mockStore.limit)
            assertEquals(6, mockStore.offset)
        }
}
