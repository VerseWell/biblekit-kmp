package com.aarkaystudio.biblekit.mocks

import com.aarkaystudio.biblekit.BibleStoreService
import com.aarkaystudio.biblekit.model.Verse
import com.aarkaystudio.biblekit.model.VerseID

/**
 * Mock implementation of BibleStoreService for testing
 * Provides a controlled environment for testing Bible functionality
 */
class MockBibleStoreProvider : BibleStoreService {
    var searchText: String? = null
    var startVerseID: VerseID? = null
    var endVerseID: VerseID? = null
    var verseIDs: List<VerseID>? = null
    var limit: Long? = null
    var offset: Long? = null

    override suspend fun searchVerses(
        text: String,
        verseIDs: List<VerseID>?,
        limit: Long,
        offset: Long,
    ): List<Verse> {
        this.searchText = text
        this.verseIDs = verseIDs
        this.limit = limit
        this.offset = offset
        return emptyList()
    }

    override suspend fun searchVersesInRange(
        text: String,
        startVerseID: VerseID,
        endVerseID: VerseID,
        limit: Long,
        offset: Long,
    ): List<Verse> {
        this.searchText = text
        this.startVerseID = startVerseID
        this.endVerseID = endVerseID
        this.limit = limit
        this.offset = offset
        return emptyList()
    }

    override suspend fun getVerses(
        verseIDs: List<VerseID>,
        limit: Long,
        offset: Long,
    ): List<Verse> {
        this.verseIDs = verseIDs
        this.limit = limit
        this.offset = offset
        return emptyList()
    }

    override suspend fun getVersesInRange(
        startVerseID: VerseID,
        endVerseID: VerseID,
        limit: Long,
        offset: Long,
    ): List<Verse> {
        this.startVerseID = startVerseID
        this.endVerseID = endVerseID
        this.limit = limit
        this.offset = offset
        return emptyList()
    }
}
