package org.example.lightinfrastructurecalculator.util;

import org.example.lightinfrastructurecalculator.model.InputDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScalingTest {

    @Test
    void testCalculationScaling() {
        Calculation calculation = new Calculation();
        // simulate 1 kW input (which is converted to 1000 W by JS)
        // rate 1 zł/kWh (which is converted to 0.001 zł/Wh by JS)
        InputDTO inputDTO = new InputDTO(
                1000.0, // Power in W
                0.0,    // Energy Consumption
                0.0,    // Allowed Error
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 2),
                0.001,  // Rate in zł/Wh
                0,      // Sunrise Offset
                0       // Sunset Offset
        );

        calculation.calculateResults(inputDTO);

        // For May 1st to May 2nd in Wrocław:
        // Sunset is around 20:15, Sunrise next day is around 5:15.
        // Duration is roughly 9 hours.
        
        double duration = calculation.getListOfDailyResults().get(0).getTimeWorking();
        double energyUsed = calculation.getListOfDailyResults().get(0).getEnergyUsed();
        double cost = calculation.getListOfDailyResults().get(0).getCost();

        System.out.println("[DEBUG_LOG] Duration (h): " + duration);
        System.out.println("[DEBUG_LOG] Energy Used (Wh?): " + energyUsed);
        System.out.println("[DEBUG_LOG] Cost (PLN?): " + cost);

        // If energyUsed is in Wh, and duration is ~9h, energyUsed should be ~9000 Wh.
        // But the UI expects kWh.
        // If cost is energyUsed (Wh) * rate (zł/Wh), then cost is ~9000 * 0.001 = 9 PLN.
        
        assertEquals(inputDTO.getPower() * duration, energyUsed, 0.001);
        assertEquals(energyUsed * inputDTO.getRate(), cost, 0.001);
    }
}
