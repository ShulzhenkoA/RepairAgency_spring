package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class OrderEditingFormTest {

    @Autowired
    private Validator validator;
   
    @ParameterizedTest
    @ValueSource(strings = {"100", "0.01", "99.99", "0", "0.0", "0,1"})
    public void validateOrderEditingForm_withValidData_returnNoViolations(String price) {
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
    public void validateOrderEditingForm_withInvalidPrice_returnSingleCorrespondingViolation(String price) {
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
    public void validateOrderEditingForm_withBlankComment_returnSingleCorrespondingViolation(String managerComment) {
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