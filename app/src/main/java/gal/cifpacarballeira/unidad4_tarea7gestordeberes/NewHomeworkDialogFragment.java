package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// Esta clase extiende `DialogFragment` para mostrar un cuadro de diálogo
// que permite al usuario agregar o editar un deber (tarea escolar).
// En este diálogo, el usuario puede ingresar una descripción, una fecha de entrega,
// y seleccionar la asignatura.
public class NewHomeworkDialogFragment extends DialogFragment {

    // Referencias a las vistas del diálogo.
    private EditText descriptionEditText;
    private EditText dueDateEditText;
    private Spinner subjectSpinner;
    private NewHomeworkDialogFragment.OnHomeworkSavedListener listener; // Listener para manejar el evento de guardar
    private Homework homeworkToEdit;

    // Método llamado para crear el diálogo. Se construye e infla el layout.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Crear un objeto de tipo AlertDialog.Builder, que nos permite construir un diálogo.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflar el layout del diálogo con el xml que hemos creado antes
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_homework, null);

        // Instanciar las vistas que utilizamos en el layout del diálogo
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dueDateEditText = view.findViewById(R.id.dueDateEditText);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);

        // Configurar el listener para mostrar un diálogo de selección de fecha cuando se haga clic en el campo de fecha de entrega.
        dueDateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Si se está editando un deber, cargar los datos en los campos
        if (getArguments() != null) {
            homeworkToEdit = getArguments().getParcelable("homework"); // Recuperamos el deber a editar desde los argumentos del diálogo.
            if (homeworkToEdit != null) {
                // Cargar los datos en los campos de texto del diálogo.
                descriptionEditText.setText(homeworkToEdit.getDescription());
                dueDateEditText.setText(homeworkToEdit.getDueDate());
                // Configurar el spinner para mostrar la asignatura seleccionada previamente.
                subjectSpinner.setSelection(getIndex(subjectSpinner, homeworkToEdit.getSubject()));
            }
        }

        // Instanciar los botones de guardar y cancelar.
        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Configurar los listeners de los botones
        saveButton.setOnClickListener(v -> {
            // Validar que los campos no estén vacíos antes de guardar.
            if (validateInputs()) {
                // Crear un nuevo objeto `Homework` con los valores introducidos.
                Homework homework = new Homework(
                        subjectSpinner.getSelectedItem().toString(),
                        descriptionEditText.getText().toString(),
                        dueDateEditText.getText().toString(),
                        false // Por defecto, el deber no está completado.
                );

                // Si el listener está configurado, se llama al método para guardar el deber.
                if (listener != null) {
                    listener.onHomeworkSaved(homework);
                }
                dismiss(); // Cerrar el diálogo después de guardar.
            }
        });

        cancelButton.setOnClickListener(v -> dismiss()); // Cerrar el diálogo al cancelar.

        // Asociar la vista inflada al diálogo.
        builder.setView(view);

        // Crea el diálogo y lo devuelve
        return builder.create();

    }

    // Método para obtener el índice de un elemento en el spinner, basándose en la asignatura.
    private int getIndex(Spinner subjectSpinner, String subject) {
        for (int i = 0; i < subjectSpinner.getCount(); i++) {
            if (subjectSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(subject)) {
                return i; // Retorna el índice donde se encuentra la asignatura.
            }
        }
        return 0; // Si no se encuentra la asignatura, retorna 0 (primer elemento).
    }

    // Interfaz que debe implementarse en la actividad para recibir el evento cuando se guarda un deber.
    public interface OnHomeworkSavedListener {
        void onHomeworkSaved(Homework homework);
    }

    // Método para establecer el listener que maneja el evento de guardar un deber.
    public void setOnHomeworkSavedListener(NewHomeworkDialogFragment.OnHomeworkSavedListener listener) {
        this.listener = listener;
    }

    // Método para mostrar el selector de fecha.
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                getContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // Formatear la fecha seleccionada y establecerla en el campo de texto.
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dueDateEditText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    //Sin funciones lambda
    /*
    // Método para mostrar el selector de fecha sin usar funciones lambda.
private void showDatePickerDialog() {
    // Crear un objeto de calendario con la fecha actual.
    Calendar calendar = Calendar.getInstance();

    // Crear un listener para el DatePickerDialog.
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            // Formatear la fecha seleccionada en formato dd/MM/yyyy.
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            // Establecer la fecha en el campo de texto.
            dueDateEditText.setText(date);
        }
    };

    // Crear el DatePickerDialog con el listener y la fecha actual.
    DatePickerDialog datePickerDialog = new DatePickerDialog(
            getContext(), // El contexto donde se muestra el diálogo.
            dateSetListener, // El listener que maneja la selección de fecha.
            calendar.get(Calendar.YEAR), // Año actual.
            calendar.get(Calendar.MONTH), // Mes actual.
            calendar.get(Calendar.DAY_OF_MONTH) // Día actual.
    );

    // Mostrar el DatePickerDialog.
    datePickerDialog.show();
}

     */

    // Método para validar los campos del diálogo (descripción y fecha).
    private boolean validateInputs() {
        if (TextUtils.isEmpty(descriptionEditText.getText())) {
            descriptionEditText.setError("La descripción es obligatoria");
            return false;
        }
        if (TextUtils.isEmpty(dueDateEditText.getText())) {
            dueDateEditText.setError("La fecha de entrega es obligatoria");
            return false;
        }
        return true; // Si todos los campos son válidos, retorna true.
    }

}
