package ua.javaexternal_shulzhenko.repair_agency.entities.forms;

import ua.javaexternal_shulzhenko.repair_agency.constants.Parameters;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;

import javax.servlet.http.HttpServletRequest;

public class UserEditingForm implements Form {


    private final int id;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final Role role;

    public UserEditingForm(HttpServletRequest req) {
        id = extractId(req);
        firstName = req.getParameter(Parameters.F_NAME);
        lastName = req.getParameter(Parameters.L_NAME);
        email = req.getParameter(Parameters.EMAIL);
        role = extractRole(req);
    }

    private int extractId(HttpServletRequest req) {
        String id = req.getParameter(Parameters.EDITING_USER_ID);
        if(id != null){
            return Integer.parseInt(id);
        }
        return 0;
    }

    private Role extractRole(HttpServletRequest req) {
        String role = req.getParameter(Parameters.ROLE);
        if(role != null){
            return Role.valueOf(role);
        }
        return null;
    }

    public int getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() { return lastName; }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
