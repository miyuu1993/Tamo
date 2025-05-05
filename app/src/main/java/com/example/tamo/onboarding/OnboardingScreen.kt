package com.example.tamo.onboarding

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tamo.R
import androidx.navigation.NavHostController
import com.example.tamo.MainActivity


@Composable
fun OnboardingScreen(
    navController: NavHostController,
    onFinish: () -> Unit) {
    val activity = LocalActivity.current as? MainActivity

    DisposableEffect(Unit) {
        activity?.hideSystemBars()
        onDispose {
            activity?.showSystemBars()
        }
    }


    val pages = listOf(
        OnboardingPage(
            R.drawable.onboarding1,
            "Tamo",
            "Tamoは、今日やることを\nかんたんにメモできるアプリです。"
        ),
        OnboardingPage(
            R.drawable.onboarding2,
            "日常の｢ちょっとした｣メモに",
            "「牛乳を買う」「郵便を出す」\n「〇〇さんに連絡する」など、\n気付いたら即メモ！"
        ),
        OnboardingPage(
            R.drawable.onboarding3,
            "うっかりさんでも大丈夫。",
            "リマインド機能付きだから、\n忘れそうな予定もバッチリフォロー。\nTamoがあなたをそっとサポートします。"
        ),
        OnboardingPage(R.drawable.onboarding4, "さあ、はじめましょう！", "")
    )

    val pagerState = rememberPagerState(pageCount = {
        4
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF6D1F)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(pages[page], isLast = (page == pages.lastIndex)) {
                onFinish()
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val selected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (selected) Color.White else Color.LightGray)
                )
            }
        }
    }
}
