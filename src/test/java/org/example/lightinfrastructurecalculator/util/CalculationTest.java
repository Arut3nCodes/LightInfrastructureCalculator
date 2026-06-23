package org.example.lightinfrastructurecalculator.util;

import org.example.lightinfrastructurecalculator.model.DayResult;
import org.example.lightinfrastructurecalculator.model.InputDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculationTest {

    @Test
    void testMonthlySummary() {
        Calculation calculation = new Calculation();
        
        // Add some dummy results
        LocalDate date1 = LocalDate.of(2026, 5, 1);
        LocalDate date2 = LocalDate.of(2026, 5, 2);
        LocalDate date3 = LocalDate.of(2026, 6, 1);
        
        calculation.getListOfDailyResults().add(new DayResult(date1, LocalDateTime.now(), LocalDateTime.now(), 1.0, 10.0, 5.0));
        calculation.getListOfDailyResults().add(new DayResult(date2, LocalDateTime.now(), LocalDateTime.now(), 1.0, 20.0, 10.0));
        calculation.getListOfDailyResults().add(new DayResult(date3, LocalDateTime.now(), LocalDateTime.now(), 1.0, 15.0, 7.5));
        
        Map<YearMonth, Double> energyMap = calculation.getMonthlyEnergyUsed();
        Map<YearMonth, Double> costMap = calculation.getMonthlyCost();
        
        assertEquals(2, energyMap.size());
        assertEquals(30.0, energyMap.get(YearMonth.of(2026, 5)));
        assertEquals(15.0, energyMap.get(YearMonth.of(2026, 6)));
        
        assertEquals(2, costMap.size());
        assertEquals(15.0, costMap.get(YearMonth.of(2026, 5)));
        assertEquals(7.5, costMap.get(YearMonth.of(2026, 6)));
    }
}
