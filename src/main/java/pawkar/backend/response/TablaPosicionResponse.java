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
    private Integer golesAFavor;
    private Integer golesEnContra;
    private Integer diferenciaGoles;
    private Integer posicion;
    private Integer serieId;
    private String serieNombre;
    private Integer categoriaId;
    private String categoriaNombre;

    // Constructors
    public TablaPosicionResponse() {}

    public TablaPosicionResponse(Integer subcategoriaId, Integer equipoId, String equipoNombre, 
                               Integer partidosJugados, Integer victorias, Integer derrotas, 
                               Integer empates, Integer golesAFavor, Integer golesEnContra,
                               Integer diferenciaGoles, Integer puntos, Integer posicion) {
        this.subcategoriaId = subcategoriaId;
        this.equipoId = equipoId;
        this.equipoNombre = equipoNombre;
        this.partidosJugados = partidosJugados;
        this.victorias = victorias;
        this.derrotas = derrotas;
        this.empates = empates;
        this.golesAFavor = golesAFavor;
        this.golesEnContra = golesEnContra;
        this.diferenciaGoles = diferenciaGoles;
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

    public Integer getGolesAFavor() {
        return golesAFavor;
    }

    public void setGolesAFavor(Integer golesAFavor) {
        this.golesAFavor = golesAFavor;
    }

    public Integer getGolesEnContra() {
        return golesEnContra;
    }

    public void setGolesEnContra(Integer golesEnContra) {
        this.golesEnContra = golesEnContra;
    }

    public Integer getDiferenciaGoles() {
        return diferenciaGoles;
    }

    public void setDiferenciaGoles(Integer diferenciaGoles) {
        this.diferenciaGoles = diferenciaGoles;
    }

    public Integer getSerieId() {
        return serieId;
    }

    public void setSerieId(Integer serieId) {
        this.serieId = serieId;
    }

    public String getSerieNombre() {
        return serieNombre;
    }

    public void setSerieNombre(String serieNombre) {
        this.serieNombre = serieNombre;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }
}
