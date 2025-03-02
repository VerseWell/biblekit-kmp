package com.aarkaystudio.biblekitdb

/**
 * Provides access to Bible verse data using a [VerseRepository].
 *
 * @property repository The [VerseRepository] for data access.
 */
public class BibleStoreProvider(
    private val repository: VerseRepository,
) {
    public companion object {
        /**
         * Creates a new instance of [BibleStoreProvider].
         *
         * Initializes the database and retrieves the [VerseRepository].
         *
         * @param db The [BibleDatabase] instance.
         * @return A new [BibleStoreProvider] instance.
         */
        public fun create(db: BibleDatabase): BibleStoreProvider {
            val repository = VerseDataSource(db = db)
            return BibleStoreProvider(repository = repository)
        }
    }

    /**
     * Searches for verses containing the specified text within a verse range.
     *
     * @param text The text to search for within verses
     * @param startVerse The starting verse ID (inclusive)
     * @param endVerse The ending verse ID (inclusive)
     * @param limit The maximum number of results to return
     * @param offset The offset for paginated results
     * @return A list of [VerseEntity] objects matching the search criteria within the range
     */
    public suspend fun searchVersesInRange(
        text: String,
        startVerse: String,
        endVerse: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> =
        repository.searchVersesInRange(
            text = text,
            startVerse = startVerse,
            endVerse = endVerse,
            limit = limit,
            offset = offset,
        )

    /**
     * Retrieves Bible verses based on the provided criteria.
     *
     * @param text The text to search for within verses.
     * @param verseIDs A list of verse IDs to retrieve (optional).
     * @param limit The maximum number of results to return.
     * @param offset The offset for paginated results.
     * @return A list of [VerseEntity] objects.
     */
    public suspend fun searchVerses(
        text: String,
        verseIDs: List<String>?,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> =
        if (verseIDs != null) {
            repository.searchVersesByIDs(
                text = text,
                ids = verseIDs,
                limit = limit,
                offset = offset,
            )
        } else {
            repository.searchVerses(
                text = text,
                limit = limit,
                offset = offset,
            )
        }

    /**
     * Retrieves Bible verses within a specified range.
     *
     * @param startVerse The starting verse ID (inclusive)
     * @param endVerse The ending verse ID (inclusive)
     * @param limit The maximum number of results to return
     * @param offset The offset for paginated results
     * @return A list of [VerseEntity] objects within the specified range
     */
    public suspend fun getVersesInRange(
        startVerse: String,
        endVerse: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> =
        repository.getVersesInRange(
            startVerse = startVerse,
            endVerse = endVerse,
            limit = limit,
            offset = offset,
        )

    /**
     * Retrieves Bible verses based on the provided criteria.
     *
     * @param verseIDs A list of verse IDs to retrieve.
     * @param limit The maximum number of results to return.
     * @param offset The offset for paginated results.
     * @return A list of [VerseEntity] objects.
     */
    public suspend fun getVerses(
        verseIDs: List<String>,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> =
        repository.getVersesByIds(
            ids = verseIDs,
            limit = limit,
            offset = offset,
        )
}
