package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.os.Parcel;
import android.os.Parcelable;

public class Homework implements Parcelable {

    private Integer id;
    private String subject; // Asignatura (PMDM, AD, etc.)
    private String description; // Descripción del deber
    private String dueDate; // Fecha de entrega en formato dd/MM/yyyy
    private boolean isCompleted; // Estado del deber

    // Constructor
    public Homework(Integer id, String subject, String description, String dueDate, boolean isCompleted) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public Homework(String subject, String description, String dueDate, boolean isCompleted) {
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    // Implementación de Parcelable para permitir que la tarea se pase entre actividades
    // Constructor que recibe un objeto Parcel
    protected Homework(Parcel in) {
        subject = in.readString(); // Leer la asignatura desde el Parcel
        description = in.readString(); // Leer la descripción desde el Parcel
        dueDate = in.readString(); // Leer la fecha de entrega desde el Parcel
        isCompleted = in.readByte() != 0; // Leer el estado de completado (convertido de byte)
    }

    // CREATOR es necesario para poder crear objetos Homework a partir de un Parcel
    public static final Creator<Homework> CREATOR = new Creator<Homework>() {
        @Override
        public Homework createFromParcel(Parcel in) {
            return new Homework(in);// Crear un nuevo objeto Homework desde el Parcel
        }

        @Override
        public Homework[] newArray(int size) {
            return new Homework[size]; // Crear un arreglo de objetos Homework
        }
    };

    // Método obligatorio para la interfaz Parcelable
    @Override
    public int describeContents() {
        return 0; // En este caso no se usa, por lo que devuelve 0
    }

    // Método obligatorio para la interfaz Parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject); // Escribir la asignatura en el Parcel
        dest.writeString(description); // Escribir la descripción en el Parcel
        dest.writeString(dueDate); // Escribir la fecha de entrega en el Parcel
        dest.writeByte((byte) (isCompleted ? 1 : 0)); // Escribir el estado de completado (convertido a byte)
    }
}

