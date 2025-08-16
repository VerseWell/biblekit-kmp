package com.aarkaystudio.biblekitdb

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for BibleStoreProvider's get operations
 * Tests the functionality of retrieving verses from the database
 */
class BibleStoreGetTest {
    /**
     * Tests retrieving verses by their IDs
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Retrieve specific verses by their IDs
     * 3. Verify that correct verses are returned in the correct order
     */
    @Test
    fun testGetVersesByIds() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insert(verses = TestData.allVerses)
            val store = BibleStoreProvider(repository = verseDataSource)
            val ids =
                listOf(
                    "1:1:2",
                    "1:1:4",
                    "1:1:6",
                )

            val verses =
                store
                    .getVerses(
                        verseIDs = ids,
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            assertEquals(ids, verses)
        }

    /**
     * Tests pagination in verse retrieval
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Retrieve verses with different limit and offset values
     * 3. Verify that correct subsets of verses are returned
     */
    @Test
    fun testPagination() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insert(verses = TestData.allVerses)
            val store = BibleStoreProvider(repository = verseDataSource)
            val ids =
                listOf(
                    "1:1:1",
                    "1:1:2",
                    "1:1:3",
                    "1:1:4",
                    "1:1:5",
                    "1:1:6",
                )

            // Test with limit
            val limitedVerses =
                store
                    .getVerses(
                        verseIDs = ids,
                        limit = 3,
                        offset = 0,
                    ).map { it.id }
            assertEquals(ids.take(3), limitedVerses)

            // Test with offset
            val offsetVerses =
                store
                    .getVerses(
                        verseIDs = ids,
                        limit = Long.MAX_VALUE,
                        offset = 3,
                    ).map { it.id }
            assertEquals(ids.drop(3), offsetVerses)

            // Test with both limit and offset
            val paginatedVerses =
                store
                    .getVerses(
                        verseIDs = ids,
                        limit = 2,
                        offset = 2,
                    ).map { it.id }
            assertEquals(ids.drop(2).take(2), paginatedVerses)
        }

    /**
     * Tests retrieving verses within a specified range
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Retrieve verses between two verse IDs across different books
     * 3. Verify that correct verses are returned in sequential order
     */
    @Test
    fun testGetVersesInRange() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insert(verses = TestData.allVerses)
            val store = BibleStoreProvider(repository = verseDataSource)

            val verses =
                store.getVersesInRange(
                    startVerse = "1:1:2",
                    endVerse = "43:3:19",
                    limit = Long.MAX_VALUE,
                    offset = Long.MIN_VALUE,
                )

            assertEquals(TestData.allVerses.drop(1).dropLast(2), verses)
        }

    /**
     * Tests pagination when retrieving verses within a range across different books
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Retrieve verses with different limit and offset values
     * 3. Verify that correct subsets of verses are returned
     */
    @Test
    fun testGetVersesInRangePagination() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insert(verses = TestData.allVerses)
            val store = BibleStoreProvider(repository = verseDataSource)

            // Test with limit
            val limitedVerses =
                store
                    .getVersesInRange(
                        startVerse = "1:1:1",
                        endVerse = "43:3:19",
                        limit = 3,
                        offset = 0,
                    ).map { it.id }
            assertEquals(
                listOf("1:1:1", "1:1:2", "1:1:3"),
                limitedVerses,
            )

            // Test with offset to get verses from John
            val offsetVerses =
                store
                    .getVersesInRange(
                        startVerse = "43:3:16",
                        endVerse = "43:3:19",
                        limit = Long.MAX_VALUE,
                        offset = 0,
                    ).map { it.id }
            assertEquals(
                listOf("43:3:16", "43:3:17", "43:3:18", "43:3:19"),
                offsetVerses,
            )

            // Test with both limit and offset in John
            val paginatedVerses =
                store
                    .getVersesInRange(
                        startVerse = "43:3:16",
                        endVerse = "43:3:19",
                        limit = 2,
                        offset = 1,
                    ).map { it.id }
            assertEquals(
                listOf("43:3:17", "43:3:18"),
                paginatedVerses,
            )
        }
}
