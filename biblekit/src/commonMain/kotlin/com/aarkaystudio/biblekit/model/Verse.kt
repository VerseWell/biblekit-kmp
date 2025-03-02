package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookCollection
import com.aarkaystudio.biblekitdb.VerseEntity

/**
 * Represents a single verse in the Bible with its unique identifier and text content.
 * This class provides functionality for verse manipulation, range selection, and text formatting.
 *
 * @property id The unique identifier for the verse in the format book:chapter:verse
 * @property text The actual text content of the verse
 * @property sortKey A numeric key used for sorting verses in their biblical order
 */
public data class Verse internal constructor(
    public val id: VerseID,
    public val text: String,
) {
    internal val sortKey: Long = VerseEntity.sortKey(id = id.value)

    public companion object {
        /**
         * Analyzes a list of verses and groups them into continuous ranges.
         * This is useful for displaying or sharing multiple verses in a compact format.
         *
         * For example, verses 1-3 and 5-7 would be grouped into two separate ranges.
         *
         * @param verses The list of verses to analyze
         * @return A list of [SelectedVerseRange] objects representing continuous verse ranges
         * @throws IllegalArgumentException if verses list is empty or contains verses from multiple books
         */
        public fun selectedVerseRange(verses: List<Verse>): List<SelectedVerseRange> {
            if (verses.isEmpty()) {
                throw IllegalArgumentException("Verses should not be empty.")
            }

            val sortedVerses = verses.sortedBy { it.sortKey }
            val allBooks = sortedVerses.map { it.id.components().first() }.toSet().sorted()

            if (allBooks.size != 1) {
                throw IllegalArgumentException("Multiple books not supported.")
            }

            val bookName = sortedVerses.first().id.bookName()

            val selectedVerses =
                if (sortedVerses.size == 1) {
                    // Handle single verse case
                    val components = sortedVerses.first().id.components()
                    listOf(
                        SelectedVerseRange(
                            startBook = components.first(),
                            endBook = components.first(),
                            startChapter = components.drop(1).first(),
                            endChapter = components.drop(1).first(),
                            startVerse = components.last(),
                            endVerse = components.last(),
                        ),
                    )
                } else {
                    // Handle multiple verses case
                    val selectedVerses = mutableListOf<SelectedVerseRange>()

                    // Get all verse IDs from the book and find our starting point
                    val allVerseIDs =
                        BookCollection.mapping[bookName]!!
                            .allVerseIDs()
                            .dropWhile { verseId ->
                                Verse(id = verseId, text = "").sortKey <
                                    Verse(
                                        id = sortedVerses.first().id,
                                        text = "",
                                    ).sortKey
                            }

                    var selectedVerseIDs = sortedVerses.reversed()

                    var calculatingRange = false
                    var startChapter =
                        selectedVerseIDs
                            .last()
                            .id
                            .components()
                            .drop(1)
                            .first()
                    var endChapter =
                        selectedVerseIDs
                            .last()
                            .id
                            .components()
                            .drop(1)
                            .first()
                    var startIdx =
                        selectedVerseIDs
                            .last()
                            .id
                            .components()
                            .last()
                    var endIdx =
                        selectedVerseIDs
                            .last()
                            .id
                            .components()
                            .last()

                    // Helper function to add a completed range
                    fun addBreak() {
                        selectedVerses.add(
                            SelectedVerseRange(
                                startBook =
                                    sortedVerses
                                        .first()
                                        .id
                                        .components()
                                        .first(),
                                endBook =
                                    sortedVerses
                                        .first()
                                        .id
                                        .components()
                                        .first(),
                                startChapter = startChapter,
                                endChapter = endChapter,
                                startVerse = startIdx,
                                endVerse = endIdx,
                            ),
                        )
                    }

                    // Iterate through all verses to find continuous ranges
                    for (verseId in allVerseIDs) {
                        if (verseId.value == selectedVerseIDs.lastOrNull()?.id?.value) {
                            if (!calculatingRange) {
                                startChapter = verseId.components().drop(1).first()
                                startIdx = verseId.components().last()
                            }
                            calculatingRange = true
                            endChapter = verseId.components().drop(1).first()
                            endIdx = verseId.components().last()
                            selectedVerseIDs = selectedVerseIDs.dropLast(1)
                        } else if (selectedVerseIDs.isEmpty()) {
                            break
                        } else {
                            if (calculatingRange) addBreak()
                            calculatingRange = false
                        }
                    }

                    addBreak()
                    selectedVerses
                }

            return selectedVerses
        }

        /**
         * Formats a list of verses for sharing, adding appropriate chapter and verse numbers.
         * The verses are formatted according to standard Bible citation rules:
         * - First verse in a chapter includes the chapter number
         * - Subsequent verses in the same chapter only show verse number
         * - Chapter numbers are repeated when crossing chapter boundaries
         *
         * @param verses The list of verses to format
         * @return List of formatted verse strings
         * @throws IllegalArgumentException if verses list is empty or contains verses from multiple books
         */
        public fun shareVersesText(verses: List<Verse>): List<String> {
            if (verses.isEmpty()) {
                throw IllegalArgumentException("Verses should not be empty.")
            }

            val sortedVerses = verses.sortedBy { it.sortKey }
            val allBooks = sortedVerses.map { it.id.components().first() }.toSet().sorted()

            if (allBooks.size != 1) {
                throw IllegalArgumentException("Multiple books not supported.")
            }

            val bookName = sortedVerses.first().id.bookName()
            val startBook = BookCollection.mapping[bookName]!!

            var addChapterPrefix = false
            val versesText = mutableListOf<String>()

            var startChapter =
                sortedVerses
                    .first()
                    .id
                    .components()
                    .drop(1)
                    .first()

            if (sortedVerses.size == 1) {
                versesText.add(sortedVerses.first().text)
            } else {
                for (verse in sortedVerses) {
                    val currentChapter =
                        verse.id
                            .components()
                            .drop(1)
                            .first()

                    var prefix = ""

                    // Add chapter number if needed
                    if (addChapterPrefix || startChapter != currentChapter) {
                        prefix = "${verse.id.components().drop(1).first()}:"
                    }

                    versesText.add("[${prefix}${verse.id.components().last()}] ${verse.text}")

                    // Check if we need to add chapter prefix in next iteration
                    if (startBook.totalVerses(chapter = currentChapter) == verse.id.components().last()) {
                        if (startBook.totalChapters == currentChapter) {
                            // Right now the method doesn't support sharing across multiple books
                            // There's nothing to select more in this book.
                        } else {
                            // We should prefix with chapter on next loop.
                            addChapterPrefix = true
                        }
                    } else {
                        addChapterPrefix = false
                    }

                    startChapter = currentChapter
                }
            }

            return versesText
        }

        /**
         * Creates a complete share text including the verse reference and formatted verse text.
         * For example: "Genesis 1:1-3 - [1] In the beginning... [2] And the earth... [3] And God said..."
         *
         * @param verses The list of verses to share
         * @return A formatted string containing the verse reference and text
         * @throws IllegalArgumentException if verses list is empty or contains verses from multiple books
         */
        public fun createShareText(verses: List<Verse>): String {
            val title = SelectedVerseRange.shareTitle(range = selectedVerseRange(verses = verses))
            val verseText = shareVersesText(verses = verses).joinToString(separator = " ")
            return "$title - $verseText"
        }
    }
}
