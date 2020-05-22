package ua.javaexternal_shulzhenko.repair_agency.controller.commands;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.Form;

import javax.validation.Valid;

public interface FormHandler {
    String handleForm(@Valid Form form, BindingResult bindingResult, Model model);
}
