package org.example.lightinfrastructurecalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class InputDTO {
    private double power;
    private double energyConsumption;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate calculationStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate calculationEndDate;
    private double rate;
    private int sunriseOffset;
    private int sunsetOffset;
}
