package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookCollection
import com.aarkaystudio.biblekit.data.BookName

/**
 * Represents a reference to a specific verse within a chapter of a book.
 *
 * @property chapter The chapter reference.
 * @property index The verse number within the chapter.
 */
public data class VerseReference(
    public val chapter: ChapterReference,
    internal val index: Int,
) {
    /**
     * Creates a [VerseID] object from this verse reference.
     *
     * @return The corresponding [VerseID].
     */
    public fun verseID(): VerseID = chapter.verseID(verse = index)

    /**
     * Compares this [VerseReference] to another [VerseReference] for order.
     *
     * The comparison is performed in the following order:
     * 1. Book name
     * 2. Chapter index
     * 3. Verse index
     *
     * @param to The other [VerseReference] to compare to.
     * @return A negative integer if this [VerseReference] is less than [to],
     * zero if they are equal, or a positive integer if this [VerseReference]
     * is greater than [to].
     */
    public operator fun compareTo(to: VerseReference): Int {
        // 1. Compare book names
        val bookComparison = chapter.bookName.compareTo(to.chapter.bookName)
        // If book names are different, return the comparison result
        if (bookComparison != 0) {
            return bookComparison
        }

        // 2. If book names are the same, compare chapter indices
        val chapterComparison = chapter.index.compareTo(to.chapter.index)
        // If chapter indices are different, return the comparison result
        if (chapterComparison != 0) {
            return chapterComparison
        }

        // 3. If both book names and chapter indices are the same, compare verse indices
        return index.compareTo(to.index)
    }

    public companion object {
        /**
         * Creates a [VerseReference] from a [VerseID] string.
         *
         * @param value The [VerseID] string.
         * @return A [VerseReference] if the input is valid, otherwise null.
         */
        public fun fromVerseID(value: String): VerseReference? {
            val verseID = VerseID(value = value)
            // Get the components (book, chapter, verse) from the VerseID
            val components = verseID.components()
            // Check if the VerseID has the correct number of components
            if (components.size != 3) {
                return null
            }

            // Extract book index and validate it
            val bookIdx = components[0]
            if (bookIdx !in 1..BookName.entries.size) {
                return null
            }

            // Get book name and book object from the mapping
            val bookName = BookName.entries[bookIdx - 1]
            val book = BookCollection.mapping[bookName]!!

            // Extract chapter index and validate it
            val chapterIdx = components[1]
            if (chapterIdx !in 1..book.totalChapters) {
                return null
            }

            // Extract verse index and validate it
            val verseIdx = components[2]
            if (verseIdx !in 1..book.totalVerses(chapter = chapterIdx)) {
                return null
            }

            return VerseReference(
                chapter =
                    ChapterReference(
                        bookName = bookName,
                        index = chapterIdx,
                    ),
                index = verseIdx,
            )
        }
    }
}
