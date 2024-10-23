package com.example.crfpos2024

import android.content.Context
import androidx.room.Room
import com.example.data.repository.GoodsRepository
import com.example.data.repository.LocalGoodsRepository
import com.example.database.GoodsDatabase
import com.example.database.dao.GoodsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GoodsDatabase {
        return Room.databaseBuilder(
            context,
            GoodsDatabase::class.java,
            "goods.db",
        ).build()

    }

    @Provides
    @Singleton
    fun provideGoodsDao(db: GoodsDatabase): GoodsDao {
        return db.goodsDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    @Singleton
    abstract fun bindGoodsRepository(impl: LocalGoodsRepository): GoodsRepository
}