package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekit.mocks.MockVerseRepository
import com.aarkaystudio.biblekit.model.Verse
import com.aarkaystudio.biblekit.model.VerseID
import com.aarkaystudio.biblekitdb.BibleStoreProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test class for DefaultBibleStoreService
 * Tests the service layer's functionality using a mock provider
 */
class DefaultBibleStoreServiceTest {
    /**
     * Tests retrieving verses by their IDs
     *
     * Steps:
     * 1. Set up mock provider with test data
     * 2. Create DefaultBibleStoreService with mock provider
     * 3. Retrieve verses by IDs
     * 4. Verify correct parameters are passed and verses are converted
     */
    @Test
    fun testGetVerses() =
        runTest {
            val mockProvider = MockVerseRepository()
            val service =
                DefaultBibleStoreService(
                    provider =
                        BibleStoreProvider(
                            repository = mockProvider,
                        ),
                )
            val verseIds =
                listOf(
                    VerseID("1:1:1"),
                    VerseID("1:1:2"),
                )

            val verses =
                service.getVerses(
                    verseIDs = verseIds,
                    limit = 5,
                    offset = 0,
                )

            // Verify parameters were correctly passed to provider
            assertNull(mockProvider.searchText)
            assertNull(mockProvider.startVerse)
            assertNull(mockProvider.endVerse)
            assertEquals(verseIds.map { it.value }, mockProvider.verseIDs)
            assertEquals(5, mockProvider.limit)
            assertEquals(0, mockProvider.offset)
            assertEquals(
                verseIds.map { Verse(id = it, text = "Verse text here..") },
                verses,
            )
        }

    /**
     * Tests retrieving verses within a range
     *
     * Steps:
     * 1. Set up mock provider with test data
     * 2. Create DefaultBibleStoreService with mock provider
     * 3. Retrieve verses within range
     * 4. Verify correct parameters are passed and verses are converted
     */
    @Test
    fun testGetVersesInRange() =
        runTest {
            val mockProvider = MockVerseRepository()
            val service =
                DefaultBibleStoreService(
                    provider =
                        BibleStoreProvider(
                            repository = mockProvider,
                        ),
                )
            val startVerseId = VerseID("1:1:1")
            val endVerseId = VerseID("1:1:10")

            val verses =
                service.getVersesInRange(
                    startVerseID = startVerseId,
                    endVerseID = endVerseId,
                    limit = 10,
                    offset = 5,
                )

            // Verify parameters were correctly passed to provider
            assertNull(mockProvider.searchText)
            assertNull(mockProvider.verseIDs)
            assertEquals(startVerseId.value, mockProvider.startVerse)
            assertEquals(endVerseId.value, mockProvider.endVerse)
            assertEquals(10, mockProvider.limit)
            assertEquals(5, mockProvider.offset)
            assertEquals(
                listOf(startVerseId, endVerseId)
                    .map { Verse(id = it, text = "Verse text here..") },
                verses,
            )
        }

    /**
     * Tests searching verses with text and optional verse IDs
     *
     * Steps:
     * 1. Set up mock provider with test data
     * 2. Create DefaultBibleStoreService with mock provider
     * 3. Search verses with text and verse IDs
     * 4. Verify correct parameters are passed and verses are converted
     */
    @Test
    fun testSearchVerses() =
        runTest {
            val mockProvider = MockVerseRepository()
            val service =
                DefaultBibleStoreService(
                    provider =
                        BibleStoreProvider(
                            repository = mockProvider,
                        ),
                )
            val searchText = "God"
            val verseIds =
                listOf(
                    VerseID("1:1:1"),
                    VerseID("1:1:2"),
                )

            val verses =
                service.searchVerses(
                    text = searchText,
                    verseIDs = verseIds,
                    limit = 15,
                    offset = 10,
                )

            // Verify parameters were correctly passed to provider
            assertEquals(searchText, mockProvider.searchText)
            assertEquals(verseIds.map { it.value }, mockProvider.verseIDs)
            assertNull(mockProvider.startVerse)
            assertNull(mockProvider.endVerse)
            assertEquals(15, mockProvider.limit)
            assertEquals(10, mockProvider.offset)
            assertEquals(
                verseIds.map { Verse(id = it, text = "Verse text here..") },
                verses,
            )
        }

    /**
     * Tests searching verses with text only (no verse IDs)
     *
     * Steps:
     * 1. Set up mock provider with test data
     * 2. Create DefaultBibleStoreService with mock provider
     * 3. Search verses with text only
     * 4. Verify correct parameters are passed and verses are converted
     */
    @Test
    fun testSearchVersesWithoutIds() =
        runTest {
            val mockProvider = MockVerseRepository()
            val service =
                DefaultBibleStoreService(
                    provider =
                        BibleStoreProvider(
                            repository = mockProvider,
                        ),
                )
            val searchText = "God"

            val verses =
                service.searchVerses(
                    text = searchText,
                    verseIDs = null,
                    limit = 20,
                    offset = 15,
                )

            // Verify parameters were correctly passed to provider
            assertEquals(searchText, mockProvider.searchText)
            assertNull(mockProvider.verseIDs)
            assertNull(mockProvider.startVerse)
            assertNull(mockProvider.endVerse)
            assertEquals(20, mockProvider.limit)
            assertEquals(15, mockProvider.offset)
            assertTrue(verses.isEmpty())
        }

    /**
     * Tests searching verses within a range
     *
     * Steps:
     * 1. Set up mock provider with test data
     * 2. Create DefaultBibleStoreService with mock provider
     * 3. Search verses with text within range
     * 4. Verify correct parameters are passed and verses are converted
     */
    @Test
    fun testSearchVersesInRange() =
        runTest {
            val mockProvider = MockVerseRepository()
            val service =
                DefaultBibleStoreService(
                    provider =
                        BibleStoreProvider(
                            repository = mockProvider,
                        ),
                )
            val searchText = "God"
            val startVerseId = VerseID("1:1:1")
            val endVerseId = VerseID("1:1:10")

            val verses =
                service.searchVersesInRange(
                    text = searchText,
                    startVerseID = startVerseId,
                    endVerseID = endVerseId,
                    limit = 25,
                    offset = 20,
                )

            // Verify parameters were correctly passed to provider
            assertEquals(searchText, mockProvider.searchText)
            assertNull(mockProvider.verseIDs)
            assertEquals(startVerseId.value, mockProvider.startVerse)
            assertEquals(endVerseId.value, mockProvider.endVerse)
            assertEquals(25, mockProvider.limit)
            assertEquals(20, mockProvider.offset)
            assertEquals(
                listOf(startVerseId, endVerseId)
                    .map { Verse(id = it, text = "Verse text here..") },
                verses,
            )
        }
}
