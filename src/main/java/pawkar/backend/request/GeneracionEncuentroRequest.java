package pawkar.backend.request;

import pawkar.backend.enums.TipoGeneracionEncuentro;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GeneracionEncuentroRequest {
    private Integer subcategoriaId;
    private TipoGeneracionEncuentro tipoGeneracion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer partidosPorDia;
    private LocalTime horaInicio;
    private Integer duracionPartidoMinutos;
    private Integer descansoEntrePartidosMinutos;
    private List<EncuentroManualRequest> encuentrosManuales;

    // Getters and Setters
    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public TipoGeneracionEncuentro getTipoGeneracion() {
        return tipoGeneracion;
    }

    public void setTipoGeneracion(TipoGeneracionEncuentro tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getPartidosPorDia() {
        return partidosPorDia;
    }

    public void setPartidosPorDia(Integer partidosPorDia) {
        this.partidosPorDia = partidosPorDia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getDuracionPartidoMinutos() {
        return duracionPartidoMinutos;
    }

    public void setDuracionPartidoMinutos(Integer duracionPartidoMinutos) {
        this.duracionPartidoMinutos = duracionPartidoMinutos;
    }

    public Integer getDescansoEntrePartidosMinutos() {
        return descansoEntrePartidosMinutos;
    }

    public void setDescansoEntrePartidosMinutos(Integer descansoEntrePartidosMinutos) {
        this.descansoEntrePartidosMinutos = descansoEntrePartidosMinutos;
    }

    public List<EncuentroManualRequest> getEncuentrosManuales() {
        return encuentrosManuales;
    }

    public void setEncuentrosManuales(List<EncuentroManualRequest> encuentrosManuales) {
        this.encuentrosManuales = encuentrosManuales;
    }

    public static class EncuentroManualRequest {
        private Integer equipoLocalId;
        private Integer equipoVisitanteId;
        private LocalDate fecha;
        private LocalTime hora;
        private String estadio;

        // Getters and Setters
        public Integer getEquipoLocalId() {
            return equipoLocalId;
        }

        public void setEquipoLocalId(Integer equipoLocalId) {
            this.equipoLocalId = equipoLocalId;
        }

        public Integer getEquipoVisitanteId() {
            return equipoVisitanteId;
        }

        public void setEquipoVisitanteId(Integer equipoVisitanteId) {
            this.equipoVisitanteId = equipoVisitanteId;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public LocalTime getHora() {
            return hora;
        }

        public void setHora(LocalTime hora) {
            this.hora = hora;
        }

        public String getEstadio() {
            return estadio;
        }

        public void setEstadio(String estadio) {
            this.estadio = estadio;
        }
    }
}
