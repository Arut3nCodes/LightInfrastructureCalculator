package org.example.lightinfrastructurecalculator.controller;

import org.example.lightinfrastructurecalculator.model.InputDTO;
import org.example.lightinfrastructurecalculator.util.Calculation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class InputController {

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("inputDTO", new InputDTO(
                0, 0, LocalDate.now(), LocalDate.now().plusDays(1), 0, 0, 0
        ));
        return "form";
    }

    @PostMapping("/submit")
    public String handleForm(@ModelAttribute InputDTO inputDTO, Model model) {
        Calculation calculation = new Calculation();
        calculation.calculateResults(inputDTO);
        model.addAttribute("calculation", calculation);
        return "result";
    }
}