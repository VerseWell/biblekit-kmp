package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookCollection
import com.aarkaystudio.biblekit.data.BookName

/**
 * Represents a reference to a specific chapter within a book.
 *
 * @property bookName The name of the book.
 * @property index The chapter number within the book.
 * @property startVerseID The first verse ID of the chapter.
 * @property endVerseID The last verse ID of the chapter.
 */
public data class ChapterReference(
    public val bookName: BookName,
    internal val index: Int,
) {
    /**
     * The first verse ID of the chapter.
     */
    internal val startVerseID: VerseID
        get() = verseID(verse = 1)

    /**
     * The last verse ID of the chapter.
     */
    internal val endVerseID: VerseID
        get() = verseID(verse = totalVerses())

    /**
     * Retrieves the [Book] object associated with this chapter reference.
     *
     * @return The corresponding [Book] object.
     */
    public fun book(): Book = BookCollection.mapping[bookName]!!

    /**
     * Gets the total number of verses in this chapter.
     *
     * @return The total number of verses.
     */
    internal fun totalVerses(): Int = book().totalVerses(chapter = index)

    /**
     * Generates a list of [VerseID] objects for all verses in this chapter.
     *
     * @return A list of [VerseID] objects.
     */
    internal fun allVerseIDs(): List<VerseID> = book().allVerseIDs(chapter = index)

    /**
     * Creates a [VerseID] from the verse number.
     *
     * @param verse The verse number within the chapter.
     * @return The corresponding [VerseID].
     */
    internal fun verseID(verse: Int): VerseID = book().verseID(chapter = index, verse = verse)
}
