package ua.javaexternal_shulzhenko.car_repair_agency.tags;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public final class EntityNumOnPage extends SimpleTagSupport {

    private String loop_count_num;
    private String page_num;
    private String entities_page_amount;
    int counter;
    int pageOffsetNum;
    int entitiesPerPage;

    @Override
    public void doTag() throws IOException {

        getCounterFromAttr();
        getPageOffsetNumFromAttr();
        getEntitiesPerPageFromAttr();

        int entityNum = computeEntityNum();
        if (entityNum > 0) {
            getJspContext().getOut().print(entityNum);
        }
    }

    private void getCounterFromAttr() {
        if (loop_count_num.trim().length() != 0) {
            counter = Integer.parseInt(loop_count_num);
        }
    }

    private void getEntitiesPerPageFromAttr() {
        if (entities_page_amount.trim().length() != 0) {
            entitiesPerPage = Integer.parseInt(entities_page_amount);
        }
    }

    private void getPageOffsetNumFromAttr() {
        if (page_num.trim().length() != 0) {
            pageOffsetNum = Integer.parseInt(page_num) - 1;
        }
    }

    private int computeEntityNum() {
        return pageOffsetNum * entitiesPerPage + counter;
    }

    public void setLoop_count_num(String loop_count_num) {
        this.loop_count_num = loop_count_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public void setEntities_page_amount(String entities_page_amount) {
        this.entities_page_amount = entities_page_amount;
    }
}
