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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                            SearchScreen(
                                provider =
                                    BibleProvider.create(
                                        dbFactory =
                                            BibleDatabaseFactory(
                                                context = applicationContext,
                                                replaceDatabase = false,
                                                completionHandler = {},
                                            ),
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}
