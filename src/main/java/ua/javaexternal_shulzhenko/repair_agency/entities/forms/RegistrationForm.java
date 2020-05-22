package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.Parameters;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RegistrationForm implements Form {


    private final String firstName;

    private final String lastName;

    private final String email;

    private final String password;

    private final String passwordConfirmation;

    private final Role role;

    public RegistrationForm(HttpServletRequest req) {
        firstName = req.getParameter(Parameters.F_NAME);
        lastName = req.getParameter(Parameters.L_NAME);
        email = req.getParameter(Parameters.EMAIL);
        password = req.getParameter(Parameters.PASS);
        passwordConfirmation = req.getParameter(Parameters.PASS_CONF);
        role = extractRole(req);
    }

    private Role extractRole(HttpServletRequest req) {
        String role = req.getParameter(Parameters.ROLE);
        if(role != null){
            return Role.valueOf(role);
        }
        return null;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public Role getRole() {
        return role;
    }
}
