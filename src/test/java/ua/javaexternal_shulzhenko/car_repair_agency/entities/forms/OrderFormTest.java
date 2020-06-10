package ua.javaexternal_shulzhenko.car_repair_agency.entities.forms;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.RepairType;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class OrderFormTest {

    @Autowired
    private Validator validator;


    @ParameterizedTest
    @CsvSource({"Car brand, Car model-100, 2015"})
    public void validateOrderForm_withValidData_returnsNoViolations(
            String carBrand, String carModel, String carYear) {
        OrderForm orderForm = OrderForm
                .builder()
                .carBrand(carBrand)
                .carModel(carModel)
                .carYear(carYear)
                .repairType(RepairType.ENGINE_REPAIR)
                .repairDescription("Some repair description")
                .build();

        Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {" Brand", "Brand/", "1brand", "1234", "b", "Brand0123456789012345678901234567"})
    public void validateOrderForm_withInvalidCarBrand_returnsSingleCorrespondingViolation(String carBrand) {
        OrderForm orderForm = OrderForm
                .builder()
                .carBrand(carBrand)
                .carModel("Model-100")
                .carYear("2015")
                .repairType(RepairType.ENGINE_REPAIR)
                .repairDescription("Some repair description")
                .build();

        Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("carBrand", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {" Model", "Model+", "m", "Model0123456789012345678901234567"})
    public void validateOrderForm_withInvalidCarModel_returnsSingleCorrespondingViolation(
            String carModel) {
        OrderForm orderForm = OrderForm
                .builder()
                .carBrand("Brand")
                .carModel(carModel)
                .carYear("2015")
                .repairType(RepairType.ENGINE_REPAIR)
                .repairDescription("Some repair description")
                .build();

        Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("carModel", violations.stream().findAny().get().getPropertyPath().toString()));
    }


    @ParameterizedTest
    @ValueSource(strings = {"2100", "1899", "200+", "20001", "199"})
    public void validateOrderForm_withInvalidCarYear_returnsSingleCorrespondingViolation(String carYear) {
        OrderForm orderForm = OrderForm
                .builder()
                .carBrand("Brand")
                .carModel("Car model-100")
                .carYear(carYear)
                .repairType(RepairType.ENGINE_REPAIR)
                .repairDescription("Some repair description")
                .build();

        Set<ConstraintViolation<OrderForm>> violations = validator.validate(orderForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("carYear", violations.stream().findAny().get().getPropertyPath().toString()));
    }
}