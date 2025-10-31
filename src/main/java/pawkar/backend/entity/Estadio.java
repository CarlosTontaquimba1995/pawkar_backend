package pawkar.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "estadios")
@SQLDelete(sql = "UPDATE estadios SET activo = false WHERE estadio_id = ?")
@SQLRestriction("activo = true")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estadio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estadio_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @Column(length = 50)
    private String estado = "Disponible";

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean activo = true;
}
