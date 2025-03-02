package com.aarkaystudio.biblekit.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Test class for Verse model and its sharing functionality
 * Tests various scenarios of verse reference formatting and text sharing
 * Covers single verses, multiple verses, chapter breaks, and verse ranges
 */
class VerseTest {
    /**
     * Tests that creating share text with empty verse list throws exception
     */
    @Test
    fun testVerseShareText_emptyList() {
        assertFailsWith<IllegalArgumentException> {
            Verse.createShareText(emptyList())
        }
    }

    /**
     * Tests share title formatting for a single verse
     * Example: "Genesis 1:1"
     */
    @Test
    fun testShareTitle_singleVerse() {
        val verses = listOf("1:1:1").map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:1", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for multiple verses at chapter end
     * Example: "Genesis 50:23-26"
     */
    @Test
    fun testShareTitle_multipleVerses_end() {
        val verses =
            listOf("1:50:23", "1:50:24", "1:50:25", "1:50:26")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 50:23-26", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[23] ... [24] ... [25] ... [26] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for two consecutive verses
     * Example: "Genesis 1:1-2"
     */
    @Test
    fun testShareTitle_twoVerses() {
        val verses = listOf("1:1:1", "1:1:2").map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:1-2", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[1] ... [2] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for multiple consecutive verses
     * Example: "Genesis 1:1-3"
     */
    @Test
    fun testShareTitle_multipleVerses() {
        val verses = listOf("1:1:1", "1:1:2", "1:1:3").map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:1-3", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[1] ... [2] ... [3] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for non-consecutive verses with single verse gap
     * Example: "Genesis 1:2-3,5"
     */
    @Test
    fun testShareTitle_multipleVerses_withBreak_singleVerse() {
        val verses = listOf("1:1:2", "1:1:3", "1:1:5").map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:2-3,5", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[2] ... [3] ... [5] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for non-consecutive verses with multiple verse ranges
     * Example: "Genesis 1:2-3,5-7"
     */
    @Test
    fun testShareTitle_multipleVerses_withBreak_twoVerses() {
        val verses =
            listOf("1:1:2", "1:1:3", "1:1:5", "1:1:6", "1:1:7")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:2-3,5-7", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[2] ... [3] ... [5] ... [6] ... [7] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for multiple non-consecutive verse ranges
     * Example: "Genesis 1:2,4-6,9,14-15"
     */
    @Test
    fun testShareTitle_multipleVerses_withMultipleBreak_multipleVerses() {
        val verses =
            listOf("1:1:2", "1:1:4", "1:1:5", "1:1:6", "1:1:9", "1:1:14", "1:1:15")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:2,4-6,9,14-15", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[2] ... [4] ... [5] ... [6] ... [9] ... [14] ... [15] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for verses spanning multiple chapters
     * Example: "Genesis 1:31-2:1"
     */
    @Test
    fun testShareTitle_multipleChapters_singleVerse() {
        val verses =
            listOf("1:1:31", "1:2:1")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:31-2:1", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[31] ... [2:1] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for non-consecutive verses across chapters
     * Example: "Genesis 1:31,2:2"
     */
    @Test
    fun testShareTitle_multipleChapters_singleVerse_withBreak() {
        val verses =
            listOf("1:1:31", "1:2:2")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:31,2:2", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[31] ... [2:2] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for consecutive verses across chapters
     * Example: "Genesis 1:30-2:4"
     */
    @Test
    fun testShareTitle_multipleChapters_multipleVerses() {
        val verses =
            listOf("1:1:30", "1:1:31", "1:2:1", "1:2:2", "1:2:3", "1:2:4")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:30-2:4", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[30] ... [31] ... [2:1] ... [2] ... [3] ... [4] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for non-consecutive verses across chapters
     * Example: "Genesis 1:30-2:1,3-4"
     */
    @Test
    fun testShareTitle_multipleChapters_multipleVerses_withBreak() {
        val verses =
            listOf("1:1:30", "1:1:31", "1:2:1", "1:2:3", "1:2:4")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:30-2:1,3-4", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[30] ... [31] ... [2:1] ... [3] ... [4] ...", Verse.shareVersesText(verses).joinToString(" "))
    }

    /**
     * Tests share title formatting for complex verse ranges across multiple chapters
     * Example: "Genesis 1:30-2:1,3-5,25-3:2,24,4:2"
     */
    @Test
    fun testShareTitle_multipleChapters_multipleVerses_withMultipleBreak_multipleVerses() {
        val verses =
            listOf("1:1:30", "1:1:31", "1:2:1", "1:2:3", "1:2:4", "1:2:5", "1:2:25", "1:3:1", "1:3:2", "1:3:24", "1:4:2")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:30-2:1,3-5,25-3:2,24,4:2", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals(
            "[30] ... [31] ... [2:1] ... [3] ... [4] ... [5] ... [25] ... [3:1] ... [2] ... [24] ... [4:2] ...",
            Verse.shareVersesText(verses).joinToString(" "),
        )
    }

    /**
     * Tests share title formatting for non-consecutive verses across chapters
     * Example: "Genesis 1:30,2:2"
     */
    @Test
    fun shareTitle_multipleChapters_singleVerse_withBreak() {
        val verses =
            listOf("1:1:30", "1:2:2")
                .map { Verse(id = VerseID(value = it), text = "...") }
        val selectedVerses = Verse.selectedVerseRange(verses = verses)
        assertEquals("Genesis 1:30,2:2", SelectedVerseRange.shareTitle(range = selectedVerses))
        assertEquals("[30] ... [2:2] ...", Verse.shareVersesText(verses).joinToString(" "))
    }
}
