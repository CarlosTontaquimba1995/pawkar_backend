package pawkar.backend.response;

public class ArtistaResponse {
    private Long artistaId;
    private String nombre;
    private String genero;

    public ArtistaResponse() {
    }

    public ArtistaResponse(Long artistaId, String nombre, String genero) {
        this.artistaId = artistaId;
        this.nombre = nombre;
        this.genero = genero;
    }

    // Getters and Setters
    public Long getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(Long artistaId) {
        this.artistaId = artistaId;
    }

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
