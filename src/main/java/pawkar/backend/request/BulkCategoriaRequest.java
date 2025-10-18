package pawkar.backend.request;

import java.util.List;

public class BulkCategoriaRequest {
    private List<CategoriaRequest> categorias;

    public List<CategoriaRequest> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaRequest> categorias) {
        this.categorias = categorias;
    }
}
