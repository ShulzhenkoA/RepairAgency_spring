package ua.javaexternal_shulzhenko.repair_agency.services.pagination;

import org.springframework.data.domain.Page;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PaginationModel;

public interface Pagination {
    PaginationModel createPaginationModel(String currentUri, Page<?> page);
}
