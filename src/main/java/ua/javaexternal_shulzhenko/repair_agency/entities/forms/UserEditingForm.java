package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import lombok.Getter;
import lombok.Setter;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserEditingForm {

    @NotNull
    private int id;

    @Pattern(regexp = "^[\\p{L}](?=.*[\\p{L}])[- '\\p{L}]{1,63}")
    private String firstName;

    @Pattern(regexp = "^[\\p{L}](?=.*[\\p{L}])[- '\\p{L}]{1,63}")
    private String lastName;

    @Pattern(regexp = "[A-Za-z0-9._-]+@[A-Za-z0-9._-]+\\.[a-z]{2,4}")
    private String email;

    @NotNull
    private Role role;
}
