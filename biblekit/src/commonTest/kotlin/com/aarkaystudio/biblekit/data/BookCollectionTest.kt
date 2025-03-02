package com.aarkaystudio.biblekit.data

import com.aarkaystudio.biblekit.model.ChapterReference
import com.aarkaystudio.biblekit.model.Reference
import com.aarkaystudio.biblekit.model.VerseReference
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for BookCollection
 * Verifies the integrity and accuracy of Bible book data including:
 * - Book counts in Old and New Testaments
 * - Chapter and verse counts for each book
 * - Verse references and IDs
 */
class BookCollectionTest {
    /**
     * Tests the total count of books in Old Testament, New Testament, and complete Bible
     * Verifies:
     * - Old Testament has 39 books
     * - New Testament has 27 books
     * - Complete Bible has 66 books
     */
    @Test
    fun testTotalCount() {
        assertEquals(BookCollection.oldTestamentBooks.size, 39)
        assertEquals(BookCollection.newTestamentBooks.size, 27)
        assertEquals(BookCollection.allBooks.size, 66)
    }

    /**
     * Tests that each book has the correct number of chapters with verse counts
     * Verifies that the verses array size matches the total number of chapters for each book
     */
    @Test
    fun testVersesTotalExistsForEachChapter() {
        BookCollection.allBooks.forEach { book ->
            assertEquals(book.verses.size, book.totalChapters)
        }
    }

    /**
     * Tests the total verse count from Genesis to Revelation
     * Verifies that the complete Bible contains exactly 31,102 verses
     */
    @Test
    fun testReferenceTotalBookCount() {
        val reference =
            Reference(
                from = VerseReference(chapter = ChapterReference(bookName = BookName.Genesis, index = 1), index = 1),
                to = VerseReference(chapter = ChapterReference(bookName = BookName.Revelation, index = 22), index = 21),
            )

        assertEquals(31102, reference.verseIDs().count())
    }

