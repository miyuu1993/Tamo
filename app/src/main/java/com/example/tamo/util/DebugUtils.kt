package com.example.tamo.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.tamo.data.OnboardingPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 開発中のデバッグに役立つユーティリティクラス
 */
object DebugUtils {
    private const val TAG = "DebugUtils"

    /**
     * オンボーディング状態をリセットする
     * 開発中にテスト用途で使用する
     */
    fun resetOnboarding(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d(TAG, "オンボーディング状態のリセット要求")
                val result = withContext(Dispatchers.IO) {
                    OnboardingPreferences.resetOnboardingCompleted(context.applicationContext)
                }

                if (result) {
                    // リセット成功時の処理
                    Log.d(TAG, "オンボーディング状態のリセットに成功しました")
                } else {
                    // リセット失敗時の処理
                    Log.e(TAG, "オンボーディング状態のリセットに失敗しました")
                }
            } catch (e: Exception) {
                Log.e(TAG, "オンボーディング状態のリセット中に例外が発生", e)
                Toast.makeText(context, "エラー: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 現在のオンボーディング状態を確認しToastで表示する
     */
    fun checkOnboardingState(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d(TAG, "オンボーディング状態の確認要求")
                OnboardingPreferences.logCurrentState(context.applicationContext)

                withContext(Dispatchers.IO) {
                    val isCompleted = OnboardingPreferences.readOnboardingCompleted(context.applicationContext).firstOrNull() ?: false
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "オンボーディング完了状態: $isCompleted",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "オンボーディング状態の確認中に例外が発生", e)
                Toast.makeText(context, "エラー: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * アプリ情報（バージョン、ビルド情報など）をログに出力
     */
    fun logAppInfo(context: Context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = packageInfo.versionName
            val versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }

            Log.d(TAG, "アプリ情報:")
            Log.d(TAG, "パッケージ名: ${context.packageName}")
            Log.d(TAG, "バージョン名: $versionName")
            Log.d(TAG, "バージョンコード: $versionCode")
            Log.d(TAG, "デバイス: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
            Log.d(TAG, "Android バージョン: ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})")
        } catch (e: Exception) {
            Log.e(TAG, "アプリ情報の取得中に例外が発生", e)
        }
    }
}