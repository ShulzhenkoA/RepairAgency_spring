package ua.javaexternal_shulzhenko.car_repair_agency.services.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    User findUsersByEmail(String email);

    List<User> findAllByRoleOrderByIdDesc(Role role);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
