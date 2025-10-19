package pawkar.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tabla_posiciones")
@IdClass(TablaPosicionId.class)
public class TablaPosicion {

    @Id
    @ManyToOne
    @JoinColumn(name = "subcategoria_id", referencedColumnName = "subcategoriaId", nullable = false)
    private Subcategoria subcategoria;

    @Id
    @ManyToOne
    @JoinColumn(name = "equipo_id", referencedColumnName = "equipo_id", nullable = false)
    private Equipo equipo;

    @Column(name = "partidos_jugados", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer partidosJugados = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer victorias = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer derrotas = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer empates = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer puntos = 0;

    @Column(name = "goles_a_favor", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer golesAFavor = 0;

    @Column(name = "goles_en_contra", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer golesEnContra = 0;

    @Column(name = "diferencia_goles", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer diferenciaGoles = 0;

    // Getters and Setters
    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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
}
