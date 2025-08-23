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
                "https://github.com/VerseWell/biblekit-kmp/releases/download/0.1.1/BibleKit.xcframework.zip",
            checksum: "9f5975bfe7df89fd5fdc867a201d6c757a9c7dbeff1770afa6929e8135cde446"
        )
    ]
) 
