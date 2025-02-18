package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHomework extends SQLiteOpenHelper {

    private static final String DbName = "Homework Database"; // Nombre de la base de datos
    private static final int DbVersion = 1; // Versión de la base de datos (incrementar si se hacen cambios en la estructura)

    // Constructor que inicializa la base de datos
    public DataBaseHomework(@Nullable Context context) {
        super(context, DbName, null, DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Definición de la consulta SQL para crear la tabla "homework"
        String CREATE_TABLE = "CREATE TABLE homework (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "subject text NOT NULL, " +
                "description text, " +
                "dueDate text, " +
                "isCompleted integer)";

        // Ejecuta la consulta para crear la tabla
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Elimina la tabla si ya existe para actualizar la estructura
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS homework");
        // Llama a onCreate para recrear la base de datos con la nueva estructura
        onCreate(sqLiteDatabase);
    }
}
