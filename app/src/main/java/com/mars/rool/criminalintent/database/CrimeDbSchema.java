package com.mars.rool.criminalintent.database;

public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crime";

        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String REQUIRE_POLICE = "require_police";
        }
    }
    public static final class UserTable {
        public static final String NAME = "user";

        public static final class Columns {
            public static final String EMAIL = "email";
            public static final String PASSWORD = "password";
        }
    }
}
