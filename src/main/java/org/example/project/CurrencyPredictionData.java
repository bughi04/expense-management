package org.example.project;

public class CurrencyPredictionData {
    private String currency;
    private String currentRate;
    private String predictedRate;
    private String changePercentage;
    private String recommendation;

    public CurrencyPredictionData() {
    }

    public CurrencyPredictionData(String currency, String currentRate, String predictedRate,
                                  String changePercentage, String recommendation) {
        this.currency = currency;
        this.currentRate = currentRate;
        this.predictedRate = predictedRate;
        this.changePercentage = changePercentage;
        this.recommendation = recommendation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(String currentRate) {
        this.currentRate = currentRate;
    }

    public String getPredictedRate() {
        return predictedRate;
    }

    public void setPredictedRate(String predictedRate) {
        this.predictedRate = predictedRate;
    }

    public String getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(String changePercentage) {
        this.changePercentage = changePercentage;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}