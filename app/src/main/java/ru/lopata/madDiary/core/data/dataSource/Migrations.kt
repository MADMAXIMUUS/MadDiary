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