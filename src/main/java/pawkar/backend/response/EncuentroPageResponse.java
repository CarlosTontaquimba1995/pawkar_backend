package pawkar.backend.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import pawkar.backend.request.EncuentroResponse;

import java.util.List;

@Getter
@Setter
public class EncuentroPageResponse {
    private List<EncuentroResponse> content;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
    private boolean hasNext;
    private boolean hasPrevious;

    public EncuentroPageResponse(Page<EncuentroResponse> page) {
        this.content = page.getContent();
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.itemsPerPage = page.getSize();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }
}
