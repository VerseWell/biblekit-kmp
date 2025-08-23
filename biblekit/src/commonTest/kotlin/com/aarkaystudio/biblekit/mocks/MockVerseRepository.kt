package com.aarkaystudio.biblekit.mocks

import com.aarkaystudio.biblekitdb.VerseEntity
import com.aarkaystudio.biblekitdb.VerseRepository

/**
 * Mock implementation of VerseRepository for testing
 * Provides a controlled environment for testing Bible service functionality
 * Contains a fixed set of test verses from Genesis 1:1-2
 */
class MockVerseRepository : VerseRepository {
    var searchText: String? = null
    var verseIDs: List<String>? = null
    var startVerse: String? = null
    var endVerse: String? = null
    var limit: Long? = null
    var offset: Long? = null

    override suspend fun searchVerses(
        text: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        searchText = text
        this.limit = limit
        this.offset = offset
        return emptyList()
    }

    override suspend fun searchVersesByIDs(
        text: String,
        ids: List<String>,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        searchText = text
        verseIDs = ids
        this.limit = limit
        this.offset = offset
        return ids.map { VerseEntity(id = it, text = "Verse text here..") }
    }

    override suspend fun searchVersesInRange(
        text: String,
        startVerse: String,
        endVerse: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        searchText = text
        this.startVerse = startVerse
        this.endVerse = endVerse
        this.limit = limit
        this.offset = offset
        return listOf(startVerse, endVerse)
            .map { VerseEntity(id = it, text = "Verse text here..") }
    }

    override suspend fun getVersesByIds(
        ids: List<String>,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        verseIDs = ids
        this.limit = limit
        this.offset = offset
        return ids
            .map { VerseEntity(id = it, text = "Verse text here..") }
    }

    override suspend fun getVersesInRange(
        startVerse: String,
        endVerse: String,
        limit: Long,
        offset: Long,
    ): List<VerseEntity> {
        this.startVerse = startVerse
        this.endVerse = endVerse
        this.limit = limit
        this.offset = offset
        return listOf(startVerse, endVerse)
            .map { VerseEntity(id = it, text = "Verse text here..") }
    }

    override suspend fun insert(verses: List<VerseEntity>) {
        TODO("Not yet implemented")
    }
}
