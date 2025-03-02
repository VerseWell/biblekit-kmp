package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekit.model.Book
import com.aarkaystudio.biblekit.model.ChapterReference
import com.aarkaystudio.biblekit.model.Reference
import com.aarkaystudio.biblekit.model.Verse
import com.aarkaystudio.biblekit.model.VerseID
import com.aarkaystudio.biblekitdb.BibleStoreProvider

/**
 * Main provider class for accessing and searching Bible content.
 * This class serves as the primary interface for retrieving verses and performing searches
 * within the Bible database.
 *
 * Features:
 * - Full-text search with different search types (word, substring, prefix, suffix)
 * - Verse retrieval by chapter, book, or specific verse IDs
 * - Support for paginated results
 * - Reference-based search scoping
 *
 * @property store The [BibleStoreService] implementation used to access Bible data
 */
public class BibleProvider internal constructor(
    private val store: BibleStoreService,
) {
    public companion object {
        /**
         * Creates a new instance of [BibleProvider] with a default [BibleStoreService] implementation.
         * This factory method sets up the database connection and wraps it with the appropriate service layer.
         *
         * @param dbFactory The [BibleDatabaseFactory] instance to use for data access
         * @return A new [BibleProvider] instance ready for use
         */
        public fun create(dbFactory: BibleDatabaseFactory): BibleProvider {
            val provider = BibleStoreProvider.create(db = dbFactory.create())
            return BibleProvider(store = DefaultBibleStoreService(provider = provider))
        }
    }

    /**
     * Performs a text search across Bible verses with optional verse ID filtering.
     * The search can be customized using different search types and supports pagination.
     * If verseIDs is provided, the search will be limited to only those verses.
     *
     * @param query The text to search for within verses
     * @param verseIDs Optional list of specific verse IDs to search within, if null searches all verses
     * @param limit Maximum number of results to return (default: [Long.MAX_VALUE])
     * @param offset Number of results to skip for pagination (default: [Long.MIN_VALUE])
     * @return List of [Verse] objects matching the search criteria
     * @throws Exception if there's an error accessing the database
     */
    @Throws(Exception::class)
    public suspend fun search(
        query: String,
        verseIDs: List<VerseID>?,
        limit: Long = Long.MAX_VALUE,
        offset: Long = Long.MIN_VALUE,
    ): List<Verse> {
        // Return an empty list if the query is empty or contains only whitespace
        if (query.trim().isEmpty()) {
            return emptyList()
        }

        return store.searchVerses(
            text = query,
            verseIDs = verseIDs,
            limit = limit,
            offset = offset,
        )
    }

    /**
     * Performs a text search within a specific range of verses defined by start and end verse IDs.
     * This is a more direct way to search within a specific range compared to using Reference.
     *
     * @param query The text to search for within verses
     * @param reference The [Reference] defining the range to search within
     * @param limit Maximum number of results to return (default: [Long.MAX_VALUE])
     * @param offset Number of results to skip for pagination (default: [Long.MIN_VALUE])
     * @return List of [Verse] objects matching the search criteria within the specified range
     * @throws Exception if there's an error accessing the database
     */
    @Throws(Exception::class)
    public suspend fun search(
        query: String,
        reference: Reference,
        limit: Long = Long.MAX_VALUE,
        offset: Long = Long.MIN_VALUE,
    ): List<Verse> {
        // Return an empty list if the query is empty or contains only whitespace
        if (query.trim().isEmpty()) {
            return emptyList()
        }

        // Ensure the verse range is in the correct order (from <= to)
        val fixedReference = reference.fixup()

        return store.searchVersesInRange(
            text = query,
            startVerseID = fixedReference.from.verseID(),
            endVerseID = fixedReference.to.verseID(),
            limit = limit,
            offset = offset,
        )
    }

    /**
     * Retrieves all verses from a specific chapter.
     *
     * @param chapter The [ChapterReference] specifying which chapter to retrieve
     * @param limit Maximum number of verses to return (default: [Long.MAX_VALUE])
     * @param offset Number of verses to skip (default: [Long.MIN_VALUE])
     * @return List of [Verse] objects from the specified chapter
     * @throws Exception if there's an error accessing the database
     */
    @Throws(Exception::class)
    public suspend fun chapter(
        chapter: ChapterReference,
        limit: Long = Long.MAX_VALUE,
        offset: Long = Long.MIN_VALUE,
    ): List<Verse> =
        store.getVersesInRange(
            startVerseID = chapter.startVerseID,
            endVerseID = chapter.endVerseID,
            limit = limit,
            offset = offset,
        )

    /**
     * Retrieves all verses from a specific book of the Bible.
     *
     * @param book The [Book] to retrieve verses from
     * @param limit Maximum number of verses to return (default: [Long.MAX_VALUE])
     * @param offset Number of verses to skip (default: [Long.MIN_VALUE])
     * @return List of [Verse] objects from the specified book
     * @throws Exception if there's an error accessing the database
     */
    @Throws(Exception::class)
    public suspend fun book(
        book: Book,
        limit: Long = Long.MAX_VALUE,
        offset: Long = Long.MIN_VALUE,
    ): List<Verse> =
        store.getVersesInRange(
            startVerseID = book.startVerseID,
            endVerseID = book.endVerseID,
            limit = limit,
            offset = offset,
        )

    /**
     * Retrieves specific verses by their IDs.
     *
     * @param ids List of [VerseID] objects identifying the verses to retrieve
     * @param limit Maximum number of verses to return (default: [Long.MAX_VALUE])
     * @param offset Number of verses to skip (default: [Long.MIN_VALUE])
     * @return List of requested [Verse] objects
     * @throws Exception if there's an error accessing the database
     */
    @Throws(Exception::class)
    public suspend fun verses(
        ids: List<VerseID>,
        limit: Long = Long.MAX_VALUE,
        offset: Long = Long.MIN_VALUE,
    ): List<Verse> =
        store.getVerses(
            verseIDs = ids,
            limit = limit,
            offset = offset,
        )
}
