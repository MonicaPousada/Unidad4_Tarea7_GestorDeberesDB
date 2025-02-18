package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

//Extiende AndroidViewModel para poder tener acceso al contexto de la aplicación
public class ListHomeworkViewModel extends AndroidViewModel {
    // MutableLiveData que almacena la lista de tareas, se utilizará para notificar cambios a la UI.
    private MutableLiveData<ArrayList<Homework>> lista = new MutableLiveData<>(new ArrayList<>());
    // Instancia de HomeworkDAO para gestionar las operaciones de base de datos.
    HomeworkDAO homeworkDAO;

    // Constructor del ViewModel que se inicializa con la aplicación.
    public ListHomeworkViewModel(@NonNull Application application) {
        super(application); // Llamada al constructor de la clase base AndroidViewModel
        // Obtener la instancia de HomeworkDAO (debe ser única, según el patrón Singleton).
        homeworkDAO = HomeworkDAO.obtenerInstancia(application);
        // Inicializar la lista con los datos obtenidos desde la base de datos.
        this.lista.setValue(homeworkDAO.readHomework());
    }

    // Método para obtener la lista de tareas, se utiliza LiveData para observar cambios.
    public LiveData<ArrayList<Homework>> getLista() {
        return lista; // Devuelve el LiveData que contiene la lista de tareas.
    }

    // Método para actualizar la lista de tareas con nuevos datos (por ejemplo, al modificarla desde la UI).
    public void setLista(List<Homework> lista) {
        this.lista.setValue((ArrayList<Homework>) lista);// Establecer una nueva lista y notificar los observadores.
    }

    // Método para agregar una tarea a la lista y la base de datos.
    public void addHomework(Homework homework){
        // Obtener la lista actual de tareas.
        ArrayList<Homework> temporal = this.lista.getValue();
        // Añadir la nueva tarea a la lista temporal.
        temporal.add(homework);
        // Actualizar la lista en LiveData para notificar la UI del cambio.
        this.lista.setValue(temporal);
        // Insertar la tarea en la base de datos
        homeworkDAO.insertHomework(homework);
    }

    // Método para eliminar una tarea de la lista y la base de datos.
    public void removeHomework(Homework homework){
        // Obtener la lista actual de tareas.
        ArrayList<Homework> temporal = this.lista.getValue();

        // Recorrer la lista y eliminar la tarea especificada.
        for (int i = 0; i < temporal.size(); i++) {
            if (temporal.get(i).equals(homework)){
                temporal.remove(i); // Eliminar la tarea de la lista.
            }
        }

        // Actualizar la lista en LiveData para reflejar el cambio en la UI.
        this.lista.setValue(temporal);

        // Eliminar la tarea de la base de datos.
        homeworkDAO.deleteHomework(homework);
    }

    // Método para editar una tarea en la lista y la base de datos.
    public void editHomework(Homework homeworkToEdit, Homework homework){
        // Obtener la lista actual de tareas.
        ArrayList<Homework> temporal = this.lista.getValue();
        // Encontrar la tarea a editar y reemplazarla con la nueva tarea.
        temporal.set(temporal.indexOf(homeworkToEdit), homework);
        // Actualizar la lista en LiveData para reflejar el cambio en la UI.
        this.lista.setValue(temporal);
        // Actualizar la tarea en la base de datos.
        homeworkDAO.updateHomework(homework);
    }

}
