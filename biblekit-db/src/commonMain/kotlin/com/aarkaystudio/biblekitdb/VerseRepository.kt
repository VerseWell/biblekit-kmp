package com.aarkaystudio.biblekitdb

/**
 * The Repository for accessing [VerseEntity] objects.
 */
public interface VerseRepository {
    /**
     * Returns a list of [VerseEntity] objects with the given IDs.
     *
     * @param limit The maximum number of results to return.
     * @param offset The offset for paginated results.
     * @return A Flow of [VerseEntity] objects with the matching IDs.
     */
    public suspend fun searchVerses(
        text: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity>

    /**
     * Returns a list of [VerseEntity] objects within the specified verse range that contain the search text.
     *
     * @param text The text to search for within verses
     * @param startVerse The starting verse ID (inclusive)
     * @param endVerse The ending verse ID (inclusive)
     * @param limit The maximum number of results to return
     * @param offset The offset for paginated results
     * @return A list of [VerseEntity] objects matching both the range and search text
     */
    public suspend fun searchVersesInRange(
        text: String,
        startVerse: String,
        endVerse: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity>

    /**
     * Returns a list of [VerseEntity] objects with the given IDs.
     *
     * @param ids The list of IDs to retrieve.
     * @param limit The maximum number of results to return.
     * @param offset The offset for paginated results.
     * @return A Flow of [VerseEntity] objects with the matching IDs.
     */
    public suspend fun searchVersesByIDs(
        text: String,
        ids: List<String>,
        limit: Long,
        offset: Long,
    ): List<VerseEntity>

    /**
     * Returns a list of [VerseEntity] objects with the given IDs.
     *
     * @param ids The list of IDs to retrieve.
     * @param limit The maximum number of results to return.
     * @param offset The offset for paginated results.
     * @return A Flow of [VerseEntity] objects with the matching IDs.
     */
    public suspend fun getVersesByIds(
        ids: List<String>,
        limit: Long,
        offset: Long,
    ): List<VerseEntity>

    /**
     * Inserts a list of [VerseEntity] objects into the database.
     *
     * @param verses The list of [VerseEntity] objects to insert.
     */
    public suspend fun insert(verses: List<VerseEntity>)

    /**
     * Returns a list of [VerseEntity] objects within the specified verse range.
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
    ): List<VerseEntity>
}
