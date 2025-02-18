package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class HomeworkDAO {
    // Base de datos de solo lectura para consultar los datos.
    private SQLiteDatabase dbLectura;
    // Base de datos de lectura y escritura para modificar los datos.
    private SQLiteDatabase dbEscritura;
    // Instancia estática para implementar el patrón Singleton (una única instancia de la clase).
    private static HomeworkDAO homeworkDAO;
    // Contexto de la aplicación.
    @SuppressLint("StaticFieldLeak") // Advertencia para evitar fugas de memoria al mantener el contexto estático
    private static Context context;

    // Constructor privado para inicializar las bases de datos (lectura y escritura).
    private HomeworkDAO(Context context){
        this.context = context;
        // Crear una instancia de la base de datos para lectura.
        dbLectura = new DataBaseHomework(context).getReadableDatabase();
        // Crear una instancia de la base de datos para escritura.
        dbEscritura = new DataBaseHomework(context).getWritableDatabase();
    }

    // Método público estático para obtener la instancia única de HomeworkDAO (patrón Singleton).
    public static HomeworkDAO obtenerInstancia(Context context){
        if(homeworkDAO == null){ // Si aún no se ha creado la instancia
            homeworkDAO = new HomeworkDAO(context); // Crear una nueva instancia
        }
        return homeworkDAO; // Devolver la instancia existente o la recién creada
    }

    // Método para obtener el último ID de tarea de la base de datos.
    public Integer obtenerId(){
        // Consultar la base de datos para obtener el valor máximo de id
        Cursor cursor = dbLectura.query("homework", new String[]{"MAX(id)"}, null, null, null, null, null);
        if(cursor.moveToFirst()){ // Si hay resultados en el cursor
            return cursor.getInt(0); // Devolver el valor del máximo id encontrado
        }
        return null; // Si no hay resultados, devolver null
    }

    // Método para insertar una tarea en la base de datos.
    public Integer insertHomework(Homework homework){
        Integer isCompleted;
        // Verificar si la tarea está completada, almacenarla como 1 (completada) o 0 (pendiente).
        if (homework.isCompleted()){
            isCompleted = 1;
        }else{
            isCompleted=0;
        }

        // Crear un objeto ContentValues para almacenar los datos de la tarea
        ContentValues contentValues = new ContentValues();
        contentValues.put("subject", homework.getSubject());
        contentValues.put("description", homework.getDescription());
        contentValues.put("dueDate", homework.getDueDate());
        contentValues.put("isCompleted", isCompleted);

        // Insertar los valores en la tabla "homework"
        dbEscritura.insert("homework", null, contentValues);

        // Obtener y devolver el último ID insertado
        return obtenerId();

    }

    // Método para actualizar los datos de una tarea existente en la base de datos.
    public long updateHomework(Homework homework){
        // Convertir el estado de completado en 1 o 0
        Integer isCompleted;
        if (homework.isCompleted()){
            isCompleted = 1;
        }else{
            isCompleted=0;
        }

        // Convertir el id de la tarea a una cadena
        String id = String.valueOf(homework.getId());

        // Crear un objeto ContentValues con los datos de la tarea para actualizar
        ContentValues contentValues = new ContentValues();
        contentValues.put("subject", homework.getSubject());
        contentValues.put("description", homework.getDescription());
        contentValues.put("dueDate", homework.getDueDate());
        contentValues.put("isCompleted", isCompleted);

        // Realizar la actualización en la base de datos usando el ID de la tarea
        return dbEscritura.update("homework", contentValues, "id = ?", new String[]{id});
    }

    // Método para eliminar una tarea de la base de datos.
    public long deleteHomework(Homework homework){
        // Convertir el id de la tarea a una cadena
        String id = String.valueOf(homework.getId());
        // Eliminar la tarea con el ID correspondiente
        return dbEscritura.delete("homework", "id = ?", new String[]{id});
    }

    // Método para leer todas las tareas desde la base de datos y devolverlas en una lista.
    public ArrayList<Homework> readHomework (){
        // Crear una lista para almacenar las tareas
        ArrayList<Homework> homework = new ArrayList<>();

        // Consultar la base de datos para obtener todas las tareas
        Cursor cursor = dbLectura.query("homework", new String[]{"id", "subject", "description", "dueDate", "isCompleted"}, null, null, null, null, null);

        if (cursor.moveToFirst()){ // Si el cursor tiene datos
            do{
                // Convertir el valor de "isCompleted" de 0 a false y de 1 a true
                Boolean isCompleted;
                if(cursor.getInt(4) == 0){
                    isCompleted = false;
                }else {
                    isCompleted=true;
                }

                // Crear un objeto Homework con los datos obtenidos del cursor
                Homework h = new Homework(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), isCompleted);
                // Añadir la tarea a la lista
                homework.add(h);
            }while (cursor.moveToNext()); // Repetir para todos los registros
        }

        // Devolver la lista de tareas
        return homework;
    }

    // Método para seleccionar una tarea específica de la base de datos utilizando su ID.
    public Homework selectHomework(Homework homework){
        // Convertir el ID de la tarea a una cadena
        String id = String.valueOf(homework.getId());

        // Consultar la base de datos para obtener la tarea con el ID correspondiente
        Cursor cursor = dbLectura.query("homework", new String[]{"id", "subject", "description", "dueDate", "isCompleted"}, "id = ?", new String[]{id}, null, null, null, null);

        if(cursor.moveToFirst()){ // Si el cursor tiene datos
            Boolean isCompleted;
            // Convertir el valor de "isCompleted" de 0 a false y de 1 a true
            if(cursor.getInt(4) == 0){
                isCompleted = false;
            }else {
                isCompleted=true;
            }

            // Crear un objeto Homework con los datos obtenidos del cursor
            Homework h = new Homework(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), isCompleted);
            return h; // Devolver la tarea seleccionada
        }else {
            return null; // Si no se encuentra la tarea, devolver null
        }
    }
}
