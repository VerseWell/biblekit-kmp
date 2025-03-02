package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekit.model.Verse
import com.aarkaystudio.biblekit.model.VerseID

/**
 * Service interface for accessing and retrieving Bible verses from a data store.
 * This interface defines the core operations needed to search and retrieve verses,
 * supporting both text-based search and direct verse access.
 *
 * Implementations of this interface should:
 * - Handle database connections and queries efficiently
 * - Support pagination for large result sets
 * - Provide thread-safe access to the verse store
 * - Handle errors appropriately
 */
internal interface BibleStoreService {
    /**
     * Searches for verses containing specific text, optionally limited to a set of verse IDs.
     * This method supports full-text search within the Bible database.
     *
     * @param text The text to search for within verses
     * @param verseIDs Optional list of verse IDs to limit the search scope (null for entire Bible)
     * @param limit Maximum number of results to return
     * @param offset Number of results to skip for pagination
     * @return List of [Verse] objects matching the search criteria
     * @throws Exception if there's an error accessing the data store
     */
    public suspend fun searchVerses(
        text: String,
        verseIDs: List<VerseID>?,
        limit: Long,
        offset: Long,
    ): List<Verse>

    /**
     * Searches for verses containing specific text within a range of verse IDs (inclusive).
     * This method provides efficient search within a verse range without requiring the full list of IDs.
     *
     * @param text The text to search for within verses
     * @param startVerseID The starting verse ID of the range (null to start from beginning)
     * @param endVerseID The ending verse ID of the range (null to end at last verse)
     * @param limit Maximum number of results to return
     * @param offset Number of results to skip for pagination
     * @return List of [Verse] objects matching the search criteria within the specified range
     * @throws Exception if there's an error accessing the data store
     */
    public suspend fun searchVersesInRange(
        text: String,
        startVerseID: VerseID,
        endVerseID: VerseID,
        limit: Long,
        offset: Long,
    ): List<Verse>

    /**
     * Retrieves specific verses by their IDs.
     * This method provides direct access to verses without text search.
     *
     * @param verseIDs List of verse IDs to retrieve
     * @param limit Maximum number of verses to return
     * @param offset Number of verses to skip for pagination
     * @return List of requested [Verse] objects in the order specified by verseIDs
     * @throws Exception if there's an error accessing the data store
     */
    public suspend fun getVerses(
        verseIDs: List<VerseID>,
        limit: Long,
        offset: Long,
    ): List<Verse>

    /**
     * Retrieves verses between two verse IDs (inclusive).
     * This method provides efficient access to a range of verses without requiring the full list of IDs.
     *
     * @param startVerseID The starting verse ID of the range
     * @param endVerseID The ending verse ID of the range
     * @param limit Maximum number of verses to return
     * @param offset Number of verses to skip for pagination
     * @return List of [Verse] objects between startVerseID and endVerseID (inclusive)
     * @throws Exception if there's an error accessing the data store
     */
    public suspend fun getVersesInRange(
        startVerseID: VerseID,
        endVerseID: VerseID,
        limit: Long,
        offset: Long,
    ): List<Verse>
}
