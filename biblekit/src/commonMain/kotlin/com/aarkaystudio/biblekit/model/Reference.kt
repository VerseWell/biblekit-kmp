package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookCollection
import com.aarkaystudio.biblekit.data.BookName

/**
 * Represents a range of verses in a Bible reference, from a starting verse to an ending verse.
 *
 * @property from The starting verse reference.
 * @property to The ending verse reference.
 */
public data class Reference(
    public val from: VerseReference,
    public val to: VerseReference,
) {
    /**
     * Ensures that the 'from' verse reference is less than or equal to the 'to' verse reference.
     *
     * If 'from' is greater than 'to', the references are swapped to maintain the correct order.
     *
     * @return A new [Reference] object with the 'from' and 'to' references in the correct order.
     */
    public fun fixup(): Reference =
        if (from > to) {
            Reference(from = to, to = from)
        } else {
            Reference(from = from, to = to)
        }

    /**
     * Generates a list of [VerseID] objects representing the individual verses within the reference range.
     *
     * @return A list of [VerseID] objects.
     */
    internal fun verseIDs(): List<VerseID> {
        val startBookIdx = BookName.entries.indexOf(from.chapter.bookName)
        val endBookIdx = BookName.entries.indexOf(to.chapter.bookName)

        if (from.chapter.bookName == to.chapter.bookName) {
            if (from.chapter.index == to.chapter.index) {
                return buildList {
                    // Add all verses from the starting verse to the ending verse of the chapter
                    for (verseIdx in from.index..to.index) {
                        add(from.chapter.verseID(verse = verseIdx))
                    }
                }
            } else {
                return buildList {
                    val bookName = from.chapter.bookName
                    val startChapterIdx = from.chapter.index
                    val endChapterIdx = to.chapter.index
                    for (chapterIdx in startChapterIdx..endChapterIdx) {
                        val chapterRef =
                            ChapterReference(
                                bookName = from.chapter.bookName,
                                index = chapterIdx,
                            )
                        when (chapterIdx) {
                            startChapterIdx -> {
                                // If it's the starting chapter, add verses from the starting verse to the end of the chapter
                                addAll(
                                    createWithBook(
                                        bookName = bookName,
                                        fromChapter = chapterIdx,
                                        toChapter = chapterIdx,
                                        fromVerse = from.index,
                                        toVerse = chapterRef.totalVerses(),
                                    ).verseIDs(),
                                )
                            }
                            endChapterIdx -> {
                                // If it's the ending chapter, add verses from the beginning of the chapter to the ending verse
                                addAll(
                                    createWithBook(
                                        bookName = bookName,
                                        fromChapter = chapterIdx,
                                        toChapter = chapterIdx,
                                        fromVerse = 1,
                                        toVerse = to.index,
                                    ).verseIDs(),
                                )
                            }
                            else -> {
                                // For other chapters, add all verses from the beginning to the end of the chapter
                                addAll(chapterRef.allVerseIDs())
                            }
                        }
                    }
                }
            }
        } else {
            return buildList {
                for (bookIdx in startBookIdx..endBookIdx) {
                    val bookName = BookName.entries[bookIdx]
                    when (bookIdx) {
                        startBookIdx -> {
                            val book = from.chapter.book()
                            addAll(
                                createWithBook(
                                    bookName = bookName,
                                    fromChapter = from.chapter.index,
                                    toChapter = book.totalChapters,
                                    fromVerse = from.index,
                                    toVerse = book.totalVerses(chapter = book.totalChapters),
                                ).verseIDs(),
                            )
                        }

                        endBookIdx -> {
                            // If it's the ending book, add verses from the beginning of the ending chapter to the ending verse
                            addAll(
                                createWithBook(
                                    bookName = bookName,
                                    fromChapter = 1,
                                    toChapter = to.chapter.index,
                                    fromVerse = 1,
                                    toVerse = to.index,
                                ).verseIDs(),
                            )
                        }

                        else -> {
                            // For other books, add all verses from the book using the mapping
                            val book = BookCollection.mapping[bookName]!!
                            addAll(book.allVerseIDs())
                        }
                    }
                }
            }
        }
    }

    public companion object {
        /**
         * Creates a [Reference] object for a specific range of verses within a book and chapter.
         *
         * @param bookName The name of the book.
         * @param fromChapter The starting chapter number.
         * @param toChapter The ending chapter number.
         * @param fromVerse The starting verse number.
         * @param toVerse The ending verse number.
         * @return A [Reference] object representing the specified verse range.
         */
        public fun createWithBook(
            bookName: BookName,
            fromChapter: Int,
            toChapter: Int,
            fromVerse: Int,
            toVerse: Int,
        ): Reference =
            Reference(
                from =
                    VerseReference(
                        chapter =
                            ChapterReference(
                                bookName = bookName,
                                index = fromChapter,
                            ),
                        index = fromVerse,
                    ),
                to =
                    VerseReference(
                        chapter =
                            ChapterReference(
                                bookName = bookName,
                                index = toChapter,
                            ),
                        index = toVerse,
                    ),
            )
    }
}
