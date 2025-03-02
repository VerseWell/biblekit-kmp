package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookName

/**
 * Represents a book in the Bible, containing information about its chapters and verses.
 *
 * @property bookName The name of the book.
 * @property verses A list of verse counts for each chapter.
 * @property totalChapters The total number of chapters in the book.
 * @property startVerseID The first verse ID of the book.
 * @property endVerseID The last verse ID of the book.
 */
public data class Book internal constructor(
    public val bookName: BookName,
    public val totalChapters: Int,
    internal val verses: List<Int>,
) {
    /**
     * The first verse ID of the book (1:1:1 format).
     */
    internal val startVerseID: VerseID
        get() = verseID(chapter = 1, verse = 1)

    /**
     * The last verse ID of the book (book:lastChapter:lastVerse format).
     */
    internal val endVerseID: VerseID
        get() = verseID(chapter = totalChapters, verse = totalVerses(totalChapters))

    /**
     * Returns the total number of verses in a given chapter.
     *
     * @param chapter The chapter number.
     * @return The total number of verses in the chapter.
     */
    public fun totalVerses(chapter: Int): Int = verses[chapter - 1]

    /**
     * Generates a list of [VerseID] objects for all verses in this book.
     *
     * @return A list of [VerseID] objects.
     */
    public fun allVerseIDs(): List<VerseID> =
        (1..totalChapters)
            .flatMap { allVerseIDs(it) }

    /**
     * Generates a list of [VerseID] objects for all verses in a specific chapter in this book.
     *
     * @param chapter The chapter number.
     * @return A list of [VerseID] objects.
     */
    public fun allVerseIDs(chapter: Int): List<VerseID> {
        val bookIndex = bookIdx()
        return (1..totalVerses(chapter = chapter))
            .map {
                verseID(book = bookIndex, chapter = chapter, verse = it)
            }
    }

    /**
     * Creates a [VerseID] from the book number, chapter number, and verse number.
     *
     * @param chapter The chapter number within the book.
     * @param verse The verse number within the chapter.
     * @return The corresponding [VerseID].
     */
    internal fun verseID(
        chapter: Int,
        verse: Int,
    ): VerseID = verseID(book = bookIdx(), chapter = chapter, verse = verse)

    /**
     * Returns the index of the book within the [BookName] entries.
     *
     * @return The book index.
     */
    private fun bookIdx(): Int = BookName.entries.indexOfFirst { it == bookName } + 1

    private companion object {
        /**
         * Creates a [VerseID] from the book number, chapter number, and verse number.
         *
         * @param book The book number.
         * @param chapter The chapter number within the book.
         * @param verse The verse number within the chapter.
         * @return The corresponding [VerseID].
         */
        private fun verseID(
            book: Int,
            chapter: Int,
            verse: Int,
        ): VerseID = VerseID(value = "$book:$chapter:$verse")
    }
}
