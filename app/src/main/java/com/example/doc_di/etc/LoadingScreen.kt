package com.example.doc_di.etc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.Line

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        LinearProgressIndicator(
            trackColor = Line,
            color = LightBlue,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
        )
    }
}