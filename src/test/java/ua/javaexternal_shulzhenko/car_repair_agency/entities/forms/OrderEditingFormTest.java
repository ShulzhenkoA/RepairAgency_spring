package ua.javaexternal_shulzhenko.car_repair_agency.entities.forms;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderEditingFormTest {

    @Autowired
    private Validator validator;
   
    @ParameterizedTest
    @ValueSource(strings = {"100", "0.01", "99.99", "0", "0.0", "0,1"})
    public void validateOrderEditingForm_withValidData_returnsNoViolations(String price) {
        OrderEditingForm orderEditingForm = OrderEditingForm
                .builder()
                .price(price)
                .managerComment("Manager comment")
                .build();

        Set<ConstraintViolation<OrderEditingForm>> violations = validator.validate(orderEditingForm);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1b", "1.0.0", "0.001", "1+0", ".0", "1."})
    public void validateOrderEditingForm_withInvalidPrice_returnsSingleCorrespondingViolation(String price) {
        OrderEditingForm orderEditingForm = OrderEditingForm
                .builder()
                .price(price)
                .managerComment("Manager comment")
                .build();

        Set<ConstraintViolation<OrderEditingForm>> violations = validator.validate(orderEditingForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("price", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @ValueSource(strings = " ")
    public void validateOrderEditingForm_withBlankComment_returnsSingleCorrespondingViolation(String managerComment) {
        OrderEditingForm orderEditingForm = OrderEditingForm
                .builder()
                .price("100")
                .managerComment(managerComment)
                .build();

        Set<ConstraintViolation<OrderEditingForm>> violations = validator.validate(orderEditingForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("managerComment", violations.stream().findAny().get().getPropertyPath().toString()));
    }
}