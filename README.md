# BibleKit KMP

[![Kotlin](https://img.shields.io/badge/kotlin-2.3.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-orange.svg?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![Maven Central](https://img.shields.io/maven-central/v/com.aarkaystudio.biblekit/biblekit)](https://central.sonatype.com/artifact/com.aarkaystudio.biblekit/biblekit)
[![Documentation](https://img.shields.io/badge/docs-dokka-green)](https://versewell.github.io/biblekit-kmp)
[![codecov](https://codecov.io/gh/VerseWell/biblekit-kmp/branch/main/graph/badge.svg)](https://codecov.io/gh/VerseWell/biblekit-kmp)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

BibleKit KMP is a Kotlin Multiplatform library that provides Bible-related functionality for iOS, macOS, and Android platforms.

## Features

- 🔍 Powerful verse search functionality
- 📚 Cross-platform Bible database access
- 📱 Support for iOS, macOS, and Android
- ⚡️ Built with Kotlin Multiplatform

## Requirements

- Android: minSdk 24+
- iOS: 13.0+
- macOS: 10.15+

## Installation

### Gradle (Android)

```kotlin
dependencies {
    implementation("com.aarkaystudio.biblekit:biblekit:0.2.0")
}
```

### Swift Package Manager (iOS/macOS)

Add the following to your Package.swift file:

```swift
dependencies: [
    .package(url: "https://github.com/VerseWell/biblekit-kmp.git", from: "0.2.0")
]
```

#### Alternative: Native Swift Library

If you prefer using native Swift instead of Kotlin Multiplatform, we maintain a dedicated Swift library alongside this one:

- **[BibleKit Swift](https://github.com/VerseWell/BibleKit-swift)** - A native Swift library for iOS/macOS developers who want the most idiomatic Swift APIs and experience for Apple platforms

> As a Swift developer first, I am actively maintaining both libraries. This KMP version allows me to explore cross-platform development potential while providing a solution for teams that need Android support. Both libraries provide the same core functionality, so choose the one that best fits your project's architecture and team preferences.

## Usage

> [!IMPORTANT]
> Your app must include a file named `bible.db` in its bundle. This SQLite database file contains the Bible content and is required for the library to function. You can find:
>
> - Example database file: [bible.db](androidApp/src/main/assets/bible.db) (World English Bible with [translation modifications](#bible-translation))
> - Database schema: [database.sq](biblekit-db/src/commonMain/sqldelight/com/aarkaystudio/biblekitdb/database.sq)
>
> You can replace the `bible.db` file with your preferred translation as long as it follows the same database schema.

### Kotlin (Android)

```kotlin
// Copy bible.db from assets to app directory if it doesn't exist
val dbFile = File(applicationContext.filesDir, "bible.db")
if (!dbFile.exists()) {
    applicationContext.assets.open("bible.db").use { input ->
        FileOutputStream(dbFile).use { output ->
            input.copyTo(output)
        }
    }
}

// Initialize with the database file path
val provider = BibleProvider.create(
    dbFactory = BibleDatabaseFactory(context = applicationContext),
    filePath = dbFile.absolutePath
)

// Search
val results = provider.search(
    query = "love",
    verseIDs = null
)
```

### Swift (iOS/macOS)

```swift
// Get the bible.db path from the app bundle
guard let dbPath = Bundle.main.path(forResource: "bible", ofType: "db") else {
    fatalError("bible.db not found in bundle")
}

// Initialize with the database file path
let provider = BibleProvider.companion.create(
    dbFactory: BibleDatabaseFactory(),
    filePath: dbPath
)

// Search
let results = try await provider.search(
    query: "love",
    verseIDs: nil
)
```

## Documentation

For detailed API documentation, visit [BibleKit KMP Documentation](https://versewell.github.io/biblekit-kmp)

## Bible Translation

The example database included with BibleKit uses the **World English Bible** translation (Protestant, US English) with **modifications**.

- You can visit the [official WEB page](https://ebible.org/find/show.php?id=engwebp) for more information about this translation.
- For details regarding the modifications we've made to the WEB translation in our database, please refer to our [Translation Modifications documentation](https://versewell.github.io/translation#translation-modifications).

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
