package ua.javaexternal_shulzhenko.car_repair_agency.entities.pagination;

import java.util.List;

public class PaginationModel {

    private String previousUri;
    private String nextUri;
    private List<PageAddress> pages;

    public PaginationModel(String previousUri, String nextUri, List<PageAddress> pages) {
        this.previousUri = previousUri;
        this.nextUri = nextUri;
        this.pages = pages;
    }

    public boolean isPrevious(){
        return previousUri != null;
    }

    public boolean isNext(){
        return nextUri != null;
    }

    public String getPreviousUri() {
        return previousUri;
    }

    public String getNextUri() {
        return nextUri;
    }

    public List<PageAddress> getPages() {
        return pages;
    }
}
