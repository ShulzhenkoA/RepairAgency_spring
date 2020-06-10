package ua.javaexternal_shulzhenko.car_repair_agency.services.editing.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.car_repair_agency.services.editing.Editor;

import java.util.LinkedList;
import java.util.List;

@Service
@Scope("prototype")
public class OrderEditor implements Editor {
    private OrderEditingForm form;
    private Order order;
    private final List<OrderEdits> edits;

    public OrderEditor() {
        edits = new LinkedList<>();
    }

    public OrderEditor comparePrice() {
        double formPrice = Double.parseDouble(form.getPrice());
        if (formPrice > 0 && formPrice != order.getPrice()) {
            edits.add(OrderEdits.PRICE);
        }
        return this;
    }

    public OrderEditor compareMasters() {
        int masterID = form.getMasterID();
        if (masterID != 0) {
            if (order.getMaster() == null) {
                edits.add(OrderEdits.MASTER_ID);
            } else if (masterID != order.getMaster().getId()) {
                edits.add(OrderEdits.MASTER_ID);
            }
        }
        return this;
    }

    public OrderEditor compareStatus() {
        if (!form.getStatus().equals(order.getStatus())) {
            edits.add(OrderEdits.STATUS);
        }
        return this;
    }

    public OrderEditor compareManagerComment() {
        if (!form.getManagerComment().equals(order.getManagerComment())) {
            edits.add(OrderEdits.MANAGER_COMMENT);
        }
        return this;
    }

    public OrderEditor setForm(OrderEditingForm form) {
        this.form = form;
        return this;
    }

    public OrderEditor setOrder(Order order) {
        this.order = order;
        return this;
    }

    @Override
    public List<OrderEdits> getEdits() {
        return edits;
    }

    public enum OrderEdits {
        PRICE,
        MASTER_ID,
        STATUS,
        MANAGER_COMMENT
    }
}
