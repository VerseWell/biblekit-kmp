package com.aarkaystudio.biblekitdb

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for BibleStoreProvider's search operations
 * Tests the functionality of searching verses in the database
 */
class BibleStoreSearchTest {
    /**
     * Tests searching verses without specifying verse IDs
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Search for verses containing specific text across all verses
     * 3. Verify that correct verses are returned
     */
    @Test
    fun testSearchAllVerses() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insertAll(TestData.allVerses)
            val store = BibleStoreProvider(repository = verseDataSource)

            // Search for "waters" which appears in verses 2 and 6
            val verses =
                store
                    .searchVerses(
                        text = "waters",
                        verseIDs = null,
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            assertEquals(listOf("1:1:2", "1:1:6", "19:23:2"), verses)
        }

    /**
     * Tests searching verses within a specific verse range
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Search for verses containing "waters" within a specific verse range
     * 3. Verify that only verses within the range are returned
     */

    @Test
    fun testSearchVerseRange() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insertAll(TestData.allVerses)
            val store = BibleStoreProvider(repository = verseDataSource)

            // Search for "waters" within verses 1:1:1 to 1:1:5
            val verses =
                store
                    .searchVersesInRange(
                        text = "waters",
                        startVerse = "1:1:3",
                        endVerse = "19:23:3",
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            assertEquals(listOf("1:1:6", "19:23:2"), verses)
        }

    /**
     * Tests searching verses containing the word "God" in specified verse IDs
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Search for verses containing "God" within specific verse IDs
     * 3. Verify that correct verses are returned
     */
    @Test
    fun testSearchSpecificVerses() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insertAll(TestData.genesisVerses)
            val store = BibleStoreProvider(repository = verseDataSource)
            val ids =
                listOf(
                    "1:1:4",
                    "1:1:5",
                    "1:1:6",
                )

            val verses =
                store
                    .searchVerses(
                        text = "God",
                        verseIDs = ids,
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            assertEquals(ids, verses)
        }

    /**
     * Tests searching verses with a non-existent word
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Search for verses containing "Inni" (non-existent word)
     * 3. Verify that no verses are returned
     */
    @Test
    fun testSearchNonExistentWord() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insertAll(TestData.genesisVerses)
            val store = BibleStoreProvider(repository = verseDataSource)
            val ids =
                listOf(
                    "1:1:1",
                    "1:1:2",
                    "1:1:3",
                    "1:1:4",
                )

            val verses =
                store
                    .searchVerses(
                        text = "Inni",
                        verseIDs = ids,
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            assertEquals(emptyList(), verses)
        }

    /**
     * Tests case sensitivity in verse search
     *
     * Steps:
     * 1. Set up test database and insert mock data
     * 2. Search for verses with different case variations
     * 3. Verify that search is case-insensitive
     */
    @Test
    fun testSearchCaseSensitivity() =
        runTest {
            val db = BibleDatabase(testDbConnection())
            val verseDataSource = VerseDataSource(db = db)
            verseDataSource.insertAll(TestData.genesisVerses)
            val store = BibleStoreProvider(repository = verseDataSource)
            val ids = listOf("1:1:1", "1:1:2", "1:1:3")

            // Search with different case variations
            val upperCaseResults =
                store
                    .searchVerses(
                        text = "GOD",
                        verseIDs = ids,
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            val lowerCaseResults =
                store
                    .searchVerses(
                        text = "god",
                        verseIDs = ids,
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            val mixedCaseResults =
                store
                    .searchVerses(
                        text = "GoD",
                        verseIDs = ids,
                        limit = Long.MAX_VALUE,
                        offset = Long.MIN_VALUE,
                    ).map { it.id }

            assertEquals(upperCaseResults, ids)
            assertEquals(mixedCaseResults, ids)
            assertEquals(lowerCaseResults, ids)
        }
}
