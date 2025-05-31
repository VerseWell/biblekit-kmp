import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.skie)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    alias(libs.plugins.binaryCompatibilityValidator)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    explicitApi()

    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64(),
    )

    sourceSets {
        commonMain.dependencies {
            // put your multiplatform dependencies here
            implementation(libs.sqldelight.coroutines)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }

        androidUnitTest.dependencies {
            implementation(libs.sqldelight.jvm)
            implementation(libs.slf4j.nop)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.jvm)
            implementation(libs.slf4j.nop)
        }

        nativeMain.dependencies {
            implementation(libs.sqldelight.native)
        }
    }
}

android {
    namespace = "com.aarkaystudio.biblekitdb"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()
    defaultConfig {
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }
}

sqldelight {
    databases {
        create("BibleDatabase") {
            packageName.set("com.aarkaystudio.biblekitdb")
        }
    }
}

mavenPublishing {
    coordinates("com.aarkaystudio.biblekit", "biblekit-db", "0.1.0")

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    pom {
        name.set("BibleKit")
        description.set(
            "BibleKit KMP is a Kotlin Multiplatform library that provides Bible-related functionality for iOS, macOS, and Android platforms.",
        )
        inceptionYear.set("2025")
        url.set("https://github.com/VerseWell/biblekit-kmp/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("rahul0x24")
                name.set("Rahul Katariya")
                url.set("https://github.com/rahul0x24/")
            }
        }
        scm {
            url.set("https://github.com/VerseWell/biblekit-kmp/")
            connection.set("scm:git:git://github.com/VerseWell/biblekit-kmp.git")
            developerConnection.set("scm:git:ssh://git@github.com/VerseWell/biblekit-kmp.git")
        }
    }
}
