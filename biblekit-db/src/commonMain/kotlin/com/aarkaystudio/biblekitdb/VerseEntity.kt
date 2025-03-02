package com.aarkaystudio.biblekitdb

/**
 * Represents a verse entity in the database.
 *
 * @property id The unique identifier for the verse.
 * @property text The text content of the verse.
 */
public data class VerseEntity(
    public val id: String,
    public val text: String,
) {
    internal val key: Long = sortKey(id = id)

    public companion object {
        public fun sortKey(id: String): Long {
            // Helper function to pad the numbers
            fun pad(number: String): String =
                when (number.length) {
                    1 -> "00$number"
                    2 -> "0$number"
                    else -> number
                }

            // Split the ID into components
            val components = id.split(":")

            // Guard: Ensure that we have exactly 3 components
            if (components.size != 3) {
                throw IllegalArgumentException("ID must have exactly 3 components separated by ':'")
            }

            val chapter = components[1]
            val verse = components[2]

            // Pad chapter and verse
            val paddedChapter = pad(chapter)
            val paddedVerse = pad(verse)

            // Combine the parts into the final number string
            val numberString = components[0] + paddedChapter + paddedVerse

            // Convert to an long safely
            val sortKey =
                numberString.toLongOrNull()
                    ?: throw IllegalArgumentException("Invalid number string formed from id")

            return sortKey
        }
    }
}
