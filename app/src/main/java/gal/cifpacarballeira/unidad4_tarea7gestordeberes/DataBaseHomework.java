package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHomework extends SQLiteOpenHelper {

    private static final String DbName = "Homework Database";
    private static final int DbVersion = 1;

    public DataBaseHomework(@Nullable Context context) {
        super(context, DbName, null, DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE homework (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "subject text NOT NULL, " +
                "description text, " +
                "dueDate text, " +
                "isCompleted integer)";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS homework");
        onCreate(sqLiteDatabase);
    }
}
