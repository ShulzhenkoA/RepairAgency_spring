package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class LoginForm implements Form {

    @Pattern(regexp = "[A-Za-z0-9._-]+@[A-Za-z0-9._-]+\\.[a-z]{2,4}", message = "ERROR")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$")
    private String password;
}
