package com.anlmk.base.data.room

import androidx.room.*
import com.anlmk.base.data.impl.*

@Database(entities = [User::class, Mealtime::class], version = 1)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealtimeDao(): MealtimeDao

    companion object {
       const val DATABASE_NAME = "DatabaseName"
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS payment (\n" +
//                            "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT 0,\n" +
//                            "    name TEXT ,amount TEXT " +
//                            ")\n"
//                )
////                database.execSQL("ALTER TABLE payment ADD COLUMN name TEXT NOT NULL DEFAULT 'defaultValue'")
////                database.execSQL("ALTER TABLE payment ADD COLUMN amount TEXT NOT NULL DEFAULT 'defaultValue'")
//
//                // Copy data from old_table to new_table
////                database.execSQL("INSERT INTO new_table (id, name) SELECT id, name FROM old_table")
//
//                // Drop the old table
////                database.execSQL("DROP TABLE IF EXISTS old_table")
//            }
//        }
    }
}
