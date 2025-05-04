package com.example.tamo.di

import android.content.Context
import androidx.room.Room
import com.example.tamo.data.AppDatabase
import com.example.tamo.data.MeigenApi
import com.example.tamo.data.MeigenRepository
import com.example.tamo.data.TabDao
import com.example.tamo.data.TabRepository
import com.example.tamo.data.TaskDao
import com.example.tamo.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tamo_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }

    @Provides
    fun provideTabDao(db: AppDatabase): TabDao = db.tabDao()

    @Provides
    @Singleton
    fun provideTabRepository(tabDao: TabDao, taskRepository: TaskRepository): TabRepository = TabRepository(tabDao,taskRepository)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://meigen.doodlenote.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMeigenApi(retrofit: Retrofit): MeigenApi {
        return retrofit.create(MeigenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMeigenRepository(meigenApi: MeigenApi): MeigenRepository {
        return MeigenRepository(meigenApi)
    }
}
