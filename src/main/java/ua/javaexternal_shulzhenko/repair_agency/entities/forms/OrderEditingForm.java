package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import lombok.Getter;
import lombok.Setter;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class OrderEditingForm {

    private int id;

    @Pattern(regexp = "^\\d+((\\.|,)\\d{1,2})?$")
    private String price;

    private int masterID;

    private OrderStatus status;

    @NotBlank
    private String managerComment;
}
