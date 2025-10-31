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
@SQLDelete(sql = "UPDATE estadios SET estado = false WHERE estadio_id = ?")
@SQLRestriction("estado = true")
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

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean estado = true;
}
