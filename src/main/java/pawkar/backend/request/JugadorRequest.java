package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class JugadorRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String apellido;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(max = 20, message = "El documento de identidad no puede tener más de 20 caracteres")
    private String documentoIdentidad;

    private Integer equipoId;

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }
}
