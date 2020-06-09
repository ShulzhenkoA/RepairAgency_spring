package ua.javaexternal_shulzhenko.repair_agency.services.editing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EditingValidatorTest {

    @Autowired
    private EditingValidator editingValidator;

    @MockBean
    private OrderEditingForm form;

    @MockBean
    private Model model;

    @Test
    void checkingNeedMaster_needCase_setsCorrespondingAttribute() {

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getMasterID()).thenReturn(0);

        editingValidator.needMasterForThisStatus(form, model);

        verify(model, times(1)).addAttribute(Attributes.MASTER, "");
    }

    @Test
    void checkingNeedMaster_needCase_returnsTrue() {

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getMasterID()).thenReturn(0);

        assertTrue(editingValidator.needMasterForThisStatus(form, model));
    }

    @Test
    void checkingNeedMaster_notNeedCase_returnsFalse() {

        when(form.getStatus()).thenReturn(OrderStatus.REJECTED);
        when(form.getMasterID()).thenReturn(0);

        assertFalse(editingValidator.needMasterForThisStatus(form, model));
    }

    @Test
    void checkingNeedPrice_needCase_setsCorrespondingAttribute() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(false);

        editingValidator.needPreviousPrice(form, bindingResult, model);

        verify(model, times(1)).addAttribute(Attributes.PRICE, "");
    }

    @Test
    void checkingNeedPrice_needCase_returnsTrue() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(false);

        assertTrue(editingValidator.needPreviousPrice(form, bindingResult, model));
    }

    @Test
    void checkingNeedPrice_notNeedCase_returnsFalse() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.REJECTED);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(false);

        assertFalse(editingValidator.needPreviousPrice(form, bindingResult, model));
    }

    @Test
    void checkingNeedPrice_bindingResultContainsPrice_returnsTrue() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.REJECTED);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(true);

        assertTrue(editingValidator.needPreviousPrice(form, bindingResult, model));
    }
}