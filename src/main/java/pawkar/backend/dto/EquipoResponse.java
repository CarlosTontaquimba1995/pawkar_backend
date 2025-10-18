package pawkar.backend.dto;

import java.time.LocalDate;

public class EquipoResponse {
    private Integer equipoId;
    private Integer subcategoriaId;
    private String subcategoriaNombre;
    private Integer serieId;
    private String serieNombre;
    private String nombre;
    private LocalDate fundacion;

    // Getters and Setters
    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }

    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public String getSubcategoriaNombre() {
        return subcategoriaNombre;
    }

    public void setSubcategoriaNombre(String subcategoriaNombre) {
        this.subcategoriaNombre = subcategoriaNombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFundacion() {
        return fundacion;
    }

    public void setFundacion(LocalDate fundacion) {
        this.fundacion = fundacion;
    }
}
