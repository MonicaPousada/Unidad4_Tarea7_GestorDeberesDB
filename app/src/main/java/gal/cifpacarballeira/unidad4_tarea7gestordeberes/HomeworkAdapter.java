package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {
    // Lista de tareas que se va a mostrar en el RecyclerView
    private final List<Homework> homeworkList;
    // Interfaz que manejará el clic en una tarea
    private final OnHomeworkClickListener listener;

    // Constructor que recibe la lista de tareas y el listener para manejar clics
    public HomeworkAdapter(List<Homework> homeworkList, OnHomeworkClickListener listener) {
        this.homeworkList = homeworkList;
        this.listener = listener;
    }

    // Método encargado de crear las vistas para los elementos del RecyclerView
    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de un item (tarea) y crear el ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework, parent, false);
        return new HomeworkViewHolder(view); // Devolver el ViewHolder con la vista inflada
    }

    // Método que vincula los datos del objeto Homework con la vista en el ViewHolder
    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder holder, int position) {
        // Obtener el objeto Homework en la posición correspondiente
        Homework homework = homeworkList.get(position);

        // Asignar los datos del objeto Homework a los elementos de la vista
        holder.subjectTextView.setText(homework.getSubject());
        holder.descriptionTextView.setText(homework.getDescription());
        holder.dueDateTextView.setText(homework.getDueDate());
        holder.statusTextView.setText(homework.isCompleted() ? "Completado" : "Pendiente");

        // Manejar el clic en el item
        //holder.itemView.setOnClickListener(v -> listener.onHomeworkClick(homework));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar al método onHomeworkClick del listener y pasarle la tarea
                listener.onHomeworkClick(homework);
            }
        });

    }

    // Método que devuelve la cantidad de elementos en la lista
    @Override
    public int getItemCount() {
        return homeworkList.size(); // Devolver el tamaño de la lista
    }

    // ViewHolder es una clase interna que extiende RecyclerView.ViewHolder
    public static class HomeworkViewHolder extends RecyclerView.ViewHolder {
        // Referencias a los TextViews que mostrarán los datos de la tarea
        TextView subjectTextView;
        TextView descriptionTextView;
        TextView dueDateTextView;
        TextView statusTextView;

        // Constructor del ViewHolder que recibe la vista de un item
        public HomeworkViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas usando findViewById
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }

    // Interfaz para manejar el clic en un item de la lista
    public interface OnHomeworkClickListener {
        void onHomeworkClick(Homework homework); // Método que se ejecuta cuando se hace clic en un item
    }
}

