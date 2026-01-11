import BibleKit

/// Verifies that BibleKit can be imported and its APIs are callable.
public enum BibleKitIntegration {
    /// Performs a search using the BibleProvider API.
    public static func search(
        dbPath: String,
        query: String
    ) async throws -> [Verse] {
        let startVerse = VerseReference.companion.fromVerseID(value: VerseID.companion.start.value)!
        let endVerse = VerseReference.companion.fromVerseID(value: VerseID.companion.end.value)!
        let reference = Reference(from: startVerse, to: endVerse)

        let bibleProvider = BibleProvider.companion.create(
            dbFactory: BibleDatabaseFactory(),
            filePath: dbPath
        )

        let searchResults = try await bibleProvider.search(
            query: query,
            reference: reference,
            limit: .max,
            offset: 0
        )

        return searchResults
    }
}
