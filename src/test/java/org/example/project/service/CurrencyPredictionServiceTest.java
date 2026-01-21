package org.example.project.service;

import org.example.project.CurrencyPredictor;
import org.example.project.dto.ApiDTOs.CurrencyPredictionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyPredictionServiceTest {

    @Mock
    private CurrencyPredictor currencyPredictor;

    private CurrencyPredictionService predictionService;

    @BeforeEach
    void setUp() {
        predictionService = new CurrencyPredictionService(currencyPredictor);
    }

    @Test
    void testGetSupportedCurrencies_Success() {
        List<String> expectedCurrencies = Arrays.asList("EUR", "GBP", "JPY", "AUD", "RON");
        when(currencyPredictor.getSupportedCurrencies()).thenReturn(expectedCurrencies);

        List<String> actualCurrencies = predictionService.getSupportedCurrencies();

        assertEquals(expectedCurrencies, actualCurrencies);
        verify(currencyPredictor, times(1)).getSupportedCurrencies();
    }

    @Test
    void testGetPredictionForCurrency_Success() throws Exception {
        String currency = "EUR";
        Map<LocalDate, Double> historicalRates = createMockHistoricalRates();
        Map<LocalDate, Double> futurePredictions = createMockFuturePredictions();

        when(currencyPredictor.getHistoricalRates(currency)).thenReturn(historicalRates);
        when(currencyPredictor.predictFutureRates(currency)).thenReturn(futurePredictions);

        CurrencyPredictionResponse response = predictionService.getPredictionForCurrency(currency);

        assertNotNull(response);
        assertEquals(currency, response.getCurrency());
        assertEquals(1.10, response.getCurrentRate(), 0.001);
        assertEquals(1.15, response.getPredictedRate(), 0.001);
        assertTrue(response.getChangePercentage() > 0);
        assertNotNull(response.getRecommendation());
        assertTrue(response.getRecommendation().contains("strengthen"));

        verify(currencyPredictor, times(1)).getHistoricalRates(currency);
        verify(currencyPredictor, times(1)).predictFutureRates(currency);
    }

    @Test
    void testGetPredictionForCurrency_StableRate() throws Exception {
        String currency = "GBP";
        Map<LocalDate, Double> historicalRates = new LinkedHashMap<>();
        historicalRates.put(LocalDate.now().minusDays(2), 0.80);
        historicalRates.put(LocalDate.now().minusDays(1), 0.80);
        historicalRates.put(LocalDate.now(), 0.80);

        Map<LocalDate, Double> futurePredictions = new LinkedHashMap<>();
        futurePredictions.put(LocalDate.now().plusDays(1), 0.801);
        futurePredictions.put(LocalDate.now().plusDays(7), 0.802);

        when(currencyPredictor.getHistoricalRates(currency)).thenReturn(historicalRates);
        when(currencyPredictor.predictFutureRates(currency)).thenReturn(futurePredictions);

        CurrencyPredictionResponse response = predictionService.getPredictionForCurrency(currency);

        assertNotNull(response);
        assertEquals(currency, response.getCurrency());
        assertTrue(Math.abs(response.getChangePercentage()) < 0.5);
        assertTrue(response.getRecommendation().contains("Stable"));
    }

    @Test
    void testGetPredictionForCurrency_WeakeningUSD() throws Exception {
        String currency = "JPY";
        Map<LocalDate, Double> historicalRates = new LinkedHashMap<>();
        historicalRates.put(LocalDate.now().minusDays(2), 140.0);
        historicalRates.put(LocalDate.now().minusDays(1), 141.0);
        historicalRates.put(LocalDate.now(), 142.0);

        Map<LocalDate, Double> futurePredictions = new LinkedHashMap<>();
        futurePredictions.put(LocalDate.now().plusDays(1), 143.0);
        futurePredictions.put(LocalDate.now().plusDays(7), 138.0);

        when(currencyPredictor.getHistoricalRates(currency)).thenReturn(historicalRates);
        when(currencyPredictor.predictFutureRates(currency)).thenReturn(futurePredictions);

        CurrencyPredictionResponse response = predictionService.getPredictionForCurrency(currency);

        assertNotNull(response);
        assertEquals(currency, response.getCurrency());
        assertTrue(response.getChangePercentage() < 0);
        assertTrue(response.getRecommendation().contains("weaken"));
    }

    @Test
    void testGetAllPredictions_Success() throws Exception {
        List<String> currencies = Arrays.asList("EUR", "GBP");
        when(currencyPredictor.getSupportedCurrencies()).thenReturn(currencies);

        Map<LocalDate, Double> historicalRates = createMockHistoricalRates();
        Map<LocalDate, Double> futurePredictions = createMockFuturePredictions();

        when(currencyPredictor.getHistoricalRates(anyString())).thenReturn(historicalRates);
        when(currencyPredictor.predictFutureRates(anyString())).thenReturn(futurePredictions);

        List<CurrencyPredictionResponse> predictions = predictionService.getAllPredictions();

        assertNotNull(predictions);
        assertEquals(2, predictions.size());

        verify(currencyPredictor, times(1)).getSupportedCurrencies();
        verify(currencyPredictor, times(2)).getHistoricalRates(anyString());
        verify(currencyPredictor, times(2)).predictFutureRates(anyString());
    }

    @Test
    void testGetAllPredictions_ThrowsException() throws Exception {
        List<String> currencies = Arrays.asList("EUR");
        when(currencyPredictor.getSupportedCurrencies()).thenReturn(currencies);
        when(currencyPredictor.getHistoricalRates("EUR")).thenThrow(new Exception("API Error"));

        assertThrows(RuntimeException.class, () -> {
            predictionService.getAllPredictions();
        });
    }

    @Test
    void testGetHistoricalRates_Success() throws Exception {
        String currency = "EUR";
        Map<LocalDate, Double> expectedRates = createMockHistoricalRates();
        when(currencyPredictor.getHistoricalRates(currency)).thenReturn(expectedRates);

        Map<LocalDate, Double> actualRates = predictionService.getHistoricalRates(currency);

        assertEquals(expectedRates, actualRates);
        verify(currencyPredictor, times(1)).getHistoricalRates(currency);
    }

    @Test
    void testGetFuturePredictions_Success() throws Exception {
        String currency = "EUR";
        Map<LocalDate, Double> expectedPredictions = createMockFuturePredictions();
        when(currencyPredictor.predictFutureRates(currency)).thenReturn(expectedPredictions);

        Map<LocalDate, Double> actualPredictions = predictionService.getFuturePredictions(currency);

        assertEquals(expectedPredictions, actualPredictions);
        verify(currencyPredictor, times(1)).predictFutureRates(currency);
    }

    @Test
    void testGetPredictedChangePercentage_Success() throws Exception {
        String currency = "EUR";
        double expectedChange = 4.5;
        when(currencyPredictor.getPredictedChangePercentage(currency)).thenReturn(expectedChange);

        double actualChange = predictionService.getPredictedChangePercentage(currency);

        assertEquals(expectedChange, actualChange);
        verify(currencyPredictor, times(1)).getPredictedChangePercentage(currency);
    }

    @Test
    void testGetPredictedChangePercentage_ThrowsException() throws Exception {
        String currency = "INVALID";
        when(currencyPredictor.getPredictedChangePercentage(currency))
                .thenThrow(new Exception("Currency not found"));

        assertThrows(Exception.class, () -> {
            predictionService.getPredictedChangePercentage(currency);
        });
    }


    private Map<LocalDate, Double> createMockHistoricalRates() {
        Map<LocalDate, Double> rates = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 3; i >= 0; i--) {
            rates.put(today.minusDays(i), 1.10);
        }

        return rates;
    }

    private Map<LocalDate, Double> createMockFuturePredictions() {
        Map<LocalDate, Double> predictions = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 1; i <= 7; i++) {
            predictions.put(today.plusDays(i), 1.15);
        }

        return predictions;
    }
}