package ua.javaexternal_shulzhenko.car_repair_agency.entities.forms;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;
import static ua.javaexternal_shulzhenko.car_repair_agency.services.encoding.PasswordEncodingService.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {

    @Pattern(regexp = "^[\\p{L}](?=.*[\\p{L}])[- '\\p{L}]{1,63}")
    private String firstName;

    @Pattern(regexp = "^[\\p{L}](?=.*[\\p{L}])[- '\\p{L}]{1,63}")
    private String lastName;

    @Pattern(regexp = "[A-Za-z0-9._-]+@[A-Za-z0-9._-]+\\.[a-z]{2,4}")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$")
    private String password;

    private String passwordConfirmation;

    @NotNull
    private Role role;

    public boolean confirmationPassMatch(){
        return password.equals(passwordConfirmation);
    }

    public User extractUser(){

        BCryptPasswordEncoder bCryptPasswordEncoder = gerBCryptPasswordEncoder();

        return User
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .role(role)
                .build();
    }
}
