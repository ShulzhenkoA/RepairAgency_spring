package ua.javaexternal_shulzhenko.repair_agency.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.UserEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.exceptions.DataBaseInteractionException;
import ua.javaexternal_shulzhenko.repair_agency.services.database.repository.UsersRepository;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.UserEditor;

import java.util.List;

@Service
public class UsersDBService implements UserDetailsService {

    private static UsersRepository userRepository;

    @Autowired
    public UsersDBService(UsersRepository userRepository) {
        UsersDBService.userRepository = userRepository;
    }

    public static void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findUsersByEmail(email);
    }

    public static User getUserById(int userId){
        return userRepository.getOne(userId);
    }

    public static List<User> getUsersByRole(Role role) {
        return userRepository.findAllByRoleOrderByIdDesc(role);
    }

    public static Page<User> getPageableUsersByRole(Role role, Pageable pageable) {
        return userRepository.findAllByRole(role, pageable);
    }

    public static boolean userEmailIsAvailable(String email, int userID) {
        User user = userRepository.findUsersByEmail(email);
        return user == null || userID == user.getId();
    }

    public static boolean userWithEmailExist(String email) {
        return userRepository.findUsersByEmail(email) != null;
    }

    @Transactional
    public void editUser(User user, UserEditingForm editingForm, List<UserEditor.UserEdits> edits){
        for (UserEditor.UserEdits edit: edits) {
            switch (edit) {
                case FIRST_NAME:
                    user.setFirstName(editingForm.getFirstName());
                    break;
                case LAST_NAME:
                    user.setLastName(editingForm.getLastName());
                    break;
                case EMAIL:
                    user.setEmail(editingForm.getEmail());
                    break;
                case ROLE:
                    user.setRole(editingForm.getRole());
                    break;
                default:
                    throw new DataBaseInteractionException("Can't edit such user data: " + edit);
            }
        }
    }

    public static void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
