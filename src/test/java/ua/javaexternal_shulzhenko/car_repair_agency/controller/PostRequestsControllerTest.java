package ua.javaexternal_shulzhenko.car_repair_agency.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.*;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.forms.*;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.OrderDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.ReviewDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.UserDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.editing.impl.EditingOrderValidator;
import ua.javaexternal_shulzhenko.car_repair_agency.services.editing.impl.OrderEditor;
import ua.javaexternal_shulzhenko.car_repair_agency.services.editing.impl.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostRequestsControllerTest {

    @Autowired
    private PostRequestsController controller;

    @MockBean
    private UserDatabaseService userDatabaseService;

    @MockBean
    private OrderDatabaseService orderDatabaseService;

    @MockBean
    private ReviewDatabaseService reviewDatabaseService;

    @MockBean
    private HttpServletResponse resp;

    @MockBean
    private HttpServletRequest req;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private Model model;

    @MockBean
    private RegistrationForm registrationForm;

    @MockBean
    private OrderForm orderForm;

    @MockBean
    private ReviewForm reviewForm;

    @MockBean
    private UserEditingForm userEditingForm;

    @MockBean
    private OrderEditingForm orderEditingForm;

    @MockBean
    private UserEditor userEditor;

    @MockBean
    private OrderEditor orderEditor;

    @MockBean
    private EditingOrderValidator editingOrderValidator;

    @Test
    public void handlingRegistration_formWithBindingResultErrors_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(true);

        controller.handleRegistration(registrationForm, bindingResult, model, resp);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, registrationForm));
    }

    @Test
    public void handlingRegistration_formWithConfirmationPassError_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(registrationForm.confirmationPassMatch()).thenReturn(false);

        controller.handleRegistration(registrationForm, bindingResult, model, resp);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, registrationForm),
                () -> verify(model, times(1)).addAttribute(Attributes.WRONG_PASS_CONFIRMATION, ""));
    }

    @Test
    public void handlingRegistration_formWithExistedEmail_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(registrationForm.confirmationPassMatch()).thenReturn(true);
        when(userDatabaseService.userEmailIsAvailable(registrationForm.getEmail())).thenReturn(false);

        controller.handleRegistration(registrationForm, bindingResult, model, resp);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, registrationForm),
                () -> verify(model, times(1)).addAttribute(Attributes.NOT_FREE_EMAIL, ""));
    }

    @Test
    public void handlingRegistration_formWithCorrectData_registersUser(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(registrationForm.confirmationPassMatch()).thenReturn(true);
        when(userDatabaseService.userEmailIsAvailable(registrationForm.getEmail())).thenReturn(true);

        controller.handleRegistration(registrationForm, bindingResult, model, resp);

        assertAll(
                () -> verify(registrationForm, times(1)).extractUser(),
                () -> verify(userDatabaseService, times(1)).createUser(registrationForm.extractUser()),
                () -> verify(model, times(1)).addAttribute(Attributes.SUCCESS, ""));
    }

    @Test
    public void handlingEditUser_formWithBindingResultErrors_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(true);

        controller.handleEditUser(userEditingForm, bindingResult, model, resp, userEditor );

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, userEditingForm));
    }

    @Test
    public void handlingEditUser_formWithExistedEmail_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userDatabaseService.userEmailIsAvailable(
                userEditingForm.getEmail(), userEditingForm.getId())).thenReturn(false);

        controller.handleEditUser(userEditingForm, bindingResult, model, resp, userEditor);

        assertAll(
                () -> verify(model, times(1)).addAttribute(Attributes.NOT_FREE_EMAIL, ""),
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, userEditingForm));
    }

    @Test
    public void handlingEditUser_formWithCorrectData_editsUserAndRedirects(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userDatabaseService.userEmailIsAvailable(
                userEditingForm.getEmail(), userEditingForm.getId())).thenReturn(true);

        User user = User.builder().build();
        when(userDatabaseService.getUserById(userEditingForm.getId())).thenReturn(user);
        when(userEditor.setForm(userEditingForm)).thenReturn(userEditor);
        when(userEditor.setUser(user)).thenReturn(userEditor);
        when(userEditor.compareFirstName()).thenReturn(userEditor);
        when(userEditor.compareLastName()).thenReturn(userEditor);
        when(userEditor.compareEmail()).thenReturn(userEditor);
        when(userEditor.compareRole()).thenReturn(userEditor);

        String returnedResource = controller.handleEditUser(userEditingForm, bindingResult, model, resp, userEditor);

        assertAll(
                () -> verify(userDatabaseService, times(1)).getUserById(userEditingForm.getId()),
                () -> verify(userDatabaseService, times(1))
                        .editUser(user, userEditingForm, Collections.emptyList()),
                () -> assertEquals(CommonConstants.REDIRECT + CRAPaths.ADMIN_HOME, returnedResource));
    }

    @Test
    public void handlingDeleteUser_deletesUserAndRedirects(){
        String returnedResource = controller.handleDeleteUser(0);

        assertAll(
                () -> verify(userDatabaseService, times(1)).deleteUser(0),
                () -> assertEquals(CommonConstants.REDIRECT + CRAPaths.ADMIN_HOME, returnedResource));
    }

    @Test
    public void handlingCreateOrder_formWithBindingResultErrors_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(true);

        User user = mock(User.class);

        controller.handleCreateOrder(orderForm, bindingResult, model, resp, user);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, orderForm));
    }

    @Test
    public void handlingCreateOrder_formWithCorrectData_createsOrder(){
        when(bindingResult.hasErrors()).thenReturn(false);

        User user = mock(User.class);
        Order order = mock(Order.class);

        when(orderForm.extractOrder()).thenReturn(order);

        controller.handleCreateOrder(orderForm, bindingResult, model, resp, user);

        assertAll(
                () -> verify(order, times(1)).setCustomer(user),
                () -> verify(orderDatabaseService, times(1)).createOrder(order),
                () -> verify(model, times(1)).addAttribute(Attributes.MADE_ORDER, order));
    }

    @Test
    public void handlingEditOrder_formWithBindingResultErrors_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(true);

        List<User> masters = mock(List.class);
        Order order = mock(Order.class);

        when(userDatabaseService.getUsersByRole(Role.MASTER)).thenReturn(masters);
        when(orderDatabaseService.getOrderById(0)).thenReturn(order);
        when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        controller.handleEditOrder(orderEditingForm, bindingResult, model, resp, orderEditor, editingOrderValidator);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.CUR_ORDER_STATUS, order.getStatus()),
                () -> verify(model, times(1)).addAttribute(Attributes.MASTERS, masters),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, orderEditingForm));
    }

    @Test
    public void handlingEditOrder_formWithNeedMasterForThisStatusError_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(editingOrderValidator.needMasterForThisStatus(orderEditingForm, model)).thenReturn(true);

        List<User> masters = mock(List.class);
        Order order = mock(Order.class);

        when(userDatabaseService.getUsersByRole(Role.MASTER)).thenReturn(masters);
        when(orderDatabaseService.getOrderById(0)).thenReturn(order);
        when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        controller.handleEditOrder(orderEditingForm, bindingResult, model, resp, orderEditor, editingOrderValidator);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.CUR_ORDER_STATUS, order.getStatus()),
                () -> verify(model, times(1)).addAttribute(Attributes.MASTERS, masters),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, orderEditingForm));
    }

    @Test
    public void handlingEditOrder_formWithNeedPreviousPrice_setsBadRequestData(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(editingOrderValidator.needMasterForThisStatus(orderEditingForm, model)).thenReturn(false);
        when(editingOrderValidator.needPreviousPrice(orderEditingForm, bindingResult, model)).thenReturn(true);

        List<User> masters = mock(List.class);
        Order order = mock(Order.class);

        when(userDatabaseService.getUsersByRole(Role.MASTER)).thenReturn(masters);
        when(orderDatabaseService.getOrderById(0)).thenReturn(order);
        when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        controller.handleEditOrder(orderEditingForm, bindingResult, model, resp, orderEditor, editingOrderValidator);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.CUR_ORDER_STATUS, order.getStatus()),
                () -> verify(model, times(1)).addAttribute(Attributes.MASTERS, masters),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, orderEditingForm));
    }

    @Test
    public void handlingEditOrder_formWithCorrectData_editsOrderAndRedirects(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(editingOrderValidator.needMasterForThisStatus(orderEditingForm, model)).thenReturn(false);
        when(editingOrderValidator.needPreviousPrice(orderEditingForm, bindingResult, model)).thenReturn(false);

        Order order = mock(Order.class);

        when(orderDatabaseService.getOrderById(0)).thenReturn(order);
        when(orderEditor.setForm(orderEditingForm)).thenReturn(orderEditor);
        when(orderEditor.setOrder(order)).thenReturn(orderEditor);
        when(orderEditor.comparePrice()).thenReturn(orderEditor);
        when(orderEditor.compareMasters()).thenReturn(orderEditor);
        when(orderEditor.compareStatus()).thenReturn(orderEditor);
        when(orderEditor.compareManagerComment()).thenReturn(orderEditor);

        String returnedResource = controller.handleEditOrder(
                orderEditingForm, bindingResult, model, resp, orderEditor, editingOrderValidator);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .editOrder(order, orderEditingForm, Collections.EMPTY_LIST),
                () -> assertEquals(CommonConstants.REDIRECT + CRAPaths.MANAGER_HOME, returnedResource));
    }

    @Test
    public void handlingEditStatus_repairWork_changesStatusAndRedirects(){

        String returnedResource = controller.handleEditStatus(OrderStatus.REPAIR_WORK, 0);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .changeOrderStatus(0, OrderStatus.REPAIR_WORK),
                () -> assertEquals(CommonConstants.REDIRECT + CRAPaths.MASTER_HOME, returnedResource));
    }

    @Test
    public void handlingEditStatus_repairCompleted_changesStatusWithDateTimeAndRedirects(){

        String returnedResource = controller.handleEditStatus(OrderStatus.REPAIR_COMPLETED, 0);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .changeOrderStatus(eq(0), eq(OrderStatus.REPAIR_COMPLETED), any(LocalDateTime.class)),
                () -> assertEquals(CommonConstants.REDIRECT + CRAPaths.MASTER_HOME, returnedResource));
    }

    @Test
    public void handlingReviews_formWithBindingResultErrors_setsBadRequestDataAndPaginationData(){
        when(bindingResult.hasErrors()).thenReturn(true);

        User user = mock(User.class);
        Page<Review> reviews = Page.empty();

        when(reviewDatabaseService.getPageableReviews(
                PageRequest.of(
                        0, PaginationConstants.REVIEWS_FOR_REVIEW_PAGE,
                        Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(reviews);
        when(model.addAttribute(any(), any())).thenReturn(model);

        controller.handleReviews(reviewForm, bindingResult, model, req, resp, user);

        assertAll(
                () -> verify(resp, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST),
                () -> verify(model, times(1)).addAttribute(Attributes.BINDING_RESULT, bindingResult),
                () -> verify(model, times(1)).addAttribute(Attributes.PREV_FORM, reviewForm),
                () -> verify(model, times(1)).addAttribute(Attributes.PAGE_NUM, null),
                () -> verify(model, times(1)).addAttribute(Attributes.URI, null));
    }

    @Test
    public void handlingReviews_formWithCorrectData_addsReviewAndPaginationData(){
        when(bindingResult.hasErrors()).thenReturn(false);

        User user = mock(User.class);
        Review review = mock(Review.class);
        Page<Review> reviews = Page.empty();

        when(reviewForm.extractReview()).thenReturn(review);
        when(reviewDatabaseService.getPageableReviews(
                PageRequest.of(
                        0, PaginationConstants.REVIEWS_FOR_REVIEW_PAGE,
                        Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(reviews);
        when(model.addAttribute(any(), any())).thenReturn(model);

        controller.handleReviews(reviewForm, bindingResult, model, req, resp, user);

        assertAll(
                () -> verify(review, times(1)).setCustomer(user),
                () -> verify(reviewDatabaseService, times(1)).addReview(review),
                () -> verify(model, times(1)).addAttribute(Attributes.SUCCESS, ""),
                () -> verify(model, times(1)).addAttribute(Attributes.PAGE_NUM, null),
                () -> verify(model, times(1)).addAttribute(Attributes.URI, null));
    }
}