package org.example.lightinfrastructurecalculator.util;

import lombok.Data;
import org.example.lightinfrastructurecalculator.model.DayResult;
import org.example.lightinfrastructurecalculator.model.InputDTO;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Data
public class Calculation {
    public static final double DAY_IN_SECONDS = 86400;
    public static final double HOUR_IN_SECONDS = 3600;
    private int calculationPath;
    private double overallCost;
    private double overallEnergyUsed;
    private boolean energyConsumptionValidation;
    private double energyDifference;
    private boolean validationPerformed;
    private double referenceEnergy;
    private double allowedError;

    ArrayList<DayResult> listOfDailyResults;


    public Calculation() {
        listOfDailyResults = new ArrayList<>();
        overallCost = 0;
        overallEnergyUsed = 0;
    }

    public void calculateResults(InputDTO inputDTO){
        if (inputDTO.getPower() != 0){
            setCalculationPath(1);
            calculationByPower(inputDTO);
            if (inputDTO.getEnergyConsumption() != null && inputDTO.getEnergyConsumption() != 0) {
                performValidation(inputDTO);
            }
        }
        else if (inputDTO.getEnergyConsumption() != null && inputDTO.getEnergyConsumption() != 0){
            setCalculationPath(2);
            calculationByEnergyConsumption(inputDTO);
        }
    }

    private void performValidation(InputDTO inputDTO) {
        validationPerformed = true;
        this.referenceEnergy = inputDTO.getEnergyConsumption();
        this.allowedError = inputDTO.getAllowedError() != null ? inputDTO.getAllowedError() : 0;
        
        energyDifference = overallEnergyUsed - referenceEnergy;
        energyConsumptionValidation = Math.abs(energyDifference) <= allowedError;
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

            Instant sunset = timeOfSunsetDay.getSet().toInstant().plusSeconds(inputDTO.getSunsetOffset());
            Instant sunrise = timeOfSunriseDay.getRise().toInstant().plusSeconds(inputDTO.getSunriseOffset());
            Duration duration = Duration.between(sunset, sunrise);
            double durationInHours = (double)duration.getSeconds() / HOUR_IN_SECONDS;
            double energyUsed = inputDTO.getPower() * durationInHours;
            double cost = energyUsed * inputDTO.getRate();

            DayResult dayResult = new DayResult(
                    iterDate,
                    sunset.atZone(zone).toLocalDateTime(),
                    sunrise.atZone(zone).toLocalDateTime(),
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

            Instant sunset = timeOfSunsetDay.getSet().toInstant().plusSeconds(inputDTO.getSunsetOffset());
            Instant sunrise = timeOfSunriseDay.getRise().toInstant().plusSeconds(inputDTO.getSunriseOffset());
            Duration duration = Duration.between(sunset, sunrise);
            double durationInHours = (double)duration.getSeconds() / HOUR_IN_SECONDS;
            double energyUsed = inputDTO.getEnergyConsumption();
            double cost = energyUsed * inputDTO.getRate();

            DayResult dayResult = new DayResult(
                    iterDate,
                    sunset.atZone(zone).toLocalDateTime(),
                    sunrise.atZone(zone).toLocalDateTime(),
                    durationInHours,
                    energyUsed,
                    cost
            );

            this.listOfDailyResults.add(dayResult);
            this.overallCost += cost;
            this.overallEnergyUsed += energyUsed;
        }
    }

    public Map<YearMonth, Double> getMonthlyEnergyUsed() {
        return listOfDailyResults.stream()
                .collect(Collectors.groupingBy(
                        day -> YearMonth.from(day.getDayOfCalculation()),
                        TreeMap::new,
                        Collectors.summingDouble(DayResult::getEnergyUsed)
                ));
    }

    public Map<YearMonth, Double> getMonthlyCost() {
        return listOfDailyResults.stream()
                .collect(Collectors.groupingBy(
                        day -> YearMonth.from(day.getDayOfCalculation()),
                        TreeMap::new,
                        Collectors.summingDouble(DayResult::getCost)
                ));
    }

    public Map<YearMonth, Double> getMonthlyTimeWorking() {
        return listOfDailyResults.stream()
                .collect(Collectors.groupingBy(
                        day -> YearMonth.from(day.getDayOfCalculation()),
                        TreeMap::new,
                        Collectors.summingDouble(DayResult::getTimeWorking)
                ));
    }
}
