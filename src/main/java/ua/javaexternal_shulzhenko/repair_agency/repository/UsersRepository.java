package ua.javaexternal_shulzhenko.repair_agency.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    User findUsersByEmail(String email);

    List<User> findAllByRoleAndRole(Role role, Role role2);

    List<User> findAllByRole(Role role);

    Page<User> findAllByRole(Role role, Pageable pageable);

    /*
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.language = ?1 WHERE u.id =?2")
    void updateUserByLanguage(String language, Integer userID);*/
}
