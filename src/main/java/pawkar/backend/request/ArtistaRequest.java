package pawkar.backend.request;

import jakarta.validation.constraints.NotBlank;

public class ArtistaRequest {
    @NotBlank(message = "El nombre del artista es obligatorio")
    private String nombre;
    
    private String genero;

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
