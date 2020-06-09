package ua.javaexternal_shulzhenko.repair_agency.services.database.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.UserEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.services.database.repository.UsersRepository;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.UserEditor;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsersDatabaseInteractionServiceTest {

    @Autowired
    private UsersDatabaseInteractionService usersDatabaseInteractionService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private User user;

    @MockBean
    private UserEditingForm userEditingForm;

    private List<UserEditor.UserEdits> edits;

    @Test
    public void creatingUser_savesUser(){
        User user = mock(User.class);
        usersDatabaseInteractionService.createUser(user);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    public void loadingUserByUsername_findsUserByEmail(){
        usersDatabaseInteractionService.loadUserByUsername("email");
        verify(usersRepository, times(1)).findUsersByEmail("email");
    }

    @Test
    public void gettingUserById_getsOne(){
        usersDatabaseInteractionService.getUserById(1);
        verify(usersRepository, times(1)).getOne(1);
    }

    @Test
    public void gettingUsersByRole_findsAllByRoleInDescOrder(){
        usersDatabaseInteractionService.getUsersByRole(Role.CUSTOMER);
        verify(usersRepository, times(1)).findAllByRoleOrderByIdDesc(Role.CUSTOMER);
    }

    @Test
    public void gettingPageableUsersByRole_findsAllByRole(){
        usersDatabaseInteractionService.getPageableUsersByRole(Role.CUSTOMER, Pageable.unpaged());
        verify(usersRepository, times(1)).findAllByRole(Role.CUSTOMER, Pageable.unpaged());
    }

    @Test
    public void checkingIfUserEmailIsAvailableByEmailAndId_findsUserByEmail(){
        usersDatabaseInteractionService.userEmailIsAvailable("email", 1);
        verify(usersRepository, times(1)).findUsersByEmail("email");
    }

    @Test
    public void checkingIfUserEmailIsAvailableByEmailAndId_whenUserWithThisEmailIsAbsent_returnsTrue(){
        when(usersRepository.findUsersByEmail("email")).thenReturn(null);
        assertTrue(usersDatabaseInteractionService.userEmailIsAvailable("email", 0));
    }

    @Test
    public void checkingIfUserEmailIsAvailableByEmailAndId_whenUserWithThisEmailExists_returnsFalse(){
        when(usersRepository.findUsersByEmail("email")).thenReturn(user);
        assertFalse(usersDatabaseInteractionService.userEmailIsAvailable("email", 1));
    }

    @Test
    public void checkingIfUserEmailIsAvailableByEmailAndId_whenUserWithThisEmailExistsAndItsSameUser_returnsTrue(){
        when(usersRepository.findUsersByEmail("email")).thenReturn(user);
        when(user.getId()).thenReturn(1);
        assertTrue(usersDatabaseInteractionService.userEmailIsAvailable("email", 1));
    }

    @Test
    public void checkingIfUserEmailIsAvailableByEmail_findsUserByEmail(){
        usersDatabaseInteractionService.userEmailIsAvailable("email");
        verify(usersRepository, times(1)).findUsersByEmail("email");
    }

    @Test
    public void checkingIfUserEmailIsAvailableByEmail_whenUserWithThisEmailIsAbsent_returnsTrue(){
        when(usersRepository.findUsersByEmail("email")).thenReturn(null);
        assertTrue(usersDatabaseInteractionService.userEmailIsAvailable("email"));
    }

    @Test
    public void checkingIfUserEmailIsAvailableByEmail_whenUserWithThisEmailExists_returnsFalse(){
        when(usersRepository.findUsersByEmail("email")).thenReturn(user);
        assertFalse(usersDatabaseInteractionService.userEmailIsAvailable("email"));
    }

    @Test
    public void editingUser_whenEditsContainsUserFirstName_setsUserFirstName(){
        edits = new LinkedList<>();
        edits.add(UserEditor.UserEdits.FIRST_NAME);
        usersDatabaseInteractionService.editUser(user, userEditingForm, edits);
        verify(user, times(1)).setFirstName(userEditingForm.getFirstName());
    }

    @Test
    public void editingUser_whenEditsContainsUserLastName_setsUserLastName(){
        edits = new LinkedList<>();
        edits.add(UserEditor.UserEdits.LAST_NAME);
        usersDatabaseInteractionService.editUser(user, userEditingForm, edits);
        verify(user, times(1)).setLastName(userEditingForm.getLastName());
    }

    @Test
    public void editingUser_whenEditsContainsEmail_setsUserEmail(){
        edits = new LinkedList<>();
        edits.add(UserEditor.UserEdits.EMAIL);
        usersDatabaseInteractionService.editUser(user, userEditingForm, edits);
        verify(user, times(1)).setEmail(userEditingForm.getEmail());
    }

    @Test
    public void editingUser_whenEditsContainsRole_setsUserRole(){
        edits = new LinkedList<>();
        edits.add(UserEditor.UserEdits.ROLE);
        usersDatabaseInteractionService.editUser(user, userEditingForm, edits);
        verify(user, times(1)).setRole(userEditingForm.getRole());
    }

    @Test
    public void deletingUser_deletesUserById(){
        usersDatabaseInteractionService.deleteUser(0);
        verify(usersRepository, times(1)).deleteById(0);
    }
}