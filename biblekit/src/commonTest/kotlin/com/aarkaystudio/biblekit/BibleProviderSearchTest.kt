package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekit.mocks.MockBibleStoreProvider
import com.aarkaystudio.biblekit.model.Reference
import com.aarkaystudio.biblekit.model.VerseID
import com.aarkaystudio.biblekit.model.VerseReference
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test class for BibleProvider's search functionality
 * Tests different search scenarios using mock Bible store
 */
class BibleProviderSearchTest {
    /**
     * Tests searching verses with text only (no verse IDs)
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Search for verses containing specific text
     * 3. Verify search parameters are correctly passed to store
     */
    @Test
    fun testSearchWithTextOnly() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val searchText = "God"

            val verses =
                provider.search(
                    query = searchText,
                    verseIDs = null,
                    limit = 10,
                    offset = 0,
                )

            assertTrue(verses.isEmpty())
            assertEquals(searchText, mockStore.searchText)
            assertNull(mockStore.verseIDs)
            assertNull(mockStore.startVerseID)
            assertNull(mockStore.endVerseID)
            assertEquals(10, mockStore.limit)
            assertEquals(0, mockStore.offset)
        }

    /**
     * Tests searching verses with default limit and offset values
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Search for verses without specifying limit and offset
     * 3. Verify default pagination values are correctly used
     */
    @Test
    fun testSearchWithDefaultLimitAndOffset() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val searchText = "God"

            val verses =
                provider.search(
                    query = searchText,
                    verseIDs = null,
                )

            assertTrue(verses.isEmpty())
            assertEquals(searchText, mockStore.searchText)
            assertNull(mockStore.verseIDs)
            assertNull(mockStore.startVerseID)
            assertNull(mockStore.endVerseID)
            // Verify default values
            assertEquals(Long.MAX_VALUE, mockStore.limit)
            assertEquals(Long.MIN_VALUE, mockStore.offset)
        }

    /**
     * Tests searching verses with text and specific verse IDs
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Search for verses containing text within specific verse IDs
     * 3. Verify search parameters are correctly passed to store
     */
    @Test
    fun testSearchWithTextAndVerseIds() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val searchText = "God"
            val verseIds =
                listOf(
                    VerseID("1:1:1"),
                    VerseID("1:1:2"),
                    VerseID("1:1:3"),
                )

            val verses =
                provider.search(
                    query = searchText,
                    verseIDs = verseIds,
                    limit = 10,
                    offset = 0,
                )

            assertTrue(verses.isEmpty())
            assertEquals(searchText, mockStore.searchText)
            assertEquals(verseIds, mockStore.verseIDs)
            assertNull(mockStore.startVerseID)
            assertNull(mockStore.endVerseID)
            assertEquals(10, mockStore.limit)
            assertEquals(0, mockStore.offset)
        }

    /**
     * Tests searching verses with text and reference range
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Search for verses containing text within reference range
     * 3. Verify search parameters are correctly passed to store
     */
    @Test
    fun testSearchWithTextAndReference() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val searchText = "God"
            val reference =
                Reference(
                    from = VerseReference.fromVerseID("1:1:1")!!,
                    to = VerseReference.fromVerseID("1:1:10")!!,
                )

            val verses =
                provider.search(
                    query = searchText,
                    reference = reference,
                    limit = 10,
                    offset = 0,
                )

            assertTrue(verses.isEmpty())
            assertEquals(searchText, mockStore.searchText)
            assertNull(mockStore.verseIDs)
            assertEquals(VerseID("1:1:1"), mockStore.startVerseID)
            assertEquals(VerseID("1:1:10"), mockStore.endVerseID)
            assertEquals(10, mockStore.limit)
            assertEquals(0, mockStore.offset)
        }

    /**
     * Tests searching verses with text and reference that requires fixup
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Search for verses with reference where end comes before start
     * 3. Verify reference is fixed up before searching
     */
    @Test
    fun testSearchWithTextAndReferenceFixup() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val searchText = "God"
            val reference =
                Reference(
                    from = VerseReference.fromVerseID("1:1:10")!!, // End comes before start
                    to = VerseReference.fromVerseID("1:1:1")!!,
                )

            val verses =
                provider.search(
                    query = searchText,
                    reference = reference,
                    limit = 10,
                    offset = 0,
                )

            assertTrue(verses.isEmpty())
            assertEquals(searchText, mockStore.searchText)
            assertNull(mockStore.verseIDs)
            // After fixup, start should be before end
            assertEquals(VerseID("1:1:1"), mockStore.startVerseID)
            assertEquals(VerseID("1:1:10"), mockStore.endVerseID)
            assertEquals(10, mockStore.limit)
            assertEquals(0, mockStore.offset)
        }

    /**
     * Tests searching with empty text and verse IDs
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Search with empty text and verse IDs
     * 3. Verify empty results are returned without calling store
     */
    @Test
    fun testEmptySearchWithVerseIds() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val verseIds =
                listOf(
                    VerseID("1:1:1"),
                    VerseID("1:1:2"),
                    VerseID("1:1:3"),
                )

            val verses =
                provider.search(
                    query = "", // Empty search text
                    verseIDs = verseIds,
                    limit = 10,
                    offset = 0,
                )

            assertTrue(verses.isEmpty())
            assertNull(mockStore.searchText) // Store should not be called
            assertNull(mockStore.verseIDs) // Store should not be called
            assertNull(mockStore.startVerseID)
            assertNull(mockStore.endVerseID)
            assertNull(mockStore.limit) // Store should not be called
            assertNull(mockStore.offset) // Store should not be called
        }

    /**
     * Tests searching with empty text and reference range
     *
     * Steps:
     * 1. Set up mock Bible store
     * 2. Search with empty text and reference range
     * 3. Verify empty results are returned without calling store
     */
    @Test
    fun testEmptySearchWithReference() =
        runTest {
            val mockStore = MockBibleStoreProvider()
            val provider = BibleProvider(store = mockStore)
            val reference =
                Reference(
                    from = VerseReference.fromVerseID("1:1:1")!!,
                    to = VerseReference.fromVerseID("1:1:10")!!,
                )

            val verses =
                provider.search(
                    query = "   ", // Whitespace-only search text
                    reference = reference,
                    limit = 10,
                    offset = 0,
                )

            assertTrue(verses.isEmpty())
            assertNull(mockStore.searchText) // Store should not be called
            assertNull(mockStore.verseIDs)
            assertNull(mockStore.startVerseID) // Store should not be called
            assertNull(mockStore.endVerseID) // Store should not be called
            assertNull(mockStore.limit) // Store should not be called
            assertNull(mockStore.offset) // Store should not be called
        }
}
