package org.example.project.dto;

import java.util.List;

public class ApiDTOs {

    public static class CategoryRequest {
        private String name;

        public CategoryRequest() {}

        public CategoryRequest(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class CategoryResponse {
        private String name;
        private String message;

        public CategoryResponse() {}

        public CategoryResponse(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ExpenseRequest {
        private String categoryName;
        private double amount;
        private String transactionDate;
        private String expenseDate;
        private String currency;

        public ExpenseRequest() {}

        public ExpenseRequest(String categoryName, double amount, String transactionDate, 
                             String expenseDate, String currency) {
            this.categoryName = categoryName;
            this.amount = amount;
            this.transactionDate = transactionDate;
            this.expenseDate = expenseDate;
            this.currency = currency;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getExpenseDate() {
            return expenseDate;
        }

        public void setExpenseDate(String expenseDate) {
            this.expenseDate = expenseDate;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class ExpenseResponse {
        private int id;
        private String category;
        private double amount;
        private String transactionDate;
        private String expenseDate;
        private String currency;
        private String message;

        public ExpenseResponse() {}

        public ExpenseResponse(String message) {
            this.message = message;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getExpenseDate() {
            return expenseDate;
        }

        public void setExpenseDate(String expenseDate) {
            this.expenseDate = expenseDate;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ReportResponse {
        private double totalExpenses;
        private List<CategoryBreakdown> categoryBreakdowns;

        public ReportResponse() {}

        public ReportResponse(double totalExpenses, List<CategoryBreakdown> categoryBreakdowns) {
            this.totalExpenses = totalExpenses;
            this.categoryBreakdowns = categoryBreakdowns;
        }

        public double getTotalExpenses() {
            return totalExpenses;
        }

        public void setTotalExpenses(double totalExpenses) {
            this.totalExpenses = totalExpenses;
        }

        public List<CategoryBreakdown> getCategoryBreakdowns() {
            return categoryBreakdowns;
        }

        public void setCategoryBreakdowns(List<CategoryBreakdown> categoryBreakdowns) {
            this.categoryBreakdowns = categoryBreakdowns;
        }

        public static class CategoryBreakdown {
            private String category;
            private double total;
            private double percentage;

            public CategoryBreakdown() {}

            public CategoryBreakdown(String category, double total, double percentage) {
                this.category = category;
                this.total = total;
                this.percentage = percentage;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public double getTotal() {
                return total;
            }

            public void setTotal(double total) {
                this.total = total;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }
        }
    }

    public static class CurrencyPredictionResponse {
        private String currency;
        private double currentRate;
        private double predictedRate;
        private double changePercentage;
        private String recommendation;

        public CurrencyPredictionResponse() {}

        public CurrencyPredictionResponse(String currency, double currentRate, double predictedRate,
                                         double changePercentage, String recommendation) {
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

        public double getCurrentRate() {
            return currentRate;
        }

        public void setCurrentRate(double currentRate) {
            this.currentRate = currentRate;
        }

        public double getPredictedRate() {
            return predictedRate;
        }

        public void setPredictedRate(double predictedRate) {
            this.predictedRate = predictedRate;
        }

        public double getChangePercentage() {
            return changePercentage;
        }

        public void setChangePercentage(double changePercentage) {
            this.changePercentage = changePercentage;
        }

        public String getRecommendation() {
            return recommendation;
        }

        public void setRecommendation(String recommendation) {
            this.recommendation = recommendation;
        }
    }

    public static class ErrorResponse {
        private String error;
        private String message;
        private long timestamp;

        public ErrorResponse() {
            this.timestamp = System.currentTimeMillis();
        }

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}