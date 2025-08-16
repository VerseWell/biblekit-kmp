package com.aarkaystudio.biblekitdb

/**
 * A data source class that implements the [VerseRepository] interface using SQLDelight.
 *
 * This class provides methods for retrieving and inserting Bible verses from a database.
 * It uses SQLDelight for type-safe database access and query execution.
 *
 * @property db The SQLDelight [BibleDatabase] instance used to access the database.
 */
internal class VerseDataSource(
    private val db: BibleDatabase,
) : VerseRepository {
    /**
     * The [DatabaseQueries] object used to execute SQL queries.
     * This is initialized from the database instance for efficient query execution.
     */
    private val queries = db.databaseQueries

    /**
     * Searches for verses containing the specified text.
     *
     * @param text The text to search for within verses
     * @param limit The maximum number of results to return
     * @param offset The offset for paginated results
     * @return A list of [VerseEntity] objects matching the search criteria
     */
    override suspend fun searchVerses(
        text: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        // Execute the search query and map results to VerseEntity objects
        val query =
            queries.getAllVersesByText(
                searchText = text,
                limit = limit,
                offset = offset,
            )

        return query.asyncExecuteAsList().map {
            VerseEntity(id = it.number, text = it.text)
        }
    }

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
    override suspend fun searchVersesInRange(
        text: String,
        startVerse: String,
        endVerse: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        // Execute the search query with ID range and text, then map results
        val query =
            queries.getAllVersesInRangeAndText(
                id = VerseEntity(id = startVerse, text = "").key,
                id_ = VerseEntity(id = endVerse, text = "").key,
                searchText = text,
                limit = limit,
                offset = offset,
            )

        return query.asyncExecuteAsList().map {
            VerseEntity(id = it.number, text = it.text)
        }
    }

    /**
     * Searches for verses with specific IDs containing the specified text.
     *
     * @param text The text to search for within verses
     * @param ids A list of verse IDs to search within
     * @param limit The maximum number of results to return
     * @param offset The offset for paginated results
     * @return A list of [VerseEntity] objects matching both the IDs and search text
     */
    override suspend fun searchVersesByIDs(
        text: String,
        ids: List<String>,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        // Execute the search query with ID range and text, then map results
        val query =
            queries.getAllVersesByIDsAndText(
                ids.map { VerseEntity(id = it, text = "").key },
                searchText = text,
                limit = limit,
                offset = offset,
            )

        return query.asyncExecuteAsList().map {
            VerseEntity(id = it.number, text = it.text)
        }
    }

    /**
     * Retrieves verses by their IDs without text search.
     *
     * @param ids A list of verse IDs to retrieve
     * @param limit The maximum number of results to return
     * @param offset The offset for paginated results
     * @return A list of [VerseEntity] objects matching the provided IDs
     */
    override suspend fun getVersesByIds(
        ids: List<String>,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        // Execute the query to get verses by ID range and map results
        val query =
            queries.getAllVersesByIDs(
                ids.map { VerseEntity(id = it, text = "").key },
                limit = limit,
                offset = offset,
            )

        return query.asyncExecuteAsList().map {
            VerseEntity(id = it.number, text = it.text)
        }
    }

    /**
     * Returns a list of [VerseEntity] objects within the specified verse range.
     *
     * @param startVerse The starting verse ID (inclusive)
     * @param endVerse The ending verse ID (inclusive)
     * @param limit The maximum number of results to return
     * @param offset The offset for paginated results
     * @return A list of [VerseEntity] objects within the specified range
     */
    override suspend fun getVersesInRange(
        startVerse: String,
        endVerse: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        // Execute the query to get verses by ID range and map results
        val query =
            queries.getAllVersesInRange(
                id = VerseEntity(id = startVerse, text = "").key,
                id_ = VerseEntity(id = endVerse, text = "").key,
                limit = limit,
                offset = offset,
            )

        return query.asyncExecuteAsList().map {
            VerseEntity(id = it.number, text = it.text)
        }
    }

    /**
     * Inserts a list of verses into the database.
     * Each verse is inserted with its ID, text, and searchable text content.
     *
     * @param verses The list of [VerseEntity] objects to insert
     */
    override suspend fun insert(verses: List<VerseEntity>) {
        // Insert each verse with its key, ID, text, and searchable content
        verses.forEach {
            queries.insertVerse(
                id = it.key,
                number = it.id,
                text = it.text,
                searchText = it.text,
            )
        }
    }
}
