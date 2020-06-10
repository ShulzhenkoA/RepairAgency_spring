package ua.javaexternal_shulzhenko.car_repair_agency.services.editing.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderEditorTest {

    @Autowired
    private OrderEditor editor;

    private List<OrderEditor.OrderEdits> edits;

    @MockBean
    OrderEditingForm editingForm;

    @MockBean
    Order order;

    @BeforeEach
    void setUp(){
        editor.setForm(editingForm).setOrder(order);
    }

    @ParameterizedTest
    @CsvSource({"100, 15.4", "65.32, 10"})
    void comparing_differentPrice_givesOnePriceEdit(String formPrice, double orderPrice) {
        when(editingForm.getPrice()).thenReturn(formPrice);
        when(order.getPrice()).thenReturn(orderPrice);
        editor = editor.comparePrice();
        edits = editor.getEdits();
        assertAll(
                () -> assertEquals(1, edits.size()),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.PRICE)));
    }

    @ParameterizedTest
    @CsvSource({"100, 100.0", "65.32, 65.32"})
    void comparing_samePrice_givesNoEdit(String formPrice, double orderPrice) {
        when(editingForm.getPrice()).thenReturn(formPrice);
        when(order.getPrice()).thenReturn(orderPrice);
        editor = editor.comparePrice();
        edits = editor.getEdits();
        assertTrue(edits.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"21, 15", "3, 31"})
    void comparing_differentMasters_givesOneMasterEdit(int formMasterID, int orderMasterID) {
        User user = User.builder()
                .id(orderMasterID).build();
        when(editingForm.getMasterID()).thenReturn(formMasterID);
        when(order.getMaster()).thenReturn(user);
        editor = editor.compareMasters();
        edits = editor.getEdits();
        assertAll(
                () -> assertEquals(1, edits.size()),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.MASTER_ID)));
    }

    @ParameterizedTest
    @CsvSource({"21, 21", "3, 3"})
    void comparing_sameMasters_givesNoEdit(int formMasterID, int orderMasterID) {
        User user = User.builder().id(orderMasterID).build();
        when(editingForm.getMasterID()).thenReturn(formMasterID);
        when(order.getMaster()).thenReturn(user);
        editor = editor.compareMasters();
        edits = editor.getEdits();
        assertTrue(edits.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"REJECTED, CAR_WAITING", "REPAIR_COMPLETED, ORDER_COMPLETED"})
    void comparing_differentStatuses_givesOneStatusEdit(String formStatus, String orderStatus) {
        when(editingForm.getStatus()).thenReturn(OrderStatus.valueOf(formStatus));
        when(order.getStatus()).thenReturn(OrderStatus.valueOf(orderStatus));
        editor = editor.compareStatus();
        edits = editor.getEdits();
        assertAll(
                () -> assertEquals(1, edits.size()),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.STATUS)));
    }

    @ParameterizedTest
    @CsvSource({"CAR_WAITING, CAR_WAITING", "REPAIR_COMPLETED, REPAIR_COMPLETED"})
    void comparing_sameStatuses_givesNoEdit(String formStatus, String orderStatus) {
        when(editingForm.getStatus()).thenReturn(OrderStatus.valueOf(formStatus));
        when(order.getStatus()).thenReturn(OrderStatus.valueOf(orderStatus));
        editor = editor.compareStatus();
        edits = editor.getEdits();
        assertTrue(edits.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"Comment from form, Comment from database",
            "Another comment from form, Another comment form database"})
    void comparing_differentManagerComments_givesOneStatusEdit(String formComment, String orderComment) {
        when(editingForm.getManagerComment()).thenReturn(formComment);
        when(order.getManagerComment()).thenReturn(orderComment);
        editor = editor.compareManagerComment();
        edits = editor.getEdits();
        assertAll(
                () -> assertEquals(1, edits.size()),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.MANAGER_COMMENT)));
    }

    @ParameterizedTest
    @CsvSource({"Comment is same in the form and database, Comment is same in the form and database",
            "Another comment is same in the from and database, Another comment is same in the from and database"})
    void comparing_sameManagerComments_givesNoEdit(String formComment, String orderComment) {
        when(editingForm.getManagerComment()).thenReturn(formComment);
        when(order.getManagerComment()).thenReturn(orderComment);
        editor = editor.compareManagerComment();
        edits = editor.getEdits();
        assertTrue(edits.isEmpty());
    }


    @ParameterizedTest
    @CsvSource({"100.50, 21, REJECTED, Some comment, 15.25, 3, CAR_WAITING, Other comment"})
    void comparing_differentData_givesFourEdits(String formPrice, int formMasterID, String formStatus, String formComment,
                                               double orderPrice, int orderMasterID, String orderStatus, String orderComment) {
        User user = User.builder().id(orderMasterID).build();
        when(editingForm.getPrice()).thenReturn(formPrice);
        when(order.getPrice()).thenReturn(orderPrice);
        when(editingForm.getMasterID()).thenReturn(formMasterID);
        when(order.getMaster()).thenReturn(user);
        when(editingForm.getStatus()).thenReturn(OrderStatus.valueOf(formStatus));
        when(order.getStatus()).thenReturn(OrderStatus.valueOf(orderStatus));
        when(editingForm.getManagerComment()).thenReturn(formComment);
        when(order.getManagerComment()).thenReturn(orderComment);
        editor = editor.comparePrice().compareMasters().compareStatus().compareManagerComment();
        edits = editor.getEdits();
        assertAll(
                () -> assertEquals(4, edits.size()),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.PRICE)),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.MASTER_ID)),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.STATUS)),
                () -> assertTrue(edits.contains(OrderEditor.OrderEdits.MANAGER_COMMENT)));
    }

    @ParameterizedTest
    @CsvSource({"100.50, 21, CAR_WAITING, Some comment, 100.50, 21, CAR_WAITING, Some comment"})
    void comparing_sameData_givesNoEdits(String formPrice, int formMasterID, String formStatus, String formComment,
                                        double orderPrice, int orderMasterID, String orderStatus, String orderComment) {
        User user = User.builder().id(orderMasterID).build();
        when(editingForm.getPrice()).thenReturn(formPrice);
        when(order.getPrice()).thenReturn(orderPrice);
        when(editingForm.getMasterID()).thenReturn(formMasterID);
        when(order.getMaster()).thenReturn(user);
        when(editingForm.getStatus()).thenReturn(OrderStatus.valueOf(formStatus));
        when(order.getStatus()).thenReturn(OrderStatus.valueOf(orderStatus));
        when(editingForm.getManagerComment()).thenReturn(formComment);
        when(order.getManagerComment()).thenReturn(orderComment);
        editor = editor.comparePrice().compareMasters().compareStatus().compareManagerComment();
        edits = editor.getEdits();
        assertTrue(edits.isEmpty());
    }
}