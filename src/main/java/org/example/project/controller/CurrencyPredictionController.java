package org.example.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.project.dto.ApiDTOs.*;
import org.example.project.service.CurrencyPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/predictions")
@Tag(name = "Currency Predictions (AI)", description = "AI-powered currency exchange rate predictions using linear regression")
public class CurrencyPredictionController {

    @Autowired
    private CurrencyPredictionService predictionService;

    @Operation(
            summary = "Get all currency predictions", 
            description = "Get AI-powered predictions for all supported currencies using linear regression on 30 days of historical data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Predictions generated successfully"),
            @ApiResponse(responseCode = "500", description = "Error generating predictions")
    })
    @GetMapping
    public ResponseEntity<?> getAllPredictions() {
        try {
            List<CurrencyPredictionResponse> predictions = predictionService.getAllPredictions();
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("PREDICTION_ERROR", "Error generating predictions: " + e.getMessage()));
        }
    }

    @Operation(
            summary = "Get prediction for specific currency", 
            description = "Get detailed prediction for a specific currency including current rate, predicted rate, and recommendation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prediction generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid currency code"),
            @ApiResponse(responseCode = "500", description = "Error generating prediction")
    })
    @GetMapping("/{currency}")
    public ResponseEntity<?> getPredictionForCurrency(
            @Parameter(description = "Currency code (e.g., EUR, GBP, JPY)") 
            @PathVariable String currency) {
        try {
            if (!predictionService.getSupportedCurrencies().contains(currency)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("INVALID_CURRENCY", "Currency not supported: " + currency));
            }

            CurrencyPredictionResponse prediction = predictionService.getPredictionForCurrency(currency);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("PREDICTION_ERROR", e.getMessage()));
        }
    }

    @Operation(
            summary = "Get historical rates", 
            description = "Get 30 days of historical exchange rates for a currency"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historical data retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid currency code"),
            @ApiResponse(responseCode = "500", description = "Error retrieving data")
    })
    @GetMapping("/{currency}/historical")
    public ResponseEntity<?> getHistoricalRates(
            @Parameter(description = "Currency code (e.g., EUR, GBP, JPY)") 
            @PathVariable String currency) {
        try {
            if (!predictionService.getSupportedCurrencies().contains(currency)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("INVALID_CURRENCY", "Currency not supported: " + currency));
            }

            Map<LocalDate, Double> historicalRates = predictionService.getHistoricalRates(currency);
            return ResponseEntity.ok(historicalRates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATA_ERROR", e.getMessage()));
        }
    }

    @Operation(
            summary = "Get future predictions", 
            description = "Get 7-day future predictions for a currency"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Future predictions generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid currency code"),
            @ApiResponse(responseCode = "500", description = "Error generating predictions")
    })
    @GetMapping("/{currency}/future")
    public ResponseEntity<?> getFuturePredictions(
            @Parameter(description = "Currency code (e.g., EUR, GBP, JPY)") 
            @PathVariable String currency) {
        try {
            if (!predictionService.getSupportedCurrencies().contains(currency)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("INVALID_CURRENCY", "Currency not supported: " + currency));
            }

            Map<LocalDate, Double> futurePredictions = predictionService.getFuturePredictions(currency);
            return ResponseEntity.ok(futurePredictions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("PREDICTION_ERROR", e.getMessage()));
        }
    }

    @Operation(
            summary = "Get supported currencies", 
            description = "Get a list of all currencies supported for predictions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    @GetMapping("/supported")
    public ResponseEntity<List<String>> getSupportedCurrencies() {
        return ResponseEntity.ok(predictionService.getSupportedCurrencies());
    }

    @Operation(
            summary = "Get change percentage", 
            description = "Get the predicted percentage change for a currency over the next 7 days"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change percentage calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid currency code"),
            @ApiResponse(responseCode = "500", description = "Error calculating change")
    })
    @GetMapping("/{currency}/change")
    public ResponseEntity<?> getChangePercentage(
            @Parameter(description = "Currency code (e.g., EUR, GBP, JPY)") 
            @PathVariable String currency) {
        try {
            if (!predictionService.getSupportedCurrencies().contains(currency)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("INVALID_CURRENCY", "Currency not supported: " + currency));
            }

            double changePercentage = predictionService.getPredictedChangePercentage(currency);
            return ResponseEntity.ok(new ChangeResponse(currency, changePercentage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("CALCULATION_ERROR", e.getMessage()));
        }
    }

    private static class ChangeResponse {
        private String currency;
        private double changePercentage;

        public ChangeResponse(String currency, double changePercentage) {
            this.currency = currency;
            this.changePercentage = changePercentage;
        }

        public String getCurrency() {
            return currency;
        }

        public double getChangePercentage() {
            return changePercentage;
        }
    }
}