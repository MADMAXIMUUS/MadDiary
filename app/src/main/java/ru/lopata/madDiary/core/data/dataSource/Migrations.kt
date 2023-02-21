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

val MIGRATION_10_11 = object : Migration(10, 11) {
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
                    "notification TEXT NOT NULL)"
        )
        database.execSQL(
            "CREATE TABLE ATTACHMENTS(" +
                    "atId INTEGER PRIMARY KEY," +
                    "eventId INTEGER NOT NULL," +
                    "type INTEGER NOT NULL," +
                    "uri TEXT NOT NULL)"
        )
    }
}

val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE EVENTS ADD COLUMN isAttachmentAdded integer(1) NOT NULL"
        )
        database.execSQL(
            "DROP TABLE ATTACHMENTS"
        )
        database.execSQL(
            "CREATE TABLE ATTACHMENTS(" +
                    "atId INTEGER PRIMARY KEY," +
                    "eventOwnerId INTEGER NOT NULL," +
                    "type INTEGER NOT NULL," +
                    "uri TEXT NOT NULL," +
                    "FOREIGN KEY(eventOwnerId) REFERENCES EVENTS(eventId) ON UPDATE NO ACTION ON DELETE CASCADE)"
        )
        database.execSQL(
            "DROP TABLE REPEATS"
        )
        database.execSQL(
            "CREATE TABLE REPEATS(" +
                    "repeatId INTEGER PRIMARY KEY," +
                    "eventOwnerId INTEGER NOT NULL," +
                    "repeatStart BIGINT NOT NULL," +
                    "repeatEnd BIGINT NOT NULL," +
                    "repeatInterval BIGINT NOT NULL," +
                    "FOREIGN KEY(eventOwnerId) REFERENCES EVENTS(eventId) ON UPDATE NO ACTION ON DELETE CASCADE)"
        )
    }
}

val MIGRATION_12_13 = object : Migration(12, 13) {
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
                    "cover TEXT NOT NULL," +
                    "isAttachmentAdded integer(1) NOT NULL)"
        )
    }
}

val MIGRATION_13_14 = object : Migration(13, 14) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE ATTACHMENTS")
        database.execSQL(
            "CREATE TABLE ATTACHMENTS(" +
                    "atId INTEGER PRIMARY KEY," +
                    "eventOwnerId INTEGER NOT NULL," +
                    "type INTEGER NOT NULL," +
                    "duration BIGINT NOT NULL," +
                    "size BIGINT NOT NULL," +
                    "uri TEXT NOT NULL," +
                    "FOREIGN KEY(eventOwnerId) REFERENCES EVENTS(eventId) ON UPDATE NO ACTION ON DELETE CASCADE)"
        )
    }
}

val MIGRATION_14_15 = object : Migration(14, 15) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE ATTACHMENTS ADD COLUMN name TEXT NOT NULL"
        )
    }
}

val MIGRATION_15_16 = object : Migration(15, 16) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE ATTACHMENTS ADD COLUMN fileExtension TEXT DEFAULT '' NOT NULL"
        )
    }
}

val MIGRATION_16_17 = object : Migration(16, 17) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE NOTIFICATIONS(" +
                    "id INTEGER PRIMARY KEY," +
                    "eventOwnerId INTEGER NOT NULL," +
                    "time BIGINT NOT NULL," +
                    "FOREIGN KEY(eventOwnerId) REFERENCES EVENTS(eventId) ON UPDATE NO ACTION ON DELETE CASCADE)"
        )
    }
}

val MIGRATION_17_18 = object : Migration(17, 18) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE EVENTS ADD COLUMN type TEXT DEFAULT '' NOT NULL"
        )
    }
}