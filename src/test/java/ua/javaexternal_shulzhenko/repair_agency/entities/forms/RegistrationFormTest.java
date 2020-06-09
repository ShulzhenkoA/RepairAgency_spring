package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationFormTest {

    @Autowired
    private Validator validator;

    @ParameterizedTest
    @CsvSource({"Username, Userlastname, user@mail.com, User1234"})
    public void validateRegistrationForm_withValidData_returnsNoViolations(
            String firstName, String lastName, String email, String password) {
        RegistrationForm registrationForm = RegistrationForm
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(Role.CUSTOMER)
                .build();
        Set<ConstraintViolation<RegistrationForm>> violations = validator.validate(registrationForm);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"User1", "User?", " User", "U", "UserUserUserUserUserUserUserUserUserUserUserUserUserUserUserUserU"})
    public void validateRegistrationForm_withInvalidFirstName_returnsSingleCorrespondingViolation(String firstName) {

        RegistrationForm registrationForm = RegistrationForm
                .builder()
                .firstName(firstName)
                .lastName("Userlastname")
                .email("user@mail.com")
                .password("User1234")
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<RegistrationForm>> violations = validator.validate(registrationForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("firstName", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Lastname1", "Lastname?", " Lastname", "L", "LastnameLastnameLastnameLastnameLastnameLastnameLastnameLastnameL"})
    public void validateRegistrationForm_withInvalidLastName_returnsSingleCorrespondingViolation(String lastName) {

        RegistrationForm registrationForm = RegistrationForm
                .builder()
                .firstName("Username")
                .lastName(lastName)
                .email("user@mail.com")
                .password("User1234")
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<RegistrationForm>> violations = validator.validate(registrationForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("lastName", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"usermail.com", "user@mailcom", "user@.com", "user@mail.", "user@mail.c", "user@mail.commm",
            "user@mail.Com", "user@mail.co1", "user%@mail.com", "user@mail/.com", "юser@mail.com"})
    public void validateRegistrationForm_withInvalidEmail_returnsSingleCorrespondingViolation(String email) {

        RegistrationForm registrationForm = RegistrationForm
                .builder()
                .firstName("Username")
                .lastName("Userlastname")
                .email(email)
                .password("User1234")
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<RegistrationForm>> violations = validator.validate(registrationForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("email", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @CsvSource({"User123", "User12345678901234567", "user1234", "U1234567", "Userpass", "User1234%", "Юser1234"})
    public void validateRegistrationForm_withInvalidPassword_returnsSingleCorrespondingViolation(String password) {

        RegistrationForm registrationForm = RegistrationForm
                .builder()
                .firstName("Username")
                .lastName("Userlastname")
                .email("user@mail.com")
                .password(password)
                .role(Role.CUSTOMER)
                .build();

        Set<ConstraintViolation<RegistrationForm>> violations = validator.validate(registrationForm);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("password", violations.stream().findAny().get().getPropertyPath().toString()));
    }

    @ParameterizedTest
    @CsvSource("User1234, User1234")
    public void checkingConfirmationPassword_withCorrectConfirmation_returnsTrue(
            String password, String passwordConfirmation){

        RegistrationForm registrationForm = RegistrationForm
                .builder()
                .password(password)
                .passwordConfirmation(passwordConfirmation)
                .build();

        assertTrue(registrationForm.confirmationPassMatch());
    }

    @ParameterizedTest
    @CsvSource("User1234, User12345678")
    public void checkingConfirmationPassword_withWrongConfirmation_returnsFalse(
            String password, String passwordConfirmation){

        RegistrationForm registrationForm = RegistrationForm
                .builder()
                .password(password)
                .passwordConfirmation(passwordConfirmation)
                .build();

        assertFalse(registrationForm.confirmationPassMatch());
    }
}