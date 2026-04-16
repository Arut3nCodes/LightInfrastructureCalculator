package org.example.lightinfrastructurecalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class DayResult {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dayOfCalculation;
    private LocalDateTime timeStarted;
    private LocalDateTime timeEnded;
    private double timeWorking;
    private double energyUsed;
    private double cost;
}
