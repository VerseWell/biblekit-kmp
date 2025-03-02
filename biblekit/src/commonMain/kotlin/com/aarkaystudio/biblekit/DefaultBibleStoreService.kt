package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekit.model.Verse
import com.aarkaystudio.biblekit.model.VerseID
import com.aarkaystudio.biblekitdb.BibleStoreProvider

/**
 * Default implementation of BibleStoreService that wraps BibleStoreProvider.
 * This class serves as an adapter between the database layer and the domain layer,
 * converting between database entities and domain models.
 *
 * Features:
 * - Converts between VerseID and String representations
 * - Maps database VerseEntity to domain Verse objects
 * - Handles pagination and search operations
 *
 * @property provider The underlying BibleStoreProvider for database access
 */
internal class DefaultBibleStoreService(
    private val provider: BibleStoreProvider,
) : BibleStoreService {
    /**
     * Searches for verses containing specific text, optionally limited to a set of verse IDs.
     *
     * @param text The text to search for within verses
     * @param verseIDs Optional list of verse IDs to limit the search scope
     * @param limit Maximum number of results to return
     * @param offset Number of results to skip for pagination
     * @return List of matching Verse objects
     */
    override suspend fun searchVerses(
        text: String,
        verseIDs: List<VerseID>?,
        limit: Long,
        offset: Long,
    ): List<Verse> =
        provider
            .searchVerses(
                text = text,
                verseIDs = verseIDs?.map { it.value },
                limit = limit,
                offset = offset,
            ).map {
                Verse(
                    id = VerseID(value = it.id),
                    text = it.text,
                )
            }

    /**
     * Searches for verses containing specific text within a range of verse IDs.
     *
     * @param text The text to search for within verses
     * @param startVerseID The starting verse ID of the range
     * @param endVerseID The ending verse ID of the range
     * @param limit Maximum number of results to return
     * @param offset Number of results to skip for pagination
     * @return List of matching Verse objects within the specified range
     */
    override suspend fun searchVersesInRange(
        text: String,
        startVerseID: VerseID,
        endVerseID: VerseID,
        limit: Long,
        offset: Long,
    ): List<Verse> =
        provider
            .searchVersesInRange(
                text = text,
                startVerse = startVerseID.value,
                endVerse = endVerseID.value,
                limit = limit,
                offset = offset,
            ).map {
                Verse(
                    id = VerseID(value = it.id),
                    text = it.text,
                )
            }

    /**
     * Retrieves specific verses by their IDs.
     *
     * @param verseIDs List of verse IDs to retrieve
     * @param limit Maximum number of verses to return
     * @param offset Number of verses to skip for pagination
     * @return List of requested Verse objects
     */
    override suspend fun getVerses(
        verseIDs: List<VerseID>,
        limit: Long,
        offset: Long,
    ): List<Verse> =
        provider
            .getVerses(
                verseIDs = verseIDs.map { it.value },
                limit = limit,
                offset = offset,
            ).map {
                Verse(
                    id = VerseID(value = it.id),
                    text = it.text,
                )
            }

    /**
     * Retrieves verses between two verse IDs (inclusive).
     *
     * @param startVerseID The starting verse ID of the range
     * @param endVerseID The ending verse ID of the range
     * @param limit Maximum number of verses to return
     * @param offset Number of verses to skip for pagination
     * @return List of Verse objects between startVerseID and endVerseID
     */
    override suspend fun getVersesInRange(
        startVerseID: VerseID,
        endVerseID: VerseID,
        limit: Long,
        offset: Long,
    ): List<Verse> =
        provider
            .getVersesInRange(
                startVerse = startVerseID.value,
                endVerse = endVerseID.value,
                limit = limit,
                offset = offset,
            ).map {
                Verse(
                    id = VerseID(value = it.id),
                    text = it.text,
                )
            }
}
