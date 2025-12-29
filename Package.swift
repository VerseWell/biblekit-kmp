// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

/**
 * Swift Package Manager manifest for BibleKit KMP
 * 
 * This package provides Bible-related functionality for iOS and macOS platforms
 * through a pre-built XCFramework distributed via GitHub releases.
 */
let package = Package(
    name: "BibleKit",
    platforms: [
        .iOS(.v13),
        .macOS(.v10_15)
    ],
    products: [
        // Main library product exposing BibleKit functionality
        .library(
            name: "BibleKit",
            targets: ["BibleKit"]
        ),
    ],
    dependencies: [
        // No external dependencies required
    ],
    targets: [
        // Binary target that downloads the XCFramework from GitHub releases
        .binaryTarget(
            name: "BibleKit",
            url:
                "https://github.com/VerseWell/biblekit-kmp/releases/download/0.1.2/BibleKit.xcframework.zip",
            checksum: "4eac83e05be05b4b55f5867753cc2991bb74d98456c03e4cdbd729eb69d64712"
        )
    ]
) 
