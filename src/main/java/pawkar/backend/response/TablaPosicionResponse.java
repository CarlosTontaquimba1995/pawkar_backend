package pawkar.backend.response;

public class TablaPosicionResponse {
    private Integer subcategoriaId;
    private Integer equipoId;
    private String equipoNombre;
    private Integer partidosJugados;
    private Integer victorias;
    private Integer derrotas;
    private Integer empates;
    private Integer puntos;
    private Integer posicion;

    // Constructors
    public TablaPosicionResponse() {}

    public TablaPosicionResponse(Integer subcategoriaId, Integer equipoId, String equipoNombre, 
                               Integer partidosJugados, Integer victorias, Integer derrotas, 
                               Integer empates, Integer puntos, Integer posicion) {
        this.subcategoriaId = subcategoriaId;
        this.equipoId = equipoId;
        this.equipoNombre = equipoNombre;
        this.partidosJugados = partidosJugados;
        this.victorias = victorias;
        this.derrotas = derrotas;
        this.empates = empates;
        this.puntos = puntos;
        this.posicion = posicion;
    }

    // Getters and Setters
    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }

    public String getEquipoNombre() {
        return equipoNombre;
    }

    public void setEquipoNombre(String equipoNombre) {
        this.equipoNombre = equipoNombre;
    }

    public Integer getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(Integer partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public Integer getVictorias() {
        return victorias;
    }

    public void setVictorias(Integer victorias) {
        this.victorias = victorias;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }

    public Integer getEmpates() {
        return empates;
    }

    public void setEmpates(Integer empates) {
        this.empates = empates;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
}
