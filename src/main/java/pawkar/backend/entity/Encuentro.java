package pawkar.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "encuentros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Encuentro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "encuentro_id", columnDefinition = "SERIAL")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "subcategoria_id", nullable = false)
    private Subcategoria subcategoria;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "estadio_lugar", nullable = false, length = 150)
    private String estadioLugar;

    @Column(length = 50)
    private String estado = "Pendiente";

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;
}
