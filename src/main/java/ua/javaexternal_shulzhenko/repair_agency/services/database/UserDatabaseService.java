package ua.javaexternal_shulzhenko.repair_agency.services.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.UserEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.UserEditor;

import java.util.List;

public interface UserDatabaseService extends UserDetailsService {

    void createUser(User user);

    User getUserById(int userId);

    List<User> getUsersByRole(Role role);

    Page<User> getPageableUsersByRole(Role role, Pageable pageable);

    boolean userEmailIsAvailable(String email, int userID);

    boolean userEmailIsAvailable(String email);

    void editUser(User user, UserEditingForm editingForm, List<UserEditor.UserEdits> edits);

    void deleteUser(int userId);
}
