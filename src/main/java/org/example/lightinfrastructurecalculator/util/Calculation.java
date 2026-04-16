package org.example.lightinfrastructurecalculator.util;

import lombok.Data;
import org.example.lightinfrastructurecalculator.model.DayResult;
import org.example.lightinfrastructurecalculator.model.InputDTO;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Data
public class Calculation {
    public static final double DAY_IN_SECONDS = 86400;
    private double overallCost;
    private double overallEnergyUsed;

    ArrayList<DayResult> listOfDailyResults;


    public Calculation() {
        listOfDailyResults = new ArrayList<>();
        overallCost = 0;
        overallEnergyUsed = 0;
    }

    public void calculateResults(InputDTO inputDTO){
        if (inputDTO.getPower() != 0){
            calculationByPower(inputDTO);
        }
        else if (inputDTO.getEnergyConsumption() != 0){
            calculationByEnergyConsumption(inputDTO);
        }
    }

    public void calculationByPower(InputDTO inputDTO){
        // Wrocław coordinates
        double lat = 51.1079;
        double lon = 17.0385;

        ZoneId zone = ZoneId.of("Europe/Warsaw");

        for(LocalDate iterDate = inputDTO.getCalculationStartDate(); iterDate.isBefore(inputDTO.getCalculationEndDate()); iterDate = iterDate.plusDays(1)){
            Date date = Date.from(iterDate.atStartOfDay(zone).toInstant());
            Date nextDate = Date.from(iterDate.plusDays(1).atStartOfDay(zone).toInstant());

            SunTimes timeOfSunsetDay = SunTimes.compute()
                    .on(date)
                    .at(lat, lon)
                    .execute();

            SunTimes timeOfSunriseDay = SunTimes.compute()
                    .on(nextDate)
                    .at(lat, lon)
                    .execute();

            Instant sunset = timeOfSunsetDay.getSet().toInstant();
            Instant sunrise = timeOfSunriseDay.getRise().toInstant();
            Duration duration = Duration.between(sunset, sunrise);
            double durationInHours = duration.toHours();
            double energyUsed = inputDTO.getPower() * durationInHours;
            double cost = inputDTO.getPower() * durationInHours * inputDTO.getRate();

            DayResult dayResult = new DayResult(
                    iterDate,
                    timeOfSunsetDay.getSet().toLocalDateTime(),
                    timeOfSunriseDay.getRise().toLocalDateTime(),
                    durationInHours,
                    energyUsed,
                    cost
            );

            this.listOfDailyResults.add(dayResult);
            this.overallCost += cost;
            this.overallEnergyUsed += energyUsed;
        }
    }

    public void calculationByEnergyConsumption(InputDTO inputDTO){

    }
}
