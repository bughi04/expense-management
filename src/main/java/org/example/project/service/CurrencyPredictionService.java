package org.example.project.service;

import org.example.project.CurrencyPredictor;
import org.example.project.dto.ApiDTOs.CurrencyPredictionResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyPredictionService {

    private final CurrencyPredictor predictor;

    public CurrencyPredictionService() {
        this.predictor = new CurrencyPredictor();
    }

    public CurrencyPredictionService(CurrencyPredictor predictor) {
        this.predictor = predictor;
    }

    public List<CurrencyPredictionResponse> getAllPredictions() throws Exception {
        List<String> currencies = predictor.getSupportedCurrencies();

        return currencies.stream()
                .map(currency -> {
                    try {
                        return getPredictionForCurrency(currency);
                    } catch (Exception e) {
                        throw new RuntimeException("Error predicting currency: " + currency, e);
                    }
                })
                .collect(Collectors.toList());
    }

    public CurrencyPredictionResponse getPredictionForCurrency(String currency) throws Exception {
        Map<LocalDate, Double> historicalRates = predictor.getHistoricalRates(currency);
        Map<LocalDate, Double> predictions = predictor.predictFutureRates(currency);

        double currentRate = historicalRates.values().stream()
                .reduce((first, second) -> second)
                .orElse(1.0);

        double futureRate = predictions.values().stream()
                .reduce((first, second) -> second)
                .orElse(currentRate);

        double changePercentage = ((futureRate - currentRate) / currentRate) * 100;

        String recommendation = generateRecommendation(changePercentage, currency);

        return new CurrencyPredictionResponse(
                currency,
                currentRate,
                futureRate,
                changePercentage,
                recommendation
        );
    }

    public Map<LocalDate, Double> getHistoricalRates(String currency) throws Exception {
        return predictor.getHistoricalRates(currency);
    }

    public Map<LocalDate, Double> getFuturePredictions(String currency) throws Exception {
        return predictor.predictFutureRates(currency);
    }

    public List<String> getSupportedCurrencies() {
        return predictor.getSupportedCurrencies();
    }

    private String generateRecommendation(double changePercentage, String currency) {
        if (Math.abs(changePercentage) < 0.5) {
            return "Stable - No significant change expected";
        } else if (changePercentage > 0) {
            return String.format("USD likely to strengthen against %s (%.2f%% increase)", 
                               currency, changePercentage);
        } else {
            return String.format("USD likely to weaken against %s (%.2f%% decrease)", 
                               currency, Math.abs(changePercentage));
        }
    }

    public double getPredictedChangePercentage(String currency) throws Exception {
        return predictor.getPredictedChangePercentage(currency);
    }
}