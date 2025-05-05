package com.example.tamo.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import kotlin.math.roundToInt

@Composable
fun SimpleMarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    speedPxPerSecond: Float = 80f
) {
    var textWidth by remember { mutableStateOf(0) }
    var visibleWidth by remember { mutableStateOf(0) }

    val distance = (textWidth + visibleWidth).coerceAtLeast(1)

    val durationMillis = (distance / speedPxPerSecond * 1000).toInt()


    // アニメーション用の状態
    val infiniteTransition = rememberInfiniteTransition(label = "marquee")

    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = distance.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "marquee"
    )

    Box(
        modifier = modifier.clipToBounds()
    ) {
        val textToShow = text + "    " + text // テキストを2回表示してシームレスに見せる

        Text(
            text = textToShow,
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(maxWidth = Int.MAX_VALUE)
                )

                textWidth = placeable.width
                visibleWidth = constraints.maxWidth

                // テキストが表示領域より長い場合のみアニメーション
                val offset = if (textWidth > visibleWidth) {
                    (-animatedOffset % (textWidth + 40)).roundToInt() // 少し余白を加える
                } else {
                    0
                }

                layout(visibleWidth, placeable.height) {
                    placeable.place(offset, 0)
                }
            }
        )
    }
}