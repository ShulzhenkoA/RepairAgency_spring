package ua.javaexternal_shulzhenko.repair_agency.services.editing;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CommonConstants;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;

public interface EditingOrderValidator {

    static boolean needMasterForThisStatus(OrderEditingForm form, Model model){
        int masterID = form.getMasterID();
        OrderStatus status = form.getStatus();
        if(!status.equals(OrderStatus.REJECTED) && masterID == 0){
            model.addAttribute(Attributes.MASTER, "");
            return true;
        } else {
            return false;
        }
    }

    static boolean needPreviousPrice(OrderEditingForm form, BindingResult bindingResult, Model model){
        if(!bindingResult.hasFieldErrors(Attributes.PRICE)){
            double price = Double.parseDouble(form.getPrice());
            OrderStatus status = form.getStatus();
            if(!status.equals(OrderStatus.REJECTED) && price <= 0){
                model.addAttribute(Attributes.PRICE, "");
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
