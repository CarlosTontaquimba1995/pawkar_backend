package pawkar.backend.request;

public class ResultadoPartidoRequest {
    private Integer golesLocal;
    private Integer golesVisitante;
    private String estado; // "FINALIZADO", "SUSPENDIDO", "APLAZADO"
    
    // Getters y Setters
    public Integer getGolesLocal() {
        return golesLocal;
    }
    
    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
    }
    
    public Integer getGolesVisitante() {
        return golesVisitante;
    }
    
    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
