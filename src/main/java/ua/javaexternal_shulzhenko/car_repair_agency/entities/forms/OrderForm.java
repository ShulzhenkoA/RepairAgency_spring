package ua.javaexternal_shulzhenko.car_repair_agency.entities.forms;

import lombok.*;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.RepairType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm {

    @Pattern(regexp = "^[\\p{L}](?=.*[\\d\\p{L}])[- .\\d\\p{L}]{1,31}")
    private String carBrand;

    @Pattern(regexp = "^[\\d\\p{L}](?=.*[\\d\\p{L}])[- .\\d\\p{L}]{1,31}")
    private String carModel;

    @Pattern(regexp = "^(19|20)\\d{2}")
    private String carYear;

    @NotNull
    private RepairType repairType;

    @NotBlank
    private String repairDescription;

    public Order extractOrder(){
        return Order
                .builder()
                .car(Order.Car
                        .builder()
                        .carBrand(carBrand)
                        .carModel(carModel)
                        .carYear(carYear)
                        .build())
                .repairType(repairType)
                .repairDescription(repairDescription)
                .date(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();
    }
}
