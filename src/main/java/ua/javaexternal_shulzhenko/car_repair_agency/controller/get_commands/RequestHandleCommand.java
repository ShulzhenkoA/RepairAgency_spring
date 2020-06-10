package ua.javaexternal_shulzhenko.car_repair_agency.controller.get_commands;

import org.springframework.ui.Model;

public interface RequestHandleCommand {
    String handleRequest(Model model);
}
