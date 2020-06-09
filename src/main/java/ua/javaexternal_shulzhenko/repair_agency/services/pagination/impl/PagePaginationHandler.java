package ua.javaexternal_shulzhenko.repair_agency.services.pagination.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.repair_agency.constants.CommonConstants;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PageAddress;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PaginationModel;
import ua.javaexternal_shulzhenko.repair_agency.services.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagePaginationHandler implements Pagination {

    public PaginationModel createPaginationModel(String currentUri, Page<?> page) {

        int totalPages = page.getTotalPages();
        int currentPageNum = page.getNumber() + 1;

        if (totalPages < 2) { return null; }

        String previousUrl = createPreviousUri(currentUri, page);
        String nextUrl = createNextUri(currentUri, page);
        List<PageAddress> pagesAddresses = new ArrayList<>();

        if (currentPageNum > 4 && currentPageNum < totalPages - 3) {
            formMiddlePaginationModel(currentUri, currentPageNum, totalPages, pagesAddresses);
        } else if (currentPageNum > 4) {
            formLeftPaginationModel(currentUri, currentPageNum, totalPages, pagesAddresses);
        } else if (currentPageNum < totalPages - 3) {
            formRightPaginationModel(currentUri, currentPageNum, totalPages, pagesAddresses);
        } else {
            createSidePages(pagesAddresses, currentUri, currentPageNum, 1, totalPages);
        }
        return new PaginationModel(previousUrl, nextUrl, pagesAddresses);
    }

    private static void formMiddlePaginationModel(String currentUri, int currentPageNum, int totalPages, List<PageAddress> pages) {
        formLeftPaginationModel(currentUri, currentPageNum, currentPageNum + 2, pages);
        createSinglePage(pages, CommonConstants.ELLIPSIS, -1);
        createSinglePage(pages, currentUri, totalPages);
    }

    private static void formLeftPaginationModel(String currentUri, int currentPageNum, int totalPages, List<PageAddress> pages) {
        createSinglePage(pages, currentUri, 1);
        createSinglePage(pages, CommonConstants.ELLIPSIS, -1);
        createSidePages(pages, currentUri, currentPageNum, currentPageNum - 2, totalPages);
    }

    private static void formRightPaginationModel(String currentUri, int currentPageNum, int totalPages, List<PageAddress> pages) {
        createSidePages(pages, currentUri, currentPageNum, 1, currentPageNum + 2);
        createSinglePage(pages, CommonConstants.ELLIPSIS, -1);
        createSinglePage(pages, currentUri, totalPages);
    }

    private static String createPreviousUri(String currentUri, Page<?> page) {
        if (page.hasPrevious()) {
            return setPageUri(currentUri, page.getNumber());
        } else {
            return null;
        }
    }

    private static String createNextUri(String currentUri, Page<?> page) {
        if (page.hasNext()) {
            return setPageUri(currentUri, page.getNumber() + 2);
        } else {
            return null;
        }
    }

    private static String setPageUri(String currentUri, int pageNum) {
        if (pageNum > 1 && !currentUri.equals(CommonConstants.CURRENT)) {
            return currentUri + CommonConstants.PAGE_EQUAL + pageNum;
        } else {
            return currentUri;
        }
    }

    private static void createSinglePage(List<PageAddress> pages, String currentUri, int pageNum) {
        pages.add(new PageAddress(setPageUri(currentUri, pageNum), pageNum ));
    }

    private static void createSidePages(List<PageAddress> pages, String currentUri, int currentPage, int startPage, int totalPages) {
        for (int i = startPage; i <= totalPages; i++) {
            if (i == currentPage) {
                createSinglePage(pages, CommonConstants.CURRENT, i);
            } else {
                createSinglePage(pages, currentUri, i);
            }
        }
    }
}