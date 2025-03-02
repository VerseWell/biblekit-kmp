#!/bin/sh

set -e

rm -rf ./BibleKit.xcframework.zip ./BibleKit.xcframework
rm -rf ./biblekit/build/XCFrameworks/
./gradlew assembleBibleKitXCFramework
cp -R ./biblekit/build/XCFrameworks/release/BibleKit.xcframework ./
zip -r BibleKit.xcframework.zip BibleKit.xcframework/
shasum -a 256 BibleKit.xcframework.zip
