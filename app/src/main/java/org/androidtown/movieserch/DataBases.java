package org.androidtown.movieserch;

import android.provider.BaseColumns;

/**
 * Created by JuHyeok on 2017-01-29.
 */
public class DataBases {
    public static final class CreateDB implements BaseColumns{
        public static final String NAME = "name";
        public static final String GENRE = "genre";
        public static final String STORY = "story";
        public static final String _TABLENAME = "movie";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +NAME+" text not null , "
                        +GENRE+" text not null , "
                        +STORY+" text not null );";
    }
}
