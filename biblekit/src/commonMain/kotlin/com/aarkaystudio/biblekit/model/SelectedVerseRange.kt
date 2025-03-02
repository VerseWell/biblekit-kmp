package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookCollection

/**
 * Represents a range of selected verses in the Bible.
 *
 * @property startBook The book number where the selection starts
 * @property endBook The book number where the selection ends
 * @property startChapter The chapter number where the selection starts
 * @property endChapter The chapter number where the selection ends
 * @property startVerse The verse number where the selection starts
 * @property endVerse The verse number where the selection ends
 */
public data class SelectedVerseRange internal constructor(
    private val startBook: Int,
    private val endBook: Int,
    private val startChapter: Int,
    private val endChapter: Int,
    private val startVerse: Int,
    private val endVerse: Int,
) {
    public companion object {
        /**
         * Creates a formatted string representation of the verse range for sharing.
         *
         * @return A string in the format "BookName chapter:verse-verse" or "BookName chapter:verse-chapter:verse"
         */
        public fun shareTitle(range: List<SelectedVerseRange>): String {
            val bookName = BookCollection.allBooks[range.first().startBook - 1].bookName.value

            return "$bookName ${range.first().startChapter}:" +
                range
                    .fold(
                        Triple(emptyList<String>(), range.first().startChapter, range.first().startBook),
                    ) { (acc, prevChapter, prevBook), next ->
                        val text =
                            when {
                                next.startBook == next.endBook -> {
                                    when {
                                        next.startChapter == next.endChapter -> {
                                            var prefix = ""
                                            if (next.startChapter != prevChapter) {
                                                prefix = "${next.startChapter}:"
                                            }
                                            if (next.startVerse == next.endVerse) {
                                                "$prefix${next.startVerse}"
                                            } else {
                                                "$prefix${next.startVerse}-${next.endVerse}"
                                            }
                                        }
                                        else -> "${next.startVerse}-${next.endChapter}:${next.endVerse}"
                                    }
                                }
                                else -> error("Support multiple books.")
                            }

                        Triple(acc + text, next.endChapter, next.endBook)
                    }.first
                    .joinToString(",")
        }
    }
}
