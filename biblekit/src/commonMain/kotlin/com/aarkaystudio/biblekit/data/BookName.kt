package com.aarkaystudio.biblekit.data

/**
 * Represents the canonical books of the Bible in their traditional order.
 * This enum provides both full names and standardized abbreviations for all 66 books,
 * divided into the Old Testament (Genesis through Malachi) and
 * New Testament (Matthew through Revelation).
 *
 * Each book has:
 * - A full name (e.g., "Genesis", "1 Corinthians")
 * - A standardized abbreviation (e.g., "Gen", "1Co")
 * - An implicit ordinal position (1-66) based on its declaration order
 *
 * @property value The full name of the book
 * @property shortName The standardized abbreviation for the book
 */
public enum class BookName(
    public val value: String,
    public val shortName: String,
) {
    // Old Testament Books (1-39)
    Genesis("Genesis", "Gen"),
    Exodus("Exodus", "Exo"),
    Leviticus("Leviticus", "Lev"),
    Numbers("Numbers", "Num"),
    Deuteronomy("Deuteronomy", "Deu"),
    Joshua("Joshua", "Jos"),
    Judges("Judges", "Judg"),
    Ruth("Ruth", "Rth"),
    Samuel1("1 Samuel", "1Sa"),
    Samuel2("2 Samuel", "2Sa"),
    Kings1("1 Kings", "1Ki"),
    Kings2("2 Kings", "2Ki"),
    Chronicles1("1 Chronicles", "1Ch"),
    Chronicles2("2 Chronicles", "2Ch"),
    Ezra("Ezra", "Eza"),
    Nehemiah("Nehemiah", "Neh"),
    Esther("Esther", "Est"),
    Job("Job", "Job"),
    Psalms("Psalms", "Psa"),
    Proverbs("Proverbs", "Pro"),
    Ecclesiastes("Ecclesiastes", "Ecc"),
    SongOfSolomon("Song of Solomon", "SS"),
    Isaiah("Isaiah", "Isa"),
    Jeremiah("Jeremiah", "Jer"),
    Lamentations("Lamentations", "Lam"),
    Ezekiel("Ezekiel", "Ezk"),
    Daniel("Daniel", "Dan"),
    Hosea("Hosea", "Hos"),
    Joel("Joel", "Joe"),
    Amos("Amos", "Amo"),
    Obadiah("Obadiah", "Obd"),
    Jonah("Jonah", "Jon"),
    Micah("Micah", "Mic"),
    Nahum("Nahum", "Nah"),
    Habakkuk("Habakkuk", "Hab"),
    Zephaniah("Zephaniah", "Zep"),
    Haggai("Haggai", "Hag"),
    Zechariah("Zechariah", "Zch"),
    Malachi("Malachi", "Mal"),

    // New Testament Books (40-66)
    Matthew("Matthew", "Mat"),
    Mark("Mark", "Mar"),
    Luke("Luke", "Luk"),
    John("John", "Jn"),
    Acts("Acts", "Act"),
    Romans("Romans", "Rom"),
    Corinthians1("1 Corinthians", "1Co"),
    Corinthians2("2 Corinthians", "2Co"),
    Galatians("Galatians", "Gal"),
    Ephesians("Ephesians", "Eph"),
    Philippians("Philippians", "Phi"),
    Colossians("Colossians", "Col"),
    Thessalonians1("1 Thessalonians", "1Th"),
    Thessalonians2("2 Thessalonians", "2Th"),
    Timothy1("1 Timothy", "1Ti"),
    Timothy2("2 Timothy", "2Ti"),
    Titus("Titus", "Tit"),
    Philemon("Philemon", "Phm"),
    Hebrews("Hebrews", "Heb"),
    James("James", "Jam"),
    Peter1("1 Peter", "1Pe"),
    Peter2("2 Peter", "2Pe"),
    John1("1 John", "1Jo"),
    John2("2 John", "2Jo"),
    John3("3 John", "3Jo"),
    Jude("Jude", "Jud"),
    Revelation("Revelation", "Rev"),
}
