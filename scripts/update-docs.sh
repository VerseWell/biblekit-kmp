#!/bin/sh

set -e

rm -rf build/dokka && ./gradlew dokkaHtmlMultiModule
rm -rf ./docs && cp -R build/dokka/htmlMultiModule docs
