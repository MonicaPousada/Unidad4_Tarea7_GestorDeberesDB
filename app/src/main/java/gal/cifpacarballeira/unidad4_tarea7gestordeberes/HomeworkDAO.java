package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Array;
import java.util.ArrayList;

public class HomeworkDAO {
    private SQLiteDatabase dbLectura;
    private SQLiteDatabase dbEscritura;

    public HomeworkDAO(Context context){
        dbLectura = new DataBaseHomework(context).getReadableDatabase();
        dbEscritura = new DataBaseHomework(context).getWritableDatabase();
    }

    public Integer obtenerId(){
        Cursor cursor = dbLectura.query("homework", new String[]{"MAX(id)"}, null, null, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return null;
    }

    public Integer insertHomework(Homework homework){
        Integer isCompleted;
        if (homework.isCompleted()){
            isCompleted = 1;
        }else{
            isCompleted=0;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("subject", homework.getSubject());
        contentValues.put("description", homework.getDescription());
        contentValues.put("dueDate", homework.getDueDate());
        contentValues.put("isCompleted", isCompleted);

        dbEscritura.insert("homework", null, contentValues);

        return obtenerId();

    }

    public long updateHomework(Homework homework){
        Integer isCompleted;
        if (homework.isCompleted()){
            isCompleted = 1;
        }else{
            isCompleted=0;
        }

        String id = String.valueOf(homework.getId());

        ContentValues contentValues = new ContentValues();
        contentValues.put("subject", homework.getSubject());
        contentValues.put("description", homework.getDescription());
        contentValues.put("dueDate", homework.getDueDate());
        contentValues.put("isCompleted", isCompleted);

        return dbEscritura.update("homework", contentValues, "id = ?", new String[]{id});
    }

    public long deleteHomework(Homework homework){
        String id = String.valueOf(homework.getId());
        return dbEscritura.delete("homework", "id = ?", new String[]{id});
    }

    public ArrayList<Homework> readHomework (){
        ArrayList<Homework> homework = new ArrayList<>();

        Cursor cursor = dbLectura.query("homework", new String[]{"id", "subject", "description", "dueDate", "isCompleted"}, null, null, null, null, null);

        if (cursor.moveToFirst()){
            do{
                Boolean isCompleted;
                if(cursor.getInt(4) == 0){
                    isCompleted = false;
                }else {
                    isCompleted=true;
                }

                Homework h = new Homework(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), isCompleted);
                homework.add(h);
            }while (cursor.moveToNext());
        }

        return homework;
    }

    public Homework selectHomework(Homework homework){
        String id = String.valueOf(homework.getId());

        Cursor cursor = dbLectura.query("homework", new String[]{"id", "subject", "description", "dueDate", "isCompleted"}, "id = ?", new String[]{id}, null, null, null, null);

        if(cursor.moveToFirst()){
            Boolean isCompleted;
            if(cursor.getInt(4) == 0){
                isCompleted = false;
            }else {
                isCompleted=true;
            }

            Homework h = new Homework(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), isCompleted);
            return h;
        }else {
            return null;
        }
    }
}
