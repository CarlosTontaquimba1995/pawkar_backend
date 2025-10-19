package pawkar.backend.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class BulkSerieRequest {
    @NotNull(message = "La lista de series no puede ser nula")
    @Size(min = 1, message = "Debe proporcionar al menos una serie")
    private List<@Valid SerieRequest> series;

    public List<SerieRequest> getSeries() {
        return series;
    }

    public void setSeries(List<SerieRequest> series) {
        this.series = series;
    }
}
