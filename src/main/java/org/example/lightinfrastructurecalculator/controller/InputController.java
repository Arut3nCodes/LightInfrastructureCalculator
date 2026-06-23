package org.example.lightinfrastructurecalculator.controller;

import jakarta.validation.Valid;
import org.example.lightinfrastructurecalculator.model.InputDTO;
import org.example.lightinfrastructurecalculator.util.Calculation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Locale;

@Controller
public class InputController {

    @GetMapping("/")
    public String showForm(Model model) {
        if (!model.containsAttribute("inputDTO")) {
            model.addAttribute("inputDTO", new InputDTO(
                    0, 0.0, 0.0, LocalDate.now(), LocalDate.now().plusDays(1), 0, 0, 0
            ));
        }
        return "form";
    }

    @PostMapping("/submit")
    public String handleForm(@Valid @ModelAttribute InputDTO inputDTO, BindingResult bindingResult, Model model) {
        if (inputDTO.getCalculationStartDate() != null && inputDTO.getCalculationEndDate() != null) {
            if (inputDTO.getCalculationEndDate().isBefore(inputDTO.getCalculationStartDate())) {
                bindingResult.rejectValue("calculationEndDate", "error.inputDTO", "Data zakończenia nie może być przed datą rozpoczęcia");
            }
        }

        if (bindingResult.hasErrors()) {
            return "form";
        }

        Calculation calculation = new Calculation();
        calculation.calculateResults(inputDTO);
        model.addAttribute("calculation", calculation);
        return "result";
    }
}