package com.example.tamo.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val TAG = "OnboardingPrefs"
private const val PREFERENCES_NAME = "onboarding_preferences"

// DataStoreのExtension Property（Contextに拡張関数として追加）
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

object OnboardingPreferences {
    // 定数としてキーを定義
    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    //オンボーディング完了フラグを読み取る
    fun readOnboardingCompleted(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "DataStoreの読み取りに失敗しました", exception)
                    emit(androidx.datastore.preferences.core.emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val isCompleted = preferences[ONBOARDING_COMPLETED_KEY] ?: false
                Log.d(TAG, "オンボーディング完了状態を読み取り: $isCompleted (キー: onboarding_completed)")
                isCompleted
            }
    }

    //* オンボーディング完了フラグを保存する* @return 保存が成功したかどうか
    suspend fun saveOnboardingCompleted(context: Context): Boolean {
        return try {
            Log.d(TAG, "オンボーディング完了状態の保存を開始")
            context.dataStore.edit { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = true
                Log.d(TAG, "DataStoreに保存: ONBOARDING_COMPLETED_KEY = true")
            }
            Log.d(TAG, "オンボーディング完了状態の保存に成功")
            true
        } catch (e: Exception) {
            Log.e(TAG, "オンボーディング完了状態の保存に失敗", e)
            false
        }
    }

    //現在の設定状態をログ出力（デバッグ用）
    suspend fun logCurrentState(context: Context) {
        try {
            context.dataStore.data.first().let { preferences ->
                val isCompleted = preferences[ONBOARDING_COMPLETED_KEY] ?: false
                Log.d(TAG, "現在のオンボーディング状態: 完了=${isCompleted}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "設定状態のログ出力に失敗", e)
        }
    }

    //オンボーディング完了フラグをリセット（テスト用）
    suspend fun resetOnboardingCompleted(context: Context): Boolean {
        return try {
            context.dataStore.edit { preferences ->
                preferences.remove(ONBOARDING_COMPLETED_KEY)
                Log.d(TAG, "オンボーディング完了状態をリセット")
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "オンボーディング完了状態のリセットに失敗", e)
            false
        }
    }
}