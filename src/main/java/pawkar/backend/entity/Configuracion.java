package pawkar.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuraciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "configuracion_id")
    private Long configuracionId;

    @Column(name = "primario", nullable = false, length = 20)
    private String primario;

    @Column(name = "secundario", nullable = false, length = 20)
    private String secundario;

    @Column(name = "acento_1", nullable = false, length = 20)
    private String acento1;

    @Column(name = "acento_2", nullable = false, length = 20)
    private String acento2;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
