package com.aarkaystudio.biblekitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aarkaystudio.biblekit.BibleDatabaseFactory
import com.aarkaystudio.biblekit.BibleProvider
import com.aarkaystudio.biblekitapp.ui.theme.biblekitappTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Copy bible.db from assets to app directory if it doesn't exist
        val dbFile = File(applicationContext.filesDir, "bible.db")
        if (!dbFile.exists()) {
            applicationContext.assets.open("bible.db").use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }
        }

        // Initialize BibleProvider with the new filePath API
        val provider =
            BibleProvider.create(
                dbFactory = BibleDatabaseFactory(context = applicationContext),
                filePath = dbFile.absolutePath,
            )

        setContent {
            biblekitappTheme {
                Surface {
                    Scaffold { innerPadding ->
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .imePadding(),
                        ) {
                            SearchScreen(provider = provider)
                        }
                    }
                }
            }
        }
    }
}
