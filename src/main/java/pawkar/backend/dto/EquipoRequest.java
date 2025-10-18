package pawkar.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EquipoRequest {
    @NotNull(message = "El ID de subcategoría es obligatorio")
    private Integer subcategoriaId;
    
    @NotNull(message = "El ID de serie es obligatorio")
    private Integer serieId;
    
    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;
    
    private LocalDate fundacion;

    // Getters and Setters
    public Integer getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Integer subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public Integer getSerieId() {
        return serieId;
    }

    public void setSerieId(Integer serieId) {
        this.serieId = serieId;
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
