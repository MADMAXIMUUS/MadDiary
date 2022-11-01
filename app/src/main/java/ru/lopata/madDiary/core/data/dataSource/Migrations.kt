package ru.lopata.madDiary.core.data.dataSource

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE EVENTS ADD COLUMN allDay INTEGER NOT NULL")
    }
}
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE EVENTS ADD COLUMN completed INTEGER NOT NULL")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE EVENTS")
        database.execSQL(
            "CREATE TABLE EVENTS(" +
                    "eventId INTEGER PRIMARY KEY," +
                    "title TEXT NOT NULL," +
                    "completed integer(1) NOT NULL," +
                    "startDateTime BIGINT NOT NULL," +
                    "endDateTime BIGINT NOT NULL," +
                    "allDay integer(1) NOT NULL," +
                    "location TEXT NOT NULL," +
                    "note TEXT NOT NULL," +
                    "repeat TEXT NOT NULL," +
                    "notification TEXT NOT NULL," +
                    "attachment TEXT NOT NULL)"
        )
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE EVENTS ADD COLUMN color integer NOT NULL"
        )
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE REPEATS(" +
                    "repeatId INTEGER PRIMARY KEY," +
                    "eventOwnerId INTEGER NOT NULL," +
                    "repeatStart BIGINT NOT NULL," +
                    "repeatInterval BIGINT NOT NULL)"
        )
    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE EVENTS")
        database.execSQL(
            "CREATE TABLE EVENTS(" +
                    "eventId INTEGER PRIMARY KEY," +
                    "title TEXT NOT NULL," +
                    "completed integer(1) NOT NULL," +
                    "startDateTime BIGINT NOT NULL," +
                    "endDateTime BIGINT NOT NULL," +
                    "allDay integer(1) NOT NULL," +
                    "location TEXT NOT NULL," +
                    "note TEXT NOT NULL," +
                    "color INTEGER NOT NULL," +
                    "notification TEXT NOT NULL," +
                    "attachment TEXT NOT NULL)"
        )
    }
}

val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE REPEATS")
        database.execSQL(
            "CREATE TABLE REPEATS(" +
                    "repeatId INTEGER PRIMARY KEY," +
                    "eventOwnerId INTEGER NOT NULL," +
                    "repeatStart BIGINT NOT NULL," +
                    "repeatEnd BIGINT NOT NULL," +
                    "repeatInterval BIGINT NOT NULL)"
        )
    }
}