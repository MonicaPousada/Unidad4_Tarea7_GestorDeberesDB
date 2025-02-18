package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // RecyclerView para mostrar la lista de tareas
    private HomeworkAdapter adapter; // Adaptador para el RecyclerView
    //private List<Homework> homeworkList; // Lista de tareas (comentado porque se usa ViewModel en su lugar)
    private ListHomeworkViewModel homeworkList; // ViewModel para gestionar la lista de tareas
    //private HomeworkDAO homeworkDAO; // DAO para acceder a la base de datos (comentado)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //homeworkDAO = HomeworkDAO.obtenerInstancia(getApplicationContext());

        // Inicialización de componentes
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);
//        homeworkList = new ArrayList<>();

        // Inicialización del ViewModel
        homeworkList = new ViewModelProvider(this).get(ListHomeworkViewModel.class);

        // Observa los cambios en la lista de tareas y actualiza el RecyclerView
        homeworkList.getLista().observe(this, new Observer<ArrayList<Homework>>() {
            @Override
            public void onChanged(ArrayList<Homework> homework) {
                adapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
            }
        });

        // Crear y configurar el adaptador
        //adapter = new HomeworkAdapter(homeworkList.getLista().getValue(), homework -> showBottomSheet(homework));

        // Creación y configuración del adaptador con un listener para los clics en los elementos
        adapter = new HomeworkAdapter(homeworkList.getLista().getValue(), new HomeworkAdapter.OnHomeworkClickListener(){
            @Override
            public void onHomeworkClick(Homework homework) {
                showBottomSheet(homework);
            }
        });

        // Este código sería lo mismo que la anterior línea
        // adapter = new HomeworkAdapter(homeworkList, this::showBottomSheet);
        // ¿Por qué le paso ese segundo parámetro?
        // Porque le estoy pasando la función que quiero que se lance al hacer click en un elemento
        // Investiga sobre "operador de referencia de método en Java"


        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Configuración del botón flotante para agregar nuevas tareas
        fab.setOnClickListener(v -> showAddHomeworkDialog(null));
    }

    // Método para mostrar el diálogo de agregar o editar tarea
    private void showAddHomeworkDialog(Homework homeworkToEdit) {
        NewHomeworkDialogFragment dialog = new NewHomeworkDialogFragment();

        // Pasarle el objeto Homework al diálogo si se está editando
        if (homeworkToEdit != null) {
            Bundle args = new Bundle();
            args.putParcelable("homework", homeworkToEdit);
            dialog.setArguments(args);
        }

        // Listener para guardar o editar una tarea
        dialog.setOnHomeworkSavedListener(homework -> {
                    if (homeworkToEdit == null) {
                        homeworkList.addHomework(homework); // Agrega una nueva tarea
                    } else {
                        homeworkList.editHomework(homeworkToEdit, homework); // Edita la tarea existente
                    }
                });

        // Muestra el diálogo
        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
//
//        AddHomeworkDialogFragment dialog = AddHomeworkDialogFragment.newInstance(homeworkToEdit);
//        dialog.setOnHomeworkSavedListener(homework -> {
//            if (homeworkToEdit == null) {
//                homeworkList.add(homework);
//            } else {
//                homeworkList.set(homeworkList.indexOf(homeworkToEdit), homework);
//            }
//            adapter.notifyDataSetChanged();
//        });
//        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
    }

    // Método para mostrar el menú inferior con opciones de edición y eliminación
    private void showBottomSheet(Homework homework) {
        // Creación del diálogo
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflar el layout del menú inferior
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_homework_options, null);

        // Asignar acciones a los botones

        // Opción de editar
        view.findViewById(R.id.editOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Cierra el diálogo
            showAddHomeworkDialog(homework); // Abre el diálogo de edición
        });

        // Opción de eliminar
        view.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Cierra el diálogo
            showDeleteConfirmation(homework); // Muestra la confirmación de eliminación
        });


        // Opción de marcar como completada
        view.findViewById(R.id.completeOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Cierra el diálogo
            //homework.setCompleted(true);
            Homework homeworkTemporal = homework;
            homeworkTemporal.setCompleted(true); // Marca la tarea como completada
            homeworkList.editHomework(homework, homeworkTemporal); // Actualiza la tarea en la lista
            Toast.makeText(this, "Tarea marcada como completada", Toast.LENGTH_SHORT).show(); // Mensaje de confirmación
        });

        // Mostrar el diálogo
        // Muestra el menú inferior
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    // Método para mostrar un cuadro de diálogo de confirmación antes de eliminar una tarea
    private void showDeleteConfirmation(Homework homework) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación") // Título del cuadro de diálogo
                .setMessage("¿Estás seguro de que deseas eliminar este deber?") // Mensaje de confirmación
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    homeworkList.removeHomework(homework); // Elimina la tarea de la lista
                })
                .setNegativeButton("Cancelar", null) // Cierra el cuadro de diálogo si se cancela
                .show();
    }

    //Sin funciones lamda
    /*
    private void showDeleteConfirmation(Homework homework) {
    new AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este deber?")
            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    homeworkList.removeHomework(homework);
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Cierra el diálogo sin hacer nada
                }
            })
            .show();
}
     */
}
