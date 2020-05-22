package ua.javaexternal_shulzhenko.repair_agency.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.exceptions.AuthenticationException;
import ua.javaexternal_shulzhenko.repair_agency.repository.UsersRepository;

import java.util.List;

@Service
public final class UsersDBService implements UserDetailsService {

    private static UsersRepository userRepository;

    @Autowired
    public UsersDBService(UsersRepository userRepository) {
        UsersDBService.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        return userRepository.findUsersByEmail(email);
    }

    public static void addUser(User user) {
        userRepository.save(user);
    }

    public static List<User> getUserByTwoRole(Role role, Role role2) {
        return userRepository.findAllByRoleAndRole(role, role2);
    }

    public static List<User> getUsersByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    public static Page<User> getPageableUsersByRole(Role role, Pageable pageable){
        return userRepository.findAllByRole(role, pageable);
    }

    public static User getUserInfoByEmail(String email){
        User user = userRepository.findUsersByEmail(email);
        return User
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
