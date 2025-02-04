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

public class ListHomeworkViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Homework>> lista = new MutableLiveData<>(new ArrayList<>());
    HomeworkDAO homeworkDAO;

    public ListHomeworkViewModel(@NonNull Application application) {
        super(application);
        homeworkDAO = HomeworkDAO.obtenerInstancia(application);
        this.lista.setValue(homeworkDAO.readHomework());
    }

    public LiveData<ArrayList<Homework>> getLista() {
        return lista;
    }

    public void setLista(List<Homework> lista) {
        this.lista.setValue((ArrayList<Homework>) lista);
    }

    public void addHomework(Homework homework){
        ArrayList<Homework> temporal = this.lista.getValue();
        temporal.add(homework);
        this.lista.setValue(temporal);
        homeworkDAO.insertHomework(homework);
    }

    public void removeHomework(Homework homework){
        ArrayList<Homework> temporal = this.lista.getValue();
        for (int i = 0; i < temporal.size(); i++) {
            if (temporal.get(i).equals(homework)){
                temporal.remove(i);
            }
        }
        this.lista.setValue(temporal);
        homeworkDAO.deleteHomework(homework);
    }

    public void editHomework(Homework homeworkToEdit, Homework homework){
        ArrayList<Homework> temporal = this.lista.getValue();
        temporal.set(temporal.indexOf(homeworkToEdit), homework);
        this.lista.setValue(temporal);
        homeworkDAO.updateHomework(homework);
    }

}
