package ua.javaexternal_shulzhenko.repair_agency.controller.get_commands;

import org.springframework.ui.Model;

public interface RequestHandler {
    String handleRequest(Model model);
}
