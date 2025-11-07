package pawkar.backend.request;

public class ActualizarTablaRequest {
    private Integer subcategoriaId;
    private Integer equipoLocalId;
    private Integer equipoVisitanteId;
    private Integer golesLocal;
    private Integer golesVisitante;
    private String estadoPartido;

    // Getters y Setters
    public Integer getSubcategoriaId() { return subcategoriaId; }
    public void setSubcategoriaId(Integer subcategoriaId) { this.subcategoriaId = subcategoriaId; }
    public Integer getEquipoLocalId() { return equipoLocalId; }
    public void setEquipoLocalId(Integer equipoLocalId) { this.equipoLocalId = equipoLocalId; }
    public Integer getEquipoVisitanteId() { return equipoVisitanteId; }
    public void setEquipoVisitanteId(Integer equipoVisitanteId) { this.equipoVisitanteId = equipoVisitanteId; }
    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }
    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }
    public String getEstadoPartido() { return estadoPartido; }
    public void setEstadoPartido(String estadoPartido) { this.estadoPartido = estadoPartido; }
}
