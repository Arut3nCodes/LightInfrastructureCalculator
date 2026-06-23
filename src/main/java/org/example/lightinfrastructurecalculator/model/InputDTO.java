package org.example.lightinfrastructurecalculator.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InputDTO {
    @Positive(message = "Moc musi być dodatnia")
    private double power;

    @PositiveOrZero(message = "Zużycie energii nie może być ujemne")
    private Double energyConsumption;

    @PositiveOrZero(message = "Błąd nie może być ujemny")
    private Double allowedError;

    @NotNull(message = "Data rozpoczęcia jest wymagana")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate calculationStartDate;

    @NotNull(message = "Data zakończenia jest wymagana")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate calculationEndDate;

    @Positive(message = "Stawka musi być dodatnia")
    private double rate;

    @PositiveOrZero(message = "Offset nie może być ujemny")
    private int sunriseOffset;

    @PositiveOrZero(message = "Offset nie może być ujemny")
    private int sunsetOffset;
}
