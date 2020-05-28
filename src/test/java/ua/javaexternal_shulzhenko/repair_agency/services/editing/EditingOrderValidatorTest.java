package ua.javaexternal_shulzhenko.repair_agency.services.editing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.editing.EditingOrderValidator.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class EditingOrderValidatorTest {

    @MockBean
    private OrderEditingForm form;

    @MockBean
    private Model model;

    @Test
    void checkingNeedMaster_needCase_setsCorrespondingAttribute() {

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getMasterID()).thenReturn(0);

        needMasterForThisStatus(form, model);

        verify(model, times(1)).addAttribute(Attributes.MASTER, "");
    }

    @Test
    void checkingNeedMaster_needCase_returnTrue() {

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getMasterID()).thenReturn(0);

        assertTrue(needMasterForThisStatus(form, model));
    }

    @Test
    void checkingNeedMaster_notNeedCase_returnFalse() {

        when(form.getStatus()).thenReturn(OrderStatus.REJECTED);
        when(form.getMasterID()).thenReturn(0);

        assertFalse(needMasterForThisStatus(form, model));
    }

    @Test
    void checkingNeedPrice_needCase_setsCorrespondingAttribute() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(false);

        needPreviousPrice(form, bindingResult, model);

        verify(model, times(1)).addAttribute(Attributes.PRICE, "");
    }

    @Test
    void checkingNeedPrice_needCase_returnTrue() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.CAR_WAITING);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(false);

        assertTrue(needPreviousPrice(form, bindingResult, model));
    }

    @Test
    void checkingNeedPrice_notNeedCase_returnFalse() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.REJECTED);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(false);

        assertFalse(needPreviousPrice(form, bindingResult, model));
    }

    @Test
    void checkingNeedPrice_bindingResultContainsPrice_returnTrue() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(form.getStatus()).thenReturn(OrderStatus.REJECTED);
        when(form.getPrice()).thenReturn("0");
        when(bindingResult.hasFieldErrors(Attributes.PRICE)).thenReturn(true);

        assertTrue(needPreviousPrice(form, bindingResult, model));
    }
}