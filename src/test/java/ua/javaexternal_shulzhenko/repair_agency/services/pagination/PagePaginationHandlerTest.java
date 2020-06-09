package ua.javaexternal_shulzhenko.repair_agency.services.pagination;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PageAddress;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PaginationModel;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PagePaginationHandlerTest {

    @Autowired
    private Pagination pagination;

    @MockBean
    private Page<Review> page;

    @ParameterizedTest
    @CsvSource({"/somePage, 1, 1"})
    void creationPagModel_totalPagesEqualOne_givesNullPagModel(
            String currentUri, int currentPageNum, int totalPages) {

        when(page.getNumber()).thenReturn(currentPageNum - 1);
        when(page.getTotalPages()).thenReturn(totalPages);

        PaginationModel paginationModel = pagination.createPaginationModel(currentUri, page);

        assertNull(paginationModel);
    }

    @ParameterizedTest
    @CsvSource({"/somePage, 1, 10, ,false",
            "/somePage, 6, 10, /somePage?page=5, true",
            "/somePage, 10, 10, /somePage?page=9, true"})
    void creationPagModel_concretePage_givesCorrespondingPrevPageUri(
            String currentUri, int currentPageNum, int totalPages, String previousUri, boolean hasPrevious) {

        when(page.getNumber()).thenReturn(currentPageNum - 1);
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.hasPrevious()).thenReturn(hasPrevious);

        PaginationModel paginationModel = pagination.createPaginationModel(currentUri, page);

        assert paginationModel != null;
        assertEquals(previousUri, paginationModel.getPreviousUri());
    }

    @ParameterizedTest
    @CsvSource({"/somePage, 1, 10, /somePage?page=2, true",
            "/somePage, 6, 10, /somePage?page=7, true",
            "/somePage, 10, 10, ,false"})
    void creationPagModel_concretePage_givesCorrespondingNextPageUri(
            String currentUri, int currentPageNum, int totalPages, String nextUri, boolean hasNext) {

        when(page.getNumber()).thenReturn(currentPageNum - 1);
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.hasNext()).thenReturn(hasNext);

        PaginationModel paginationModel = pagination.createPaginationModel(currentUri, page);

        assert paginationModel != null;
        assertEquals(nextUri, paginationModel.getNextUri());
    }

    @ParameterizedTest
    @CsvSource({"/somePage, 1, 10, current, /somePage?page=2, /somePage?page=3, ellipsis, /somePage?page=10"})
    void creationPagModel_fromFirstPage_createsCurrent_NexTwo_Ellipsis_Last_Pages(
            String currentUri, int currentPageNum, int totalPages,
            String current, String nextPage, String secondNextPage, String ellipsis, String lastPage) {

        when(page.getNumber()).thenReturn(currentPageNum - 1);
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.hasNext()).thenReturn(true);

        PaginationModel paginationModel = pagination.createPaginationModel(currentUri, page);

        assert paginationModel != null;
        List<PageAddress> pagPages = paginationModel.getPages();

        assertAll(
                () -> assertEquals(current, pagPages.get(0).getPageUri()),
                () -> assertEquals(nextPage, pagPages.get(1).getPageUri()),
                () -> assertEquals(secondNextPage, pagPages.get(2).getPageUri()),
                () -> assertEquals(ellipsis, pagPages.get(3).getPageUri()),
                () -> assertEquals(lastPage, pagPages.get(4).getPageUri()));
    }


    @ParameterizedTest
    @CsvSource({"/somePage, 10, 10, /somePage, ellipsis, /somePage?page=8, /somePage?page=9, current"})
    void creationPagModel_fromLastPage_createsFirst_Ellipsis_PrevTwo_Current_Pages(
            String currentUri, int currentPageNum, int totalPages,
            String firstPage, String ellipsis, String prevSecondPage, String prevPage, String current) {

        when(page.getNumber()).thenReturn(currentPageNum - 1);
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.hasPrevious()).thenReturn(true);

        PaginationModel paginationModel = pagination.createPaginationModel(currentUri, page);

        List<PageAddress> pagPages = paginationModel.getPages();

        assertAll(
                () -> assertEquals(firstPage, pagPages.get(0).getPageUri()),
                () -> assertEquals(ellipsis, pagPages.get(1).getPageUri()),
                () -> assertEquals(prevSecondPage, pagPages.get(2).getPageUri()),
                () -> assertEquals(prevPage, pagPages.get(3).getPageUri()),
                () -> assertEquals(current, pagPages.get(4).getPageUri()));
    }

    @ParameterizedTest
    @CsvSource({
                    "/somePage, 5, 10, " +
                    "/somePage, ellipsis, /somePage?page=3, /somePage?page=4, current, " +
                    "/somePage?page=6, /somePage?page=7, ellipsis, /somePage?page=10"})
    void creationPagModel_fromMiddlePage_createsFirst_Ellipsis_PrevTwo_Current_NextTwo_Ellipsis_Last_Pages(
            String currentUri, int currentPageNum, int totalPages,
            String firstPage, String firstEllipsis, String prevSecondPage, String prevPage, String current,
            String nextPage, String secondNextPage, String secondEllipsis, String lastPage) {

        when(page.getNumber()).thenReturn(currentPageNum - 1);
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.hasPrevious()).thenReturn(true);
        when(page.hasNext()).thenReturn(true);

        PaginationModel paginationModel = pagination.createPaginationModel(currentUri, page);

        List<PageAddress> pagPages = paginationModel.getPages();

        assertAll(
                () -> assertEquals(firstPage, pagPages.get(0).getPageUri()),
                () -> assertEquals(firstEllipsis, pagPages.get(1).getPageUri()),
                () -> assertEquals(prevSecondPage, pagPages.get(2).getPageUri()),
                () -> assertEquals(prevPage, pagPages.get(3).getPageUri()),
                () -> assertEquals(current, pagPages.get(4).getPageUri()),
                () -> assertEquals(nextPage, pagPages.get(5).getPageUri()),
                () -> assertEquals(secondNextPage, pagPages.get(6).getPageUri()),
                () -> assertEquals(secondEllipsis, pagPages.get(7).getPageUri()),
                () -> assertEquals(lastPage, pagPages.get(8).getPageUri()));
    }


    @ParameterizedTest
    @CsvSource({
            "/somePage, 4, 7, /somePage, /somePage?page=2, /somePage?page=3, current, " +
                    "/somePage?page=5, /somePage?page=6, /somePage?page=7"})
    void creationPagModel_pageWithoutEllipsis_createsFirst_PrevTwo_Current_NextTwo_Last_Pages(
            String currentUri, int currentPageNum, int totalPages,
            String firstPage, String prevSecondPage, String prevPage, String current,
            String nextPage, String secondNextPage, String lastPage) {

        when(page.getNumber()).thenReturn(currentPageNum - 1);
        when(page.getTotalPages()).thenReturn(totalPages);
        when(page.hasPrevious()).thenReturn(true);
        when(page.hasNext()).thenReturn(true);

        PaginationModel paginationModel = pagination.createPaginationModel(currentUri, page);

        List<PageAddress> pagPages = paginationModel.getPages();

        assertAll(
                () -> assertEquals(firstPage, pagPages.get(0).getPageUri()),
                () -> assertEquals(prevSecondPage, pagPages.get(1).getPageUri()),
                () -> assertEquals(prevPage, pagPages.get(2).getPageUri()),
                () -> assertEquals(current, pagPages.get(3).getPageUri()),
                () -> assertEquals(nextPage, pagPages.get(4).getPageUri()),
                () -> assertEquals(secondNextPage, pagPages.get(5).getPageUri()),
                () -> assertEquals(lastPage, pagPages.get(6).getPageUri()));
    }
}