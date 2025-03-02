package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookName
import kotlin.text.split
import kotlin.text.toIntOrNull

/**
 * Represents the unique identifier for a verse in the Bible.
 * The ID follows the format "book:chapter:verse" where:
 * - book is a number from 1-66 representing the book's position in the Bible
 * - chapter is the chapter number within that book
 * - verse is the verse number within that chapter
 *
 * For example, "1:1:1" represents Genesis 1:1 (first book, first chapter, first verse)
 *
 * @property value The string representation of the verse ID in the format "book:chapter:verse"
 */
public data class VerseID(
    public val value: String,
) {
    public companion object {
        /**
         * The [VerseID] of the first verse in the Bible (Genesis 1:1).
         */
        public val start: VerseID = VerseID(value = "1:1:1")

        /**
         * The [VerseID] of the last verse in the Bible (Revelation 22:21).
         */
        public val end: VerseID = VerseID(value = "66:22:21")
    }

    /**
     * Returns a human-readable description of the verse ID, including the book name and chapter:verse.
     * For example, "Genesis 1:1" for verse ID "1:1:1".
     *
     * @return The formatted string in the format "BookName chapter:verse"
     */
    public fun bookChapterVerse(): String = "${bookName().value} ${chapterVerse()}"

    /**
     * Gets the [BookName] enum value for this verse's book.
     * The book number (first component) is used to index into the [BookName] entries.
     *
     * @return The [BookName] representing this verse's book
     */
    public fun bookName(): BookName = BookName.entries[components().first() - 1]

    /**
     * Returns the chapter and verse numbers as a colon-separated string.
     * For example, "1:1" for verse ID "1:1:1".
     *
     * @return The chapter:verse portion of the verse reference
     */
    public fun chapterVerse(): String = chapterVerseNumbers().joinToString(separator = ":")

    /**
     * Returns a list containing just the chapter and verse numbers.
     * For example, [1, 1] for verse ID "1:1:1".
     *
     * @return List containing [chapter, verse] numbers
     */
    internal fun chapterVerseNumbers(): List<Int> = components().drop(n = 1)

    /**
     * Splits the verse ID string into its components (book, chapter, verse) and returns them as integers.
     * For example, "1:1:1" becomes [1, 1, 1].
     *
     * @return A list of integers [book, chapter, verse]
     * @throws IllegalArgumentException if any component cannot be parsed as an integer
     */
    internal fun components(): List<Int> = value.split(":").mapNotNull { it.toIntOrNull() }
}
