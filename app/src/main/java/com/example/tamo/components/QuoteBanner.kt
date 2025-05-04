package com.example.tamo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tamo.SimpleMarqueeText

@Composable
fun QuoteBanner(text: String?, author: String?) {
    SimpleMarqueeText(
        text = buildString {
            append(text ?: "名言を読み込み中...")
            author?.let { append("  ― $it") }
        },
        modifier = Modifier
            .fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
    )
}
