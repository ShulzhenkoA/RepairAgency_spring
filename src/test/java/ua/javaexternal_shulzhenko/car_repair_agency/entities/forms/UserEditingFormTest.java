package ua.javaexternal_shulzhenko.car_repair_agency.entities.forms;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.Role;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserEditingFormTest {

    @Autowired
    private Validator validator;

    @ParameterizedTest
    @CsvSource({"Username, Userlastname, user@mail.com"})
    public void validateRegistrationForm_withValidData_returnsNoViolations(String firstName, String lastName, String email) {
        UserEditingForm userEditingForm = UserEditingForm
                .builder()
                .id(1)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<UserEditingForm>> violations = validator.validate(userEditingForm);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"User1", "User?", " User", "U", "UserUserUserUserUserUserUserUserUserUserUserUserUserUserUserUserU"})
    public void validateRegistrationForm_withInvalidFirstName_returnsSingleCorrespondingViolation(String firstName) {
        UserEditingForm userEditingForm = UserEditingForm
                .builder()
                .id(1)
                .firstName(firstName)
                .lastName("Userlastname")
                .email("user@mail.com")
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<UserEditingForm>> violations = validator.validate(userEditingForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("firstName", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Lastname1", "Lastname?", " Lastname", "L", "LastnameLastnameLastnameLastnameLastnameLastnameLastnameLastnameL"})
    public void validateRegistrationForm_withInvalidLastName_returnsSingleCorrespondingViolation(String lastName) {
        UserEditingForm userEditingForm = UserEditingForm
                .builder()
                .id(1)
                .firstName("Username")
                .lastName(lastName)
                .email("user@mail.com")
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<UserEditingForm>> violations = validator.validate(userEditingForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("lastName", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"usermail.com", "user@mailcom", "user@.com", "user@mail.", "user@mail.c", "user@mail.commm",
            "user@mail.Com", "user@mail.co1", "user%@mail.com", "user@mail/.com", "юser@mail.com"})
    public void validateRegistrationForm_withInvalidEmail_returnsSingleCorrespondingViolation(String email) {
        UserEditingForm userEditingForm = UserEditingForm
                .builder()
                .id(1)
                .firstName("Username")
                .lastName("Userlastname")
                .email(email)
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<UserEditingForm>> violations = validator.validate(userEditingForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("email", violations.stream().findAny().get().getPropertyPath().toString()));
    }
}