// swift-tools-version: 6.0

import PackageDescription

/// Integration package to verify the locally built XCFramework compiles correctly.
let package = Package(
    name: "BibleKitIntegration",
    platforms: [
        .iOS(.v13),
        .macOS(.v10_15),
    ],
    products: [
        .library(
            name: "BibleKitIntegration",
            targets: ["BibleKitIntegration"]
        )
    ],
    targets: [
        .target(
            name: "BibleKitIntegration",
            dependencies: ["BibleKit"]
        ),
        .binaryTarget(
            name: "BibleKit",
            path: "../BibleKit.xcframework"
        )
    ]
)
