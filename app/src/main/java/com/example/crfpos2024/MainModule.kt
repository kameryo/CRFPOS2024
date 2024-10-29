package com.example.crfpos2024

import android.content.Context
import androidx.room.Room
import com.example.data.repository.GoodsRepository
import com.example.data.repository.LocalGoodsRepository
import com.example.data.repository.LocalRecordRepository
import com.example.data.repository.RecordRepository
import com.example.database.GoodsDatabase
import com.example.database.RecordDatabase
import com.example.database.dao.GoodsDao
import com.example.database.dao.RecordDao
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
    fun provideGoodsDatabase(@ApplicationContext context: Context): GoodsDatabase {
        return Room.databaseBuilder(
            context,
            GoodsDatabase::class.java,
            "goods.db",
        ).build()

    }

    @Provides
    @Singleton
    fun provideRecordDatabase(@ApplicationContext context: Context): RecordDatabase {
        return Room.databaseBuilder(
            context,
            RecordDatabase::class.java,
            "record.db",
        ).build()

    }

    @Provides
    @Singleton
    fun provideGoodsDao(db: GoodsDatabase): GoodsDao {
        return db.goodsDao()
    }

    @Provides
    @Singleton
    fun provideRecordDao(db: RecordDatabase): RecordDao {
        return db.recordDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    @Singleton
    abstract fun bindGoodsRepository(impl: LocalGoodsRepository): GoodsRepository

    @Binds
    @Singleton
    abstract fun bindRecordRepository(impl: LocalRecordRepository): RecordRepository
}

