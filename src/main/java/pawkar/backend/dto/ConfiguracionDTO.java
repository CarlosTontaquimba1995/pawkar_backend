package pawkar.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long configuracionId;
    private String primario;
    private String secundario;
    private String acento1;
    private String acento2;
}