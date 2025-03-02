package com.aarkaystudio.biblekit.model

import com.aarkaystudio.biblekit.data.BookCollection
import com.aarkaystudio.biblekit.data.BookName
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for Reference model
 * Tests verse reference ranges and verse ID generation
 * Covers various scenarios from single chapter to entire Bible
 */
class ReferenceTest {
    /**
     * Tests verse ID generation for verses within a single chapter
     * Example: Genesis 1:1-3
     * Verifies correct sequence of verse IDs is generated
     */
    @Test
    fun testVerseIDs_singleBook_singleChapter() {
        val ref =
            Reference(
                from = VerseReference.fromVerseID(value = "1:1:1")!!,
                to = VerseReference.fromVerseID(value = "1:1:3")!!,
            )

        assertEquals(
            listOf(
                VerseID(value = "1:1:1"),
                VerseID(value = "1:1:2"),
                VerseID(value = "1:1:3"),
            ),
            ref.verseIDs(),
        )
    }

    /**
     * Tests verse ID generation across multiple chapters in a single book
     * Example: Genesis 1:30-3:3
     * Verifies:
     * - Correct handling of chapter transitions
     * - Complete verse sequences within chapters
     */
    @Test
    fun testVerseIDs_singleBook_multipleChapters() {
        val ref =
            Reference(
                from = VerseReference.fromVerseID(value = "1:1:30")!!,
                to = VerseReference.fromVerseID(value = "1:3:3")!!,
            )

        assertEquals(
            buildList {
                add(VerseID(value = "1:1:30"))
                add(VerseID(value = "1:1:31"))

                addAll(
                    BookCollection.mapping[BookName.Genesis]!!
                        .allVerseIDs(chapter = 2),
                )

                add(VerseID(value = "1:3:1"))
                add(VerseID(value = "1:3:2"))
                add(VerseID(value = "1:3:3"))
            },
            ref.verseIDs(),
        )
    }

    /**
     * Tests verse ID generation across multiple books
     * Example: Genesis 50:25 - Leviticus 1:3
     * Verifies:
     * - Correct handling of book transitions
     * - Complete verse sequences within books
     */
    @Test
    fun testVerseIDs_multipleBooks() {
        val ref =
            Reference(
                from = VerseReference.fromVerseID(value = "1:50:25")!!,
                to = VerseReference.fromVerseID(value = "3:1:3")!!,
            )

        assertEquals(
            buildList {
                add(VerseID(value = "1:50:25"))
                add(VerseID(value = "1:50:26"))

                addAll(
                    BookCollection.mapping[BookName.Exodus]!!
                        .allVerseIDs(),
                )

                add(VerseID(value = "3:1:1"))
                add(VerseID(value = "3:1:2"))
                add(VerseID(value = "3:1:3"))
            },
            ref.verseIDs(),
        )
    }

    /**
     * Tests verse ID generation from start of Bible to a specific verse
     * Example: Genesis 1:1 - Leviticus 1:3
     * Verifies complete verse sequence from Genesis through specified books
     */
    @Test
    fun testVerseIDs_multipleBooks_startingGenesis() {
        val ref =
            Reference(
                from = VerseReference.fromVerseID(value = VerseID.start.value)!!,
                to = VerseReference.fromVerseID(value = "3:1:3")!!,
            )

        assertEquals(
            buildList {
                addAll(
                    BookCollection.mapping[BookName.Genesis]!!
                        .allVerseIDs(),
                )

                addAll(
                    BookCollection.mapping[BookName.Exodus]!!
                        .allVerseIDs(),
                )

                add(VerseID(value = "3:1:1"))
                add(VerseID(value = "3:1:2"))
                add(VerseID(value = "3:1:3"))
            },
            ref.verseIDs(),
        )
    }

    /**
     * Tests verse ID generation from a specific verse to end of Bible
     * Example: 3 John 1:13 - Revelation 22:21
     * Verifies complete verse sequence through end of Revelation
     */
    @Test
    fun testVerseIDs_multipleBooks_endingRevelation() {
        val ref =
            Reference(
                from = VerseReference.fromVerseID(value = "64:1:13")!!,
                to = VerseReference.fromVerseID(value = VerseID.end.value)!!,
            )

        assertEquals(
            buildList {
                add(VerseID(value = "64:1:13"))
                add(VerseID(value = "64:1:14"))

                addAll(
                    BookCollection.mapping[BookName.Jude]!!
                        .allVerseIDs(),
                )

                addAll(
                    BookCollection.mapping[BookName.Revelation]!!
                        .allVerseIDs(),
                )
            },
            ref.verseIDs(),
        )
    }

    /**
     * Tests verse ID generation for entire Bible
     * Genesis 1:1 through Revelation 22:21
     * Verifies complete sequence of all verse IDs in canonical order
     */
    @Test
    fun testVerseIDs_allBooks() {
        val ref =
            Reference(
                from = VerseReference.fromVerseID(value = VerseID.start.value)!!,
                to = VerseReference.fromVerseID(value = VerseID.end.value)!!,
            )

        assertEquals(
            buildList {
                BookCollection.allBooks.forEach {
                    addAll(it.allVerseIDs())
                }
            },
            ref.verseIDs(),
        )
    }

    /**
     * Tests the fixup method of Reference class
     * Verifies that:
     * 1. When 'from' verse is greater than 'to' verse, they are swapped
     * 2. When verses are already in correct order, no change occurs
     */
    @Test
    fun testFixup() {
        // Test case where swapping is needed (from > to)
        val refNeedsSwap =
            Reference(
                from = VerseReference.fromVerseID(value = "3:1:3")!!,
                to = VerseReference.fromVerseID(value = "1:1:1")!!,
            )
        val swappedRef = refNeedsSwap.fixup()
        assertEquals("1:1:1", swappedRef.from.verseID().value)
        assertEquals("3:1:3", swappedRef.to.verseID().value)

        // Test case where no swapping is needed (from < to)
        val refNoSwap =
            Reference(
                from = VerseReference.fromVerseID(value = "1:1:1")!!,
                to = VerseReference.fromVerseID(value = "3:1:3")!!,
            )
        val unchangedRef = refNoSwap.fixup()
        assertEquals("1:1:1", unchangedRef.from.verseID().value)
        assertEquals("3:1:3", unchangedRef.to.verseID().value)
    }
}