    /**
     * Comprehensive test of each book's data in the Bible
     * For each book, verifies:
     * - Total number of chapters
     * - Total number of verses
     * Tests all 66 books of the Bible in order:
     * - Old Testament (39 books)
     * - New Testament (27 books)
     */
    @Test
    fun testBookCollection() {
        // Old Testament
        val genesisBook = BookCollection.mapping[BookName.Genesis]!!
        assertEquals(genesisBook.totalChapters, 50)
        assertEquals(genesisBook.allVerseIDs().count(), 1533)
        val exodusBook = BookCollection.mapping[BookName.Exodus]!!
        assertEquals(exodusBook.totalChapters, 40)
        assertEquals(exodusBook.allVerseIDs().count(), 1213)
        val leviticusBook = BookCollection.mapping[BookName.Leviticus]!!
        assertEquals(leviticusBook.totalChapters, 27)
        assertEquals(leviticusBook.allVerseIDs().count(), 859)
        val numbersBook = BookCollection.mapping[BookName.Numbers]!!
        assertEquals(numbersBook.totalChapters, 36)
        assertEquals(numbersBook.allVerseIDs().count(), 1288)
        val deuteronomyBook = BookCollection.mapping[BookName.Deuteronomy]!!
        assertEquals(deuteronomyBook.totalChapters, 34)
        assertEquals(deuteronomyBook.allVerseIDs().count(), 959)

        val joshuaBook = BookCollection.mapping[BookName.Joshua]!!
        assertEquals(joshuaBook.totalChapters, 24)
        assertEquals(joshuaBook.allVerseIDs().count(), 658)
        val judgesBook = BookCollection.mapping[BookName.Judges]!!
        assertEquals(judgesBook.totalChapters, 21)
        assertEquals(judgesBook.allVerseIDs().count(), 618)
        val ruthBook = BookCollection.mapping[BookName.Ruth]!!
        assertEquals(ruthBook.totalChapters, 4)
        assertEquals(ruthBook.allVerseIDs().count(), 85)
        val samuel1Book = BookCollection.mapping[BookName.Samuel1]!!
        assertEquals(samuel1Book.totalChapters, 31)
        assertEquals(samuel1Book.allVerseIDs().count(), 810)
        val samuel2Book = BookCollection.mapping[BookName.Samuel2]!!
        assertEquals(samuel2Book.totalChapters, 24)
        assertEquals(samuel2Book.allVerseIDs().count(), 695)
        val kings1Book = BookCollection.mapping[BookName.Kings1]!!
        assertEquals(kings1Book.totalChapters, 22)
        assertEquals(kings1Book.allVerseIDs().count(), 816)
        val kings2Book = BookCollection.mapping[BookName.Kings2]!!
        assertEquals(kings2Book.totalChapters, 25)
        assertEquals(kings2Book.allVerseIDs().count(), 719)
        val chronicles1Book = BookCollection.mapping[BookName.Chronicles1]!!
        assertEquals(chronicles1Book.totalChapters, 29)
        assertEquals(chronicles1Book.allVerseIDs().count(), 942)
        val chronicles2Book = BookCollection.mapping[BookName.Chronicles2]!!
        assertEquals(chronicles2Book.totalChapters, 36)
        assertEquals(chronicles2Book.allVerseIDs().count(), 822)
        val ezraBook = BookCollection.mapping[BookName.Ezra]!!
        assertEquals(ezraBook.totalChapters, 10)
        assertEquals(ezraBook.allVerseIDs().count(), 280)
        val nehemiahBook = BookCollection.mapping[BookName.Nehemiah]!!
        assertEquals(nehemiahBook.totalChapters, 13)
        assertEquals(nehemiahBook.allVerseIDs().count(), 406)
        val estherBook = BookCollection.mapping[BookName.Esther]!!
        assertEquals(estherBook.totalChapters, 10)
        assertEquals(estherBook.allVerseIDs().count(), 167)

        val jobBook = BookCollection.mapping[BookName.Job]!!
        assertEquals(jobBook.totalChapters, 42)
        assertEquals(jobBook.allVerseIDs().count(), 1070)
        val psalmsBook = BookCollection.mapping[BookName.Psalms]!!
        assertEquals(psalmsBook.totalChapters, 150)
        assertEquals(psalmsBook.allVerseIDs().count(), 2461)
        val proverbsBook = BookCollection.mapping[BookName.Proverbs]!!
        assertEquals(proverbsBook.totalChapters, 31)
        assertEquals(proverbsBook.allVerseIDs().count(), 915)
        val ecclesiastesBook = BookCollection.mapping[BookName.Ecclesiastes]!!
        assertEquals(ecclesiastesBook.totalChapters, 12)
        assertEquals(ecclesiastesBook.allVerseIDs().count(), 222)
        val songOfSolomonBook = BookCollection.mapping[BookName.SongOfSolomon]!!
        assertEquals(songOfSolomonBook.totalChapters, 8)
        assertEquals(songOfSolomonBook.allVerseIDs().count(), 117)

        val isaiahBook = BookCollection.mapping[BookName.Isaiah]!!
        assertEquals(isaiahBook.totalChapters, 66)
        assertEquals(isaiahBook.allVerseIDs().count(), 1292)
        val jeremiahBook = BookCollection.mapping[BookName.Jeremiah]!!
        assertEquals(jeremiahBook.totalChapters, 52)
        assertEquals(jeremiahBook.allVerseIDs().count(), 1364)
        val lamentationsBook = BookCollection.mapping[BookName.Lamentations]!!
        assertEquals(lamentationsBook.totalChapters, 5)
        assertEquals(lamentationsBook.allVerseIDs().count(), 154)
        val ezekielBook = BookCollection.mapping[BookName.Ezekiel]!!
        assertEquals(ezekielBook.totalChapters, 48)
        assertEquals(ezekielBook.allVerseIDs().count(), 1273)
        val danielBook = BookCollection.mapping[BookName.Daniel]!!
        assertEquals(danielBook.totalChapters, 12)
        assertEquals(danielBook.allVerseIDs().count(), 357)
        val hoseaBook = BookCollection.mapping[BookName.Hosea]!!
        assertEquals(hoseaBook.totalChapters, 14)
        assertEquals(hoseaBook.allVerseIDs().count(), 197)
        val joelBook = BookCollection.mapping[BookName.Joel]!!
        assertEquals(joelBook.totalChapters, 3)
        assertEquals(joelBook.allVerseIDs().count(), 73)
        val amosBook = BookCollection.mapping[BookName.Amos]!!
        assertEquals(amosBook.totalChapters, 9)
        assertEquals(amosBook.allVerseIDs().count(), 146)
        val obadiahBook = BookCollection.mapping[BookName.Obadiah]!!
        assertEquals(obadiahBook.totalChapters, 1)
        assertEquals(obadiahBook.allVerseIDs().count(), 21)
        val jonahBook = BookCollection.mapping[BookName.Jonah]!!
        assertEquals(jonahBook.totalChapters, 4)
        assertEquals(jonahBook.allVerseIDs().count(), 48)
        val micahBook = BookCollection.mapping[BookName.Micah]!!
        assertEquals(micahBook.totalChapters, 7)
        assertEquals(micahBook.allVerseIDs().count(), 105)
        val nahumBook = BookCollection.mapping[BookName.Nahum]!!
        assertEquals(nahumBook.totalChapters, 3)
        assertEquals(nahumBook.allVerseIDs().count(), 47)
        val habakkukBook = BookCollection.mapping[BookName.Habakkuk]!!
        assertEquals(habakkukBook.totalChapters, 3)
        assertEquals(habakkukBook.allVerseIDs().count(), 56)
        val zephaniahBook = BookCollection.mapping[BookName.Zephaniah]!!
        assertEquals(zephaniahBook.totalChapters, 3)
        assertEquals(zephaniahBook.allVerseIDs().count(), 53)
        val haggaiBook = BookCollection.mapping[BookName.Haggai]!!
        assertEquals(haggaiBook.totalChapters, 2)
        assertEquals(haggaiBook.allVerseIDs().count(), 38)
        val zechariahBook = BookCollection.mapping[BookName.Zechariah]!!
        assertEquals(zechariahBook.totalChapters, 14)
        assertEquals(zechariahBook.allVerseIDs().count(), 211)
        val malachiBook = BookCollection.mapping[BookName.Malachi]!!
        assertEquals(malachiBook.totalChapters, 4)
        assertEquals(malachiBook.allVerseIDs().count(), 55)

        // New Testament
        val matthewBook = BookCollection.mapping[BookName.Matthew]!!
        assertEquals(matthewBook.totalChapters, 28)
        assertEquals(matthewBook.allVerseIDs().count(), 1071)
        val markBook = BookCollection.mapping[BookName.Mark]!!
        assertEquals(markBook.totalChapters, 16)
        assertEquals(markBook.allVerseIDs().count(), 678)
        val lukeBook = BookCollection.mapping[BookName.Luke]!!
        assertEquals(lukeBook.totalChapters, 24)
        assertEquals(lukeBook.allVerseIDs().count(), 1151)
        val johnBook = BookCollection.mapping[BookName.John]!!
        assertEquals(johnBook.totalChapters, 21)
        assertEquals(johnBook.allVerseIDs().count(), 879)

        val actsBook = BookCollection.mapping[BookName.Acts]!!
        assertEquals(actsBook.totalChapters, 28)
        assertEquals(actsBook.allVerseIDs().count(), 1007)

        val romansBook = BookCollection.mapping[BookName.Romans]!!
        assertEquals(romansBook.totalChapters, 16)
        assertEquals(romansBook.allVerseIDs().count(), 433)
        val corinthians1Book = BookCollection.mapping[BookName.Corinthians1]!!
        assertEquals(corinthians1Book.totalChapters, 16)
        assertEquals(corinthians1Book.allVerseIDs().count(), 437)
        val corinthians2Book = BookCollection.mapping[BookName.Corinthians2]!!
        assertEquals(corinthians2Book.totalChapters, 13)
        assertEquals(corinthians2Book.allVerseIDs().count(), 257)

        val galatiansBook = BookCollection.mapping[BookName.Galatians]!!
        assertEquals(galatiansBook.totalChapters, 6)
        assertEquals(galatiansBook.allVerseIDs().count(), 149)
        val ephesiansBook = BookCollection.mapping[BookName.Ephesians]!!
        assertEquals(ephesiansBook.totalChapters, 6)
        assertEquals(ephesiansBook.allVerseIDs().count(), 155)
        val philippiansBook = BookCollection.mapping[BookName.Philippians]!!
        assertEquals(philippiansBook.totalChapters, 4)
        assertEquals(philippiansBook.allVerseIDs().count(), 104)
        val colossiansBook = BookCollection.mapping[BookName.Colossians]!!
        assertEquals(colossiansBook.totalChapters, 4)
        assertEquals(colossiansBook.allVerseIDs().count(), 95)

        val thessalonians1Book = BookCollection.mapping[BookName.Thessalonians1]!!
        assertEquals(thessalonians1Book.totalChapters, 5)
        assertEquals(thessalonians1Book.allVerseIDs().count(), 89)
        val thessalonians2Book = BookCollection.mapping[BookName.Thessalonians2]!!
        assertEquals(thessalonians2Book.totalChapters, 3)
        assertEquals(thessalonians2Book.allVerseIDs().count(), 47)
        val timothy1Book = BookCollection.mapping[BookName.Timothy1]!!
        assertEquals(timothy1Book.totalChapters, 6)
        assertEquals(timothy1Book.allVerseIDs().count(), 113)
        val timothy2Book = BookCollection.mapping[BookName.Timothy2]!!
        assertEquals(timothy2Book.totalChapters, 4)
        assertEquals(timothy2Book.allVerseIDs().count(), 83)
        val titusBook = BookCollection.mapping[BookName.Titus]!!
        assertEquals(titusBook.totalChapters, 3)
        assertEquals(titusBook.allVerseIDs().count(), 46)

        val philemonBook = BookCollection.mapping[BookName.Philemon]!!
        assertEquals(philemonBook.totalChapters, 1)
        assertEquals(philemonBook.allVerseIDs().count(), 25)
        val hebrewsBook = BookCollection.mapping[BookName.Hebrews]!!
        assertEquals(hebrewsBook.totalChapters, 13)
        assertEquals(hebrewsBook.allVerseIDs().count(), 303)
        val jamesBook = BookCollection.mapping[BookName.James]!!
        assertEquals(jamesBook.totalChapters, 5)
        assertEquals(jamesBook.allVerseIDs().count(), 108)

        val peter1Book = BookCollection.mapping[BookName.Peter1]!!
        assertEquals(peter1Book.totalChapters, 5)
        assertEquals(peter1Book.allVerseIDs().count(), 105)
        val peter2Book = BookCollection.mapping[BookName.Peter2]!!
        assertEquals(peter2Book.totalChapters, 3)
        assertEquals(peter2Book.allVerseIDs().count(), 61)

        val john1Book = BookCollection.mapping[BookName.John1]!!
        assertEquals(john1Book.totalChapters, 5)
        assertEquals(john1Book.allVerseIDs().count(), 105)
        val john2Book = BookCollection.mapping[BookName.John2]!!
        assertEquals(john2Book.totalChapters, 1)
        assertEquals(john2Book.allVerseIDs().count(), 13)
        val john3Book = BookCollection.mapping[BookName.John3]!!
        assertEquals(john3Book.totalChapters, 1)
        assertEquals(john3Book.allVerseIDs().count(), 14)
        val judeBook = BookCollection.mapping[BookName.Jude]!!
        assertEquals(judeBook.totalChapters, 1)
        assertEquals(judeBook.allVerseIDs().count(), 25)

        val revelationBook = BookCollection.mapping[BookName.Revelation]!!
        assertEquals(revelationBook.totalChapters, 22)
        assertEquals(revelationBook.allVerseIDs().count(), 404)
    }
}
