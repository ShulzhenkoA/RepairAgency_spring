package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import ua.javaexternal_shulzhenko.repair_agency.constants.Parameters;
import ua.javaexternal_shulzhenko.repair_agency.constants.OrderStatus;

import javax.servlet.http.HttpServletRequest;

public class OrderEditingForm implements Form{

    private final int id;


    private final String price;
    private final String masterID;
    private final OrderStatus status;

    private final String managerComment;

    public OrderEditingForm(HttpServletRequest req) {
        id = extractId(req);
        price = req.getParameter(Parameters.PRICE);
        masterID = req.getParameter(Parameters.MASTER_ID);
        status = extractStatus(req);
        managerComment = req.getParameter(Parameters.MANAGER_COMMENT);
    }

    private int extractId(HttpServletRequest req) {
        String id = req.getParameter(Parameters.EDITING_ORDER_ID);
        if(id != null){
            return Integer.parseInt(id);
        }
        return 0;
    }

    private OrderStatus extractStatus(HttpServletRequest req) {
        String status = req.getParameter(Parameters.STATUS);
        if(status != null){
            return OrderStatus.valueOf(status);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String  getMasterID() {
        return masterID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getManagerComment() {
        return managerComment;
    }

}
