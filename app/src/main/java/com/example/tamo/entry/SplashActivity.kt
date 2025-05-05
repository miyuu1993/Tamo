package com.example.tamo.entry

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.example.tamo.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashRoot = findViewById<View>(R.id.splashRoot)

        // Android 11 以前 → systemUiVisibility で非表示
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        } else {
            // Android 12 以降 → WindowInsetsController で非表示
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        // レイアウト表示（ローディングスピナー付き）
        setContentView(R.layout.activity_splash)

        Log.d("SplashActivity", "start splash delay")
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("SplashActivity", "fade out started")
            splashRoot.animate()
                .alpha(0f)              // ← フェードアウト
                .setDuration(600)      // ← アニメーション時間（ms）
                .withEndAction {
                    Log.d("SplashActivity", "navigating to Launcher")
                    // 遷移
                    startActivity(Intent(this, LauncherActivity::class.java))
                    finish()
                }
                .start()
        }, 800) // ← 表示しておく時間（ms）
    }
}

