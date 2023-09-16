package com.anlmk.base.di

import android.app.Application
import androidx.room.Room
import com.anlmk.base.data.impl.*
import com.anlmk.base.data.room.MyAppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

//used to save local data in application
val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideUserDao(get()) }
    single { provideMealsTimeDao(get()) }
}

private fun provideDatabase(application: Application): MyAppDatabase {
    return Room.databaseBuilder(application, MyAppDatabase::class.java, MyAppDatabase.DATABASE_NAME).build()
//        .addMigrations(MyAppDatabase.MIGRATION_1_2).build()
}

private fun provideUserDao(database: MyAppDatabase): UserDao {
    return UserDaoImpl(database)
}

private fun provideMealsTimeDao(database: MyAppDatabase): MealtimeDao {
    return MealtimeDaoImpl(database)
}